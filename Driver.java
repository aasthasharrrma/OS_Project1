import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Driver {
    private static final List<String> history = new ArrayList<>();
    private static BufferedReader userInput;
    private static BufferedReader encryptOutput;
    private static PrintWriter encryptInput;
    private static PrintWriter loggerInput;
    private static Process encryptProcess;
    private static Process loggerProcess;

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Driver <log-file>");
            return;
        }

        try {
            userInput = new BufferedReader(new InputStreamReader(System.in));
            startPrograms(args[0]);
            log("START Driver started.");
            runMenu();
        } catch (IOException error) {
            System.out.println("Unable to start the program: " + error.getMessage());
            stopPrograms();
        }
    }

    private static void startPrograms(String logFile) throws IOException {
        String java = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        String classpath = System.getProperty("java.class.path");

        loggerProcess = new ProcessBuilder(java, "-cp", classpath, "Logger", logFile)
                .redirectError(ProcessBuilder.Redirect.INHERIT).start();
        encryptProcess = new ProcessBuilder(java, "-cp", classpath, "EncryptionProgram")
                .redirectError(ProcessBuilder.Redirect.INHERIT).start();

        loggerInput = new PrintWriter(loggerProcess.getOutputStream(), true);
        encryptInput = new PrintWriter(encryptProcess.getOutputStream(), true);
        encryptOutput = new BufferedReader(new InputStreamReader(encryptProcess.getInputStream()));
    }

    private static void runMenu() throws IOException {
        while (true) {
            System.out.println("\nCommands: password, encrypt, decrypt, history, quit");
            String command = prompt("Command: ");
            if (command == null || command.equalsIgnoreCase("quit")) {
                log("COMMAND quit");
                log("EXIT Driver exiting.");
                stopPrograms();
                return;
            }

            switch (command.trim().toLowerCase()) {
                case "password":
                    setPassword();
                    break;
                case "encrypt":
                    encryptOrDecrypt("ENCRYPT", "encrypt");
                    break;
                case "decrypt":
                    encryptOrDecrypt("DECRYPT", "decrypt");
                    break;
                case "history":
                    log("COMMAND history");
                    showHistory();
                    log("RESULT History displayed.");
                    break;
                default:
                    System.out.println("Please choose a command from the menu.");
                    log("ERROR Invalid command: " + command.trim());
            }
        }
    }

    private static void setPassword() throws IOException {
        log("COMMAND password");
        String password = chooseText("password", false);
        if (password == null) {
            return;
        }
        String response = sendEncryption("PASS " + password);
        printAndLogResponse(response, "password");
    }

    private static void encryptOrDecrypt(String operation, String label) throws IOException {
        log("COMMAND " + label);
        String text = chooseText(label, true);
        if (text == null) {
            return;
        }
        String response = sendEncryption(operation + " " + text);
        if (response.startsWith("RESULT ")) {
            String result = response.substring(7);
            System.out.println("Result: " + result);
            history.add(result);
            log("RESULT " + label + " completed.");
        } else {
            printAndLogResponse(response, label);
        }
    }

    private static String chooseText(String label, boolean saveNewText) throws IOException {
        while (true) {
            String choice = prompt("Use a value from history? (y/n): ");
            if (choice == null) {
                return null;
            }
            if (choice.equalsIgnoreCase("y")) {
                showHistory();
                String selected = prompt("Enter a history number, or 0 for a new " + label + ": ");
                if (selected == null) {
                    return null;
                }
                try {
                    int index = Integer.parseInt(selected);
                    if (index == 0) {
                        return enterNewText(label, saveNewText);
                    }
                    if (index >= 1 && index <= history.size()) {
                        return history.get(index - 1);
                    }
                } catch (NumberFormatException ignored) {
                }
                System.out.println("That is not a valid history selection.");
            } else if (choice.equalsIgnoreCase("n")) {
                return enterNewText(label, saveNewText);
            } else {
                System.out.println("Please enter y or n.");
            }
        }
    }

    private static String enterNewText(String label, boolean saveNewText) throws IOException {
        while (true) {
            String text = prompt("Enter " + label + ": ");
            if (text == null) {
                return null;
            }
            if (text.matches("[a-zA-Z]+")) {
                String normalized = text.toUpperCase();
                if (saveNewText) {
                    history.add(normalized);
                }
                return normalized;
            }
            System.out.println("Only letters are allowed.");
            log("ERROR Invalid " + label + " input.");
        }
    }

    private static void showHistory() {
        if (history.isEmpty()) {
            System.out.println("History is empty.");
            return;
        }
        System.out.println("History:");
        for (int i = 0; i < history.size(); i++) {
            System.out.println((i + 1) + ". " + history.get(i));
        }
    }

    private static String sendEncryption(String command) throws IOException {
        encryptInput.println(command);
        String response = encryptOutput.readLine();
        if (response == null) {
            throw new IOException("Encryption program stopped unexpectedly.");
        }
        return response;
    }

    private static void printAndLogResponse(String response, String command) {
        if (response.startsWith("ERROR")) {
            System.out.println("Error: " + response.substring(5).trim());
            log("ERROR " + command + ": " + response.substring(5).trim());
        } else {
            System.out.println("Password set.");
            log("RESULT Password set.");
        }
    }

    private static String prompt(String message) throws IOException {
        System.out.print(message);
        return userInput.readLine();
    }

    private static void log(String message) {
        if (loggerInput != null) {
            loggerInput.println(message);
        }
    }

    private static void stopPrograms() {
        if (encryptInput != null) {
            encryptInput.println("QUIT");
            encryptInput.close();
        }
        if (loggerInput != null) {
            loggerInput.println("QUIT");
            loggerInput.close();
        }
        waitFor(encryptProcess);
        waitFor(loggerProcess);
    }

    private static void waitFor(Process process) {
        if (process == null) {
            return;
        }
        try {
            process.waitFor();
        } catch (InterruptedException error) {
            Thread.currentThread().interrupt();
        }
    }
}

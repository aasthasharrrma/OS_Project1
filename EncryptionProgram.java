import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class EncryptionProgram {
    private static String passkey = "";

    public static void main(String[] args) {
        try (BufferedReader input = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            while ((line = input.readLine()) != null) {
                if (line.equals("QUIT")) {
                    return;
                }
                handleCommand(line);
            }
        } catch (IOException error) {
            System.err.println("Encryption error: " + error.getMessage());
        }
    }

    private static void handleCommand(String line) {
        int split = line.indexOf(' ');
        String command = (split == -1 ? line : line.substring(0, split)).trim().toUpperCase();
        String argument = split == -1 ? "" : line.substring(split + 1).trim().toUpperCase();

        switch (command) {
            case "PASS":
                if (!argument.matches("[A-Z]+")) {
                    respond("ERROR Password must contain letters only");
                } else {
                    passkey = argument;
                    respond("RESULT");
                }
                break;
            case "ENCRYPT":
                transform(argument, true);
                break;
            case "DECRYPT":
                transform(argument, false);
                break;
            default:
                respond("ERROR Unknown command");
        }
    }

    private static void transform(String text, boolean encrypt) {
        if (passkey.isEmpty()) {
            respond("ERROR Password not set");
        } else if (!text.matches("[A-Z]+")) {
            respond("ERROR Text must contain letters only");
        } else {
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < text.length(); i++) {
                int shift = passkey.charAt(i % passkey.length()) - 'A';
                int letter = text.charAt(i) - 'A';
                int changed = encrypt ? letter + shift : letter - shift + 26;
                result.append((char) ('A' + changed % 26));
            }
            respond("RESULT " + result);
        }
    }

    private static void respond(String response) {
        System.out.println(response);
        System.out.flush();
    }
}

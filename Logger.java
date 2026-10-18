import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static final DateTimeFormatter TIME =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java Logger <log-file>");
            return;
        }

        try (BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
             BufferedWriter log = Files.newBufferedWriter(Path.of(args[0]), StandardCharsets.UTF_8,
                     StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            String line;
            while ((line = input.readLine()) != null && !line.equals("QUIT")) {
                if (!line.trim().isEmpty()) {
                    writeEntry(log, line);
                }
            }
        } catch (IOException error) {
            System.err.println("Logger error: " + error.getMessage());
        }
    }

    private static void writeEntry(BufferedWriter log, String message) throws IOException {
        String trimmed = message.trim();
        int split = 0;
        while (split < trimmed.length() && !Character.isWhitespace(trimmed.charAt(split))) {
            split++;
        }
        String action = trimmed.substring(0, split);
        String details = split == trimmed.length() ? "" : trimmed.substring(split).trim();
        log.write(LocalDateTime.now().format(TIME) + " [" + action + "] " + details);
        log.newLine();
        log.flush();
    }
}

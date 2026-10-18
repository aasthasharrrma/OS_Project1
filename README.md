# OS Project 1

This is a Java implementation of the required three-process encryption system. Running `Driver` starts both child programs and communicates with them through their standard streams.

## Requirements

A Java Development Kit (JDK) 11 or later and `make` are required. Check that Java is available with:

```sh
javac -version
```

## Project files

- `Driver.java` - interactive menu, history, validation, and child-process communication.
- `Logger.java` - receives log messages on standard input and appends timestamped entries to a file.
- `EncryptionProgram.java` - receives `PASS`, `ENCRYPT`, `DECRYPT`, and `QUIT` commands on standard input.
- `TESTING.md` - manual requirement-verification checklist.
- `devlog.md` - development-session notes.

## Build and run

From this repository directory:

```sh
make run
```

This compiles the programs and runs `Driver` with `activity.log`. To choose a different log name:

```sh
javac *.java
java Driver my-log.txt
```

Remove generated class and log files with:

```sh
make clean
```

## TA verification walkthrough

1. Start from a clean build:

   ```sh
   make clean
   make build
   java Driver ta-activity.log
   ```

2. At the menu, enter the following values when prompted:

   ```text
   password
   n
   HELLO
   encrypt
   n
   HELLO
   history
   decrypt
   y
   2
   quit
   ```

   Expected results: the password is set, encrypting `HELLO` produces `OIWWC`, history displays numbered values, and decrypting history item 2 produces `HELLO`.

3. Run another `encrypt` or `password` command and enter `Hello!`. The driver must reject it with `Only letters are allowed.` Lowercase `hello` is accepted and produces the same result as uppercase input.

4. Inspect the log:

   ```sh
   cat ta-activity.log
   ```

   It should contain timestamped `START`, `COMMAND`, `RESULT` or `ERROR`, and `EXIT` entries. The password and encryption/decryption text must not appear in the log.

`TESTING.md` lists the remaining checks, including using `0` to leave the history picker and enter a new value.

## Design notes

- `Driver` launches `Logger` and `EncryptionProgram` with Java's `ProcessBuilder` API.
- The driver writes commands to each process through its standard input stream. It reads one line of output from the encryption process for each command.
- `Logger` accepts messages until `QUIT` and writes each as `YYYY-MM-DD HH:MM [ACTION] MESSAGE`.
- The encryption process retains its passkey only for the current run and uses a letter-only, case-insensitive Vigenere cipher.
- History lasts only for the current driver run. New encrypt/decrypt input and successful results are saved; passwords are not.

## Git and submission

Before submitting, verify that the repository is clean and its history is present:

```sh
git status --short --branch
git log --oneline
```

Submit a ZIP archive containing this entire repository, including its hidden `.git` directory. From the parent directory:

```sh
make -C OS_Project1 clean
zip -r OS_Project1_submission.zip OS_Project1 -x "OS_Project1/*.class" "OS_Project1/*.log"
unzip -t OS_Project1_submission.zip
```

Submit `OS_Project1_submission.zip`, not an archive of the larger course-material folder.

# OS Project 1

This project uses three Java programs:

- `Driver` is the interactive program.
- `Logger` writes timestamped activity to the log file.
- `EncryptionProgram` stores a passkey and performs Vigenere encryption or decryption.

## Run

```sh
make run
```

Or compile and choose a log-file name yourself:

```sh
javac *.java
java Driver my-log.txt
```

The driver accepts `password`, `encrypt`, `decrypt`, `history`, and `quit`. Text input is case-insensitive and must contain letters only.

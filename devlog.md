# Development Log

## October 13, 2026 - Session 1: design and setup

I reviewed the project specification and chose Java because its `Process` API directly supports the required child-process model. The project needs three independent programs: an interactive driver, a logger, and an encryption backend. I planned to keep the communication protocol line-based so that the driver can write commands to each child process and read the encryption response with normal Java streams.

I set up the repository structure with separate Java source files, a Makefile, a README, and a `.gitignore`. I also decided that the driver would validate user input and normalize it to uppercase before passing it to the backend. That makes the interface case-insensitive while keeping the encryption process simple. My next session will focus on the logger and encryption process before connecting them to the driver.

## October 13, 2026 - Session 2: process communication and encryption

I implemented the logger to take its log-file name as a command-line argument and to read messages until `QUIT`. It separates the first word as the action and writes each event with the required timestamp format. I also implemented the encryption program with `PASS`, `ENCRYPT`, `DECRYPT`, and `QUIT` commands and `RESULT` or `ERROR` responses.

The Vigenere logic uses `A` as zero and repeats the passkey as needed. I checked the example from the assignment: encrypting `HELLO` with passkey `HELLO` produces `OIWWC`. The encryption process keeps its passkey in memory for the current run, as required. I am ready to connect both child processes to the interactive driver and test the complete pipeline.

## October 13, 2026 - Session 3: driver behavior and testing

I connected the driver to both child programs using `ProcessBuilder` and the processes' standard streams. The driver starts both processes, sends logger messages without reading output, and sends encryption commands while reading exactly one response line. This keeps the two pipe conversations separate and avoids exposing passwords in logs.

I compiled the project and tested the required missing-password response, the Vigenere example, decryption, invalid character input, history display, and clean shutdown. I found that an invalid input was displayed to the user but was not included in the activity log, so I added a concise error log entry without recording the rejected text. The final history behavior stores valid encryption/decryption inputs and successful results, but not passwords.

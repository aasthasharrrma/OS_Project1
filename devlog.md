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

## October 14, 2026 - Session 4: requirement review

I returned to the project to review it against the written requirements instead of only relying on the first end-to-end run. My plan is to make a short checklist that maps each driver feature to a concrete interaction, then run the important paths again. I especially want to verify the case-insensitive input rule, the history exit path, and the rule that passwords must not appear in the log file.

I added the checklist and ran those interactions. Encrypting lowercase `hello` produced `OIWWC`, matching the uppercase example. Selecting history and entering `0` correctly returned to the new-input prompt. The driver rejected `Hello!`, recorded a general validation error, and did not place the rejected text in the log. I also inspected the final log and confirmed that it contained start, command, result/error, and exit events without any password or encryption text.

## October 16, 2026 - Session 5: final component check

Before submission, I planned one final review that tests the separate programs as well as the full driver flow. I wanted to confirm that the encryption program handles its own command protocol correctly, that the logger independently writes the required timestamp format, and that the driver still works when the menu commands and entered letters use different cases.

The encryption program returned the expected missing-passkey error, encrypted HELLO with KEY to RIJVS, decrypted it back to HELLO, and rejected an unknown command. The logger wrote correctly formatted START and RESULT entries. In the full driver run, uppercase menu commands and lowercase text worked correctly, history selection decrypted RIJVS back to HELLO, and the final driver log did not contain the passkey or encrypted text. I cleaned the generated test files after the run and found no new issues.

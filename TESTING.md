# Requirement Verification

Run the project with `make run` from the repository directory. The checks below cover the required driver, logger, encryption, history, and validation behavior.

| Check | Input | Expected result |
| --- | --- | --- |
| Missing passkey | Encrypt `HELLO` before setting a password | `Error: Password not set` |
| Set passkey | Set password to `HELLO` | `Password set.` |
| Vigenere encryption | Encrypt `HELLO` with passkey `HELLO` | `Result: OIWWC` |
| Vigenere decryption | Decrypt `OIWWC` | `Result: HELLO` |
| Case handling | Encrypt lowercase `hello` | Same result as uppercase input |
| Input validation | Enter `Hello!` for a password or text value | `Only letters are allowed.` |
| History | Use `history` after encryption | Input and result appear with numbered choices |
| History exit | Choose history and enter `0` | The driver prompts for a new value |
| Password privacy | Inspect the log file after setting a password | Password text does not appear |
| Shutdown | Choose `quit` | Driver sends `QUIT` to both child programs and exits |

After a run, inspect the log file. It should include the driver's start and exit, each command, and its result or error in this format:

```text
YYYY-MM-DD HH:MM [ACTION] MESSAGE
```

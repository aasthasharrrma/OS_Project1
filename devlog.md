# Development Log

## October 13, 2026 - Session 1: design and setup

I reviewed the project specification and chose Java because its `Process` API directly supports the required child-process model. The project needs three independent programs: an interactive driver, a logger, and an encryption backend. I planned to keep the communication protocol line-based so that the driver can write commands to each child process and read the encryption response with normal Java streams.

I set up the repository structure with separate Java source files, a Makefile, a README, and a `.gitignore`. I also decided that the driver would validate user input and normalize it to uppercase before passing it to the backend. That makes the interface case-insensitive while keeping the encryption process simple. My next session will focus on the logger and encryption process before connecting them to the driver.

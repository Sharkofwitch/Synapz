# Synapz - Peer-to-Peer Chat Application

Synapz is a peer-to-peer chat application allowing users to join chatrooms, send messages, and manage their online status (online, busy, offline).

## Features

- **User Authentication:** Users are prompted to enter a username when connecting to the server.
- **Chatrooms:** Users can join chatrooms, send messages, and communicate with other users in real-time.
- **User Status:** Users can set their status as `online`, `busy`, or `offline`. This status is visible to other users in the chatroom.
- **Chatroom Management:** Users can see a list of members currently in the chatroom, and join/leave chatrooms.

## Getting Started

### Prerequisites

To run Synapz on your local machine, you need:

- Java 11 or later installed on your machine.
- A terminal or command line interface to run the program.

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/Sharkofwitch/Synapz.git
   ```
2. Navigate to the project directory:
   ```bash
   cd Synapz
   ```

3. Compile and run the server:
   ```bash
   javac Server.java
   java Server
   ```

4. To run the client, use:
   ```bash
   java Client
   ```

### How to Use

1. **Enter your username** when prompted.
2. **Join a chatroom** by typing `/join <chatroom_name>`.
3. **Send messages** to the chatroom by typing any text and pressing Enter.
4. **Change your status** by typing `/status <status>` (available statuses: `online`, `busy`, `offline`).
5. **See members** in your chatroom by typing `/members`.
6. **Leave a chatroom** by typing `/leave`.

### Example Commands

- `/join Gaming` - Join the "Gaming" chatroom.
- `/status busy` - Set your status to "busy".
- `/members` - See all members currently in the chatroom.
- `/leave` - Leave the current chatroom.

## Release Notes

### Version 0.2 - User Status Management
- Added feature to set and display user statuses (`online`, `busy`, `offline`).
- Status updates are visible to all members of the chatroom when a user changes their status.
- Improved chatroom member list display with user statuses.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

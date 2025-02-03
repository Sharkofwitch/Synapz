package com.synapz;

import java.io.*;
import java.net.*;
import java.util.*;

public class ClientHandler extends Thread {
    private static Map<String, Set<ClientHandler>> chatRooms = new HashMap<>(); // Chatrooms und deren Mitglieder
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private String username;
    private Set<String> rooms = new HashSet<>(); // Welche Chatrooms dieser Client beigetreten ist
    private String status = "online"; // Default-Status des Benutzers

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Benutzername abfragen
            out.println("Enter your username:");
            username = in.readLine();
            System.out.println(username + " has joined the chat!");

            out.println("Welcome, " + username + "! You are now connected.");

            // Chatroom beitreten
            out.println("Enter a chatroom to join:");
            String chatroom = in.readLine();
            joinChatRoom(chatroom);

            String message;
            while ((message = in.readLine()) != null) {
                if (message.startsWith("/join ")) {
                    String room = message.split(" ")[1];
                    joinChatRoom(room); // Client tritt einem Chatroom bei
                } else if (message.equals("/members")) {
                    showMembersInRoom(); // Mitglieder des aktuellen Raums anzeigen
                } else if (message.startsWith("/status ")) {
                    changeStatus(message.split(" ")[1]); // Status ändern
                } else if (message.equals("/leave")) {
                    leaveChatRoom(); // Chatroom verlassen
                } else {
                    sendMessageToChatRoom(message); // Nachricht an den Chatroom senden
                }
            }
        } catch (IOException e) {
            System.out.println(username + " disconnected.");
        } finally {
            // Client entfernen, wenn er sich trennt
            leaveAllChatRooms();
            try {
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Client zu einem Chatroom hinzufügen
    private void joinChatRoom(String chatroom) {
        synchronized (chatRooms) {
            chatRooms.putIfAbsent(chatroom, new HashSet<>());
            chatRooms.get(chatroom).add(this);
            rooms.add(chatroom);
            out.println("You joined the chatroom: " + chatroom);
        }
        sendMessageToChatRoom(username + " has joined the chatroom!", chatroom);
        sendStatusUpdateToChatRoom(chatroom); // Status an andere Mitglieder senden
    }

    // Client von allen Chatrooms entfernen
    private void leaveAllChatRooms() {
        synchronized (chatRooms) {
            for (String room : rooms) {
                chatRooms.get(room).remove(this);
                sendMessageToChatRoom(username + " has left the chatroom.", room);
                sendStatusUpdateToChatRoom(room); // Status an andere Mitglieder senden
            }
        }
    }

    // Chatroom verlassen
    private void leaveChatRoom() {
        if (!rooms.isEmpty()) {
            for (String room : rooms) {
                chatRooms.get(room).remove(this);
                sendMessageToChatRoom(username + " has left the chatroom.", room);
                sendStatusUpdateToChatRoom(room); // Status an andere Mitglieder senden
            }
            rooms.clear();
            out.println("You have left all chatrooms.");
        } else {
            out.println("You are not in any chatrooms.");
        }
    }

    // Nachricht an den spezifischen Chatroom senden
    private void sendMessageToChatRoom(String message) {
        for (String room : rooms) {
            sendMessageToChatRoom(message, room);
        }
    }

    // Nachricht an alle Mitglieder eines Chatrooms senden
    private void sendMessageToChatRoom(String message, String chatroom) {
        synchronized (chatRooms) {
            Set<ClientHandler> clientsInRoom = chatRooms.get(chatroom);
            for (ClientHandler client : clientsInRoom) {
                if (client != this) { // Nachricht nicht an den Sender selbst senden
                    client.out.println("[" + chatroom + "] " + username + ": " + message); // Absendername hinzugefügt
                }
            }
        }
    }

    // Mitglieder des aktuellen Chatrooms anzeigen
    private void showMembersInRoom() {
        for (String room : rooms) {
            Set<ClientHandler> members = chatRooms.get(room);
            out.println("Members in chatroom " + room + ": ");
            for (ClientHandler member : members) {
                out.println("- " + member.username + " (" + member.status + ")");
            }
        }
    }

    // Benutzerstatus ändern
    private void changeStatus(String newStatus) {
        if (newStatus.equals("online") || newStatus.equals("busy") || newStatus.equals("offline")) {
            status = newStatus;
            out.println("Your status is now: " + status);
            for (String room : rooms) {
                sendStatusUpdateToChatRoom(room); // Statusänderung an alle Clients im Chatroom senden
            }
        } else {
            out.println("Invalid status. Available statuses: online, busy, offline");
        }
    }

    // Statusupdate an andere Mitglieder im Chatroom senden
    private void sendStatusUpdateToChatRoom(String chatroom) {
        synchronized (chatRooms) {
            Set<ClientHandler> clientsInRoom = chatRooms.get(chatroom);
            for (ClientHandler client : clientsInRoom) {
                client.out.println("[" + chatroom + "] " + username + " is now " + status);
            }
        }
    }
}
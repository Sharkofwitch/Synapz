package com.synapz;

import java.io.*;
import java.net.*;

public class Server {
    private static final int PORT = 5001;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running on port " + PORT);

            while (true) {
                // Warten auf neue Client-Verbindung
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected!");

                // Starte einen neuen Thread für diesen Client
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

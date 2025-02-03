package com.synapz;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 5001;

        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner userInput = new Scanner(System.in)) {

            System.out.println("Connected to the server. Type a message:");

            // Reading input from user and sending it to server
            String message = userInput.nextLine();
            out.println(message);

            // Reading and displaying server response
            String response = in.readLine();
            System.out.println("Server response: " + response);

            System.out.println("Message sent to the server: " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

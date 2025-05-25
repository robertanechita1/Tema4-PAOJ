package ex4;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static final int PORT = 8888;
    private static final Set<ClientHandler> clients = Collections.synchronizedSet(new HashSet<>());
    private static volatile boolean running = true;
    private static ServerSocket serverSocket;

    public static void main(String[] args) throws IOException {
        serverSocket = new ServerSocket(PORT);
        System.out.println("Serverul ruleaza pe port " + PORT);

        while (running) {
            Socket clientSocket = serverSocket.accept(); //se blocheaza si se asteapta un client
            ClientHandler handler = new ClientHandler(clientSocket, clients);
            clients.add(handler);
            new Thread(handler).start(); //aici se ajunge la run din clienthandler
        }

        // inchid toti clientii la shutdown
        synchronized (clients) {
            for (ClientHandler c : clients) {
                c.shutdown();
                System.exit(0);
            }
        }

        serverSocket.close();
        System.out.println("Serverul s-a oprit.");
    }

    public static void shutdownServer() {
        running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();  // întrerupe accept()
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


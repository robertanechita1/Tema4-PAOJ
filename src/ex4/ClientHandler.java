package ex4;

import java.io.*;
import java.net.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class ClientHandler implements Runnable {
    private static final ThreadLocal<Socket> localSocket = new ThreadLocal<>();
    private final Set<ClientHandler> clients;
    private final Socket socket;
    private PrintWriter out;
    private String name = "";

    public ClientHandler(Socket socket, Set<ClientHandler> clients) {
        this.socket = socket;
        this.clients = clients;
    }

    public String getName() {
        return name;
    }

    public void run() {
        localSocket.set(socket);
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ) {
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println("Scrie-ti numele:");
            name = in.readLine();

            sendMsg(name + " s a alaturat.");
            out.println("Te-ai alaturat, acum poti scrie mesaje:");
            String msg;
            while ((msg = in.readLine()) != null) { //primesc prin socket de la server
                //System.out.println("!!!!!!!!!!!!!!!!!mesajul este "+msg);
                if (msg.equalsIgnoreCase("/quit"))
                    break;

                if (msg.equalsIgnoreCase("/shutdown") && name.equalsIgnoreCase("admin")) {
                    sendMsg("Serverul se va opri");
                    ChatServer.shutdownServer();
                    System.exit(0);
                }

                sendMsg(name + ": " + msg);
            }
        } 
        catch (IOException e) {
            System.out.println("Eroare la client: " + e.getMessage());
        } 
        finally {
            shutdown();
        }
    }

    public void sendMsg(String msg) {
        synchronized (clients) {
            for (ClientHandler client : clients) {
                if (client != this && client.out != null) { //ca sa nu mi trimit mie
                    client.out.println(msg);
                }
            }
        }
    }



    public void shutdown() {
        try {
            clients.remove(this);
            if (socket != null && !socket.isClosed())
                socket.close();
        }
        catch (IOException ignored) {}
    }
}


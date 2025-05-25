package ex4;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 8888); // conexiune intre server si client
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        Thread readThread = new Thread(() -> {
            try {
                String serverMsg;
                while ((serverMsg = in.readLine()) != null) { //citeste mesajul  clientului prin socket
                    if (serverMsg.toLowerCase().contains("serverul se va opri")) {
                        System.out.println("Serverul s-a oprit. ");
                        socket.close(); // inchid conexiunea
                        System.exit(0); // opresc aplicatia
                    }
                    System.out.println(serverMsg);
                }
            }
            catch (IOException e) {
                System.out.println("Conexiune inchisa.");
                System.exit(0);
            }
        });

        Thread writeThread = new Thread(() -> {
            try (
                    BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))
            ) {
                String userMsg;
                while (!socket.isClosed() && (userMsg = userInput.readLine()) != null) { // scriu la tastatura
                    out.println(userMsg); //trimit catre server prin socket
                    if (userMsg.equalsIgnoreCase("/quit"))
                        break;
                }
            }
            catch (IOException e) {
                System.out.println("Eroare la scriere catre server.");
            }
        });

        readThread.start();
        writeThread.start();
    }
}


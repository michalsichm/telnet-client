package utb.fai;


import java.io.*;
import java.net.*;

public class TelnetClient {

    private String serverIp;
    private int port;
    private volatile boolean running = true;

    public TelnetClient(String serverIp, int port) {
        this.serverIp = serverIp;
        this.port = port;
    }

    public void run() {
        try {
            Socket socket = new Socket(serverIp, port);

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            Thread receiveThread = new Thread(() -> {
                try {
                    while (running) {
                        if (socket.getInputStream().available() > 0) {
                            String serverResponse = reader.readLine();
                            if (serverResponse != null) {
                                System.out.println("Server: " + serverResponse);
                            }
                        }
                        Thread.sleep(20);
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            });

            Thread sendThread = new Thread(() -> {
                try {
                    String userCommand;
                    while (running) {
                        if (userInput.ready()) {
                            userCommand = userInput.readLine();

                            if (userCommand.equalsIgnoreCase("/QUIT")) {
                                running = false;
                                break;
                            }

                            writer.println(userCommand);
                        }
                        Thread.sleep(20);
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            });

            receiveThread.start();
            sendThread.start();

            sendThread.join();
            receiveThread.join();

            socket.close();
            reader.close();
            writer.close();
            userInput.close();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}


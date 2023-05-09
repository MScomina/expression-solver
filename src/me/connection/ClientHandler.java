package me.connection;

import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread {

    private final Socket socket;
    private final LineProcessingServer server;
    public ClientHandler(Socket socket, LineProcessingServer server) {
        this.socket = socket;
        this.server = server;
    }
    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            while (true) {
                String line = br.readLine();
                server.incrementNumberOfRequests();
                if (line.equals(server.getQuitCommand())) {
                    socket.close();
                    break;
                }
                bw.write(server.process(line) + System.lineSeparator());
                bw.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

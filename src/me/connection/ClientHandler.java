package me.connection;

import me.utils.LoggerUtils;

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
                if (line == null) {
                    LoggerUtils.log("Client disconnected abruptly: " + socket.getInetAddress().getHostAddress(), "Thread-" + this.threadId());
                    socket.close();
                    break;
                }
                if (line.equals(server.getQuitCommand())) {
                    LoggerUtils.log("Client disconnected: " + socket.getInetAddress().getHostAddress(), "Thread-" + this.threadId());
                    socket.close();
                    break;
                }
                bw.write(server.process(line) + System.lineSeparator());
                bw.flush();
            }
        } catch (IOException e) {
            LoggerUtils.log("IOException has been thrown: " + socket.getInetAddress().getHostAddress(), "Thread-" + this.threadId(), "ERROR");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                LoggerUtils.log("IOException has been thrown: " + socket.getInetAddress().getHostAddress(), "Thread-" + this.threadId(), "ERROR");
            }
        }
    }
}

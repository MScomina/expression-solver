package me.connection;

import me.requests.StatRequest;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class LineProcessingServer {
    private final int port;
    private long numberOfRequests;
    private final String quitCommand;
    public LineProcessingServer(int port, String quitCommand) {
        this.port = port;
        this.quitCommand = quitCommand;
        this.numberOfRequests = 0;
    }
    public void run() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server started on port " + port);
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("Client connected: " + socket.getInetAddress().getHostAddress());
            ClientHandler clientHandler = new ClientHandler(socket, this);
            clientHandler.start();
        }
    }
    public synchronized String process(String input) {
        StatRequest statRequest = new StatRequest(this, StatRequest.StatType.REQS);
        return String.valueOf(statRequest.process());
    }
    public String getQuitCommand() {
        return quitCommand;
    }
    public long getNumberOfRequests() {
        return numberOfRequests;
    }
    public void incrementNumberOfRequests() {
        numberOfRequests++;
    }
}
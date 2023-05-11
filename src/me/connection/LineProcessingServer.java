package me.connection;

import me.requests.ComputationRequest;
import me.requests.Request;
import me.requests.StatRequest;
import me.utils.RequestParseUtils;

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
        try {
            Request request = RequestParseUtils.parseRequest(this, input);
            String output = "Accepted request " + input + ", identified as: " + (request instanceof StatRequest ? "StatRequest" : (request instanceof ComputationRequest ? "ComputationRequest" : "Error")) + ".";
            return output;
        } catch (Exception e) {
            return e.getMessage();
        }
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
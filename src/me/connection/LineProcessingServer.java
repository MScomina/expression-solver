package me.connection;

import me.utils.LoggerUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.concurrent.CopyOnWriteArrayList;

public class LineProcessingServer {
    private final int port;
    protected final CopyOnWriteArrayList<Double> responseTimes;
    private final String quitCommand;
    public LineProcessingServer(int port, String quitCommand) {
        this.port = port;
        this.quitCommand = quitCommand;
        this.responseTimes = new CopyOnWriteArrayList<>();
    }
    public void run() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            LoggerUtils.log("Server started on port " + port);
            //Since it's a server, it should run forever.
            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket, this);
                LoggerUtils.log("Client connected on " + socket.getInetAddress().getHostAddress() + ", given Thread ID: " + clientHandler.threadId(), "Server");
                clientHandler.start();
            }
        } catch (IOException e) {
            LoggerUtils.log("IOException has been thrown by the ServerSocket: " + e.getMessage(), "Server", "ERROR");
        }
    }

    public String process(String input) {
        return input;
    }

    public String getQuitCommand() {
        return quitCommand;
    }

    public int getNumberOfRequests() {
        return responseTimes.size();
    }

    public double averageResponseTime() {
        if (responseTimes.size() == 0) return 0.0d; //This is the first request, so there is no average.
        double out = 0.0d;
        for (double responseTime : responseTimes) {
            out += responseTime;
        }
        return out / responseTimes.size();
    }

    public double maxResponseTime() {
        try {
            return Collections.max(responseTimes);
        } catch (NoSuchElementException e) {
            //This is the first request, so there is no max.
            return 0.0d;
        }
    }
}
package me.connection;

import me.exceptions.RequestException;
import me.requests.Request;
import me.utils.RequestParseUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

public class LineProcessingServer {
    private final int port;
    private final List<Double> responseTimes;
    private final String quitCommand;
    public LineProcessingServer(int port, String quitCommand) {
        this.port = port;
        this.quitCommand = quitCommand;
        this.responseTimes = new ArrayList<>();
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
            double[] result = request.process();
            responseTimes.add(result[0]);
            return "OK;" + result[0] + ";" + result[1];
        } catch (RequestException e) {
            return e.getMessage();
        }
    }

    public String getQuitCommand() {
        return quitCommand;
    }

    public double getNumberOfRequests() {
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
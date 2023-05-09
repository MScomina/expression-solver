package me.requests;

import me.connection.LineProcessingServer;

public class StatRequest implements Request {
    private final StatType statType;
    private final LineProcessingServer server;
    /**
     * This is used to determine what kind of statistic is being requested.<br>
     * REQS: The amount of requests done on this server, excluding this one.<br>
     * AVG_TIME: The average time required to process a request.<br>
     * MAX_TIME: The maximum time required to process a request.<br>
     */
    public enum StatType {
        REQS,
        AVG_TIME,
        MAX_TIME;
    }
    public StatRequest(LineProcessingServer server, StatType statType) {
        this.server = server;
        this.statType = statType;
    }
    @Override
    public double process() {
        return switch (statType) {
            case REQS -> server.getNumberOfRequests();
            case AVG_TIME -> 0;
            case MAX_TIME -> 0;
            default -> 0;
        };
    }

    public StatType getStatType() {
        return statType;
    }
}

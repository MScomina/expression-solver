package me.requests;

import me.connection.LineProcessingServer;

public class StatRequest implements Request {
    private final StatType statType;
    private final LineProcessingServer server;
    /**
     * This is used to determine what kind of statistic is being requested. <br>
     * Also used in {@link me.utils.RequestParseUtils} for checking command list.
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

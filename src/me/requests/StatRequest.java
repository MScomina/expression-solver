package me.requests;

import me.connection.LineProcessingServer;

public class StatRequest implements Request {
    private final StatType statType;
    private final LineProcessingServer server;

    /**
     * Starts the retrieving of the specific statistic.
     *
     * @return A double, containing the required statistic.
     */
    @Override
    public double process() {
        return switch (statType) {
            case REQS -> server.getNumberOfRequests();
            case AVG_TIME -> server.averageResponseTime();
            case MAX_TIME -> server.maxResponseTime();
        };
    }

    public StatRequest(LineProcessingServer server, StatType statType) {
        this.server = server;
        this.statType = statType;
    }

    /**
     * This is used to determine what kind of statistic is being requested.
     * Also used in {@link me.utils.RequestParseUtils} for checking command list.
     */
    public enum StatType {
        REQS,
        AVG_TIME,
        MAX_TIME
    }
}

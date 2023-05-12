package me.requests;

import me.connection.LineProcessingServer;

public class StatRequest implements Request {
    private final StatType statType;
    private final LineProcessingServer server;
    @Override
    public double[] process() {
        int startingTime = (int) System.currentTimeMillis();
        double output = 0;
        switch (statType) {
            case REQS:
                output = server.getNumberOfRequests();
                break;
            case AVG_TIME:
                output = 0;
                break;
            case MAX_TIME:
                output = 0;
                break;
        }
        double secondsElapsed = ((double)(System.currentTimeMillis() - startingTime))/1000.0d;
        return new double[]{secondsElapsed, output};
    }
    public StatRequest(LineProcessingServer server, StatType statType) {
        this.server = server;
        this.statType = statType;
    }
    /**
     * This is used to determine what kind of statistic is being requested. <br>
     * Also used in {@link me.utils.RequestParseUtils} for checking command list.
     */
    public enum StatType {
        REQS,
        AVG_TIME,
        MAX_TIME
    }

    public StatType getStatType() {
        return statType;
    }
}

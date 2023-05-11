package me.utils;

import me.connection.LineProcessingServer;
import me.exceptions.ParsingException;
import me.exceptions.CommandNotFoundException;
import me.expression.Node;
import me.expression.Parser;
import me.expression.VariableValues;
import me.requests.ComputationRequest;
import me.requests.Request;
import me.requests.StatRequest;

public class RequestParseUtils {
    /**
     * Parses a request string into a Request object.
     * @param server The server that is processing the request.
     * @param requestString The request string to be parsed.
     * @throws ParsingException if the request string could not be parsed into a proper request.
     * @throws CommandNotFoundException if the request string is not contained in the list of expected commands.
     * @return A {@link Request} object. Can then be further casted into a {@link StatRequest} or {@link ComputationRequest}, depending on the original request.
     */
    public static Request parseRequest(LineProcessingServer server, String requestString) throws ParsingException, CommandNotFoundException {
        // Handling of STAT, or StatRequests.
        if(requestString.startsWith("STAT_")) {
            return parseStatRequest(server, requestString.split("_", 2)[1]);
        }
        // Handling of ComputationRequests.
        return parseComputationRequest(requestString);
    }

    /**
     * Handles the parsing of a StatRequest.
     * @throws CommandNotFoundException if the request string is not contained in the list of commands inside StatType.
     */
    private static StatRequest parseStatRequest(LineProcessingServer server, String request) throws CommandNotFoundException {
        try {
            StatRequest.StatType statType = StatRequest.StatType.valueOf(request);
            return new StatRequest(server, statType);
        } catch (IllegalArgumentException e) {
            throw new CommandNotFoundException("Could not find \"" + request + "\" as a command.");
        }
    }

    /**
     * Handles the parsing of a ComputationRequest.
     * @throws ParsingException if the request string could not be parsed as a ComputationRequest.
     * @throws CommandNotFoundException if the request string is not contained in the combination of ComputationKind and ValuesKind commands.
     */
    private static ComputationRequest parseComputationRequest(String request) throws ParsingException, CommandNotFoundException {
        String[] requestSplit = request.split(";");
        if(requestSplit.length < 3) {
            throw new ParsingException("Could not parse \"" + request + "\" as a ComputationRequest: not enough arguments.");
        }
        // Checks and parses the first argument.
        ComputationRequest.ComputationKind computationKind;
        ComputationRequest.ValuesKind valuesKind;
        try {
            computationKind = ComputationRequest.ComputationKind.valueOf(requestSplit[0].split("_")[0]);
            valuesKind = ComputationRequest.ValuesKind.valueOf(requestSplit[0].split("_")[1]);
        } catch (IllegalArgumentException e) {
            throw new CommandNotFoundException("Could not find \"" + requestSplit[0] + "\" as a command.");
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ParsingException("Could not parse \"" + requestSplit[0] + "\": Could not split into ComputationKind and ValuesKind.");
        }
        // Checks and parses the second argument.
        String[] variableValuesSplit = requestSplit[1].split(",");
        VariableValues[] variableValues = new VariableValues[variableValuesSplit.length];
        for (int k = 0; k < variableValuesSplit.length; k++) {
            try {
                String[] variableValue = variableValuesSplit[k].split(":");
                variableValues[k] = new VariableValues(variableValue);
            } catch (NumberFormatException e) {
                throw new ParsingException("Could not parse \"" + variableValuesSplit[k] + "\" as a VariableValue: could not parse String to double.");
            } catch (IllegalArgumentException e) {
                throw new ParsingException("Could not parse \"" + variableValuesSplit[k] + "\" as a VariableValue: not enough arguments.");
            }
        }
        // Checks and parses the other arguments.
        Node[] expressions = new Node[requestSplit.length - 2];
        for(int m = 2; m < requestSplit.length; m++) {
            try {
                Parser parser = new Parser(requestSplit[m]);
                expressions[m - 2] = parser.parse();
            } catch (IllegalArgumentException e) {
                throw new ParsingException("Could not parse \"" + requestSplit[m] + "\" as an expression: " + e.getMessage() + ".");
            }
        }
        return new ComputationRequest(computationKind, valuesKind, variableValues, expressions);
    }
}

package me.requests;

import me.exceptions.RequestException;

public interface Request {
    /**
     * Starts the processing of the request.
     *
     * @return A double, containing the result of the request.
     * @throws RequestException If the request cannot be processed.
     */
    double process() throws RequestException;
}

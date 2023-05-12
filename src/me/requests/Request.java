package me.requests;

import me.exceptions.RequestException;

public interface Request {
    double[] process() throws RequestException;
}

package me.connection;

import me.exceptions.RequestException;
import me.requests.Request;
import me.utils.LoggerUtils;
import me.utils.RequestParseUtils;

import java.util.concurrent.Semaphore;

public class ComputationalServer extends LineProcessingServer {
    private final Semaphore currentComputations;

    public ComputationalServer(int port, String quitCommand) {
        super(port, quitCommand);
        this.currentComputations = new Semaphore(Runtime.getRuntime().availableProcessors());
    }

    /**
     * Processes the input, converting it into a Request object and then processing it. This process is handled using a Semaphore so that it cannot do more computations than the number of available processors.
     * <p>
     * NOTE: It is assumed that the "response time" is the time taken to respond, not the time taken to perform the calculations. This means that the time will be longer if the method is blocked by the Semaphore.
     * </p>
     *
     * @param input The input request, in String form, to process.
     * @return The processed request, or an error if the request could not be processed.
     */
    @Override
    public String process(String input) {
        String output;
        long startTime = System.currentTimeMillis();
        try {
            currentComputations.acquire();
            Request request = RequestParseUtils.parseRequest(this, input);
            double result = request.process();
            double timeTaken = (System.currentTimeMillis() - startTime) / 1000.0d;
            responseTimes.add(timeTaken);
            output = "OK;" + timeTaken + ";" + result;
        } catch (RequestException e) {
            output = e.getMessage();
        } catch (InterruptedException e) {
            LoggerUtils.log("A thread has been interrupted: " + e.getMessage(), "Server", "ERROR");
            output = "ERR; (Interrupted Thread)";
        } finally {
            currentComputations.release();
        }
        return output;
    }
}

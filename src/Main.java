import me.connection.ComputationalServer;
import me.connection.LineProcessingServer;
import me.utils.LoggerUtils;

import java.io.IOException;
public class Main {
    public static void main(String... args) {
        try {
            LineProcessingServer server = new ComputationalServer(Integer.parseInt(args[0]), "BYE");
            server.run();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            LoggerUtils.log("Port must be a number.", "ERROR");
        } catch (ArrayIndexOutOfBoundsException e) {
            LoggerUtils.log("Port must be specified in the args.", "ERROR");
        }
    }
}
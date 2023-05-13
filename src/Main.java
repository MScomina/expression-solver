import me.connection.LineProcessingServer;
import java.io.IOException;
public class Main {
    public static void main(String... args) {
        try {
            LineProcessingServer server = new LineProcessingServer(Integer.parseInt(args[0]), "BYE");
            server.run();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Port must be an integer.");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Port must be specified in the args.");
        }
    }
}
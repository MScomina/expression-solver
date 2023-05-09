import me.connection.LineProcessingServer;

import java.io.IOException;


//Ricorda, per connetterti puoi farlo da terminale con il comando telnet localhost 8080
public class Main {
    public static void main(String[] args) {
        try {
            LineProcessingServer server = new LineProcessingServer(8080, "BYE");
            server.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
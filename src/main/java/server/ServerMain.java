package server;

import java.util.Locale;

public class ServerMain {
    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);

        Server server = new Server();
    }
}

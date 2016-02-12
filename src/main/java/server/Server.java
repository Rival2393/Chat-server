package server;

import server.database.DataAccessObject;
import server.database.DataSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Server {
    private final static int PORT = 5678;
    private List<Connection> connections = Collections.synchronizedList(new ArrayList<Connection>());
    private ServerSocket server;

    DataAccessObject accessObject;

    public Server(){
        try {
            server = new ServerSocket(PORT);
            accessObject = new DataAccessObject(DataSource.getConnection());
            while (true) {
                Socket socket = server.accept();

                Connection connection = new Connection(socket);
                connections.add(connection);

                connection.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class Connection extends Thread{
        private BufferedReader reader;
        private PrintWriter writer;
        private Socket socket;

        private String login;
        private String ip;
        private String status;

        public Connection(Socket socket) {
            this.socket = socket;

            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try{
                ip = socket.getRemoteSocketAddress().toString();
                ip = ip.substring(ip.indexOf('/') + 1, ip.indexOf(':'));
                System.out.println(ip);
                login = reader.readLine();
                System.out.println(login);
                status = accessObject.getStatus(login, ip);
                System.out.println(status);
                writer.println(status);
                if(status.equals("BANNED")){
                    return;
                }

                synchronized(connections) {
                    Iterator<Connection> iterator = connections.iterator();
                    while(iterator.hasNext()) {
                        (iterator.next()).writer.println(login + " connected");
                    }
                }

                String message = "";
                while (true) {
                    message = reader.readLine();
                    if(message.equals("exit")) break;
                    else if (message.contains("oracle")){
                        accessObject.setBan(ip);
                        writer.println("You are automatically banned because of forbidden message");
                        break;
                    }

                    synchronized(connections) {
                        Iterator<Connection> iter = connections.iterator();
                        while(iter.hasNext()) {
                            (iter.next()).writer.println(login + ": " + message);
                        }
                    }
                }

                synchronized(connections) {
                    Iterator<Connection> iterator = connections.iterator();
                    while(iterator.hasNext()) {
                        ((Connection) iterator.next()).writer.println(login + " has left this chat");
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

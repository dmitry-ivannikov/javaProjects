package chat.server;



import network.TCPConnection;
import network.TCPListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class ChatServer implements TCPListener{


    public static void main(String[] args){
        new ChatServer();
    }

    private final ArrayList<TCPConnection> connections = new ArrayList<>();

    private ChatServer(){
        System.out.println("Server run!");
        try (ServerSocket serverSocket = new ServerSocket(8189)){
            while (true){
                try {
                    new TCPConnection(this, serverSocket.accept());
                } catch (Exception e) {
                    System.out.println("TCPConnection exception: " + e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public synchronized void onConnectionReady(TCPConnection TCPConnection) {
        connections.add(TCPConnection);

        sentToAllConnections("Client connected" + TCPConnection);
    }

    @Override
    public synchronized void onReceiveString(TCPConnection TCPConnection, String value) {
        sentToAllConnections(value);
    }

    @Override
    public synchronized void onDisconnect(TCPConnection TCPConnection) {
        sentToAllConnections("Client disconnected" + TCPConnection);
    }

    @Override
    public synchronized void onException(TCPConnection TCPConnection, Exception e) {
        System.out.println("TCPConnection exceptionn: " + e);
    }

    private void  sentToAllConnections(String value){
        System.out.println(value);
        for (int i = 0; i < connections.size(); i++) {
            connections.get(i).sendString(value);
        }
    }
}

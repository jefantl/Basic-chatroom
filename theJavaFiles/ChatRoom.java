package theJavaFiles;
import theJavaFiles.*;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatRoom implements Runnable {

    public ServerSocket server = null;
    private Thread thread = null;
    private ClientUser client = null;
    private ArrayList<ClientUser> users;
private userTracker helperThread;

    public ChatRoom(int port) {
        users = new ArrayList<ClientUser>();
        try {
            System.out.println("Binding to port " + port + ", please wait  ...");
            server = new ServerSocket(port);
            System.out.println("Server started: " + server.getInetAddress());
            start();
            helperThread = new userTracker(this);
            helperThread.start();
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }

    public void run() {
        while (thread != null) {
//           for(ClientUser c : users) if(!c.checkConnection()) removeUser(c);
        }
    }

    public void addThread(Socket socket) {
        try {
            System.out.println("Client accepted: " + socket);
            client = new ClientUser(this, socket);
            client.open();
            client.start();
            users.add(client);
            client.sendToClient("you have entered jasons server, type leave to leave");
            System.out.println("user count: " + users.size());

        } catch (IOException ex) {
            Logger.getLogger(ChatRoom.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendToAll(String message) {
        System.out.println("Public: " + message);
        System.out.println("user count: " + users.size());

        for (ClientUser c : users) {
            c.sendToClient(message);
        }
    }

    public void removeUser(ClientUser inClient) {
        sendToAll(inClient.getID() + " has left the room");
        users.remove(inClient);
        System.out.println("user count: " + users.size());
    }

    public void start() {
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    public void stop() {
        if (thread != null) {
            thread.stop();
            thread = null;
        }
    }

    public static void main(String args[]) {
        ChatRoom server = new ChatRoom(4444);
    }
}

class userTracker extends Thread {

    ChatRoom myRoom;

    public userTracker(ChatRoom incr) {
        myRoom = incr;
    }

    public void run() {
        while (this.isAlive()) {
            System.out.println("Waiting for a client ...");
            try {
                myRoom.addThread(myRoom.server.accept());
            } catch (IOException ex) {
                Logger.getLogger(userTracker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

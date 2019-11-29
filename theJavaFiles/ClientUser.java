package theJavaFiles;
import theJavaFiles.*;
import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientUser extends Thread {

    public Socket socket = null;
    private ChatRoom server = null;
    private String ID = "";
    private DataInputStream streamIn = null;
    private DataOutputStream streamOut = null;
    private boolean running = true;

    public ClientUser(ChatRoom _server, Socket _socket) {
        server = _server;
        socket = _socket;
        ID = Integer.toString(socket.getPort());
    }

    public void run() {
        while (running) {
            try {
                String input = streamIn.readUTF();
                if (input != null) {
                    if (input.equals("leave")) {
                        server.removeUser(this);
                        running = false;
                    } else if (input.startsWith("newUser:")) {
                        ID = input.substring(8);
                        server.sendToAll("new user: " + ID);
                    } else {
                        server.sendToAll(ID + ": " + input);
                    }
                }
            } catch (IOException ioe) {
            }
        }
        try {
            close();
        } catch (IOException ex) {
            Logger.getLogger(ClientUser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean sendToClient(String message) {
        try {
            streamOut.writeUTF(message);
            streamOut.flush();
            return true;
        } catch (IOException ex) {
            Logger.getLogger(ClientUser.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean checkConnection() {
        try {
            sendToClient("heartbeat");
            Thread.sleep(1000);
            String input = streamIn.readUTF();
            if(input.contains("heartbeat")) return true;
        } catch (IOException ex) {
            Logger.getLogger(ClientUser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(ClientUser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public void open() throws IOException {
        streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        streamOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }

    public void close() throws IOException {
        try {
            if (socket != null) {
                socket.close();
            }
            if (streamIn != null) {
                streamIn.close();
            }
            if (streamOut != null) {
                streamOut.close();
            }
        } catch (IOException ioe) {
            System.out.println("Error closing ...");
        }
    }

    String getID() {
        return ID;
    }
}

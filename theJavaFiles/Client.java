package theJavaFiles;
import theJavaFiles.*;

import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {

    private Socket socket = null;
    private DataInputStream console = null;
    private DataOutputStream streamOut = null;
    private DataInputStream streamIn = null;

    public Client() {
        System.out.println("Establishing connection. Please wait ...");
        try {
            console = new DataInputStream(System.in);
            System.out.println("Welcome to client setup\nEnter an ip to connect to: ");
            String ip = console.readLine();
            System.out.println("Enter the port to connect to: (likely 4444)");
            String in = console.readLine();
                        int port = 4444;

            if(in.length() > 1)
            port = Integer.parseInt(in);

if(ip.equals("") || port <= 1000) { ip = "2601:282:4000:41e0:d5e0:59fa:b7ae:1576"; port = 4444;}
            socket = new Socket(ip, port);
            System.out.println("Connected: " + socket);
            start();
        } catch (UnknownHostException uhe) {
            System.out.println("Host unknown: " + uhe.getMessage());
        } catch (IOException ioe) {
            System.out.println("Unexpected exception: " + ioe.getMessage());
        }

        String line = "";

        try {
            System.out.println("Please enter username: ");
            line = console.readLine();
            streamOut.writeUTF("newUser:" + line);
            streamOut.flush();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

        while (!line.equals("leave")) {
            try {
                if (console.available() > 0) {
                    line = console.readLine();
                    sendMessage(line);
                }
                String message = getMessage();
                if (message != null) {
                    if (message.equals("heartbeat")) {
                        sendMessage(message);
                    } else {
                        System.out.println(message);
                    }
                }
            } catch (IOException ioe) {
                System.out.println("Sending error: " + ioe.getMessage());
            }
                    try {

            Thread.sleep(100);
            } catch (InterruptedException ex) {
            Logger.getLogger(ClientUser.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
        close();
    }

    public void sendMessage(String message) {
        try {
            streamOut.writeUTF(message);
            streamOut.flush();

        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getMessage() {
        try {
            if (streamIn.available() > 0) {
                try {
                    String line = streamIn.readUTF();
                    return line;
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void start() throws IOException {
        streamOut = new DataOutputStream(socket.getOutputStream());
        streamIn = new DataInputStream(socket.getInputStream());
    }

    public void close() {
        try {
            if (console != null) {
                console.close();
            }
            if (streamOut != null) {
                streamOut.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ioe) {
            System.out.println("Error closing ...");
        }
    }

    public static void main(String args[]) {

        Client client = new Client();

        System.out.println("You have closed this server");
    }
}

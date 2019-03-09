package server;

import javax.swing.*;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import static common.Constants.*;

/**
 * Main thread for managing connections
 */
public class ChatServer implements Runnable{

    private Map<String, ChatServerThread> loggedUsers = new HashMap<>();
    private ServerGUI gui;
    private ServerSocket serverSocket;

    ChatServer(ServerGUI gui) {
        this.gui = gui;
    }

    /**
     * Main function. Waits for the client's connection and starts the threads.
     */
    @Override
    public void run(){
        try{
            serverSocket = new ServerSocket(PORT);
            gui.addEvent("Server started!");

            while(true) {
                Socket client = serverSocket.accept();
                gui.addEvent("New connection from " + client.getInetAddress().toString());
                Thread tmp = new Thread(new ChatServerThread(client, this));
                tmp.start();

            }


        } catch (BindException e) {
            JOptionPane.showMessageDialog(null, "Port already in use!\nIs the server started already?", "Error!", JOptionPane.ERROR_MESSAGE);
            gui.forceClose();

        } catch (IOException e) {
            gui.forceClose();
        }
    }

    /**
     * Forces closing the server socket
     */
    void forceClose() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Logs in a user
     * @param username the username to log in
     * @param thread the thread responsible for that connection
     * @return true if successful, false if already exists
     */
    synchronized boolean logIn(String username, ChatServerThread thread){
        if(loggedUsers.containsKey(username)) return false;

        loggedUsers.put(username, thread);
        gui.addEvent(username + " logged in!");
        gui.updateUsers(loggedUsers.keySet());
        return true;
    }

    /**
     * Logs out a user and removes the thread from the map
     * @param username the username to log out
     */
    synchronized void logOut(String username){
        loggedUsers.remove(username);
        if(username != null) gui.addEvent(username + " logged out!");
        else gui.addEvent("Client connection aborted!");
        gui.updateUsers(loggedUsers.keySet());
    }

    /**
     * Sends a broadcast message to anyone except the sender
     * @param sender the sender's username
     * @param message the message to send
     */
    synchronized String sendBroadcast(String sender, String message){
        for(Map.Entry<String, ChatServerThread> entry : loggedUsers.entrySet()){
            if(!entry.getKey().equals(sender)) entry.getValue().sendMessage(sender, message, BROADCAST);
        }
        return Integer.toString(loggedUsers.size()-1);
    }

    /**
     * Sends a message to a user
     * @param sender the sender's username
     * @param receiver the receiver's username
     * @param message the message to send
     * @return true if successful, false if the receiver is not found
     */
    synchronized boolean sendMessage(String sender, String receiver, String message){
        ChatServerThread receiverThread = loggedUsers.get(receiver);
        if(receiverThread != null) {
            receiverThread.sendMessage(sender, message, ONETOONE);
            return true;
        }
        return false;

    }



}

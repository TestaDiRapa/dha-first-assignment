package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import static common.Constants.PORT;

public class ChatServer {

    private Map<String, ChatServerThread> loggedUsers = new HashMap<>();

    /**
     * Main function. Waits for the client's connection and starts the threads.
     */
    public void startServer(){
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server started!");

            while(true) {
                Socket client = serverSocket.accept();
                ChatServerThread tmp = new ChatServerThread(client, this);
                tmp.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Logs in a user
     * @param username the username to log in
     * @param thread the thread responsible for that connction
     * @return true if successful, false if already exists
     */
    synchronized boolean logIn(String username, ChatServerThread thread){
        if(loggedUsers.containsKey(username)) return false;

        loggedUsers.put(username, thread);
        return true;
    }

    /**
     * Logs out a user and removes the thread from the map
     * @param username the username to log out
     */
    public synchronized void logOut(String username){
        System.out.println("RIMUOVO "+username);
        loggedUsers.remove(username);
        System.out.println(loggedUsers.get(username)==null);
    }

    /**
     * Sends a broadcast message to anyone except the sender
     * @param sender the sender's username
     * @param message the message to send
     */
    synchronized void sendBroadcast(String sender, String message){
        for(Map.Entry<String, ChatServerThread> entry : loggedUsers.entrySet()){
            if(!entry.getKey().equals(sender)) entry.getValue().sendMessage(sender, message);
        }
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
            receiverThread.sendMessage(sender, message);
            return true;
        }
        return false;

    }



}

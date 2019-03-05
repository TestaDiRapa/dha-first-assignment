package server;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import static common.ArgumentExtractor.*;
import static common.Constants.*;

public class ChatServerThread extends Thread{

    private Socket socket;
    private ChatServer server;

    ChatServerThread(Socket socket, ChatServer server){
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_16))) {

            String command = reader.readLine();
            String firstArgument = extractNthArgument(command, 1);

            while(!extractCommand(command).equals(LOGIN)|| firstArgument == null || firstArgument.equals("")){
                sendProtocol(ERROR);
                command = reader.readLine();
                firstArgument = extractNthArgument(command, 1);
            }

            String username = extractNthArgument(command, 1);

            if(server.logIn(username, this)){
                sendProtocol(SUCCESS);

                while(!extractCommand(command).equals(LOGOUT)) {
                    command = reader.readLine();

                    if (extractCommand(command).equals(ONETOONE)) {
                        String receiver = extractNthArgument(command, 1);
                        String message = extractNthArgument(command, 2);
                        if (server.sendMessage(username, receiver, message)) {
                            sendProtocol(ERROR);
                        }
                    } else if (extractCommand(command).equals(BROADCAST)) {
                        String message = extractNthArgument(command, 1);
                        server.sendBroadcast(username, message);
                    }

                }

                server.logOut(username);
            }

            else{
                sendProtocol(ERROR);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized void sendProtocol(String method, String... args){
        String toSend = "<" + method + ">";
        for(String arg : args){
            toSend = toSend + " <" + arg + ">";
        }
        try(PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_16))){
            writer.println(toSend);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a message to the client
     * @param sender the username that sends the message
     * @param message the message to send
     */
    synchronized void sendMessage(String sender, String message){
        sendProtocol(MESSAGE, sender, message);
    }
}

package server;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatServerThread extends Thread{

    private Socket socket;
    private ChatServer server;

    ChatServerThread(Socket socket, ChatServer server){
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try(PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_16));
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_16))) {

            String command = reader.readLine();

            while(!command.matches("^<LOGIN>.*") || extractFirstArgument(command) == null || extractFirstArgument(command).equals("")){
                writer.println("<ERROR>");
                writer.flush();
                command = reader.readLine();
            }

            String username = extractFirstArgument(command);

            if(server.logIn(username, this)){
                writer.println("<SUCCESS>");
                writer.flush();

                while(!command.matches("^<LOGOUT>.*")){
                    command = reader.readLine();

                    if(command.matches("^<ONETOONE>.*")){
                        String receiver = extractFirstArgument(command);
                        String message = extractSecondArgument(command);
                        if(server.sendMessage(username, receiver, message)) {
                            writer.println("<ERROR>");
                            writer.flush();
                        }
                    }
                    else if(command.matches("^<BROADCAST>.*")){
                        String message = extractFirstArgument(command);
                        server.sendBroadcast(username, message);
                    }

                }
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

    /**
     * Extracts the first argument from a command
     * @param command the command
     * @return the argument, if any, or empty string
     */
    private String extractFirstArgument(String command){
        Pattern p = Pattern.compile("<[A-Z]+> <(.*)>");
        Matcher m = p.matcher(command);
        if(m.find()) return m.group(1);
        return "";
    }

    /**
     * Extracts the second argument from a command
     * @param command the command
     * @return the argument, if any, or empty string
     */
    private String extractSecondArgument(String command){
        Pattern p = Pattern.compile("<[A-Z]+> <(.*)> <(.*)>");
        Matcher m = p.matcher(command);
        if(m.find()) return m.group(2);
        return "";
    }

    /**
     * Sends a message to the client
     * @param sender the username that sends the message
     * @param message the message to send
     */
    synchronized void sendMessage(String sender, String message){
        try(PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_16))){
            writer.println("<MESSAGE> <"+sender+"> "+"<"+message+">");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

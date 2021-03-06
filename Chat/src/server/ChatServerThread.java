package server;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import static common.CommandParser.*;
import static common.Constants.*;
import static common.UserValidator.isUsernameValid;

/**
 * The class that manage the connection with a single client
 */
public class ChatServerThread implements Runnable{

    private Socket socket;
    private ChatServer server;
    private PrintWriter writer;
    private BufferedReader reader;
    private String username;

    /**
     * Constructor
     * @param socket the socket associated to the client
     * @param server the server object
     */
    ChatServerThread(Socket socket, ChatServer server){
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try(PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_16));
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_16))) {

            //Instantiate the streams (try with resources, so closed automatically)
            this.reader = reader;
            this.writer = writer;

            //Reads the first command
            String command = safeRead();
            username = extractNthArgument(command, 1);

            //If it receives a command that is not login or whose username is not suitable
            //sends an error and waits
            while(!extractCommand(command).equals(LOGIN)|| !isUsernameValid(username) || !server.logIn(username, this)){
                sendProtocol(ERROR);
                command = safeRead();
                username = extractNthArgument(command, 1);
            }

            sendProtocol(SUCCESS);

            sendProtocol(ONETOONE, "server", "Welcome into the chat, "+username);

            //Cycles until it receives a LOGOUT command
            while(!extractCommand(command).equals(LOGOUT)) {
                //Reads the command
                command = safeRead();

                //If the command is ONETOONE, extracts receiver and message
                //and asks the server to send the message
                if (extractCommand(command).equals(ONETOONE)) {
                    String receiver = extractNthArgument(command, 1);
                    String message = extractNthArgument(command, 2);

                    //If the server managed to send the message, sends a SUCCESS
                    //to the client, otherwise ERROR
                    if(server.sendMessage(username, receiver, message)) {
                        sendProtocol(SUCCESS, "-1");
                    }
                    else {
                        sendProtocol(ERROR);
                    }

                //If the command is BROADCAST, asks the server to send a broadcast message
                } else if (extractCommand(command).equals(BROADCAST)) {
                    String message = extractNthArgument(command, 1);
                    String receivers = server.sendBroadcast(username, message);
                    sendProtocol(SUCCESS, receivers);
                }

            }

            //If it exits from the while, means that the command was LOGOUT, so it logs out the user
            server.logOut(username);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnexpectedDisconnectionException e) {
            server.logOut(username);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * A safe read function to handle an unexpected disconnection from the user
     * @return the incoming command, if not null
     * @throws UnexpectedDisconnectionException if the client disconnects unexpectedly
     * @throws IOException IOException
     */
    private String safeRead() throws UnexpectedDisconnectionException, IOException {
        String ret = reader.readLine();
        if(ret == null) throw new UnexpectedDisconnectionException();
        return ret;
    }

    /**
     * A function that formats the command and send to the output stream
     * @param method the command
     * @param args the arguments
     */
    private synchronized void sendProtocol(String method, String... args){
        String toSend = createCommand(method, args);
        writer.println(toSend);
        writer.flush();
    }

    /**
     * Sends a message to the client
     * @param sender the username that sends the message
     * @param message the message to send
     */
    void sendMessage(String sender, String message, String type){
        sendProtocol(type, sender, message);
    }
}

package server;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import static common.CommandParser.*;
import static common.Constants.*;

public class ChatServerThread extends Thread{

    private Socket socket;
    private ChatServer server;
    private PrintWriter writer;
    private BufferedReader reader;

    ChatServerThread(Socket socket, ChatServer server){
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try(PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_16));
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_16))) {

            this.writer = writer;
            this.reader = reader;

            String command = readFromStream();
            String firstArgument = extractNthArgument(command, 1);

            while(!extractCommand(command).equals(LOGIN)|| firstArgument.equals("")){
                sendProtocol(ERROR);
                command = readFromStream();
                firstArgument = extractNthArgument(command, 1);
            }

            String username = extractNthArgument(command, 1);

            if(server.logIn(username, this)){
                sendProtocol(SUCCESS);

                while(!extractCommand(command).equals(LOGOUT)) {
                    command = readFromStream();

                    if (extractCommand(command).equals(ONETOONE)) {
                        String receiver = extractNthArgument(command, 1);
                        String message = extractNthArgument(command, 2);
                        if(server.sendMessage(username, receiver, message)) {
                            sendProtocol(SUCCESS);
                        }
                        else {
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

    private void sendProtocol(String method, String... args){
        synchronized (writer) {
            String toSend = createCommand(method, args);
            writer.println(toSend);
            writer.flush();
        }
    }

    private String readFromStream() throws IOException {
        synchronized (reader) {
            String response = null;

            while (response == null) {
                response = reader.readLine();
            }

            return response;
        }
    }

    /**
     * Sends a message to the client
     * @param sender the username that sends the message
     * @param message the message to send
     */
    void sendMessage(String sender, String message){
        sendProtocol(MESSAGE, sender, message);
    }
}

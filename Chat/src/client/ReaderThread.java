package client;

import java.io.BufferedReader;
import java.io.IOException;

import static common.CommandParser.extractCommand;
import static common.CommandParser.extractNthArgument;
import static common.Constants.*;

public class ReaderThread implements Runnable {

    private BufferedReader input;
    private ClientGUI gui;
    private boolean stop = false;

    public ReaderThread(BufferedReader input, ClientGUI gui) {
        this.input = input;
        this.gui = gui;
    }

    @Override
    public void run() {
        String response, sender, message;
        while (!stop) {
            try {
                response = input.readLine();
                if(response != null){
                    String command = extractCommand(response);
                    switch (command){
                        

                        case SUCCESS:
                            try{
                                String receivers = extractNthArgument(response, 1);
                                gui.writeOnChat("Message sent to: " + receivers + " users!");
                            } catch (Exception e) {
                                gui.writeOnChat("Message sent!");
                            }
                            break;

                        case ONETOONE:
                            sender = extractNthArgument(response, 1);
                            message = extractNthArgument(response,2);
                            gui.writeOnChat(sender + ": " + message);
                            break;

                        case BROADCAST:
                            sender = extractNthArgument(response, 1);
                            message = extractNthArgument(response,2);
                            gui.writeOnChat(sender + " [BROADCAST]: " + message);
                            break;
                        
                        default:
                            gui.writeOnChat("Error!");
                            break;
                        
                    }
                }
            } catch (IOException e) {
                stop = true;
            }
        }
    }

}

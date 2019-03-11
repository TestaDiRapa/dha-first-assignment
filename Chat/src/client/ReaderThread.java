package client;

import java.io.BufferedReader;
import java.io.IOException;

import static common.CommandParser.extractCommand;
import static common.CommandParser.extractNthArgument;
import static common.Constants.*;

/**
 * A class responsible for reading the messages from the server and prompting them in the chat area
 */
public class ReaderThread implements Runnable {

    private BufferedReader input;
    private ClientGUI gui;
    private boolean stop = false;

    /**
     * Constructor
     * @param input the input stream from the server
     * @param gui the gui object
     */
    ReaderThread(BufferedReader input, ClientGUI gui) {
        this.input = input;
        this.gui = gui;
    }

    @Override
    public void run() {
        String response, sender, message;
        while (!stop) {
            try {
                response = input.readLine();
                //If the response is not null parse the response and act accordingly
                if(response != null){
                    String command = extractCommand(response);
                    switch (command){
                        

                        case SUCCESS:
                            //If it manages to extract the first argument, means that is the response to a broadcast,
                            //otherwise is the success response to a ONETOONE
                            try{
                                String receivers = extractNthArgument(response, 1);
                                int numUtenti = Integer.parseInt(receivers);                                
                                if(numUtenti >= 0){
                                    gui.writeOnChat("Message sent to: " + receivers + " users!");
                                } else if(numUtenti == -1){
                                    gui.writeOnChat("Message sent!");
                                }
                            } catch (Exception e) {
                                
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
                // If the response is null means that the server disconnected, so closes the client after three seconds
                else {
                    handleCommunicationError();
                }
            } catch (IOException e){
                handleCommunicationError();
            }
        }
    }

    /**
     * A function that shows an error message three times with an interval of one second and then stops the thread
     */
    private void handleCommunicationError() {
        for (int i = 3; i > 0; i--) {
            gui.writeOnChat("WARNING! Cannot communicate with the server, closing in " + i + "...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
        stop = true;
        gui.closeProtocol();
    }

    /**
     * Stops the thread
     */
    void forceStop(){
        stop = true;
    }

}

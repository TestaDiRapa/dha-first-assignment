package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JOptionPane;

import static common.Constants.*;
import common.CommandParser;
import static common.CommandParser.createCommand;
import static java.lang.System.exit;
import java.net.ConnectException;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class ChatClient {

    private final String FILENAME = null;
    Socket socket;
    BufferedReader read;
    PrintWriter output;
    CommandParser command=new CommandParser();

    public void startClient() throws UnknownHostException, IOException,ConnectException{
        //Create socket connection
       try{
        socket = new Socket("localhost",PORT);


       } catch(ConnectException e){
          JOptionPane.showMessageDialog(null, "Server not ready");
           exit(0);
       }
        //create printwriter for sending login to server

        output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_16));
        //prompt for user name
        String username = createCommand("LOGIN",JOptionPane.showInputDialog(null, "Enter User Name:"));

        //send user name to server
        output.println(username);

        output.flush();
        

        //create Buffered reader for reading response from server
        read = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        //read response from server
        String response = read.readLine();
        System.out.println("This is the response: " + response);

        //display response
        JOptionPane.showMessageDialog(null, response);
    }

    public void fileInfo(){

    }

    public static void main(String args[]){
        ChatClient client = new ChatClient();
        try {
            client.startClient();
        }
        catch (UnknownHostException e) {
            // TODO Auto-generated catch block
         
            e.printStackTrace();
        } catch (IOException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       
    }
}


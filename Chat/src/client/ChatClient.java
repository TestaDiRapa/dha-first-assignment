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
import static common.CommandParser.createCommand;
import static java.lang.System.exit;
import java.net.ConnectException;
import java.nio.charset.StandardCharsets;

public class ChatClient {

    private final String FILENAME = null;
    Socket socket;
    BufferedReader read;
    PrintWriter output;
    //CommandParser command=new CommandParser();

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
        read = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_16));

        //read response from server
        String response = read.readLine();
       
        
        //display response
        JOptionPane.showMessageDialog(null,"This is the response: " + response);
 
        //Da qui si modifica quando si fa la GUI
        String command,realCommand;
              command=JOptionPane.showInputDialog(null, "Write the command:");
               while(!command.equals(LOGOUT)) {
                   switch(command){
                       case "BROADCAST":  
                                             realCommand= createCommand(command,JOptionPane.showInputDialog(null, "Write the message:"));
                                            output.println(realCommand);
                                            output.flush();
                                             response = read.readLine();
                
                                             JOptionPane.showMessageDialog(null,"This is the response: " + response);
                                              break;
                       case "ONETOONE": 
                                             realCommand= createCommand(command,JOptionPane.showInputDialog(null, "Write the sender:"),JOptionPane.showInputDialog(null, "Write the message:"));
                           
                                             output.println(realCommand);
                                             output.flush();
                
                                            response = read.readLine();
                
                                             JOptionPane.showMessageDialog(null,"This is the response: " + response);
                                              break;
                           
                       default :JOptionPane.showMessageDialog(null, "Error command");
                           

               }
                
        
                command=JOptionPane.showInputDialog(null, "Write the command:");   
                
        }
        
         
         output.println(createCommand("LOGOUT"));
         output.flush();
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


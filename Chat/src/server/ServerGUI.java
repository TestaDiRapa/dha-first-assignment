package server;

import javax.swing.*;
import java.util.Set;


public class ServerGUI {

    private JLabel userLabel;
    private JTextArea userList;
    private JLabel logLabel;
    private JTextArea logArea;
    private JPanel mainFrame;

    public ServerGUI(){
        new Thread(new ChatServer(this)).start();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("ServerGUI");
        frame.setContentPane(new ServerGUI().mainFrame);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void updateUsers(Set<String> usernames){
        userList.setText("");
        for(String user : usernames){
            userList.append(user+"\n");
        }
    }


    public void addEvent(String event){
        logArea.append(event+"\n");
    }

}

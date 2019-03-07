package server;

import javax.swing.*;
import java.util.HashSet;
import java.util.Set;

public class ServerGUI {

    private JLabel userLabel;
    private JList userList;
    private JLabel logLabel;
    private JTextArea logArea;

    public ServerGUI(){
        usernames = new HashSet<>();
    }

    public void addUser(String username){
        userList.add(new JLabel(username));
    }

}

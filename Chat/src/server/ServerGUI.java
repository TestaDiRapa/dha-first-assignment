package server;

import javax.swing.*;
import java.util.HashSet;
import java.util.Set;

public class ServerGUI {

    private JLabel userLabel;
    private JList userList;
    private JLabel logLabel;
    private JTextArea logArea;
    private Set<String> usernames;

    public ServerGUI(){
        usernames = new HashSet<>();
    }

    public void addUser(String username){
        usernames.add(username);

    }

}

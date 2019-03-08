/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;
import common.Constants;

import static common.CommandParser.createCommand;
import static common.CommandParser.extractCommand;
import static common.Constants.PORT;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import static java.lang.System.exit;
import java.net.ConnectException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author Alex1
 */
public class ClientGUI extends javax.swing.JFrame {
    private Socket socket;
    private PrintWriter output;
    private String username;
    private String command;
    private String windows = "";
           
    
    /**
     * Creates new form ClientGUI
     */
    private ClientGUI() throws IOException {
        initComponents();
        
        try{
            socket = new Socket("localhost",PORT);
       } catch(ConnectException e){
          JOptionPane.showMessageDialog(null, "Server not ready");
           exit(0);
       }
        //Initializes I/O streams
        output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_16));
        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_16));

        String response = null;
        while(response ==null){
            //prompt for user name
            username=JOptionPane.showInputDialog(null, "Enter User Name:");
            command= createCommand("LOGIN",username);

            //send user name to server
            output.println(command);
            output.flush();

            //read response from server
            response = input.readLine();

            if (extractCommand(response).equals(Constants.ERROR)){
                response = null;
                JOptionPane.showMessageDialog(null, "Login error!\nTry changing username");
            }
        }
        
        new Thread(new ReaderThread(input, this)).start();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        inputArea = new javax.swing.JTextArea();
        sendButton = new javax.swing.JButton();
        logoutButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        chatArea = new javax.swing.JTextArea();
        broadcastButton = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        inputArea.setColumns(20);
        inputArea.setRows(5);
        jScrollPane1.setViewportView(inputArea);

        sendButton.setText("Send");
        sendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendButtonActionPerformed(evt);
            }
        });

        logoutButton.setText("Logout");
        logoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutButtonActionPerformed(evt);
            }
        });

        chatArea.setColumns(20);
        chatArea.setRows(5);
        jScrollPane2.setViewportView(chatArea);

        broadcastButton.setText("Send broadcast message");
        broadcastButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                broadcastButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("Receiver:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 723, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(46, 46, 46)
                                .addComponent(logoutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(56, 56, 56)
                                .addComponent(sendButton, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(broadcastButton)))))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 419, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(broadcastButton)
                        .addGap(27, 27, 27)
                        .addComponent(logoutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)
                            .addComponent(sendButton))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void logoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutButtonActionPerformed
        // TODO add your handling code here:
        
         output.println(createCommand("LOGOUT"));
         output.flush();
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
         this.dispose();
    }//GEN-LAST:event_logoutButtonActionPerformed

    public synchronized void writeOnChat(String message){
        windows = windows + message + "\n";
        chatArea.setText(windows);
    }

    private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendButtonActionPerformed

        if(jTextField1.getText() != null && inputArea.getText() != null){
            command=createCommand("ONETOONE", jTextField1.getText(), inputArea.getText());
            writeOnChat(username+" to "+jTextField1.getText() + " : " + inputArea.getText());
            output.println(command);
            output.flush();

        }
       
    }//GEN-LAST:event_sendButtonActionPerformed

    private void broadcastButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_broadcastButtonActionPerformed
        if(inputArea.getText() != null) {
            command = createCommand("BROADCAST", inputArea.getText());
            writeOnChat("BROADCAST "+"by "+username+": "+inputArea.getText());
            output.println(command);
            output.flush();
        }
    }//GEN-LAST:event_broadcastButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ClientGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ClientGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ClientGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ClientGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            try{
            new ClientGUI().setVisible(true);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
        
        
        
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton broadcastButton;
    private javax.swing.JTextArea chatArea;
    private javax.swing.JTextArea inputArea;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JButton logoutButton;
    private javax.swing.JButton sendButton;
    // End of variables declaration//GEN-END:variables
}

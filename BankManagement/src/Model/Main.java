package Model;


import View.StartFrame;

import java.awt.EventQueue;

import javax.swing.JButton;

public class Main {

    public static void main(String[] args) {
        DatabaseConnection dbConnection = new DatabaseConnection(); 
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    StartFrame frame = new StartFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
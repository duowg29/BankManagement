package View;

import Model.User;

import javax.swing.*;

import Controller.CRUD;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

public class AddMoneyView extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private User user;
    private JTextField amountField;

    public AddMoneyView(User user) {
        this.user = user;

        setTitle("Add Money");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);

        // Load background image
        ImageIcon backgroundImage = new ImageIcon("resources\\SignUpView2.png"); // Replace with your image path
        Image scaledImage = backgroundImage.getImage().getScaledInstance(600,600, Image.SCALE_SMOOTH);
        ImageIcon scaledBackgroundImage = new ImageIcon(scaledImage);

        // Set background image using a JLabel
        JLabel backgroundLabel = new JLabel(scaledBackgroundImage);
        backgroundLabel.setBounds(0, 0, 400, 300);
        setContentPane(backgroundLabel);
        backgroundLabel.setLayout(null);

        // Amount field
        JLabel amountLabel = new JLabel("Nhập số tiền:");
        amountLabel.setFont(new Font("Arial", Font.BOLD, 16));
        amountLabel.setBounds(130, 230, 130, 30);
        backgroundLabel.add(amountLabel);

        amountField = new JTextField();
        amountField.setBounds(250, 230, 200, 30);
        amountField.setFont(new Font("Arial", Font.BOLD, 16));
        backgroundLabel.add(amountField);

        // Confirm button
        RoundedButton confirmButton = new RoundedButton("Xác nhận");
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setBackground(new Color(80, 0, 255));
        confirmButton.setFont(new Font("Arial", Font.BOLD, 16));
        confirmButton.setBounds(235, 350, 130, 30);
        backgroundLabel.add(confirmButton);
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
					handleAddMoney();
				} catch (ClassNotFoundException | SQLException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
        
     // Back button
        RoundedButton backButton = new RoundedButton("Quay lại");
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(255, 100, 0));
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setBounds(250, 500, 100, 30);
        backgroundLabel.add(backButton);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	goBackToMain();
            }
        });

        setVisible(true);
    }

    private void handleAddMoney() throws ClassNotFoundException, SQLException, IOException {
        String amountText = amountField.getText();
        try {
            BigDecimal amount = new BigDecimal(amountText);

            int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to add " + amount + " to your account?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                user.setBalance(user.getBalance().add(amount));
                CRUD.updateUserBalance(user);
                JOptionPane.showMessageDialog(this, "Successfully added " + amount + " to your account.");
                goBackToMain();
                dispose(); // Close the AddMoneyView
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void goBackToMain() {
        new UserMainFrame(user);
        dispose();
    }
}
package View;

import javax.swing.*;
import java.math.BigDecimal;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Model.User;
import Controller.SavingAccountController;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SavingAccountMain extends JFrame {

    private User user;

    public SavingAccountMain(User user) {
        this.user = user;

        setTitle("Open Savings Account");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);
        setLayout(null);

        // Load background image
        ImageIcon backgroundImage = new ImageIcon("resources\\StartFrame.png");
        Image scaledImage = backgroundImage.getImage().getScaledInstance(600, 600, Image.SCALE_SMOOTH);
        ImageIcon scaledBackgroundImage = new ImageIcon(scaledImage);

        // Set background image using a JLabel
        JLabel backgroundLabel = new JLabel(scaledBackgroundImage);
        backgroundLabel.setBounds(0, 0, 800, 600);
        setContentPane(backgroundLabel);
        backgroundLabel.setLayout(null);

        // Panel to hold the input field and label
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBounds(130, 240, 320, 40);
        mainPanel.setOpaque(false);
        backgroundLabel.add(mainPanel);

        // Label for entering the amount
        JLabel amountLabel = new JLabel("Nhập số tiền muốn gửi:");
        amountLabel.setForeground(Color.BLACK);
        amountLabel.setFont(new Font("Arial", Font.BOLD, 16));
        amountLabel.setBounds(0, 0, 180, 30);
        mainPanel.add(amountLabel);

        // TextField for entering the amount
        JTextField amountField = new JTextField();
        amountField.setFont(new Font("Arial", Font.BOLD, 16));
        amountField.setBounds(190, 0, 130, 30);
        mainPanel.add(amountField);

        // Button to confirm opening the savings account
        RoundedButton confirmButton = new RoundedButton("Xác nhận");
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setBackground(new Color(80, 0, 255));
        confirmButton.setFont(new Font("Arial", Font.BOLD, 16));
        confirmButton.setBounds(235, 370, 130, 30);
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmOpening(amountField.getText());
            }
        });
        backgroundLabel.add(confirmButton);

        // Back Button
        RoundedButton backButton = new RoundedButton("Quay lại");
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(255, 100, 0));
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setBounds(250, 500, 100, 30);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBackToMain();
            }
        });
        backgroundLabel.add(backButton);

        setVisible(true);
    }

    private void confirmOpening(String amountStr) {
        int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to open a savings account with initial deposit of " + amountStr + "?", "Confirm Opening Savings Account", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            try {
                BigDecimal initialDeposit = new BigDecimal(amountStr);
                SavingAccountController.openSavingAccount(user.getAccountID(), initialDeposit);
                // Get current date and time
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDateTime = now.format(formatter);

                JOptionPane.showMessageDialog(null, "Savings account opened successfully at " + formattedDateTime);
                goBackToMain();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid amount format. Please enter a valid number.");
            }
        }
    }

    private void goBackToMain() {
        new UserMainFrame(user);
        dispose();
    }
}
package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import Controller.CRUD;

public class SignUpView2 extends JFrame {

    private String citizenID; 

    public SignUpView2(String citizenID) {
        this.citizenID = citizenID;

        // Set the title of the window
        setTitle("Đăng ký tài khoản");

        // Set the size of the window
        setSize(800, 639);

        setResizable(false);
        setLocationRelativeTo(null);

        // Load and scale the background image
        ImageIcon backgroundImage = new ImageIcon("resources\\SignUpView2.png"); // Replace with your image path
        Image scaledImage = backgroundImage.getImage().getScaledInstance(800, 600, Image.SCALE_SMOOTH);
        ImageIcon scaledBackgroundImage = new ImageIcon(scaledImage);

        // Set background image using a JLabel
        JLabel backgroundLabel = new JLabel(scaledBackgroundImage);
        backgroundLabel.setBounds(0, 0, 800, 600);
        // Set the layout to null to use absolute positioning
        setLayout(null);
        add(backgroundLabel);

        // Set font
        Font font = new Font("Tahoma", Font.BOLD, 15);

        // Add the "Back" label
        JLabel backLabel = new JLabel("> Quay lại");
        backLabel.setForeground(new Color(255, 155, 0));
        backLabel.setFont(font);
        backLabel.setBounds(10, 10, 100, 20);
        backgroundLabel.add(backLabel);

        // Add the "Sign Up" label
        JLabel signUpLabel = new JLabel("Đăng ký tài khoản", SwingConstants.CENTER);
        signUpLabel.setForeground(new Color(80, 0, 255));
        signUpLabel.setFont(font);
        signUpLabel.setBounds(300, 160, 200, 20);
        backgroundLabel.add(signUpLabel);

        // Create and position the account number label and text field
        JLabel accountNumberLabel = new JLabel("Số tài khoản: ");
        accountNumberLabel.setForeground(new Color(80, 0, 255));
        accountNumberLabel.setFont(font);
        accountNumberLabel.setBounds(195, 210, 200, 25);
        backgroundLabel.add(accountNumberLabel);

        JTextField accountNumberField = new JTextField();
        accountNumberField.setBounds(415, 210, 200, 25);
        backgroundLabel.add(accountNumberField);

        // Create and position the username label and text field
        JLabel nameLabel = new JLabel("Tên người dùng: ");
        nameLabel.setForeground(new Color(80, 0, 255));
        nameLabel.setFont(font);
        nameLabel.setBounds(195, 260, 200, 25);
        backgroundLabel.add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(415, 260, 200, 25);
        backgroundLabel.add(nameField);

        // Create and position the password label and password field
        JLabel passwordLabel = new JLabel("Mật khẩu: ");
        passwordLabel.setForeground(new Color(80, 0, 255));
        passwordLabel.setFont(font);
        passwordLabel.setBounds(195, 310, 200, 25);
        backgroundLabel.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(415, 310, 200, 25);
        backgroundLabel.add(passwordField);

        // Create and position the "Xác nhận" button using RoundedButton
        RoundedButton confirmButton = new RoundedButton("Xác nhận");
        confirmButton.setFont(font);
        confirmButton.setBackground(new Color(80, 0, 255)); // Example: Set button background color
        confirmButton.setForeground(Color.WHITE); // Example: Set button text color
        confirmButton.setBounds(325, 380, 150, 30); // Adjusted Y position for the button
        backgroundLabel.add(confirmButton);

        // Set default close operation
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Add action listener to the "Xác nhận" button
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String accountNumberStr = accountNumberField.getText();
                String username = nameField.getText();
                String password = new String(passwordField.getPassword());

                // Validate account number length and digits only
                if (!isValidAccountNumber(accountNumberStr)) {
                    JOptionPane.showMessageDialog(null, "Số tài khoản chỉ nằm từ 10 ~ 15 ký tự và chỉ nhập số!");
                    return;
                }

                long accountID = Long.parseLong(accountNumberStr);

                CRUD crud = new CRUD();
                boolean success = false;
                try {
                    success = crud.signUpAccount(accountID, username, password, "User", "Opened", citizenID);
                } catch (ClassNotFoundException | IOException e1) {
                    e1.printStackTrace();
                }

                if (success) {
                    JOptionPane.showMessageDialog(null, "Đăng ký tài khoản thành công!");
                    new StartFrame();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Đăng ký tài khoản thất bại!");
                }
            }

            // Helper method to validate account number
            private boolean isValidAccountNumber(String accountNumber) {
                // Check length
                if (accountNumber.length() < 10 || accountNumber.length() > 15) {
                    return false;
                }
                // Check digits only
                return accountNumber.matches("\\d+");
            }
        });

        // Add mouse listener to the "Back" label
        backLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                CRUD crud = new CRUD();
                boolean success = false;
                try {
                    success = crud.deleteUser(citizenID);
                } catch (ClassNotFoundException | IOException e1) {
                    e1.printStackTrace();
                }
                if (success) {
                    System.out.println("User with citizenID " + citizenID + " deleted successfully.");
                } else {
                    System.out.println("Failed to delete user with citizenID " + citizenID + ".");
                }
                new StartFrame();
                dispose();
            }
        });

        // Set the window to be visible
        setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            new SignUpView2("1234567890"); // Thay 1234567890 bằng citizenID đã nhận được dưới dạng String
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

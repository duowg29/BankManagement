package View;

import Model.User;
import Model.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

public class UserInfo extends JFrame {

    private User user;

    public UserInfo(User user) {
        this.user = user;

        setTitle("User Information");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);
        setLayout(null);

        // Load background image
        ImageIcon backgroundImage = new ImageIcon("resources\\Menu.png"); // Replace with your image path
        Image scaledImage = backgroundImage.getImage().getScaledInstance(800, 600, Image.SCALE_SMOOTH);
        ImageIcon scaledBackgroundImage = new ImageIcon(scaledImage);

        // Set background image using a JLabel
        JLabel backgroundLabel = new JLabel(scaledBackgroundImage);
        backgroundLabel.setBounds(0, 0, 800, 600);
        setContentPane(backgroundLabel);
        backgroundLabel.setLayout(null);

        // User Info Label
        JLabel userInfoLabel = createUserInfoLabel();
        userInfoLabel.setBounds(120, 140, 380, 250);
        backgroundLabel.add(userInfoLabel);

        // Back Button
        RoundedButton backButton = new RoundedButton("Quay lại");
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(80, 0, 255));
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setBounds(250, 450, 100, 30); // Adjust x-coordinate from 150 to 170
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new UserMainFrame(user);
            }
        });
        backgroundLabel.add(backButton);

        // Apply the font settings to all components
        setFontRecursive(userInfoLabel, new Font("Arial", Font.BOLD, 16));

        setVisible(true);
    }

    private JLabel createUserInfoLabel() {
        JLabel label = new JLabel();
        label.setLayout(new GridLayout(8, 2, 10, 10)); // GridLayout for user info fields
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        label.add(new JLabel("Mã tài khoản:"));
        label.add(new JLabel(String.valueOf(user.getAccountID())));

        label.add(new JLabel("Tên tài khoản:"));
        label.add(new JLabel(user.getUsername()));

        JLabel balanceLabel = new JLabel();
        BigDecimal balance = user.getBalance();
        if (balance != null) {
            balanceLabel.setText(balance.toString());
        } else {
            balanceLabel.setText("N/A"); // Handle null balance
        }
        label.add(new JLabel("Số dư:"));
        label.add(balanceLabel);

        label.add(new JLabel("Họ và Tên:"));
        label.add(new JLabel(user.getFullName()));

        label.add(new JLabel("Giới tính:"));
        label.add(new JLabel(user.getGender()));

        label.add(new JLabel("Ngày sinh:"));
        label.add(new JLabel(user.getDateOfBirth()));

        label.add(new JLabel("Địa chỉ:"));
        label.add(new JLabel(user.getAddress()));

        label.add(new JLabel("Số điện thoại:"));
        label.add(new JLabel(user.getPhoneNumbers()));

        return label;
    }

    private void setFontRecursive(Component component, Font font) {
        component.setFont(font);
        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                setFontRecursive(child, font);
            }
        }
    }
}
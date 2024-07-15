package View;

import javax.swing.*;
import Controller.TransactionController;
import Model.User;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

public class GDOTPTransaction extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private RoundedButton submitButton;
    private RoundedButton cancelButton;
    private RoundedButton resetOtpButton;
    private long senderAccount;
    private long receiverAccount;
    private BigDecimal amount;
    private JPasswordField passwordField_otp;
    private User user;
    private long AccountId;

    public GDOTPTransaction(GDTransaction gdTransaction, User user) {
        this.user = user;
        setTitle("OTP Verification");
        setSize(389, 164);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Load and scale background image
        ImageIcon backgroundImage = new ImageIcon("resources\\Menu.png"); // Replace with your image path
        Image scaledImage = backgroundImage.getImage().getScaledInstance(800, 600, Image.SCALE_SMOOTH);
        ImageIcon scaledBackgroundImage = new ImageIcon(scaledImage);

        // Create main panel with background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(scaledBackgroundImage.getImage(), 0, 0, null);
            }
        };
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        getContentPane().add(mainPanel);
        mainPanel.setLayout(null);

        JPanel otpPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        otpPanel.setBounds(20, 19, 347, 47);
        otpPanel.setOpaque(false); // Make panel transparent
        mainPanel.add(otpPanel);

        JLabel otpLabel = new JLabel("OTP:");
        otpLabel.setForeground(new Color(255, 255, 255));
        otpLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        otpPanel.add(otpLabel);

        passwordField_otp = new JPasswordField(10);
        passwordField_otp.setFont(new Font("Tahoma", Font.PLAIN, 15));
        otpPanel.add(passwordField_otp);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBounds(20, 77, 347, 47);
        buttonPanel.setOpaque(false); // Make panel transparent
        mainPanel.add(buttonPanel);
        buttonPanel.setLayout(null);

        submitButton = new RoundedButton("Xác nhận");
        submitButton.setBackground(new Color(255, 155, 0)); 
        submitButton.setForeground(new Color(0, 0, 0)); 
        submitButton.setBounds(10, 10, 95, 27);
        submitButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
        buttonPanel.add(submitButton);

        cancelButton = new RoundedButton("Quay lại");
        cancelButton.setBackground(new Color(255, 155, 0));
        cancelButton.setForeground(new Color(0, 0, 0)); 
        cancelButton.setBounds(236, 10, 87, 27);
        cancelButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
        buttonPanel.add(cancelButton);

        resetOtpButton = new RoundedButton("Đặt lại OTP");
        resetOtpButton.setBackground(new Color(255, 155, 0));
        resetOtpButton.setForeground(new Color(0, 0, 0)); 
        resetOtpButton.setBounds(115, 10, 109, 27);
        resetOtpButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
        buttonPanel.add(resetOtpButton);

        submitButton.addActionListener(this);
        cancelButton.addActionListener(this);
        resetOtpButton.addActionListener(this);

        senderAccount = gdTransaction.getSenderAccount();
        receiverAccount = gdTransaction.getReceiverAccount();
        amount = gdTransaction.getAmount();
        AccountId = user.getAccountID();
    }

    public char[] getOtp() {
        return passwordField_otp.getPassword();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("Xác nhận")) {
            char[] otp = this.getOtp();
            if (TransactionController.validateOtp(otp, user.getAccountID())) {
                TransactionController.performTransaction(senderAccount, receiverAccount, amount, AccountId);
                int option = JOptionPane.showOptionDialog(this, 
                        "Giao dịch thành công!", 
                        "Success", 
                        JOptionPane.YES_NO_OPTION, 
                        JOptionPane.INFORMATION_MESSAGE, 
                        null, 
                        new String[]{"Quay lại trang chủ", "Tạo giao dịch khác"}, 
                        "Quay lại trang chủ");

                if (option == JOptionPane.YES_OPTION) {
                    new GDMain(user).setVisible(true); // Assuming GDHomePage is the homepage class
                } else if (option == JOptionPane.NO_OPTION) {
                    new GDTransaction(user).setVisible(true);
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid OTP. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                reset();
            }
        } else if (command.equals("Quay lại")) {
            new GDTransaction(user).setVisible(true);
            dispose();
        } else if (command.equals("Đặt lại OTP")) {
            try {
                TransactionController.deleteOldOtpFromDatabase(user.getAccountID());
                JOptionPane.showMessageDialog(this, "OTP reset successful. Please register a new OTP.", "Success", JOptionPane.INFORMATION_MESSAGE);
                new GDOTPManagerUI(user).setVisible(true);
                dispose();
            } catch (ClassNotFoundException | SQLException | IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to reset OTP. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void reset() {
        passwordField_otp.setText("");
    }
}

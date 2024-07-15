package View;

import javax.swing.*;
import Controller.TransactionController;
import Model.User;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

public class GDOTPManagerUI extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private JPasswordField passwordFieldOtp;
    private JPasswordField passwordFieldConfirmOtp;
    private RoundedButton saveButton;
    private RoundedButton cancelButton;
    private User user; // User instance

    public GDOTPManagerUI(User user) {
        this.user = user; // Assign user to instance variable

        setTitle("Create OTP");
        setSize(543, 345); // Set appropriate size for background image
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Load and scale background image
        ImageIcon backgroundImage = new ImageIcon("resources/NoPanel.png"); // Update to your image path
        Image scaledImage = backgroundImage.getImage().getScaledInstance(1100, 1300, Image.SCALE_SMOOTH);
        ImageIcon scaledBackgroundImage = new ImageIcon(scaledImage);

        // Set background image using JLabel
        JLabel backgroundLabel = new JLabel(scaledBackgroundImage);
        backgroundLabel.setBounds(0, 0, 788, 565);
        backgroundLabel.setLayout(new BorderLayout());

        // Create panel for input fields
        JPanel panel = new JPanel();
        panel.setOpaque(false); // Make panel transparent
        panel.setLayout(null);

        // Add OTP Label and Field
        JLabel otpLabel = new JLabel("Nhập OTP:");
        otpLabel.setForeground(new Color(255, 255, 255));
        otpLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        otpLabel.setBounds(91, 71, 112, 43);
        otpLabel.setHorizontalAlignment(SwingConstants.LEFT);
        panel.add(otpLabel);

        passwordFieldOtp = new JPasswordField(20);
        passwordFieldOtp.setBounds(264, 79, 174, 30);
        panel.add(passwordFieldOtp);

        // Add Confirm OTP Label and Field
        JLabel confirmOtpLabel = new JLabel("Xác nhận OTP:");
        confirmOtpLabel.setForeground(new Color(255, 255, 255));
        confirmOtpLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        confirmOtpLabel.setBounds(91, 151, 112, 43);
        confirmOtpLabel.setHorizontalAlignment(SwingConstants.LEFT);
        panel.add(confirmOtpLabel);

        passwordFieldConfirmOtp = new JPasswordField(20);
        passwordFieldConfirmOtp.setBounds(264, 157, 174, 30);
        panel.add(passwordFieldConfirmOtp);
        getContentPane().setLayout(null);

        // Add panels to background label
        backgroundLabel.add(panel, BorderLayout.CENTER);
        
                // Create button panel
                JPanel buttonPanel = new JPanel();
                buttonPanel.setBounds(0, 231, 531, 63);
                panel.add(buttonPanel);
                buttonPanel.setOpaque(false); // Make button panel transparent
                
                        saveButton = new RoundedButton("Lưu");
                        saveButton.setBackground(new Color(255, 155, 0)); // Đặt màu cho nút
                        saveButton.setForeground(new Color(0, 0, 0)); // Đặt màu chữ là trắng
                        saveButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
                        saveButton.setBounds(88, 11, 122, 41);
                        saveButton.addActionListener(this);
                        buttonPanel.setLayout(null);
                        buttonPanel.add(saveButton);
                        
                                cancelButton = new RoundedButton("Quay lại");
                                cancelButton.setBackground(new Color(255, 155, 0)); // Đặt màu cho nút
                                cancelButton.setForeground(new Color(0, 0, 0)); // Đặt màu chữ là trắng
                                cancelButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
                                cancelButton.setBounds(310, 11, 122, 41);
                                cancelButton.addActionListener(this);
                                buttonPanel.add(cancelButton);

        // Add background label to content pane
        getContentPane().add(backgroundLabel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("Lưu")) {
            char[] otp = passwordFieldOtp.getPassword();
            char[] confirmOtp = passwordFieldConfirmOtp.getPassword();

            if (otp.length == 0 || confirmOtp.length == 0) {
                JOptionPane.showMessageDialog(this, "Hãy điền vào cả 2 ô nhập.", "Lỗi!", JOptionPane.ERROR_MESSAGE);
                reset();
            } else if (!isNumeric(otp) || !isNumeric(confirmOtp)) {
                JOptionPane.showMessageDialog(this, "Chỉ được phép nhập số.", "Lỗi!", JOptionPane.ERROR_MESSAGE);
                reset();
            } else if (!Arrays.equals(otp, confirmOtp)) {
                JOptionPane.showMessageDialog(this, "Phải trùng khớp thông tin nhập OTP và ô xác nhận.", "Lỗi!", JOptionPane.ERROR_MESSAGE);
                reset();
            } else {
                try {
                    if (TransactionController.isOtpExistsInDatabase(otp, user.getAccountID())) {
                        JOptionPane.showMessageDialog(this, "OTP đã tồn tại", "Lỗi!", JOptionPane.ERROR_MESSAGE);
                        reset();
                    } else {
                        TransactionController.setUserOtp(otp);
                        TransactionController.saveOtpToDatabase(otp, user.getAccountID());
                        JOptionPane.showMessageDialog(this, "Lưu OTP thành công", "Thành công!", JOptionPane.INFORMATION_MESSAGE);
                        dispose(); // Close current window
                        new GDMain(user).setVisible(true); // Show GDMain window with user
                    }
                } catch (HeadlessException | IOException | ClassNotFoundException | SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi thực hiện nhập OTP. Vui lòng nhập lại.", "Lỗi!", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (command.equals("Quay lại")) {
            new GDMain(user).setVisible(true); // Show GDMain window with user
            dispose();
        }
    }

    private boolean isNumeric(char[] otp) {
        for (char c : otp) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    private void reset() {
        passwordFieldOtp.setText("");
        passwordFieldConfirmOtp.setText("");
    }
}

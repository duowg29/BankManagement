package View;

import javax.swing.*;
import Controller.TransactionController;
import Model.User;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

public class GDConfirmTransaction extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private JLabel senderAccountLabel;
    private JLabel senderNameLabel;
    private JLabel receiverAccountLabel;
    private JLabel receiverNameLabel;
    private JLabel amountLabel;
    private JLabel balanceLabel;
    private RoundedButton confirmButton;
    private RoundedButton cancelButton;
    private GDTransaction gdTransaction;
    private JTextField textField_balance;
    private JTextField textField_senderAccount;
    private JTextField textField_senderName;
    private JTextField textField_receiverAccount;
    private JTextField textField_receiverName;
    private JTextField textField_amount;
    private User user;

    public GDConfirmTransaction(GDTransaction gdTransaction, User user, long senderAccount, String senderName, long receiverAccount,
                                String receiverName, BigDecimal amount, BigDecimal balance) {
        this.gdTransaction = gdTransaction;
        this.user = user; 
        setTitle("Xác nhận giao dịch");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel backgroundLabel = new JLabel();
        backgroundLabel.setBounds(0, 0, 588, 365);
        ImageIcon backgroundImage = new ImageIcon("resources/NoPanel.png"); // Thay đổi đường dẫn và tên hình ảnh của bạn
        Image scaledImage = backgroundImage.getImage().getScaledInstance(800, 900, Image.SCALE_SMOOTH);
        ImageIcon scaledBackgroundImage = new ImageIcon(scaledImage);
        getContentPane().setLayout(null);
        backgroundLabel.setIcon(scaledBackgroundImage);
        backgroundLabel.setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel();
        contentPanel.setBounds(0, 0, 588, 272);
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(50, 40, 40, 40)); // Đặt lề cho panel nội dung
        contentPanel.setLayout(null);

        senderAccountLabel = new JLabel("Tài khoản gửi:");
        senderAccountLabel.setBounds(95, 52, 157, 27);
        senderAccountLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        senderAccountLabel.setForeground(new Color(255, 155, 0)); // Màu chữ trắng
        contentPanel.add(senderAccountLabel);

        textField_senderAccount = new JTextField(String.valueOf(senderAccount));
        textField_senderAccount.setBounds(299, 52, 239, 27);
        textField_senderAccount.setFont(new Font("Tahoma", Font.PLAIN, 15));
        textField_senderAccount.setEditable(false);
        contentPanel.add(textField_senderAccount);

        senderNameLabel = new JLabel("Tên người gửi:");
        senderNameLabel.setBounds(95, 89, 157, 27);
        senderNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        senderNameLabel.setForeground(new Color(255, 155, 0)); // Màu chữ trắng
        contentPanel.add(senderNameLabel);

        textField_senderName = new JTextField(senderName);
        textField_senderName.setBounds(299, 89, 239, 27);
        textField_senderName.setFont(new Font("Tahoma", Font.PLAIN, 15));
        textField_senderName.setEditable(false);
        contentPanel.add(textField_senderName);

        receiverAccountLabel = new JLabel("Tài khoản nhận:");
        receiverAccountLabel.setBounds(95, 126, 157, 27);
        receiverAccountLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        receiverAccountLabel.setForeground(new Color(255, 155, 0)); // Màu chữ trắng
        contentPanel.add(receiverAccountLabel);

        textField_receiverAccount = new JTextField(String.valueOf(receiverAccount));
        textField_receiverAccount.setBounds(299, 126, 239, 27);
        textField_receiverAccount.setFont(new Font("Tahoma", Font.PLAIN, 15));
        textField_receiverAccount.setEditable(false);
        contentPanel.add(textField_receiverAccount);

        receiverNameLabel = new JLabel("Tên người nhận:");
        receiverNameLabel.setBounds(95, 163, 157, 27);
        receiverNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        receiverNameLabel.setForeground(new Color(255, 155, 0)); // Màu chữ trắng
        contentPanel.add(receiverNameLabel);

        textField_receiverName = new JTextField(receiverName);
        textField_receiverName.setBounds(299, 163, 239, 27);
        textField_receiverName.setFont(new Font("Tahoma", Font.PLAIN, 15));
        textField_receiverName.setEditable(false);
        contentPanel.add(textField_receiverName);

        amountLabel = new JLabel("Số tiền giao dịch:");
        amountLabel.setBounds(95, 200, 157, 27);
        amountLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        amountLabel.setForeground(new Color(255, 155, 0)); // Màu chữ trắng
        contentPanel.add(amountLabel);

        textField_amount = new JTextField(amount.toString());
        textField_amount.setBounds(299, 200, 239, 27);
        textField_amount.setFont(new Font("Tahoma", Font.PLAIN, 15));
        textField_amount.setEditable(false);
        contentPanel.add(textField_amount);

        balanceLabel = new JLabel("Số dư:");
        balanceLabel.setBounds(95, 237, 157, 27);
        balanceLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        balanceLabel.setForeground(new Color(255, 155, 0)); // Màu chữ trắng
        contentPanel.add(balanceLabel);

        textField_balance = new JTextField(balance.toString());
        textField_balance.setBounds(299, 237, 239, 27);
        textField_balance.setFont(new Font("Tahoma", Font.PLAIN, 15));
        textField_balance.setEditable(false);
        contentPanel.add(textField_balance);


        JPanel buttonPanel = new JPanel();
        buttonPanel.setBounds(0, 298, 588, 67);
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(null);

        confirmButton = new RoundedButton("Tiếp tục");
        confirmButton.setBounds(96, 11, 144, 45);
        confirmButton.setBackground(new Color(255, 155, 0)); 
        confirmButton.setForeground(new Color(0, 0, 0));
        confirmButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
        buttonPanel.add(confirmButton);

        cancelButton = new RoundedButton("Quay lại");
        cancelButton.setBounds(338, 11, 144, 45);
        cancelButton.setBackground(new Color(255, 155, 0)); 
        cancelButton.setForeground(new Color(0, 0, 0));
        cancelButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
        buttonPanel.add(cancelButton);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.add(contentPanel);
        mainPanel.add(buttonPanel);
        mainPanel.setOpaque(false);

        backgroundLabel.add(mainPanel, BorderLayout.CENTER);

        getContentPane().add(backgroundLabel);

        confirmButton.addActionListener(this);
        cancelButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("Tiếp tục")) {
            char[] userOtp = TransactionController.retrieveUserOtpFromDatabase(user.getAccountID());
            if (userOtp == null) {
                int choice = JOptionPane.showOptionDialog(
                        this,
                        "Bạn chưa đăng ký OTP. Vui lòng đăng ký hoặc để sau.",
                        "Thông báo",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null,
                        new String[]{"Đăng ký", "Để sau"},
                        "Đăng ký"
                );

                if (choice == JOptionPane.YES_OPTION) {
                    new GDOTPManagerUI(user).setVisible(true); 
                    dispose();
                }
            } else {
                new GDOTPTransaction(gdTransaction, user).setVisible(true);
                dispose();
            }
        } else if (command.equals("Quay lại")) {
            new GDTransaction(user).setVisible(true); 
            dispose();
        }
    }
}

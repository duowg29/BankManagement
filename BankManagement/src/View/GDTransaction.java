package View;

import javax.swing.*;

import Controller.CRUD;
import Controller.TransactionController;
import Model.User;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

public class GDTransaction extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private JTextField senderAccountField;
    private JTextField receiverAccountField;
    private JTextField amountField;
    private RoundedButton submitButton;
    private RoundedButton cancelButton;

    private GDConfirmTransaction confirmView;
    private User user;

    public GDTransaction(User user) {
        this.user = user;
        setTitle("Giao dịch chuyển khoản");
        setSize(500, 413);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 500, 500);
        setContentPane(layeredPane);

        ImageIcon backgroundImage = new ImageIcon("resources\\NoPanel.png"); // Thay bằng đường dẫn hình ảnh của bạn
        Image scaledImage = backgroundImage.getImage().getScaledInstance(800, 1100, Image.SCALE_SMOOTH);
        ImageIcon scaledBackgroundImage = new ImageIcon(scaledImage);

        JLabel backgroundLabel = new JLabel(scaledBackgroundImage);
        backgroundLabel.setBounds(0, 0, 500, 500);
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

        JPanel formPanel = new JPanel();
        formPanel.setBounds(23, 50, 442, 300);
        formPanel.setOpaque(false); 
        layeredPane.add(formPanel, JLayeredPane.PALETTE_LAYER);

        JLabel senderAccountLabel = new JLabel("Tài khoản gửi:");
        senderAccountLabel.setBounds(0, 1, 216, 53);
        senderAccountLabel.setForeground(new Color(255, 155, 0));
        senderAccountLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        
        
        senderAccountField = new JTextField(String.valueOf(this.user.getAccountID()));
        senderAccountField.setBounds(226, 1, 216, 53);
        senderAccountField.setBackground(new Color(192, 192, 192));
        senderAccountField.setEditable(false);
        senderAccountField.setFont(new Font("Tahoma", Font.PLAIN, 14));

        JLabel receiverAccountLabel = new JLabel("Tài khoản nhận:");
        receiverAccountLabel.setBounds(0, 78, 216, 53);
        receiverAccountLabel.setForeground(new Color(255, 155, 0));
        receiverAccountLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        receiverAccountField = new JTextField();
        receiverAccountField.setBounds(226, 78, 216, 53);
        receiverAccountField.setFont(new Font("Tahoma", Font.PLAIN, 14));

        JLabel amountLabel = new JLabel("Số tiền:");
        amountLabel.setBounds(0, 155, 216, 53);
        amountLabel.setForeground(new Color(255, 155, 0));
        amountLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        amountField = new JTextField();
        amountField.setBounds(226, 155, 216, 53);
        amountField.setFont(new Font("Tahoma", Font.PLAIN, 14));

        submitButton = new RoundedButton("Tiếp tục");
        submitButton.setBounds(0, 246, 216, 53);
        submitButton.setBackground(new Color(255, 155, 0));
        submitButton.setForeground(new Color(0, 0, 0));
        submitButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
        cancelButton = new RoundedButton("Quay lại");
        cancelButton.setBounds(226, 246, 216, 53);
        cancelButton.setBackground(new Color(255, 155, 0)); 
        cancelButton.setForeground(new Color(0, 0, 0)); 
        cancelButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
        formPanel.setLayout(null);

        formPanel.add(senderAccountLabel);
        formPanel.add(senderAccountField);
        formPanel.add(receiverAccountLabel);
        formPanel.add(receiverAccountField);
        formPanel.add(amountLabel);
        formPanel.add(amountField);
        formPanel.add(submitButton);
        formPanel.add(cancelButton);

        submitButton.addActionListener(this);
        cancelButton.addActionListener(this);
    }

    private boolean isInputValid() {
        try {
            Long.parseLong(senderAccountField.getText());
            Long.parseLong(receiverAccountField.getText());
            BigDecimal amount = new BigDecimal(amountField.getText());
            return amount.compareTo(BigDecimal.ZERO) >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void reset() {
        senderAccountField.setText(String.valueOf(user.getAccountID()));
        receiverAccountField.setText("");
        amountField.setText("");
    }

    public long getSenderAccount() throws NumberFormatException {
        return Long.parseLong(senderAccountField.getText());
    }

    public long getReceiverAccount() throws NumberFormatException {
        return Long.parseLong(receiverAccountField.getText());
    }

    public BigDecimal getAmount() {
        return new BigDecimal(amountField.getText());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cm = e.getActionCommand();
        if ("Tiếp tục".equals(cm)) {
            handleTransaction();
        } else if ("Quay lại".equals(cm)) {
            new GDMain(user).setVisible(true);
            dispose();
        }
    }

    

    private boolean isSenderSameAsLoggedInUser(long senderAccount, long receiverAccount) {
        return senderAccount == receiverAccount;
    }


    private void handleTransaction() {
        if (!isInputValid()) {
            JOptionPane.showMessageDialog(this, "Dữ liệu nhập không hợp lệ. Vui lòng kiểm tra lại các trường nhập.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            reset();
            return;
        }

        long senderAccount = getSenderAccount();
        long receiverAccount = getReceiverAccount();
        BigDecimal amount = getAmount();

        if (isSenderSameAsLoggedInUser(senderAccount,receiverAccount)) {
            JOptionPane.showMessageDialog(this, "Không thể chuyển khoản cho chính mình. Vui lòng nhập lại tài khoản nhận.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            reset();
            return;
        }

        User receiverUser;
        try {
            receiverUser = CRUD.getUserInfoByAccountID(receiverAccount);
            if (receiverUser == null) {
                JOptionPane.showMessageDialog(this, "Tài khoản nhận không tồn tại trong hệ thống. Vui lòng kiểm tra lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                reset();
                return;
            }
        } catch (ClassNotFoundException | IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi lấy thông tin người nhận. Vui lòng thử lại sau.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            reset();
            return;
        }

        if (senderAccount == receiverUser.getAccountID()) {
            JOptionPane.showMessageDialog(this, "Không thể chuyển khoản cho chính mình. Vui lòng nhập lại tài khoản nhận.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            reset();
            return;
        }

        try {
            BigDecimal balance = TransactionController.getSenderBalance(senderAccount);
            if (balance.compareTo(amount) >= 0) {
                String senderName = TransactionController.getSenderName(senderAccount);
                String receiverName = receiverUser.getFullName();

                confirmView = new GDConfirmTransaction(this, user, senderAccount, senderName, receiverAccount, receiverName, amount, balance);
                confirmView.setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Số dư không đủ để thực hiện giao dịch. Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                reset();
            }
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Có lỗi xảy ra trong quá trình xử lý giao dịch. Vui lòng thử lại sau.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }



    
    
    


}

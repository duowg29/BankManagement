package View;

import Controller.TransactionController;
import Model.User;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class GDMain extends JFrame {
private static final long serialVersionUID = 1L;
private RoundedButton startButton;
private RoundedButton otpButton;
private User user;
private char[] otpChars;
public GDMain(User user) {
    this.user = user;
    setTitle("Main Menu");
    setBackground(new Color(255, 255, 255));
    setSize(800, 639);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Load and scale the background image
    ImageIcon backgroundImage = new ImageIcon("resources\\Menu.png"); // Thay bằng đường dẫn tới hình ảnh của bạn
    Image scaledImage = backgroundImage.getImage().getScaledInstance(800, 600, Image.SCALE_SMOOTH);
    ImageIcon scaledBackgroundImage = new ImageIcon(scaledImage);
    getContentPane().setLayout(null);

    // Set background image using a JLabel
    JLabel backgroundLabel = new JLabel(scaledBackgroundImage);
    backgroundLabel.setBounds(0, 0, 800, 600);

    // Create JPanel for buttons
    JPanel buttonPanel = new JPanel();
    buttonPanel.setOpaque(false); 
    buttonPanel.setBounds(255, 140, 500, 400);
    buttonPanel.setLayout(null);

    // Create and add buttons to buttonPanel
    startButton = new RoundedButton("Bắt đầu giao dịch");
    startButton.setBounds(0, 0, 288, 60);
    startButton.setBackground(new Color(80, 0, 255)); 
    startButton.setForeground(new Color(255, 255, 255)); 
    startButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
    buttonPanel.add(startButton);

    RoundedButton btnNewButton_save = new RoundedButton("Tra cứu giao dịch");
    btnNewButton_save.setBounds(0, 84, 288, 60);
    btnNewButton_save.setFont(new Font("Tahoma", Font.PLAIN, 15));
    btnNewButton_save.setBackground(new Color(80, 0, 255)); 
    btnNewButton_save.setForeground(new Color(255, 255, 255));
    buttonPanel.add(btnNewButton_save);

    otpButton = new RoundedButton("Tạo OTP");
    otpButton.setBounds(0, 174, 288, 60);
    otpButton.setBackground(new Color(80, 0, 255)); 
    otpButton.setForeground(new Color(255, 255, 255)); 
    otpButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
    buttonPanel.add(otpButton);

    RoundedButton btnNewButton_back = new RoundedButton("Quay lại");
    btnNewButton_back.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent e) {
    		CancelTransaction();
    	}
    });
    btnNewButton_back.setBounds(0, 260, 288, 60);
    btnNewButton_back.setBackground(new Color(80, 0, 255)); 
    btnNewButton_back.setForeground(new Color(255, 255, 255)); 
    btnNewButton_back.setFont(new Font("Tahoma", Font.PLAIN, 15));
    buttonPanel.add(btnNewButton_back);

    backgroundLabel.add(buttonPanel);

    getContentPane().add(backgroundLabel);

    otpButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            openOtpManager();
        }
    });
    btnNewButton_save.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            openTransactionSave();
        }
    });

    startButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            openTransactionView();
        }
    });
    // Call retrieveUserOtpFromDatabase function and store OTP
    long accountID = user.getAccountID();
    otpChars = TransactionController.retrieveUserOtpFromDatabase(accountID);

    
}


private void openTransactionView() {
    GDTransaction transactionView = new GDTransaction(user);
    transactionView.setVisible(true);
    dispose();
}

private void openTransactionSave() {
    GDSaveTransaction gdSaveTransaction = new GDSaveTransaction(user);
    gdSaveTransaction.setVisible(true);
    dispose();
}

private void openOtpManager() {
    GDOTPManagerUI otpManager = new GDOTPManagerUI(user);
    otpManager.setVisible(true);
    dispose();
}

public void CancelTransaction() {
	new UserMainFrame(user).setVisible(true);;
	dispose();
}
public static void main(String[] args) {
    try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        GDMain mainFrame = new GDMain(new User());
        mainFrame.setVisible(true);
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}


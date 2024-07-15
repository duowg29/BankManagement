package View;

import Model.DatabaseConnection;
import Model.User;

import javax.swing.*;

import Controller.CRUD;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

public class UserMainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private User user;

	public UserMainFrame(User user) {
		this.user = user;
		if (user == null) {
			throw new IllegalArgumentException("User không được null");
		}
		setTitle("Bank Management System");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 639);
		setLocationRelativeTo(null);

		// Load background image
		ImageIcon backgroundImage = new ImageIcon("resources\\Menu.png"); // Replace with your image path
		Image scaledImage = backgroundImage.getImage().getScaledInstance(800, 600, Image.SCALE_SMOOTH);
		ImageIcon scaledBackgroundImage = new ImageIcon(scaledImage);

		// Set background image using a JLabel
		JLabel backgroundLabel = new JLabel(scaledBackgroundImage);
		backgroundLabel.setBounds(0, 0, 800, 600);
		setContentPane(backgroundLabel);
		backgroundLabel.setLayout(null);

		// Button Panel
		JPanel buttonPanel = createButtonPanel();
		// Calculate centered position
		buttonPanel.setBounds(193, -45, 400, 650);
		backgroundLabel.add(buttonPanel);

		setVisible(true);
	}

	private JPanel createButtonPanel() {
	    setResizable(false);
	    JPanel panel = new JPanel(new GridBagLayout());
	    panel.setOpaque(false); // Make the panel transparent

	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    gbc.insets = new Insets(10, 10, 10, 10); // Padding

	    Font buttonFont = new Font("Arial", Font.BOLD, 18);

	    // "Add Money" button
	    gbc.gridy++;
	    RoundedButton addMoneyButton = new RoundedButton("Nạp tiền");
	    addMoneyButton.setFont(buttonFont);
	    addMoneyButton.setBackground(new Color(80, 0, 255));
	    addMoneyButton.setForeground(Color.WHITE);
	    addMoneyButton.setPreferredSize(new Dimension(270, 50)); 
	    addMoneyButton.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            showAddMoneyInfo();
	        }
	    });
	    panel.add(addMoneyButton, gbc);

	    // "Show Account Info" button
	    gbc.gridy++;
	    RoundedButton showAccountInfoButton = new RoundedButton("Hiển thị thông tin tài khoản");
	    showAccountInfoButton.setFont(buttonFont);
	    showAccountInfoButton.setBackground(new Color(80, 0, 255));
	    showAccountInfoButton.setForeground(Color.WHITE);
	    showAccountInfoButton.setPreferredSize(new Dimension(270, 50)); // Tăng kích thước chiều ngang lên 20 điểm
	    showAccountInfoButton.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            showUserInfo();
	        }
	    });
	    panel.add(showAccountInfoButton, gbc);

	    // "Transactions" button
	    gbc.gridy++;
	    RoundedButton transactionButton = new RoundedButton("Giao dịch");
	    transactionButton.setFont(buttonFont);
	    transactionButton.setBackground(new Color(80, 0, 255));
	    transactionButton.setForeground(Color.WHITE);
	    transactionButton.setPreferredSize(new Dimension(270, 50)); // Tăng kích thước chiều ngang lên 20 điểm
	    transactionButton.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            showTransactionsDialog();
	        }
	    });
	    panel.add(transactionButton, gbc);

	    // "Open Savings Account" button
	    gbc.gridy++;
	    RoundedButton openSavingsAccountButton = new RoundedButton("Mở sổ tiết kiệm");
	    openSavingsAccountButton.setFont(buttonFont);
	    openSavingsAccountButton.setBackground(new Color(80, 0, 255));
	    openSavingsAccountButton.setForeground(Color.WHITE);
	    openSavingsAccountButton.setPreferredSize(new Dimension(270, 50)); // Tăng kích thước chiều ngang lên 20 điểm
	    openSavingsAccountButton.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            openSavingsAccount();
	        }
	    });
	    panel.add(openSavingsAccountButton, gbc);

	    // "Log Out" button
	    gbc.gridy++;
	    RoundedButton logoutButton = new RoundedButton("Đăng xuất");
	    logoutButton.setFont(buttonFont);
	    logoutButton.setBackground(new Color(80, 0, 255));
	    logoutButton.setForeground(Color.WHITE);
	    logoutButton.setPreferredSize(new Dimension(270, 50)); // Tăng kích thước chiều ngang lên 20 điểm
	    logoutButton.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            // Show the StartFrame and dispose of the current UserMainFrame
	            new StartFrame().setVisible(true);
	            dispose();
	        }
	    });

	    panel.add(logoutButton, gbc);

	    return panel;
	}

	private void showTransactionsDialog() {
		GDMain transactionFrame = new GDMain(user);
		transactionFrame.setVisible(true);
		dispose(); // Close main interface if necessary
	}

	private void openSavingsAccount() {
		SavingAccountMain savingAccountFrame = new SavingAccountMain(user);
		savingAccountFrame.setVisible(true);
		dispose(); // Close main interface if necessary
	}

	private void showAddMoneyInfo() {
		AddMoneyView addmoneyview = new AddMoneyView(user);
		addmoneyview.setVisible(true);
		dispose();
	}

	private void showUserInfo() {
		UserInfo userinfo = new UserInfo(user);
		userinfo.setVisible(true);
		dispose();
	}

	

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			UserMainFrame mainFrame = new UserMainFrame(new User());
			mainFrame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

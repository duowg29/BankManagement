package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class AdminMenuView extends JFrame {

	public AdminMenuView() {
		// Set the title of the window
		setTitle("Giao diện Admin");

		// Set the size of the window
		setSize(800, 639);

		// Set the layout to null to use absolute positioning
		setLayout(null);

		setResizable(false); 
		
		setLocationRelativeTo(null);

		// Load background image
		ImageIcon backgroundImage = new ImageIcon("resources\\Menu.png");
		JLabel backgroundLabel = new JLabel(backgroundImage);
		backgroundLabel.setBounds(0, 0, 800, 600);
		add(backgroundLabel);

		// Set Font
		Font font = new Font("Tahoma", Font.BOLD, 15);

		// Add the "Log out" label
		JLabel logoutLabel = new JLabel("> Đăng xuất");
		logoutLabel.setForeground(new Color(255, 155, 0));
		logoutLabel.setFont(font);
		logoutLabel.setBounds(10, 10, 100, 20);
		logoutLabel.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				logoutLabelMouseClicked(evt);
			}
		});
		backgroundLabel.add(logoutLabel);

		// Add the "Hello Username" label
		JLabel helloLabel = new JLabel("Xin chào!", SwingConstants.CENTER);
		helloLabel.setForeground(new Color(80, 0, 255));
		helloLabel.setFont(font);
		helloLabel.setBounds(300, 80, 200, 20);
		backgroundLabel.add(helloLabel);

		// Add the "Account Status Manager" button
		RoundedButton accountStatusButton = new RoundedButton("Quản lý trạng thái tài khoản");
		accountStatusButton.setBackground(new Color(80, 0, 255));
		accountStatusButton.setForeground(Color.WHITE);
		accountStatusButton.setFont(font);
		accountStatusButton.setBounds(250, 150, 300, 50);
		accountStatusButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					new AdminAccountStatusManagerView();
				} catch (ClassNotFoundException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				dispose();
			}
		});
		backgroundLabel.add(accountStatusButton);

		// Add the "All Transaction Info" button
		RoundedButton transactionInfoButton = new RoundedButton("Thống kê giao dịch");
		transactionInfoButton.setBackground(new Color(80, 0, 255));
		transactionInfoButton.setForeground(Color.WHITE);
		transactionInfoButton.setFont(font);
		transactionInfoButton.setBounds(250, 250, 300, 50);
		transactionInfoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					new AdminTransactionListView();
				} catch (ClassNotFoundException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				dispose();
			}
		});
		backgroundLabel.add(transactionInfoButton);

		// Add the "All Accounts Info" button
		RoundedButton accountInfoButton = new RoundedButton("Thống kê tài khoản");
		accountInfoButton.setBackground(new Color(80, 0, 255));
		accountInfoButton.setForeground(Color.WHITE);
		accountInfoButton.setFont(font);
		accountInfoButton.setBounds(250, 350, 300, 50);
		accountInfoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					new AdminAccountListView();
				} catch (ClassNotFoundException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				dispose();
			}
		});
		backgroundLabel.add(accountInfoButton);

		// Add the "All User Info" button
		RoundedButton userInfoButton = new RoundedButton("Thống kê người dùng");
		userInfoButton.setBackground(new Color(80, 0, 255));
		userInfoButton.setForeground(Color.WHITE);
		userInfoButton.setFont(font);
		userInfoButton.setBounds(250, 450, 300, 50);
		userInfoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					new AdminUserListView();
				} catch (ClassNotFoundException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				dispose();
			}
		});
		backgroundLabel.add(userInfoButton);

		// Set default close operation
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Set the window to be visible
		setVisible(true);
	}

	private void logoutLabelMouseClicked(java.awt.event.MouseEvent evt) {
		new StartFrame();
		dispose();
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			new AdminMenuView();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

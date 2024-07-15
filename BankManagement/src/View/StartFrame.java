package View;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;

import Controller.CRUD;
import Controller.Listener;
import Model.DatabaseConnection;
import Model.User;

import javax.swing.SwingConstants;

public class StartFrame extends JFrame {
	private JTextField usernameField;
	private JPasswordField passwordField;

	public StartFrame() {
		this.init();
	}

	public void init() {
		ActionListener al = new Listener(this);

		setTitle("Personal Banking Manager");
		setSize(800, 639);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false); 
		setLocationRelativeTo(null);

		// Load and scale the background image
		ImageIcon backgroundImage = new ImageIcon("resources\\StartFrame.png"); // Replace with your image path
		Image scaledImage = backgroundImage.getImage().getScaledInstance(800, 600, Image.SCALE_SMOOTH);
		ImageIcon scaledBackgroundImage = new ImageIcon(scaledImage);

		// Set background image using a JLabel
		JLabel backgroundLabel = new JLabel(scaledBackgroundImage);
		backgroundLabel.setBounds(0, 0, 800, 600);
		add(backgroundLabel);
		setLayout(null);

		// Create and position the title label
		Font font = new Font("Tahoma", Font.BOLD, 15);
		JLabel titleLabel = new JLabel("Ứng dụng quản lý ngân hàng cá nhân");
		titleLabel.setFont(font);
		titleLabel.setForeground(new Color(80, 0, 255)); // Set text color to blue
		titleLabel.setBounds(265, 180, 800, 30); // Centered horizontally, at the top
		backgroundLabel.add(titleLabel);

		// Create and position the username label and text field
		JLabel usernameLabel = new JLabel("Username");
		usernameLabel.setFont(font);
		usernameLabel.setForeground(new Color(80, 0, 255));
		usernameLabel.setBounds(240, 230, 80, 25);
		backgroundLabel.add(usernameLabel);

		usernameField = new JTextField(20);
		usernameField.setBounds(340, 230, 220, 25);
		backgroundLabel.add(usernameField);

		// Create and position the password label and text field
		JLabel passwordLabel = new JLabel("Mật khẩu");
		passwordLabel.setFont(font);
		passwordLabel.setForeground(new Color(80, 0, 255));
		passwordLabel.setBounds(240, 280, 80, 25);
		backgroundLabel.add(passwordLabel);

		passwordField = new JPasswordField(20);
		passwordField.setBounds(340, 280, 220, 25);
		backgroundLabel.add(passwordField);

		// Create and position the login button
		RoundedButton signInButton = new RoundedButton("Đăng nhập");
		signInButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		signInButton.setBackground(new Color(80, 0, 255)); // Set button color to blue
		signInButton.setForeground(Color.WHITE); // Set text color to white
		signInButton.setBounds(325, 350, 150, 30);
		backgroundLabel.add(signInButton);
		signInButton.addActionListener(al);

		// Create and position the sign up button
		JLabel signUpButton = new JLabel("Chưa có tài khoản? Đăng ký!");
		signUpButton.setFont(new Font("Tahoma", Font.ITALIC, 13));
		signUpButton.setForeground(new Color(80, 0, 255)); // Set text color to white
		signUpButton.setBounds(470, 440, 200, 30);
		backgroundLabel.add(signUpButton);

		// Add ActionListener to the sign up button
		signUpButton.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				// Open SignUpView1
				new SignUpView1();
				dispose(); // Close StartView
			}
		});

		// Set the frame visibility to true
		setVisible(true);
	}

	public String getUsername() {
		return usernameField.getText();
	}

	public String getPassword() {
		return new String(passwordField.getPassword());
	}

	public void showAdminMenu() {
		new AdminMenuView();
		this.dispose();
	}

	public void showUserMenu() throws ClassNotFoundException, IOException, SQLException {
		String sql = "SELECT AccountID FROM Accounts WHERE Username = ?";
		Connection con = DatabaseConnection.getConnection();
		try (PreparedStatement stmt2 = con.prepareStatement(sql)) {
		    stmt2.setString(1, getUsername());
		    try (ResultSet rs2 = stmt2.executeQuery()) {
		        if (rs2.next()) {
		            long accountID = rs2.getLong("AccountID");
		            // Bây giờ bạn có thể lấy thông tin người dùng bằng cách gọi phương thức getUserInfoByAccountID(accountID)
		            User user = CRUD.getUserInfoByAccountID(accountID);

		    		new UserMainFrame(user).setVisible(true);
		    		dispose();
		            
		            if (user != null) {
		                // Hiển thị thông tin người dùng, hoặc thực hiện các hành động khác với thông tin người dùng
		                System.out.println("Thông tin người dùng: " + user.getFullName());
		            } else {
		                System.out.println("Không tìm thấy thông tin người dùng cho AccountID: " + accountID);
		            }
		        }
		    }
		}

	}
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			new StartFrame();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}


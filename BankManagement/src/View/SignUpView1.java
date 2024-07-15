package View;

import javax.swing.*;
import Controller.CRUD;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.stream.IntStream;

public class SignUpView1 extends JFrame {
	// Declare the text fields and combo boxes as instance variables
	private JTextField[] textFields;
	private JComboBox<String> genderComboBox;
	private JComboBox<String> provinceComboBox;
	private JComboBox<Integer> dayComboBox;
	private JComboBox<Integer> monthComboBox;
	private JComboBox<Integer> yearComboBox;
	private String registeredCitizenID; // Add this variable to store the registered citizenID

	public SignUpView1() {
		// Set the title of the window
		setTitle("Đăng ký");

		// Set the size of the window
		setSize(800, 639);

		setResizable(false); 
		
		setLocationRelativeTo(null);

		// Set font
		Font font = new Font("Tahoma", Font.BOLD, 15);

		// Load and scale the background image
		ImageIcon backgroundImage = new ImageIcon("resources\\SignUpView1.png"); // Replace with your image path
		Image scaledImage = backgroundImage.getImage().getScaledInstance(800, 600, Image.SCALE_SMOOTH);
		ImageIcon scaledBackgroundImage = new ImageIcon(scaledImage);

		// Set background image using a JLabel
		JLabel backgroundLabel = new JLabel(scaledBackgroundImage);
		backgroundLabel.setBounds(0, 0, 800, 600);
		// Set the layout to null to use absolute positioning
		setLayout(null);
		add(backgroundLabel);

		// Add the "Back" label
		JLabel backLabel = new JLabel("> Quay lại");
		backLabel.setForeground(new Color(255, 155, 0));
		backLabel.setFont(font);
		backLabel.setBounds(10, 10, 100, 20);
		backgroundLabel.add(backLabel);

		// Add the "Sign Up" label
		JLabel signUpLabel = new JLabel("Đăng ký", SwingConstants.CENTER);
		signUpLabel.setForeground(new Color(80, 0, 255));
		signUpLabel.setFont(font);
		signUpLabel.setBounds(350, 70, 100, 20);
		backgroundLabel.add(signUpLabel);

		// Define labels and text fields
		String[] labels = { "CCCD", "Họ và tên", "Giới tính", "Ngày sinh", "Tháng sinh", "Năm sinh", "Tỉnh/Thành phố",
				"Số điện thoại", "Email" };

		int labelX = 230;
		int fieldX = 370;
		int yStart = 110; // Adjusted starting Y position for labels and fields
		int yStep = 40;
		int width = 200;
		int height = 30;

		// Initialize the text fields array
		textFields = new JTextField[labels.length - 5]; // Excluding gender, day, month, year, and province fields

		// List of provinces and cities in Vietnam
		String[] provinces = { "An Giang", "Bà Rịa - Vũng Tàu", "Bạc Liêu", "Bắc Kạn", "Bắc Giang", "Bắc Ninh",
				"Bến Tre", "Bình Dương", "Bình Định", "Bình Phước", "Bình Thuận", "Cà Mau", "Cao Bằng", "Cần Thơ",
				"Đà Nẵng", "Đắk Lắk", "Đắk Nông", "Điện Biên", "Đồng Nai", "Đồng Tháp", "Gia Lai", "Hà Giang", "Hà Nam",
				"Hà Nội", "Hà Tĩnh", "Hải Dương", "Hải Phòng", "Hậu Giang", "Hòa Bình", "Hưng Yên", "Khánh Hòa",
				"Kiên Giang", "Kon Tum", "Lai Châu", "Lâm Đồng", "Lạng Sơn", "Lào Cai", "Long An", "Nam Định",
				"Nghệ An", "Ninh Bình", "Ninh Thuận", "Phú Thọ", "Phú Yên", "Quảng Bình", "Quảng Nam", "Quảng Ngãi",
				"Quảng Ninh", "Quảng Trị", "Sóc Trăng", "Sơn La", "Tây Ninh", "Thái Bình", "Thái Nguyên", "Thanh Hóa",
				"Thừa Thiên Huế", "Tiền Giang", "TP. Hồ Chí Minh", "Trà Vinh", "Tuyên Quang", "Vĩnh Long", "Vĩnh Phúc",
				"Yên Bái" };

		// Create and position labels and text fields directly within the JFrame
		for (int i = 0; i < labels.length; i++) {
			JLabel label = new JLabel(labels[i]);
			label.setFont(font);
			label.setForeground(new Color(80, 0, 255));
			label.setBounds(labelX, yStart + (i * yStep), width, height);
			backgroundLabel.add(label);

			if (i == 2) { // For gender
				genderComboBox = new JComboBox<>(new String[] { "Nam", "Nữ" });
				genderComboBox.setFont(font);
				genderComboBox.setBounds(fieldX, yStart + (i * yStep), width, height);
				backgroundLabel.add(genderComboBox);
			} else if (i == 3) { // For day
				dayComboBox = new JComboBox<>(IntStream.rangeClosed(1, 31).boxed().toArray(Integer[]::new));
				dayComboBox.setFont(font);
				dayComboBox.setBounds(fieldX, yStart + (i * yStep), width, height);
				backgroundLabel.add(dayComboBox);
			} else if (i == 4) { // For month
				monthComboBox = new JComboBox<>(IntStream.rangeClosed(1, 12).boxed().toArray(Integer[]::new));
				monthComboBox.setFont(font);
				monthComboBox.setBounds(fieldX, yStart + (i * yStep), width, height);
				backgroundLabel.add(monthComboBox);
			} else if (i == 5) { // For year
				yearComboBox = new JComboBox<>(IntStream.rangeClosed(1900, 2023).boxed().toArray(Integer[]::new));
				yearComboBox.setFont(font);
				yearComboBox.setBounds(fieldX, yStart + (i * yStep), width, height);
				backgroundLabel.add(yearComboBox);
			} else if (i == 6) { // For province
				provinceComboBox = new JComboBox<>(provinces);
				provinceComboBox.setFont(font);
				provinceComboBox.setBounds(fieldX, yStart + (i * yStep), width, height);
				backgroundLabel.add(provinceComboBox);
			} else {
				JTextField textField = new JTextField(50);
				textField.setBounds(fieldX, yStart + (i * yStep), width, height);
				backgroundLabel.add(textField);

				// Store the text field in the array
				if (i < 2) {
					textFields[i] = textField;
				} else if (i > 6) {
					textFields[i - 5] = textField;
				}
			}
		}

		// Create and position the "Xác nhận" button using RoundedButton
		RoundedButton confirmButton = new RoundedButton("Xác nhận");
		confirmButton.setFont(font);
		confirmButton.setBackground(new Color(80, 0, 255));// Example: Set button background color
		confirmButton.setForeground(Color.WHITE); // Example: Set button text color
		confirmButton.setBounds(300, 480, width, height); // Adjusted Y position for the button
		backgroundLabel.add(confirmButton);

		// Set default close operation
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Add action listener to the "Xác nhận" button
		confirmButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (JTextField textField : textFields) {
					if (textField.getText().trim().isEmpty()) {
						JOptionPane.showMessageDialog(null, "Người dùng chưa nhập thông tin!");
						return; // Ngăn không cho tiếp tục xử lý
					}
				}

				String citizenID = textFields[0].getText(); // Lấy citizenID dưới dạng chuỗi
				String fullName = textFields[1].getText();
				String gender = (String) genderComboBox.getSelectedItem(); // Lấy giới tính từ JComboBox
				int day = (Integer) dayComboBox.getSelectedItem();
				int month = (Integer) monthComboBox.getSelectedItem();
				int year = (Integer) yearComboBox.getSelectedItem();

				String province = (String) provinceComboBox.getSelectedItem(); // Lấy tỉnh/thành phố từ JComboBox
				String phoneNumber = textFields[2].getText();
				String email = textFields[3].getText();

				if (!fullName.matches("^[\\p{L} .'-]+$")) {
					JOptionPane.showMessageDialog(null, "Họ và tên không được chứa số và ký tự đặc biệt!");
					return;
				}

				if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
					JOptionPane.showMessageDialog(null, "Email không hợp lệ!");
					return;
				}

				CRUD crud = new CRUD();
				boolean success = false;
				try {
					success = crud.signUpUser(citizenID, fullName, gender, day, month, year, province, phoneNumber,
							email);
				} catch (ClassNotFoundException | IOException e1) {
					e1.printStackTrace();
				}

				if (success) {
					registeredCitizenID = citizenID; // Store the registered citizenID
					JOptionPane.showMessageDialog(null, "Đăng ký thành công!");
					// Mở SignUpView2 và truyền citizenID dưới dạng String
					new SignUpView2(citizenID);
					dispose(); // Đóng SignUpView1
				} else {
					JOptionPane.showMessageDialog(null, "Đăng ký thất bại!");
				}
			}
		});

		// Add mouse listener to the "Back" label
		backLabel.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				// Check if there is a registered citizenID
				if (registeredCitizenID != null) {
					// Call CRUD to delete the user
					CRUD crud = new CRUD();
					try {
						crud.deleteUser(registeredCitizenID);
					} catch (ClassNotFoundException | IOException e1) {
						e1.printStackTrace();
					}
				}
				// Open StartView and close this view
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
			new SignUpView1();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

package View;

import Controller.CRUD;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class AdminAccountStatusManagerView extends JFrame {
	private JTextField searchBar;
	private JTable resultsTable;
	private DefaultTableModel tableModel;
	private CRUD crud;

	public AdminAccountStatusManagerView() throws ClassNotFoundException, IOException {
		crud = new CRUD();

		// Set the title of the window
		setTitle("Quản lý trạng thái tài khoản");

		// Set the size of the window
		setSize(800, 639);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		setResizable(false); 
		
		setLocationRelativeTo(null);

		// Load and scale the background image
		ImageIcon backgroundImage = new ImageIcon("resources\\NoPanel.png"); // Replace with your image path
		Image scaledImage = backgroundImage.getImage().getScaledInstance(800, 600, Image.SCALE_SMOOTH);
		ImageIcon scaledBackgroundImage = new ImageIcon(scaledImage);

		// Set background image using a JLabel
		JLabel backgroundLabel = new JLabel(scaledBackgroundImage);
		backgroundLabel.setBounds(0, 0, 800, 600);
		add(backgroundLabel);

		// Define font
		Font font = new Font("Tahoma", Font.BOLD, 15);

		// Add the "Back" label
		JLabel backLabel = new JLabel("> Quay lại");
		backLabel.setForeground(new Color(255, 155, 0));
		backLabel.setFont(font);
		backLabel.setBounds(10, 10, 100, 20);
		backLabel.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				new AdminMenuView();
				dispose();
			}
		});
		backgroundLabel.add(backLabel);

		// Add the "Account Status Manager" label
		JLabel titleLabel = new JLabel("Quản lý trạng thái tài khoản", SwingConstants.CENTER);
		titleLabel.setForeground(new Color(255, 155, 0));
		titleLabel.setFont(font);
		titleLabel.setBounds(250, 50, 300, 20);
		backgroundLabel.add(titleLabel);

		JLabel accountIDLabel = new JLabel("Số tài khoản: ");
		accountIDLabel.setForeground(new Color(255, 155, 0));
		accountIDLabel.setFont(font);
		accountIDLabel.setBounds(150, 125, 300, 20);
		backgroundLabel.add(accountIDLabel);

		// Add the search bar label and text field
		searchBar = new JTextField();
		searchBar.setFont(font);
		searchBar.setBounds(260, 120, 400, 30);
		searchBar.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyReleased(java.awt.event.KeyEvent e) {
				try {
					handleSearch();
				} catch (ClassNotFoundException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		backgroundLabel.add(searchBar);

		// Add the "Show All" button
		RoundedButton showAllButton = new RoundedButton("Hiển thị tất cả");
		showAllButton.setFont(font);
		showAllButton.setBackground(new Color(255, 155, 0));
		showAllButton.setForeground(Color.BLACK);
		showAllButton.setBounds(330, 400, 150, 30);
		showAllButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					loadAccountStatusInfo();
				} catch (ClassNotFoundException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		backgroundLabel.add(showAllButton);

		// Add the export to Excel button
		RoundedButton exportButton = new RoundedButton("Xuất ra Excel");
		exportButton.setFont(font);
		exportButton.setBackground(new Color(255, 155, 0));
		exportButton.setForeground(Color.BLACK);
		exportButton.setBounds(149, 400, 150, 30);
		exportButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exportToExcel();
			}
		});
		backgroundLabel.add(exportButton);

		// Add the "Toggle Account Status" button
		RoundedButton toggleAccountButton = new RoundedButton("Khóa/Bỏ Khóa");
		toggleAccountButton.setFont(font);
		toggleAccountButton.setBackground(new Color(255, 155, 0));
		toggleAccountButton.setForeground(Color.BLACK);
		toggleAccountButton.setBounds(510, 400, 150, 30);
		backgroundLabel.add(toggleAccountButton);

		toggleAccountButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = resultsTable.getSelectedRow();
				if (selectedRow == -1) {
					JOptionPane.showMessageDialog(AdminAccountStatusManagerView.this,
							"Vui lòng chọn một tài khoản để thực hiện thao tác Khóa/Bỏ Khóa.", "Thông báo",
							JOptionPane.WARNING_MESSAGE);
					return; // Ngừng thực hiện nếu không có dòng nào được chọn
				}
				long accountID = (long) resultsTable.getValueAt(selectedRow, 0); // Lấy AccountID từ dòng được chọn
				String currentStatus = (String) resultsTable.getValueAt(selectedRow, 2); // Lấy AccountStatus từ dòng
																							// được chọn
				String newStatus = currentStatus.equals("Opened") ? "Closed" : "Opened"; // Đảo ngược trạng thái
				boolean success = false;
				try {
					success = crud.toggleAccountStatus(accountID, newStatus);
				} catch (ClassNotFoundException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if (success) {
					// Cập nhật lại bảng khi dữ liệu đã được cập nhật thành công
					try {
						loadAccountStatusInfo();
					} catch (ClassNotFoundException | IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					JOptionPane.showMessageDialog(AdminAccountStatusManagerView.this,
							"Đã cập nhật trạng thái tài khoản thành công");
				} else {
					JOptionPane.showMessageDialog(AdminAccountStatusManagerView.this,
							"Không thể cập nhật trạng thái tài khoản", "Lỗi", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// Set up the table model
		tableModel = new DefaultTableModel(new Object[] { "Số tài khoản", "Căn cước công dân", "Trạng thái tài khoản" }, 0);

		// Add the table
		resultsTable = new JTable(tableModel);
		resultsTable.setFont(font);
		resultsTable.setRowHeight(25);

		resultsTable.setEnabled(true);

		// Set default editor to null to make all cells read-only
		resultsTable.setDefaultEditor(Object.class, null);

		// Thêm MouseListener cho bảng
		resultsTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int selectedRow = resultsTable.getSelectedRow();
				if (selectedRow != -1) {
					String accountID = resultsTable.getValueAt(selectedRow, 0).toString();
					searchBar.setText(accountID);
				}
			}
		});

		JScrollPane scrollPane = new JScrollPane(resultsTable);
		scrollPane.setBounds(150, 160, 510, 200);
		backgroundLabel.add(scrollPane);

		// Set the window to be visible
		setVisible(true);

		// Load account status information on startup
		loadAccountStatusInfo();
	}

	private void handleSearch() throws ClassNotFoundException, IOException {
		String searchText = searchBar.getText().toLowerCase();
		// Perform search logic here and update tableModel
		tableModel.setRowCount(0); // Clear existing data
		List<Object[]> accountStatusInfo = crud.getAccountStatusInfo();
		for (Object[] row : accountStatusInfo) {
			if (row[1].toString().toLowerCase().contains(searchText) || row[0].toString().contains(searchText)
					|| row[2].toString().toLowerCase().contains(searchText)) {
				tableModel.addRow(row);
			}
		}
	}

	private void loadAccountStatusInfo() throws ClassNotFoundException, IOException {
		tableModel.setRowCount(0); // Clear existing data
		List<Object[]> accountStatusInfo = crud.getAccountStatusInfo();
		for (Object[] row : accountStatusInfo) {
			tableModel.addRow(row);
		}
	}

	private void exportToExcel() {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Account Status List");

		Row headerRow = sheet.createRow(0);
		for (int i = 0; i < tableModel.getColumnCount(); i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(tableModel.getColumnName(i));
		}

		for (int i = 0; i < tableModel.getRowCount(); i++) {
			Row row = sheet.createRow(i + 1);
			for (int j = 0; j < tableModel.getColumnCount(); j++) {
				Cell cell = row.createCell(j);
				cell.setCellValue(tableModel.getValueAt(i, j).toString());
			}
		}

		String desktopPath = System.getProperty("user.home") + "\\Desktop";

		try (FileOutputStream fileOut = new FileOutputStream(desktopPath + "\\AccountStatusList.xlsx")) {
			workbook.write(fileOut);
			JOptionPane.showMessageDialog(this, "Xuất file Excel thành công!");
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Lỗi khi xuất file Excel!", "Lỗi", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			new AdminAccountStatusManagerView();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

package View;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import Controller.CRUD;

public class AdminAccountListView extends JFrame {
	private JTextField searchBar;
	private JTable resultsTable;
	private DefaultTableModel tableModel;
	private CRUD crud;

	public AdminAccountListView() throws ClassNotFoundException, IOException {
		crud = new CRUD();

		// Set the title of the window
		setTitle("Danh sách tài khoản");

		// Set the size of the window
		setSize(800, 639);

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

		// Set the layout to null to use absolute positioning on the JLabel
		setLayout(null);

		// Define font
		Font font = new Font("Tahoma", Font.BOLD, 15);

		// Add the "Back" label
		JLabel backLabel = new JLabel("> Quay lại");
		backLabel.setForeground(new Color(255, 155, 0));
		backLabel.setFont(font);
		backLabel.setBounds(10, 10, 100, 20);
		backLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				backLabelMouseClicked(evt);
			}
		});
		backgroundLabel.add(backLabel);

		// Add the "Account List" label
		JLabel titleLabel = new JLabel("Thống kê tài khoản", SwingConstants.CENTER);
		titleLabel.setForeground(new Color(255, 155, 0));
		titleLabel.setFont(font);
		titleLabel.setBounds(250, 50, 300, 20);
		backgroundLabel.add(titleLabel);

		JLabel accountIDLabel = new JLabel("Số tài khoản: ");
		accountIDLabel.setForeground(new Color(255, 155, 05));
		accountIDLabel.setFont(font);
		accountIDLabel.setBounds(150, 125, 300, 20);
		backgroundLabel.add(accountIDLabel);

		// Add the search bar label and text field
		searchBar = new JTextField();
		searchBar.setFont(font);
		searchBar.setBounds(260, 120, 400, 30);
		searchBar.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
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
		showAllButton.setBackground(new Color(255, 155, 0));
		showAllButton.setForeground(Color.BLACK);
		showAllButton.setFont(font);
		showAllButton.setBounds(510, 400, 150, 30);
		showAllButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					loadAccountInfo();
				} catch (ClassNotFoundException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		backgroundLabel.add(showAllButton);

		// Add the export to Excel button
		RoundedButton exportButton = new RoundedButton("Xuất ra Excel");
		exportButton.setBackground(new Color(255, 155, 0));
		exportButton.setForeground(Color.BLACK);
		exportButton.setFont(font);
		exportButton.setBounds(149, 400, 150, 30);
		exportButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exportToExcel();
			}
		});
		backgroundLabel.add(exportButton);

		// Set up the table model
		tableModel = new DefaultTableModel(new Object[] { "Số tài khoản", "Căn cước công dân", "Tên tài khoản", "Trạng thái tài khoản" }, 0);

		// Add the table
		resultsTable = new JTable(tableModel);
		resultsTable.setFont(font);
		resultsTable.setRowHeight(25);

		
		resultsTable.setEnabled(true);

		resultsTable.setDefaultEditor(Object.class, null);

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
		scrollPane.setBounds(45, 160, 700, 200);
		backgroundLabel.add(scrollPane);

		// Set default close operation
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Set the window to be visible
		setVisible(true);

		// Load account information on startup
		loadAccountInfo();
	}

	private void backLabelMouseClicked(MouseEvent evt) {
		// Implement back navigation logic here
		new AdminMenuView();
		dispose();
	}

	private void handleSearch() throws ClassNotFoundException, IOException {
		String searchText = searchBar.getText().toLowerCase();
		// Perform search logic here and update tableModel
		tableModel.setRowCount(0); // Clear existing data
		List<Object[]> accountInfo = crud.getAccountInfo();
		for (Object[] row : accountInfo) {
			if (row[1].toString().toLowerCase().contains(searchText) || row[0].toString().contains(searchText)
					|| row[2].toString().toLowerCase().contains(searchText)
					|| row[3].toString().toLowerCase().contains(searchText)) {
				tableModel.addRow(row);
			}
		}
	}

	private void loadAccountInfo() throws ClassNotFoundException, IOException {
		tableModel.setRowCount(0); // Clear existing data
		List<Object[]> accountInfo = crud.getAccountInfo();
		for (Object[] row : accountInfo) {
			tableModel.addRow(row);
		}
	}

	private void exportToExcel() {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Account List");

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

		try (FileOutputStream fileOut = new FileOutputStream(desktopPath + "\\AccountList.xlsx")) {
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
			new AdminAccountListView();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

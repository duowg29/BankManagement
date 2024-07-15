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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class AdminTransactionListView extends JFrame {

	private JTextField searchBar;
	private JTable resultsTable;
	private DefaultTableModel tableModel;
	private CRUD crud;

	public AdminTransactionListView() throws ClassNotFoundException, IOException {
		// Set the title of the window
		setTitle("Thống kê giao dịch");

		// Set the size of the window
		setSize(800, 639);

		setResizable(false); 
		
		setLocationRelativeTo(null);

		// Initialize CRUD object
		crud = new CRUD();

		// Load and scale the background image
		ImageIcon backgroundImage = new ImageIcon("resources\\NoPanel.png");
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
		backLabel.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				backLabelMouseClicked(evt);
			}
		});
		backgroundLabel.add(backLabel);

		// Add the "Transaction Statistics" label
		JLabel titleLabel = new JLabel("Thống kê giao dịch", SwingConstants.CENTER);
		titleLabel.setForeground(new Color(255, 155, 0));
		titleLabel.setFont(font);
		titleLabel.setBounds(250, 50, 300, 20);
		backgroundLabel.add(titleLabel);

		JLabel transactionIDLabel = new JLabel("ID giao dịch: ");
		transactionIDLabel.setForeground(new Color(255, 155, 0));
		transactionIDLabel.setFont(font);
		transactionIDLabel.setBounds(150, 105, 300, 20);
		backgroundLabel.add(transactionIDLabel);

		// Add the search bar label and text field
		searchBar = new JTextField();
		searchBar.setFont(font);
		searchBar.setBounds(260, 100, 400, 30);
		searchBar.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				try {
					handleSearch();
				} catch (ClassNotFoundException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		backgroundLabel.add(searchBar);

		// Add the "Show All" button
		RoundedButton showAllButton = new RoundedButton("Hiển thị tất cả");
		showAllButton.setBackground(new Color(80, 0, 255));
		showAllButton.setForeground(Color.WHITE);
		showAllButton.setFont(font);
		showAllButton.setBounds(510, 380, 150, 30);
		showAllButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					loadTransactionInfo();
				} catch (ClassNotFoundException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		backgroundLabel.add(showAllButton);

		// Add the export to Excel button
		RoundedButton exportButton = new RoundedButton("Xuất ra Excel");
		exportButton.setBackground(new Color(80, 0, 255));
		exportButton.setForeground(Color.WHITE);
		exportButton.setFont(font);
		exportButton.setBounds(149, 380, 150, 30);
		exportButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exportToExcel();
			}
		});
		backgroundLabel.add(exportButton);

		// Set up the table model
		tableModel = new DefaultTableModel(new Object[] { "Mã giao dịch", "Số tài khoản gửi", "Số tài khoản nhận",
				"Số tiền gửi", "Thời gian thực hiện giao dịch", "Ghi chú" }, 0);

		// Add the table
		resultsTable = new JTable(tableModel);
		resultsTable.setFont(font);
		resultsTable.setRowHeight(25);

		// Ensure table is not disabled
		resultsTable.setEnabled(true);

		// Set default editor to null to make all cells read-only
		resultsTable.setDefaultEditor(Object.class, null);

		// Add MouseListener to the table
		resultsTable.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				int selectedRow = resultsTable.getSelectedRow();
				if (selectedRow != -1) {
					String transactionID = resultsTable.getValueAt(selectedRow, 0).toString();
					searchBar.setText(transactionID);
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

		// Load transaction information on startup
		loadTransactionInfo();
	}

	private void backLabelMouseClicked(java.awt.event.MouseEvent evt) {
		// Implement back navigation logic here
		new AdminMenuView(); // Replace with appropriate navigation
		dispose(); // Close current window
	}

	private void handleSearch() throws ClassNotFoundException, IOException {
		String searchText = searchBar.getText().toLowerCase();
		// Perform search logic here and update tableModel
		tableModel.setRowCount(0); // Clear existing data
		List<Object[]> transactionInfo = crud.getTransactionInfo();
		for (Object[] row : transactionInfo) {
			if (row[0].toString().contains(searchText) || row[1].toString().toLowerCase().contains(searchText)
					|| row[2].toString().toLowerCase().contains(searchText) || row[3].toString().contains(searchText)
					|| row[4].toString().contains(searchText) || row[5].toString().toLowerCase().contains(searchText)) {
				tableModel.addRow(row);
			}
		}
	}

	private void loadTransactionInfo() throws ClassNotFoundException, IOException {
		tableModel.setRowCount(0); // Clear existing data
		List<Object[]> transactionInfo = crud.getTransactionInfo();
		for (Object[] row : transactionInfo) {
			tableModel.addRow(row);
		}
	}

	private void exportToExcel() {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Transaction List");

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

		// Get path to Desktop
		String desktopPath = System.getProperty("user.home") + "\\Desktop";

		try (FileOutputStream fileOut = new FileOutputStream(desktopPath + "\\TransactionList.xlsx")) {
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
			new AdminTransactionListView();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

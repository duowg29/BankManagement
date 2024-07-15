package View;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import Controller.TransactionController;
import Model.Transaction;
import Model.User;
import java.awt.Color;

public class GDSaveTransaction extends JFrame {

    private static final long serialVersionUID = 1L;
    private JLayeredPane layeredPane;
    private JTable table_view;
    private DefaultTableModel tableModel;
    private JTextField textField_tuNgay;
    private JTextField textField_denNgay;
    private User user;

    public GDSaveTransaction(User user) {
        this.user = user;
        setTitle("Save Transaction");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 885, 382);
        setLocationRelativeTo(null);
        
        // Create a layered pane to hold components
        layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

        // Load and scale the background image
        ImageIcon backgroundImage = new ImageIcon("resources/NoPanel.png"); // Thay bằng đường dẫn hình ảnh của bạn
        Image scaledImage = backgroundImage.getImage().getScaledInstance(900, 650, Image.SCALE_SMOOTH);
        ImageIcon scaledBackgroundImage = new ImageIcon(scaledImage);

        // Set background image using a JLabel
        JLabel backgroundLabel = new JLabel(scaledBackgroundImage);
        backgroundLabel.setBounds(-29, 0, 863, 620);
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

        // Create a panel for form fields and buttons
        JPanel formPanel = new JPanel();
        formPanel.setOpaque(false);
        formPanel.setBounds(0, 184, 873, 163); // Đặt vị trí và kích thước của panel
        layeredPane.add(formPanel, JLayeredPane.PALETTE_LAYER);
        formPanel.setLayout(null);

        JLabel lblTuNgay = new JLabel("Từ ngày:");
        lblTuNgay.setForeground(new Color(255, 255, 255));
        lblTuNgay.setBounds(235, 1, 132, 37);
        lblTuNgay.setFont(new Font("Tahoma", Font.PLAIN, 16));
        formPanel.add(lblTuNgay);

        textField_tuNgay = new JTextField();
        textField_tuNgay.setBounds(377, 3, 199, 37);
        formPanel.add(textField_tuNgay);

        JLabel lblDenNgay = new JLabel("Đến ngày:");
        lblDenNgay.setForeground(new Color(255, 255, 255));
        lblDenNgay.setBounds(235, 46, 112, 37);
        lblDenNgay.setFont(new Font("Tahoma", Font.PLAIN, 16));
        formPanel.add(lblDenNgay);

        textField_denNgay = new JTextField();
        textField_denNgay.setBounds(377, 48, 199, 37);
        formPanel.add(textField_denNgay);

        RoundedButton btnNewButton_search = new RoundedButton("Tìm kiếm");
        
        btnNewButton_search.setBounds(65, 102, 177, 50);
        btnNewButton_search.setBackground(new Color(255, 155, 0)); 
        btnNewButton_search.setForeground(new Color(0, 0, 0)); 
        btnNewButton_search.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btnNewButton_search.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchTransactions();
                textField_tuNgay.setText("");
                textField_denNgay.setText("");
            }
        });
        formPanel.add(btnNewButton_search);

        RoundedButton btnNewButton_saveExcel = new RoundedButton("Lưu Excel");
        btnNewButton_saveExcel.setBackground(new Color(255, 155, 0)); 
        btnNewButton_saveExcel.setForeground(new Color(0, 0, 0)); 
        btnNewButton_saveExcel.setBounds(336, 102, 185, 50);
        btnNewButton_saveExcel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btnNewButton_saveExcel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    saveToExcel();
                } catch (ClassNotFoundException | SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
        formPanel.add(btnNewButton_saveExcel);

        RoundedButton btnNewButton_home = new RoundedButton("HomePage");
        btnNewButton_home.setBackground(new Color(255, 155, 0)); 
        btnNewButton_home.setForeground(new Color(0, 0, 0)); 
        btnNewButton_home.setBounds(609, 102, 185, 50);
        formPanel.add(btnNewButton_home);
        btnNewButton_home.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btnNewButton_home.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new GDMain(GDSaveTransaction.this.user).setVisible(true);
                dispose();
            }
        });

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(0, 0, 873, 173);
        layeredPane.add(scrollPane, JLayeredPane.PALETTE_LAYER);

        table_view = new JTable();
        table_view.setFont(new Font("Tahoma", Font.PLAIN, 13));
        tableModel = new DefaultTableModel(
            new Object[][] {},
            new String[] { "Mã giao dịch", "ID Người gửi", "ID Người nhận", "Số tiền giao dịch", "Thời gian giao dịch", "Ghi chú", "Trạng thái" }
        );
        table_view.setModel(tableModel);
        TableColumnModel columnModel = table_view.getColumnModel(); // Thêm dòng này để lấy column model
        columnModel.getColumn(0).setPreferredWidth(35);
        columnModel.getColumn(1).setPreferredWidth(30);
        columnModel.getColumn(2).setPreferredWidth(35);
        columnModel.getColumn(3).setPreferredWidth(40);
        columnModel.getColumn(4).setPreferredWidth(90);
        columnModel.getColumn(5).setPreferredWidth(250);
        columnModel.getColumn(6).setPreferredWidth(35); // Cấu hình độ rộng cho cột mới
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table_view.getColumnCount(); i++) {
            table_view.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        scrollPane.setViewportView(table_view);

        loadTransactions();
    }

    private void loadTransactions() {
        long loggedInAccountID = user.getAccountID(); // Get accountID of the currently logged in user

        tableModel.setRowCount(0); // Clear all current rows in tableModel

        try {
            List<Transaction> allTransactions = TransactionController.getAllTransactionsByAccount(loggedInAccountID);

            // Sort transactions by time (if necessary)
            Collections.sort(allTransactions, Comparator.comparing(Transaction::getTransactionTime));

            for (Transaction transaction : allTransactions) {
                if (loggedInAccountID == transaction.getSenderAccount() && "SENT".equals(transaction.getTransactionType())) {
                    // Display only sent transactions if logged in with sender account
                    String remarks = String.format("Chuyển khoản tới %s (tài khoản %d)",
                            TransactionController.getReceiverName(transaction.getReceiverAccount()),
                            transaction.getReceiverAccount());
                    Object[] rowData = {
                            transaction.getTransactionId(),
                            transaction.getSenderAccount(),
                            transaction.getReceiverAccount(),
                            transaction.getAmount(),
                            transaction.getTransactionTime(),
                            remarks,
                            transaction.getTransactionType()
                    };
                    tableModel.addRow(rowData); // Add row data to tableModel
                } else if (loggedInAccountID == transaction.getSenderAccount() && "RECEIVED".equals(transaction.getTransactionType())) {
                    // Display only received transactions if logged in with receiver account
                    String remarks = String.format("Nhận từ %s (tài khoản %d)",
                            TransactionController.getSenderName(transaction.getReceiverAccount()),
                            transaction.getReceiverAccount());
                    Object[] rowData = {
                            transaction.getTransactionId(),
                            transaction.getReceiverAccount(),
                            transaction.getSenderAccount(),
                            transaction.getAmount(),
                            transaction.getTransactionTime(),
                            remarks,
                            transaction.getTransactionType()
                    };
                    tableModel.addRow(rowData); // Add row data to tableModel
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi khi tải dữ liệu: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchTransactions() {
        String fromDate = textField_tuNgay.getText();
        String toDate = textField_denNgay.getText();
        long accountID = user.getAccountID(); // Get accountID of the currently logged in user

        if (fromDate.isEmpty() || toDate.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập thông tin ngày giao dịch cần tìm kiếm.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!isValidDateFormat(fromDate) || !isValidDateFormat(toDate)) {
            JOptionPane.showMessageDialog(this, "Định dạng ngày không hợp lệ. Vui lòng nhập theo định dạng yyyy-MM-dd.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            textField_tuNgay.setText("");
            textField_denNgay.setText("");
            return;
        }

        try {
            List<Transaction> transactions = TransactionController.searchTransactionsByTimeAndAccount(fromDate, toDate, accountID);

            if (transactions.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không có giao dịch nào trong khoảng từ ngày " + fromDate + " đến ngày " + toDate + ".", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return; // If no transactions found, no need to change tableModel
            }

            DefaultTableModel currentModel = (DefaultTableModel) table_view.getModel();

            for (Transaction transaction : transactions) {
                if (accountID == transaction.getSenderAccount() && "SENT".equals(transaction.getTransactionType())) {
                    // Display only sent transactions if logged in with sender account
                    String remarks = String.format("Chuyển khoản tới %s (tài khoản %d)",
                            TransactionController.getReceiverName(transaction.getReceiverAccount()),
                            transaction.getReceiverAccount());
                    Object[] rowData = {
                            transaction.getTransactionId(),
                            transaction.getSenderAccount(),
                            transaction.getReceiverAccount(),
                            transaction.getAmount(),
                            transaction.getTransactionTime(),
                            remarks,
                            transaction.getTransactionType()
                    };
                    currentModel.addRow(rowData); // Add row data to tableModel
                } else if (accountID != transaction.getSenderAccount() && "RECEIVED".equals(transaction.getTransactionType())) {
                    String remarks = String.format("Nhận từ %s (tài khoản %d)",
                            TransactionController.getSenderName(transaction.getSenderAccount()),
                            transaction.getSenderAccount());
                    Object[] rowData = {
                            transaction.getTransactionId(),
                            transaction.getSenderAccount(),
                            transaction.getReceiverAccount(),
                            transaction.getAmount(),
                            transaction.getTransactionTime(),
                            remarks,
                            transaction.getTransactionType()
                    };
                    currentModel.addRow(rowData); // Add row data to tableModel
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi khi tải dữ liệu: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isValidDateFormat(String dateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public void saveToExcel() throws ClassNotFoundException, SQLException {
        JFileChooser fileChooser = new JFileChooser();
        long accountID = user.getAccountID(); // Get accountID of the currently logged in user
        fileChooser.setDialogTitle("Chọn nơi lưu file Excel");
        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File fileToSave = fileChooser.getSelectedFile();

        try {
            TransactionController.saveTransactionsToExcel(tableModel,accountID, fileToSave.getAbsolutePath());
            JOptionPane.showMessageDialog(null, "Lưu file Excel thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi lưu file Excel: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}

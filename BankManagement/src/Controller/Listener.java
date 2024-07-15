package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import Model.User;
import View.SignUpView1;
import View.StartFrame;

public class Listener implements ActionListener {
    private StartFrame view;
    private CRUD crud;

    public Listener(StartFrame view) {
        this.view = view;
        this.crud = new CRUD();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("Đăng nhập")) {
            try {
				handleSignIn();
			} catch (ClassNotFoundException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        } else if (command.equals("Đăng ký")) {
            handleSignUp();
        }
    }

    private void handleSignIn() throws ClassNotFoundException, IOException, SQLException {
        String username = view.getUsername();
        String password = view.getPassword();

        String result = crud.signInAccount(username, password);

        if (result.equals("Chuyển sang menu Admin")) {
            view.showAdminMenu();
        } else if(result.equals("Chuyển sang menu User")) { 
        	view.showUserMenu();
        	
        }
        else { JOptionPane.showMessageDialog(view, result);
        }
    }

    private void handleSignUp() {
        // Mở giao diện đăng ký
        new SignUpView1();
        view.dispose();
    }
}

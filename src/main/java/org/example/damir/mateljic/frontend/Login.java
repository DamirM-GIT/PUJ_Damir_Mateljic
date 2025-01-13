package org.example.damir.mateljic.frontend;

import org.example.damir.mateljic.controllers.UserController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame {
    private JPanel mainPanel;
    private JButton loginBtn;
    private JButton registerBtn;
    private JTextField username;
    private JPasswordField password;

    public Login() {
        add(mainPanel);
        setTitle("Login");
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/app.logo.png"));
        setIconImage(icon.getImage());
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        loginBtn.addActionListener(e -> {
            String usernameText = username.getText();
            String passwordText = new String(password.getPassword());
            if(usernameText.isEmpty() || passwordText.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill in all fields.");
            } else {
                UserController userController = new UserController();
                var result=userController.login(usernameText, passwordText);
                if(result.isPresent()) {
                    JOptionPane.showMessageDialog(null, "User "+result.get().getUsername()+" logged in successfully.");
                    new MainDashboard(result.get());
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid credentials.");
                }
            }
        });
        registerBtn.addActionListener(e -> {
            new Register();
            dispose();
        });
    }
}

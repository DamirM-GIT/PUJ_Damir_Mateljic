package org.example.damir.mateljic.frontend;

import org.example.damir.mateljic.controllers.UserController;
import org.example.damir.mateljic.models.User;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Register extends JFrame {
    private JButton loginBtn;
    private JButton registerBtn;
    private JTextField username;
    private JPasswordField password;
    private JTextField role;
    private JPanel mainPanel;

    public Register() {
        add(mainPanel);
        setTitle("Register");
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/app.logo.png"));
        setIconImage(icon.getImage());
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        role.setText("employee");
        loginBtn.addActionListener(e -> {
            new Login();
            dispose();
        });
        registerBtn.addActionListener(e -> {
            String usernameText = username.getText();
            String passwordText = new String(password.getPassword());
            String roleText = role.getText();
            if(usernameText.isEmpty() || passwordText.isEmpty() || roleText.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill in all fields.");
            } else {
                UserController userController = new UserController();
                var succefully= userController.register(new User("",usernameText, passwordText, roleText));
                if(!succefully) {
                    JOptionPane.showMessageDialog(null, "User already exists,change username and password.");
                    return;
                }
                JOptionPane.showMessageDialog(null, "User "+usernameText+" registered successfully.");
            }
        });
        role.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                role.setToolTipText("You can register as employee");
            }
        });
    }

}

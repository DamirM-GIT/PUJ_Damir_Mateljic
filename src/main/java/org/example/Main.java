package org.example;

import org.example.damir.mateljic.controllers.UserController;
import org.example.damir.mateljic.frontend.Login;


public class Main {
    public static void main(String[] args) {
       var userController = new UserController();
       userController.createSuperAdmin();
       new Login();
    }
}
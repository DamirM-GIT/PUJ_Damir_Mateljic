package org.example.damir.mateljic.frontend;

import org.example.damir.mateljic.controllers.EmployeeController;
import org.example.damir.mateljic.models.Employee;

import javax.swing.*;
import java.util.Optional;

public class EmployeeCreator extends JFrame {
    private JTextField usernameField;
    private JSlider experience;
    private JTextField surnameField;
    private JComboBox<String> jobSelector;
    private JTextField salaryField;
    private JButton createButton;
    private JButton cancelButton;
    private JPanel mainPanel;
    private JLabel title;
    private EmployeeController employeeController;
    private String employeeId;

    public EmployeeCreator(String name) {
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/app.logo.png"));
        setIconImage(icon.getImage());
        if(name == null) {
            title.setText("Create Employee");
        }else{
            title.setText("Update Employee");
        }
        this.employeeId = name;
        jobSelector.addItem("Developer");
        jobSelector.addItem("Manager");
        jobSelector.addItem("Analyst");
        employeeController = new EmployeeController();
        initComponents();
        if (name != null && !name.isEmpty()) {
            loadEmployeeData(name);
        }
    }

    private void initComponents() {
        setTitle(employeeId == null ? "Create Employee" : "Edit Employee");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        setContentPane(mainPanel);

        experience.addChangeListener(e -> calculateSalary());
        jobSelector.addActionListener(e -> calculateSalary());

        createButton.setText(employeeId == null ? "Create" : "Edit");
        createButton.addActionListener(e -> {
            if (employeeId == null) {
                createEmployee();
            } else {
                editEmployee();
            }
        });

        cancelButton.addActionListener(e -> dispose());
    }

    private void loadEmployeeData(String name) {
        Optional<Employee> employee = employeeController.getEmployeeByName(name);
        if (employee.isPresent()) {
            usernameField.setText(employee.get().getName());
            surnameField.setText(employee.get().getSurname());
            jobSelector.setSelectedItem(employee.get().getJob());
            calculateSalary();
        }
    }

    private void calculateSalary() {
        String job = (String) jobSelector.getSelectedItem();
        int exp = experience.getValue();
        double baseSalary = 0;

        switch (job) {
            case "Developer":
                baseSalary = 50000;
                break;
            case "Manager":
                baseSalary = 70000;
                break;
            case "Analyst":
                baseSalary = 60000;
                break;
        }

        double salary = baseSalary * (1 + exp / 10.0);
        salaryField.setText(String.valueOf(salary));
    }

    private void createEmployee() {
        if(usernameField.getText().isEmpty() || surnameField.getText().isEmpty() || salaryField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }
        Employee employee = new Employee(
                null,
                usernameField.getText(),
                surnameField.getText(),
                (String) jobSelector.getSelectedItem(),
                salaryField.getText(),
                String.valueOf(experience.getValue())
        );
        if(employeeController.getEmployeeByName(employee.getName()).isPresent()){
            JOptionPane.showMessageDialog(this, "Employee already exists.");
            return;
        }
        employeeController.addEmployee(employee);
        JOptionPane.showMessageDialog(this, "Employee created successfully.");
        dispose();
    }

    private void editEmployee() {
        Employee employee = new Employee(
                employeeId,
                usernameField.getText(),
                surnameField.getText(),
                (String) jobSelector.getSelectedItem(),
                salaryField.getText(),
                String.valueOf(experience.getValue())
        );
        employeeController.updateEmployee(employee);
        JOptionPane.showMessageDialog(this, "Employee updated successfully.");
        dispose();
    }
}
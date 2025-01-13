package org.example.damir.mateljic.frontend;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import org.example.damir.mateljic.controllers.EmployeeController;
import org.example.damir.mateljic.models.Employee;
import org.example.damir.mateljic.models.User;

public class MainDashboard extends JFrame {
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JPanel tableView;
    private JScrollPane tableScroll;
    private JLabel loggedUser;
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private EmployeeController employeeController;
    private User loggedInUser;

    public MainDashboard(User loggedInUser) {
        this.loggedInUser = loggedInUser;
        setTitle("Main Dashboard");
        setSize(1024, 1024);
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/app.logo.png"));
        setIconImage(icon.getImage());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        employeeController = new EmployeeController();

        addButton = new JButton("Add Employee");
        updateButton = new JButton("Update Employee");
        deleteButton = new JButton("Delete Employee");

        loggedUser = new JLabel("Logged in as: " + loggedInUser.getUsername());
        loggedUser.setFont(new Font("Arial", Font.BOLD, 14));

        String[] columnNames = {"Rank", "ID", "Name", "Surname", "Job", "Salary", "Department"};
        tableModel = new DefaultTableModel(columnNames, 0);
        employeeTable = new JTable(tableModel);
        tableScroll = new JScrollPane(employeeTable);

        tableView = new JPanel(new BorderLayout());
        tableView.add(tableScroll, BorderLayout.CENTER);

        setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.add(loggedUser, BorderLayout.NORTH);
        topPanel.add(buttonPanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(tableView, BorderLayout.CENTER);

        if (!"superadmin".equals(loggedInUser.getRole())) {
            addButton.setEnabled(false);
            updateButton.setEnabled(false);
            deleteButton.setEnabled(false);
        }

        addButton.addActionListener(e -> {
            EmployeeCreator creator = new EmployeeCreator(null);
            creator.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadEmployeeData();
                }
            });
        });

        updateButton.addActionListener(e -> {
            int selectedRow = employeeTable.getSelectedRow();
            if (selectedRow >= 0) {
                String employeeName = (String) tableModel.getValueAt(selectedRow, 2);
                EmployeeCreator creator = new EmployeeCreator(employeeName);
                creator.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        loadEmployeeData();
                    }
                });
            } else {
                JOptionPane.showMessageDialog(this, "Please select an employee to update.");
            }
        });

        deleteButton.addActionListener(e -> deleteEmployee());

        loadEmployeeData();

        employeeTable.getColumnModel().getColumn(0).setCellRenderer(new RankCellRenderer());

        setVisible(true);
    }

    private void loadEmployeeData() {
        List<Employee> employees = employeeController.getAllEmployees();
        drawTable(employees);
    }

    private void drawTable(List<Employee> employees) {
        tableModel.setRowCount(0);
        for (int i = 0; i < employees.size(); i++) {
            Employee employee = employees.get(i);
            String rank = "";
            if (i == 0) {
                rank = "Gold";
            } else if (i == 1) {
                rank = "Silver";
            } else if (i == 2) {
                rank = "Bronze";
            } else {
                rank = String.valueOf(i + 1);
            }
            tableModel.addRow(new Object[]{rank, employee.getId(), employee.getName(), employee.getSurname(), employee.getJob(), employee.getSalary(), employee.getDepartment()});
        }
    }

    private void deleteEmployee() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow >= 0) {
            String employeeName = (String) tableModel.getValueAt(selectedRow, 2);
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete " + employeeName + "?", "Delete Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (employeeController.deleteEmployee(employeeName)) {
                    JOptionPane.showMessageDialog(this, "Employee deleted successfully.");
                    loadEmployeeData();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete employee.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an employee to delete.");
        }
    }

    private static class RankCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (value != null) {
                String rank = value.toString();
                switch (rank) {
                    case "Gold":
                        cell.setBackground(Color.YELLOW);
                        cell.setForeground(Color.BLACK);
                        break;
                    case "Silver":
                        cell.setBackground(Color.GRAY);
                        cell.setForeground(Color.WHITE);
                        break;
                    case "Bronze":
                        cell.setBackground(new Color(205, 127, 50));
                        cell.setForeground(Color.WHITE);
                        break;
                    default:
                        cell.setBackground(Color.WHITE);
                        cell.setForeground(Color.BLACK);
                        break;
                }
            }
            return cell;
        }
    }
}
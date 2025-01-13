package org.example.damir.mateljic.controllers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.example.damir.mateljic.database.DataBaseManager;
import org.example.damir.mateljic.models.Employee;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.logging.Logger;

public final class EmployeeController {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(EmployeeController.class);
    private final MongoDatabase database;
    private final MongoCollection<Document> employeeCollection;
    private final Logger logger = Logger.getLogger(EmployeeController.class.getName());

    public EmployeeController() {
        this.database = DataBaseManager.getInstance().getDatabase();
        this.employeeCollection = database.getCollection("employees");
    }

    public boolean addEmployee(Employee employee) {
        if (employeeExists(employee.getName())) {
            logger.info("Employee already exists: " + employee.getName());
            return false;
        }
        employee.setId(employee.getName()+new Random().nextInt(1000));
        Document newEmployee = new Document("id", employee.getId())
                .append("name", employee.getName())
                .append("surname", employee.getSurname())
                .append("job", employee.getJob())
                .append("salary", employee.getSalary())
                .append("department", employee.getDepartment());

        employeeCollection.insertOne(newEmployee);
        logger.info("Employee added successfully: " + employee.getName());
        return true;
    }

    public Optional<Employee> getEmployeeByName(String name) {
        Document query = new Document("name", name);
        Document employeeDoc = employeeCollection.find(query).first();

        if (employeeDoc != null) {
            Employee employee = new Employee(
                    employeeDoc.getString("id"),
                    employeeDoc.getString("name"),
                    employeeDoc.getString("surname"),
                    employeeDoc.getString("job"),
                    employeeDoc.getString("salary"),
                    employeeDoc.getString("department")
            );
            logger.info("Employee found: " + employee.getName());
            return Optional.of(employee);
        } else {
            logger.info("Employee not found: " + name);
            return Optional.empty();
        }
    }

    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        for (Document doc : employeeCollection.find()) {
            Employee employee = new Employee(
                    doc.getString("id"),
                    doc.getString("name"),
                    doc.getString("surname"),
                    doc.getString("job"),
                    doc.getString("salary"),
                    doc.getString("department")
            );
            employees.add(employee);
        }
        employees.sort((e1, e2) -> {
            try {
                double salary1 = Double.parseDouble(e1.getSalary());
                double salary2 = Double.parseDouble(e2.getSalary());
                return Double.compare(salary1, salary2);
            } catch (NumberFormatException ex) {
                logger.warning("Invalid salary format for employee: " + ex.getMessage());
                return 0;
            }
        });
        logger.info("All employees retrieved and sorted by salary.");
        return employees;
    }
    public boolean deleteEmployee(String name) {
        Document query = new Document("name", name);
        if (employeeCollection.deleteOne(query).getDeletedCount() > 0) {
            logger.info("Employee deleted successfully: " + name);
            return true;
        } else {
            logger.info("Employee not found: " + name);
            return false;
        }
    }
    public boolean updateEmployee(Employee employee) {
        Document query = new Document("id", employee.getId());
        Document updatedEmployee = new Document("name", employee.getName())
                .append("surname", employee.getSurname())
                .append("job", employee.getJob())
                .append("salary", employee.getSalary())
                .append("department", employee.getDepartment());

        Document update = new Document("$set", updatedEmployee);
        if (employeeCollection.updateOne(query, update).getModifiedCount() > 0) {
            logger.info("Employee updated successfully: " + employee.getName());
            return true;
        } else {
            logger.info("Employee not found: " + employee.getName());
            return false;
        }
    }

    private boolean employeeExists(String name) {
        Document query = new Document("name", name);
        return employeeCollection.find(query).first() != null;
    }
}
package org.example.damir.mateljic.models;

public final class Employee {
    private String id;
    private String name;
    private String surname;
    private String job;
    private String salary;
    private String department;

    public Employee() {
    }

    public Employee(String id, String name, String surname, String job, String salary, String department) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.job = job;
        this.salary = salary;
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getJob() {
        return job;
    }

    public String getSalary() {
        return salary;
    }

    public String getDepartment() {
        return department;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

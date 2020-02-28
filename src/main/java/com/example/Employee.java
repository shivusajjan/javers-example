package com.example;

import org.javers.core.metamodel.annotation.Id;
import org.javers.core.metamodel.annotation.TypeName;

import java.util.List;

@TypeName("Employee")
class Employee {
    @Id
    private String name;

    private int salary;

    private int age;

    private List<Address> addressList;

    public Employee(String name, int salary, int age, List<Address> addressList) {
        this.name = name;
        this.salary = salary;
        this.age = age;
        this.addressList = addressList;
    }

    public List<Address> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
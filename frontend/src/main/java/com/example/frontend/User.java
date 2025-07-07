package com.example.frontend;

public class User {
    private String name;
    private Integer age;
    private String dateOfBirth;
    private String contactNumber;
    private String country;
    private String state;
    private String city;
    private String password;

    public User() {}

    public User(String name, Integer age, String dateOfBirth, String contactNumber,
                String country, String state, String city, String password) {
        this.name = name;
        this.age = age;
        this.dateOfBirth = dateOfBirth;
        this.contactNumber = contactNumber;
        this.country = country;
        this.state = state;
        this.city = city;
        this.password = password;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.lang.Integer getAge() {
        return age;
    }

    public void setAge(java.lang.Integer age) {
        this.age = age;
    }

    public java.lang.String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(java.lang.String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public java.lang.String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(java.lang.String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public java.lang.String getCountry() {
        return country;
    }

    public void setCountry(java.lang.String country) {
        this.country = country;
    }

    public java.lang.String getState() {
        return state;
    }

    public void setState(java.lang.String state) {
        this.state = state;
    }

    public java.lang.String getCity() {
        return city;
    }

    public void setCity(java.lang.String city) {
        this.city = city;
    }

    public java.lang.String getPassword() {
        return password;
    }

    public void setPassword(java.lang.String password) {
        this.password = password;
    }
// Getters and Setters (optional, only if needed)
}

package com.example.hotelmanagmet;

public class employee {
    private String cin;
    private String firstname;
    private String lastname;
    private String phone;
    private String gender;
    private String salary;
    private String datebirth;
    private String datemember;

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getDatebirth() {
        return datebirth;
    }

    public void setDatebirth(String datebirth) {
        this.datebirth = datebirth;
    }

    public String getDatemember() {
        return datemember;
    }

    public void setDatemember(String datemember) {
        this.datemember = datemember;
    }

    public employee(String cin, String firstname, String lastname, String phone, String gender, String salary, String datebirth, String datemember) {
        this.cin = cin;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.gender = String.valueOf ( gender );
        this.salary = salary;
        this.datebirth = datebirth;
        this.datemember = datemember;
    }
}

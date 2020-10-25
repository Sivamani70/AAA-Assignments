package com.example.firebaseauth;

public class DataModel {

    String NAME, ROLL_NUMBER, GENDER, MAIL, PASSWORD, PHONE_NUMBER, BRANCH, YEAR, LANGUAGES, USERNAME;

    public DataModel() {
    }

    public DataModel(String name, String rollNumber, String gender, String mail, String password, String phoneNumber, String branch, String year, String languages, String userName) {
        this.NAME = name;
        this.ROLL_NUMBER = rollNumber;
        this.GENDER = gender;
        this.MAIL = mail;
        this.PASSWORD = password;
        this.PHONE_NUMBER = phoneNumber;
        this.BRANCH = branch;
        this.YEAR = year;
        this.LANGUAGES = languages;
        this.USERNAME = userName;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public String getNAME() {
        return NAME;
    }

    public String getROLL_NUMBER() {
        return ROLL_NUMBER;
    }

    public String getGENDER() {
        return GENDER;
    }

    public String getMAIL() {
        return MAIL;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public String getPHONE_NUMBER() {
        return PHONE_NUMBER;
    }

    public String getBRANCH() {
        return BRANCH;
    }

    public String getYEAR() {
        return YEAR;
    }

    public String getLANGUAGES() {
        return LANGUAGES;
    }
}

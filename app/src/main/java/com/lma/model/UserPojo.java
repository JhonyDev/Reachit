package com.lma.model;

public class UserPojo {

    String username;
    String email;
    String phoneNumber;
    String emergencyPhoneNumber;
    String height;
    String weight;
    String BMI;
    String isDiabetic;
    String isBloodPressure;
    String isHeartDisease;

    public UserPojo() {
    }

    public UserPojo(
            String username,
            String email,
            String phoneNumber,
            String emergencyPhoneNumber,
            String height,
            String weight,
            String BMI,
            String isDiabetic,
            String isBloodPressure,
            String isHeartDisease
    ) {
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.emergencyPhoneNumber = emergencyPhoneNumber;
        this.height = height;
        this.weight = weight;
        this.BMI = BMI;
        this.isDiabetic = isDiabetic;
        this.isBloodPressure = isBloodPressure;
        this.isHeartDisease = isHeartDisease;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmergencyPhoneNumber() {
        return emergencyPhoneNumber;
    }

    public void setEmergencyPhoneNumber(String emergencyPhoneNumber) {
        this.emergencyPhoneNumber = emergencyPhoneNumber;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getBMI() {
        return BMI;
    }

    public void setBMI(String BMI) {
        this.BMI = BMI;
    }

    public String getIsDiabetic() {
        return isDiabetic;
    }

    public void setIsDiabetic(String isDiabetic) {
        this.isDiabetic = isDiabetic;
    }

    public String getIsBloodPressure() {
        return isBloodPressure;
    }

    public void setIsBloodPressure(String isBloodPressure) {
        this.isBloodPressure = isBloodPressure;
    }

    public String getIsHeartDisease() {
        return isHeartDisease;
    }

    public void setIsHeartDisease(String isHeartDisease) {
        this.isHeartDisease = isHeartDisease;
    }

}

package com.lma.model;

public class UserPojo {

    String username;
    String email;
    String age;
    String gender;
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
            String age,
            String gender,
            String height,
            String weight,
            String BMI,
            String isDiabetic,
            String isBloodPressure,
            String isHeartDisease
    ) {
        this.username = username;
        this.email = email;
        this.age = age;
        this.gender = gender;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

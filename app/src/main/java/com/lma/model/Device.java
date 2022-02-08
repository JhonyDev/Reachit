package com.lma.model;

public class Device {
    String id;
    String name;
    String modelNumber;
    String IMEI;
    String password;

    public Device() {
    }

    public Device(String id, String name, String modelNumber, String IMEI, String password) {
        this.id = id;
        this.name = name;
        this.modelNumber = modelNumber;
        this.IMEI = IMEI;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getIMEI() {
        return IMEI;
    }

    public void setIMEI(String IMEI) {
        this.IMEI = IMEI;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

package com.ken.app;

import java.io.Serializable;

public class Computer implements Serializable {
    //private int id;
    private String mac;
    private String serial;

    public Computer(String serial, String mac) {
        //this.id = id;
        this.mac = mac.toUpperCase();
        this.serial = serial;
    }

    public String getMac() {
        return mac;
    }

    public String getSerial() {
        return serial;
    }
}

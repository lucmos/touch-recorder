package it.uniroma1.touchrecorder.data;

import com.google.gson.GsonBuilder;

/**
 * Created by luca on 29/12/17.
 */

public class SessionData {
    public DeviceData deviceData;
    public int id;
    public int totalWordNumber;
    public String name;
    public String surname;
    public int age;
    public Gender gender;
    public Handwriting handwriting;

    public SessionData(DeviceData deviceData, int id, int totalWordNumber, String name, String surname, int age, Gender gender, Handwriting handwriting) {
        this.deviceData = deviceData;
        this.id = id;
        this.totalWordNumber = totalWordNumber;
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.gender = gender;
        this.handwriting = handwriting;
    }

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }
}

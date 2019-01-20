package it.uniroma1.touchrecorder.data;

import android.annotation.SuppressLint;

import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by luca on 29/12/17.
 */

public class SessionData {
    public Configuration configuration;
    public DeviceData deviceData;
    public String name;
    public String surname;
    public int age;
    public Gender gender;
    public String date;

    @SuppressLint("SimpleDateFormat")
    public SessionData(Configuration configuration, DeviceData deviceData, String name, String surname, int age, Gender gender, String date) {
        this.configuration = configuration;
        this.deviceData = deviceData;
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.gender = gender;
        this.date = date;
    }

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }
}

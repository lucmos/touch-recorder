package it.uniroma1.touchrecorder.data;

import com.google.gson.GsonBuilder;

/**
 * Created by luca on 29/12/17.
 */

public class DeviceData {

    public String device_model;
    public String device_finger_print;
    public int width_pixels;
    public int heigth_pixels;
    public float xdpi;
    public float ydpi;

    public DeviceData(String DeviceModel, String DeviceFingerPrint, int width_pixels, int heigth_pixels,
                      float xdpi, float ydpi)
    {
        device_model = DeviceModel;
        device_finger_print = DeviceFingerPrint;
        this.width_pixels = width_pixels;
        this.heigth_pixels = heigth_pixels;
        this.xdpi = xdpi;
        this.ydpi = ydpi;
    }

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }
}

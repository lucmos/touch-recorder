package it.uniroma1.touchrecorder.data;

import com.google.gson.GsonBuilder;

/**
 * Created by luca on 29/12/17.
 */

public class DeviceData {

    public String deviceModel;
    public String deviceFingerPrint;
    public int widthPixels;
    public int heigthPixels;
    public float xdpi;
    public float ydpi;

    public DeviceData(String DeviceModel, String DeviceFingerPrint, int widthPixels, int heigthPixels,
                      float xdpi, float ydpi)
    {
        deviceModel = DeviceModel;
        deviceFingerPrint = DeviceFingerPrint;
        this.widthPixels = widthPixels;
        this.heigthPixels = heigthPixels;
        this.xdpi = xdpi;
        this.ydpi = ydpi;
    }

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }
}

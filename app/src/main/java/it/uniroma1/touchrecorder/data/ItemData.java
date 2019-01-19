package it.uniroma1.touchrecorder.data;

import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import it.uniroma1.touchrecorder.Save;

/**
 * Created by luca on 29/12/17.
 */

public class ItemData {


    public final String date;
    public SessionData sessionData;

    public int itemNumber;

    public List<TimedComponentFloatPoint> touchDownPoints = new ArrayList<>();
    public List<TimedComponentFloatPoint> touchUpPoints = new ArrayList<>();
    public List<TimedComponentFloatPoint> movementPoints = new ArrayList<>();
    public ArrayList<List<FloatPoint>> sampledPoints;

    public ItemData(SessionData sessionData, int itemNumber) {
        this.sessionData = sessionData;
        this.itemNumber = itemNumber;
        this.date = Save.getInstance().getDate();
    }

    public void addTouchDownPoint(TimedComponentFloatPoint point) {
        touchDownPoints.add(point);
    }

    public void addTouchUpPoint(TimedComponentFloatPoint point) {
        touchUpPoints.add(point);
    }

    public void addMovementPoint(TimedComponentFloatPoint point) {
        movementPoints.add(point);
    }

    public void setSampledPoints(List<List<FloatPoint>> points) {
        sampledPoints = new ArrayList<>(points);
    }

    public ItemData getNextWordData() {
        return new ItemData(sessionData, itemNumber + 1);
    }

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }

}

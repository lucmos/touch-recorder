package it.uniroma1.touchrecorder.data;

import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import it.uniroma1.touchrecorder.Save;

/**
 * Created by luca on 29/12/17.
 */

public class WordData {


    public final String date;
    public SessionData sessionData;

    public int wordNumber;

    public List<TimedComponentFloatPoint> touchDownPoints = new ArrayList<>();
    public List<TimedComponentFloatPoint> touchUpPoints = new ArrayList<>();
    public List<TimedComponentFloatPoint> movementPoints = new ArrayList<>();
    public ArrayList<List<FloatPoint>> sampledPoints;

    public WordData(SessionData sessionData, int wordNumber) {
        this.sessionData = sessionData;
        this.wordNumber = wordNumber;
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

    public WordData getNextWordData() {
        return new WordData(sessionData, wordNumber + 1);
    }

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }

}

package it.uniroma1.touchrecorder.data;

import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import it.uniroma1.touchrecorder.io.NamesManager;

/**
 * Created by luca on 29/12/17.
 */

public class ItemData {


    public final String date;
    public SessionData session_data;

    public int item_index;
    public String item;

    public List<TimedComponentFloatPoint> touch_down_points = new ArrayList<>();
    public List<TimedComponentFloatPoint> touch_up_points = new ArrayList<>();
    public List<TimedComponentFloatPoint> movement_points = new ArrayList<>();
    public List<FloatPoint> sampled_points;

    public ItemData(SessionData session_data, int item_index) {
        this.session_data = session_data;
        this.item_index = item_index;
        this.date = NamesManager.getDate();
        this.item = DataProvider.getInstance().getItems_provider().getNormalized(item_index);
    }

    public void addTouchDownPoint(TimedComponentFloatPoint point) {
        touch_down_points.add(point);
    }

    public void addTouchUpPoint(TimedComponentFloatPoint point) {
        touch_up_points.add(point);
    }

    public void addMovementPoint(TimedComponentFloatPoint point) {
        movement_points.add(point);
    }

    public void setSampledPoints(List<FloatPoint> points) {
        sampled_points = new ArrayList<>(points);
    }

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }

}

package it.uniroma1.touchrecorder.data;

/**
 * Created by luca on 29/12/17.
 */

public class TimedComponentFloatPoint extends ComponentFloatPoint{
    public long time;

    public TimedComponentFloatPoint(long time, int component, float x, float y) {
        super(component, x, y);
        this.time = time;
    }
}


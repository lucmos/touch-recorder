package it.uniroma1.touchrecorder.data;

/**
 * Created by luca on 29/12/17.
 */

public class FloatPoint {
    public  float x, y;

    public FloatPoint(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("(%f, %f)", x, y);
    }
}

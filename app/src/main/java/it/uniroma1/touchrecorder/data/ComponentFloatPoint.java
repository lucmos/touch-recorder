package it.uniroma1.touchrecorder.data;

/**
 * Created by luca on 29/12/17.
 */

public class ComponentFloatPoint extends FloatPoint {
    public int component;

    public ComponentFloatPoint( int component, float x, float y) {
        super(x, y);

        this.component = component;
    }
}

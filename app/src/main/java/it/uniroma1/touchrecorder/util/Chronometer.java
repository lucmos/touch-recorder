package it.uniroma1.touchrecorder.util;

import android.os.SystemClock;

/**
 * Created by luca on 29/12/17.
 */

public class Chronometer {

    private final long real_start_time;

    public Chronometer() {
        this.real_start_time = SystemClock.elapsedRealtime();
    }


    public long getElapsedTime() {
        return SystemClock.elapsedRealtime() - real_start_time;
    }

}

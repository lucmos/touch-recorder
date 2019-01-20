package it.uniroma1.touchrecorder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import it.uniroma1.touchrecorder.data.ComponentFloatPoint;
import it.uniroma1.touchrecorder.data.DeviceData;
import it.uniroma1.touchrecorder.data.FloatPoint;
import it.uniroma1.touchrecorder.data.SessionData;
import it.uniroma1.touchrecorder.data.TimedComponentFloatPoint;
import it.uniroma1.touchrecorder.data.ItemData;
import it.uniroma1.touchrecorder.util.Chronometer;

/**
 * Created by luca on 29/12/17.
 */
public class DrawingView extends View {

// Acquisition
    public static final float TOUCH_TOLERANCE = 0;
    public static final float SAMPLING_RATE = 5;
    private int component_count;

    private ItemData itemData;
    private SessionData sessionData;
    private DeviceData deviceData;

    // Appearance
    private DrawingActivity activity;

    public static final float LOW_BAR_PERC_POS = 0.7f;
    public static final float HIGH_BAR_PERC_POS = 0.45f;
    public static final float VERTICAL_BAR_PERC_POS = 0.1f;

    public static final float RADIUS_CURSOR = 30;
    public static final float RADIUS_MOVE = 4;
    public static final float RADIUS_UP_DOWN = 10;
    public static final float RADIUS_SAMPLED = 15;
    public static final Path.Direction CIRCLE_DIRECTION = Path.Direction.CW;


// Private stuff
    private Bitmap privateBitmap;
    private Paint privateBitmapPaint;
    private Canvas privateCanvas;

    private Path linePath;
    private Paint linePaint;

    private Path permanentLinePath;

    private Path cursorPath;
    private Paint cursorPaint;

    private Path touchMoveCirclePath;
    private Paint sampleMovePaint;

    private Path touchDownCirclePath;
    private Paint sampleDownPaint;

    private Path touchUpCirclePath;
    private Paint sampleUpPaint;

    private Path sampledCirclePath;
    private Paint sampledCirclePaint;

    private Paint borderLines;
    private Paint guideLines;


    public DrawingView(Context context,  AttributeSet attrs) {
        super(context, attrs);


        privateBitmapPaint = new Paint(Paint.DITHER_FLAG);

        // LinePath must be resetted each time for efficiency
        permanentLinePath = new Path();

        linePath = new Path();
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setDither(true);
        linePaint.setColor(Color.LTGRAY);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeJoin(Paint.Join.ROUND);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        linePaint.setStrokeWidth(1);

        sampledCirclePath = new Path();
        sampledCirclePaint = new Paint();
        sampledCirclePaint.setAntiAlias(true);
        sampledCirclePaint.setDither(true);
        sampledCirclePaint.setColor(Color.CYAN);
        sampledCirclePaint.setAlpha(50);
        sampledCirclePaint.setStyle(Paint.Style.STROKE);
        sampledCirclePaint.setStrokeJoin(Paint.Join.ROUND);
        sampledCirclePaint.setStrokeCap(Paint.Cap.ROUND);
        sampledCirclePaint.setStrokeWidth(1);

        touchMoveCirclePath = new Path();
        sampleMovePaint = new Paint();
        sampleMovePaint.setAntiAlias(true);
        sampleMovePaint.setColor(Color.WHITE);
        sampleMovePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        sampleMovePaint.setStrokeJoin(Paint.Join.MITER);
        sampleMovePaint.setStrokeWidth(1f);

        touchDownCirclePath = new Path();
        sampleDownPaint = new Paint();
        sampleDownPaint.setAntiAlias(true);
        sampleDownPaint.setColor(Color.GREEN);
        sampleDownPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        sampleDownPaint.setStrokeJoin(Paint.Join.MITER);
        sampleDownPaint.setStrokeWidth(1f);

        touchUpCirclePath = new Path();
        sampleUpPaint = new Paint();
        sampleUpPaint.setAntiAlias(true);
        sampleUpPaint.setColor(Color.RED);
        sampleUpPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        sampleUpPaint.setStrokeJoin(Paint.Join.MITER);
        sampleUpPaint.setStrokeWidth(1f);

        cursorPath = new Path();
        cursorPaint = new Paint();
        cursorPaint.setAntiAlias(true);
        cursorPaint.setColor(Color.WHITE);
        cursorPaint.setStyle(Paint.Style.STROKE);
        cursorPaint.setStrokeJoin(Paint.Join.MITER);
        cursorPaint.setStrokeWidth(4f);

        borderLines = new Paint();
        borderLines.setAntiAlias(true);
        borderLines.setDither(true);
        borderLines.setColor(Color.WHITE);
        borderLines.setStyle(Paint.Style.STROKE);
        borderLines.setStrokeJoin(Paint.Join.ROUND);
        borderLines.setStrokeCap(Paint.Cap.ROUND);
        borderLines.setStrokeWidth(2);

        guideLines = new Paint();
        guideLines.setAntiAlias(true);
        guideLines.setDither(true);
        guideLines.setColor(Color.GRAY);
        guideLines.setStyle(Paint.Style.STROKE);
        guideLines.setStrokeJoin(Paint.Join.ROUND);
        guideLines.setStrokeCap(Paint.Cap.ROUND);
        guideLines.setStrokeWidth(1);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        privateBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        privateCanvas = new Canvas(privateBitmap);
        privateCanvas.translate(-getLeft(), -getTop());
        initScreen();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(privateBitmap, 0, 0, privateBitmapPaint);

        canvas.translate(-getLeft(), -getTop());
        canvas.drawPath(linePath, linePaint);
        canvas.drawPath(touchMoveCirclePath, sampleMovePaint);
        canvas.drawPath(touchDownCirclePath, sampleDownPaint);
        canvas.drawPath(touchUpCirclePath, sampleUpPaint);

        canvas.drawPath(sampledCirclePath, sampledCirclePaint);
        canvas.drawPath(cursorPath, cursorPaint);
    }

    private Chronometer chrono;
    private float mX, mY;
    private void touch_start(float x, float y)
    {
//        Log.i("START", String.format("to: (x: %f, y: %f)", x, y));

        long time;
        if (chrono == null) {
            chrono = new Chronometer();
            time = 0;
        } else {
            time = chrono.getElapsedTime();
        }

        linePath.rewind();

        linePath.moveTo(x, y);
        permanentLinePath.moveTo(x, y);

        saveMoveEvent(time, component_count, x, y);
        saveDownEvent(time, component_count, x, y);

        mX = x;
        mY = y;
    }

    private void touch_move(float x, float y) {
//        Log.i("MOVE", String.format("to: (x: %f, y: %f)", x, y));

        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        float xavg;
        float yavg;
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            xavg = (x + mX) / 2;
            yavg = (y + mY) / 2;

            linePath.quadTo(mX, mY, xavg, yavg);
            permanentLinePath.quadTo(mX, mY, xavg, yavg);

            mX = x;
            mY = y;

            saveMoveEvent(chrono.getElapsedTime(), component_count, x, y);

            cursorPath.rewind();
            cursorPath.addCircle(x, y, RADIUS_CURSOR, CIRCLE_DIRECTION);
        }
    }

    private void touch_up() {
//        Log.i("UP", "-");


        linePath.lineTo(mX, mY);
        permanentLinePath.lineTo(mX, mY);

        saveUpEvent(chrono.getElapsedTime(), component_count, mX, mY);

        cursorPath.rewind();

        // commit the path to our offscreen
        privateCanvas.drawPath(linePath, linePaint);
        privateCanvas.drawPath(touchMoveCirclePath, sampleMovePaint);
        privateCanvas.drawPath(touchDownCirclePath, sampleDownPaint);
        privateCanvas.drawPath(touchUpCirclePath, sampleUpPaint);

        // kill this so we don't double draw
        linePath.rewind();
        touchMoveCirclePath.rewind();
        touchDownCirclePath.rewind();
        touchUpCirclePath.rewind();

        component_count++;
    }

    boolean is_only_down = false;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getRawX();
        float y = event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                is_only_down = true;

                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                is_only_down = false;
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (is_only_down)
                {
                    is_only_down = false;
                    touch_move((float) (x + 0.01), (float) (y + 0.01));
                    invalidate();
                }

                touch_up();
                invalidate();
                break;
        }
        return true;
    }


    private void saveDownEvent(long time, int component, float x, float y) {
        itemData.addTouchDownPoint(new TimedComponentFloatPoint(time, component, x, y));

        touchDownCirclePath.addCircle(x, y, RADIUS_UP_DOWN, CIRCLE_DIRECTION);
        setTimerText(time + "");
    }

    private void saveUpEvent(long time,int component, float x, float y) {
        itemData.addTouchUpPoint(new TimedComponentFloatPoint(time, component, x, y));

        touchUpCirclePath.addCircle(mX, mY, RADIUS_UP_DOWN, CIRCLE_DIRECTION);
        setTimerText(time + "");
    }

    private void saveMoveEvent(long time,int component, float x, float y) {
        itemData.addMovementPoint(new TimedComponentFloatPoint(time, component, x, y));

        touchMoveCirclePath.addCircle(x, y, RADIUS_MOVE, CIRCLE_DIRECTION);
        setTimerText(time + "");
    }

    private void sampleFuctionLine() {
        itemData.setSampledPoints(extractSampling());
    }


    public List<List<FloatPoint>> extractSampling()
    {
        List<List<FloatPoint>> path_points = new ArrayList<>();

        PathMeasure pm = new PathMeasure(permanentLinePath, false);
        float[] coordinates = new float[2];

        int connected_component = 0;
        while (pm.nextContour()) {
            ArrayList<FloatPoint> points = new ArrayList<>();

            for (float i = 0; i <= pm.getLength(); i = i + SAMPLING_RATE) {
                pm.getPosTan(i, coordinates, null);
                points.add(new ComponentFloatPoint(connected_component, coordinates[0], coordinates[1]));
            }
            path_points.add(points);

            connected_component +=1 ;
        }

//        int c = 0;
//        for (List<FloatPoint> l : path_points)
//        {
//            Log.i(String.format("COMPONENT %d", c), l + "");
//            c+=1;
//        }

        return path_points;
    }


    public void drawExtractSampling(List<List<FloatPoint>> path_points)
    {
        sampledCirclePath.rewind();
        for (List<FloatPoint> a : path_points) {
            for (FloatPoint p : a) {
                sampledCirclePath.addCircle(p.x, p.y, RADIUS_SAMPLED, CIRCLE_DIRECTION);
            }
        }
        invalidate();
    }




    public void restart() {
        itemData = new ItemData(sessionData, itemData.item_index);
        component_count = 0;

        setTimerText(getResources().getString(R.string.time));
        chrono = null;

        resetPath();
    }

    public void resetPath()
    {
        cursorPath.reset();

        linePath.reset();
        permanentLinePath.reset();

        touchDownCirclePath.reset();
        touchMoveCirclePath.reset();
        touchUpCirclePath.reset();

        sampledCirclePath.reset();

        initScreen();
    }

    private void initScreen()
    {
        float startX = getLeft();
        float startY = getTop();

        privateBitmap.eraseColor(getBackgroundColor());

        privateCanvas.drawLine(startX, startY, startX +getWidth(), startY, borderLines);
        privateCanvas.drawLine(startX, startY + getHeight(), startX + getWidth(), startY + getHeight(), borderLines);

        if (sessionData.configuration.guide_lines) {
            privateCanvas.drawLine(startX, startY+ (getHeight()* LOW_BAR_PERC_POS), startX + getWidth(), startY + (getHeight()* LOW_BAR_PERC_POS), guideLines);
            privateCanvas.drawLine(startX,startY +  (getHeight()* HIGH_BAR_PERC_POS), startX +  getWidth(), startY +  (getHeight()* HIGH_BAR_PERC_POS), guideLines);
            privateCanvas.drawLine(startX + getWidth()* VERTICAL_BAR_PERC_POS, startY, startX +  getWidth()* VERTICAL_BAR_PERC_POS, startY + getHeight(), guideLines);
            privateCanvas.drawLine(startX + getWidth()*(1- VERTICAL_BAR_PERC_POS), startY, startX+ getWidth()*(1- VERTICAL_BAR_PERC_POS), startY+ getHeight(), guideLines);
        }

        invalidate();
    }

	private int getBackgroundColor() {
		TypedValue a = new TypedValue();
		getContext().getTheme().resolveAttribute(android.R.attr.windowBackground, a, true);
		return a.data;
	}


    public void setTimerText(String s) {
        activity.setTimerTextView(s);
    }


    public ItemData getItemData() {
        sampleFuctionLine();

        return itemData;
    }

    public void setItemData(ItemData data) {
        itemData = data;
        sessionData = data.sessionData;
        deviceData = sessionData.deviceData;
    }

    public void finish()
    {
        if (privateCanvas != null) {
            privateCanvas.setBitmap(null);
            privateCanvas = null;
        }

        if (privateBitmap != null) {
            privateBitmap.recycle();
            privateBitmap = null;
        }
    }


    public void setActivity(DrawingActivity activity) {
        this.activity = activity;
    }
}
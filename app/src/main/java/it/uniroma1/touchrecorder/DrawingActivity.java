package it.uniroma1.touchrecorder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;

import it.uniroma1.touchrecorder.data.DataProvider;
import it.uniroma1.touchrecorder.data.SessionData;
import it.uniroma1.touchrecorder.data.ToastManager;
import it.uniroma1.touchrecorder.data.ItemData;
import it.uniroma1.touchrecorder.io.NamesManager;
import it.uniroma1.touchrecorder.io.Saver;

/**
 * Created by luca on 29/12/17.
 */

public class DrawingActivity extends Activity {

    public static final String SESSION_KEY = "session_data_key";
    public static final String ITEM_NUMBER_KEY = "word_number_key";
    public static final String TIMESTAMP_KEY = "timestamp_key";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.drawing_layout);

        DrawingView drawingView = findViewById(R.id.drawing_view_id);
        drawingView.setActivity(this);


        Bundle b = getIntent().getExtras();
        String session;
        ItemData itemData;
        SessionData sessionData = null;

        int item_index = -1;
        if (b != null) {
            session = b.getString(SESSION_KEY);
            item_index = b.getInt(ITEM_NUMBER_KEY);
            sessionData = new Gson().fromJson(session, SessionData.class);
        }

        if (b == null || sessionData == null || item_index == -1) {
            throw new RuntimeException("Extraction of bundle failed!");
        }


        if (sessionData.configuration == null)
            throw new RuntimeException("Configuration is null!");

        itemData = new ItemData(sessionData, item_index);
        drawingView.setItemData(itemData);

        TextView counterView = findViewById(R.id.current_number_label);
        counterView.setText(String.valueOf(item_index + 1));

        TextView totalNumberView = findViewById(R.id.total_number_label);
        totalNumberView.setText(String.valueOf(DataProvider.getInstance()
                .getItems_provider().getNumberOfItems()));

        TextView title = findViewById(R.id.title_label);
        title.setText(DataProvider.getInstance().getTitle());

        TextView itemText = findViewById(R.id.item_text);
        if (itemText == null)
        {
            Toast.makeText(this, "ERROR: There is a null class in the json!", Toast.LENGTH_SHORT).show();
            throw new RuntimeException("There is a null class in the json!");
        }
        itemText.setText(DataProvider.getInstance().getItems_provider().get(item_index));
    }

    public void setTimerTextView(String s) {
        TextView timerText = findViewById(R.id.timer);
        timerText.setText(s);
    }

    public void clear(View view) {

        DrawingView drawView = (DrawingView) findViewById(R.id.drawing_view_id);
        drawView.restart();
    }


    private Thread thread;
    private  File path_file;

    public synchronized void next(View view) {
        view.setEnabled(false);

        DrawingView drawView = (DrawingView) findViewById(R.id.drawing_view_id);
        final ItemData item_data = drawView.getItemData();

        if (item_data.touch_up_points.isEmpty()) {
            ToastManager.getInstance().toastNoWord(this);

            view.setEnabled(true);
            return;
        }
        ToastManager.getInstance().resetNoWord();

        Saver.takeScreenshot(this, NamesManager.sessionDirectory(item_data.session_data, item_data.item_index, item_data.session_data.date), NamesManager.getScreenshotName(item_data));

        thread = new Thread() {
            @Override
            public void run() {
                synchronized (DrawingActivity.this) {
                    path_file = Saver.saveItemData(item_data, item_data.session_data.date);
                }
            }
        };
        thread.start();

        int next_index = DataProvider.getInstance().getItems_provider().nextIndex(item_data.item_index);
        int total_number_items = DataProvider.getInstance().getItems_provider().getNumberOfItems();
        if (next_index >= total_number_items) {
            Toast.makeText(this, "Session completed! Thank you!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ToastManager.getInstance().toastSavedWord(this);

        String sessionData = new Gson().toJson(item_data.session_data);
        Intent intent = new Intent(this, DrawingActivity.class);
        Bundle b = new Bundle();
        b.putString(SESSION_KEY, sessionData);
        b.putInt(ITEM_NUMBER_KEY, next_index); //Your id

        intent.putExtras(b); //Put your id to your next Intent
        startActivity(intent);
        finish();
    }

    public void samples(View view) {
        DrawingView drawView = (DrawingView) findViewById(R.id.drawing_view_id);
        drawView.drawExtractSampling(drawView.extractSampling());
    }

    @Override
    protected void onDestroy() {
        if (thread != null) {
            try {
                thread.join();
                NamesManager.scanFile(this, path_file);
                ToastManager.getInstance().resetSaveWord();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        DrawingView drawView = (DrawingView) findViewById(R.id.drawing_view_id);
        drawView.finish();
        super.onDestroy();
    }
}


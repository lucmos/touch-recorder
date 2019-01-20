package it.uniroma1.touchrecorder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

    private String timestamp;

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
            timestamp = b.getString(TIMESTAMP_KEY);
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
                .getItemsProvider().getNumberOfItems()));

        TextView title = findViewById(R.id.title_label);
        title.setText(DataProvider.getInstance().getTitle());

        TextView itemText = findViewById(R.id.item_text);
        itemText.setText(DataProvider.getInstance().getItemsProvider().get(item_index));
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
        final ItemData data = drawView.getItemData();

        if (data.touchUpPoints.isEmpty()) {
            ToastManager.getInstance().toastNoWord(this);

            view.setEnabled(true);
            return;
        }
        ToastManager.getInstance().resetNoWord();


        Saver.takeScreenshot(this, NamesManager.sessionDirectory(data.sessionData, data.item_index, timestamp), NamesManager.getScreenshotName(data));

        thread = new Thread() {
            @Override
            public void run() {
                synchronized (DrawingActivity.this) {
                    path_file = Saver.saveItemData(data, timestamp);
                }
            }
        };
        thread.start();

        int next_index = DataProvider.getInstance().getItemsProvider().nextIndex(data.item_index);
        int total_number_items = DataProvider.getInstance().getItemsProvider().getNumberOfItems();
        if (next_index >= total_number_items) {
            Toast.makeText(this, "Session completed! Thank you!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ToastManager.getInstance().toastSavedWord(this);

        String sessionData = new Gson().toJson(data.sessionData);
        Intent intent = new Intent(this, DrawingActivity.class);
        Bundle b = new Bundle();
        b.putString(SESSION_KEY, sessionData);
        b.putInt(ITEM_NUMBER_KEY, next_index); //Your id
        b.putString(TIMESTAMP_KEY, timestamp);

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
                NamesManager.getInstance().scanFile(this, path_file);
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


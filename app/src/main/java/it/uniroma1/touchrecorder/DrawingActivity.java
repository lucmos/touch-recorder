package it.uniroma1.touchrecorder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;

import it.uniroma1.touchrecorder.data.SessionData;
import it.uniroma1.touchrecorder.data.ToastManager;
import it.uniroma1.touchrecorder.data.WordData;

/**
 * Created by luca on 29/12/17.
 */

public class DrawingActivity extends Activity {

    public static final String SESSION_KEY = "session_data_key";
    public static final String WORD_NUMBER_KEY = "word_number_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.drawing_layout);

        DrawingView drawingView = findViewById(R.id.drawing_view_id);
        drawingView.setActivity(this);


        Bundle b = getIntent().getExtras();
        String session;
        int word_number = -1;
        WordData wordData;
        SessionData sessionData = null;

        if (b != null) {
            session = b.getString(SESSION_KEY);
            word_number = b.getInt(WORD_NUMBER_KEY);
            sessionData = new Gson().fromJson(session, SessionData.class);
        }

        if (b == null || sessionData == null || word_number == -1) {
            throw new RuntimeException("Extraction of bundle failed!");
        }

        wordData = new WordData(sessionData, word_number);
        drawingView.setWordData(wordData);

        TextView handwriting = findViewById(R.id.handwriting_label);
        handwriting.setText(String.valueOf(wordData.sessionData.handwriting));

        TextView counterView = findViewById(R.id.current_number_label);
        counterView.setText(String.valueOf(wordData.wordNumber));

        TextView totalNumberView = findViewById(R.id.total_number_label);
        totalNumberView.setText(String.valueOf(wordData.sessionData.totalWordNumber - 1));
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

        final Save s = Save.getInstance();

        DrawingView drawView = (DrawingView) findViewById(R.id.drawing_view_id);
        final WordData data = drawView.getWordData();

        if (data.touchUpPoints.isEmpty()) {
            ToastManager.getInstance().toastNoWord(this);

            view.setEnabled(true);
            return;
        }
        ToastManager.getInstance().resetNoWord();


        s.takeScreenshot(this, s.sessionDirectory(data.sessionData), s.getScreenshotName(data));

        thread = new Thread() {
            @Override
            public void run() {
                synchronized (DrawingActivity.this) {
                    path_file = s.saveWordData(data);
                }
            }
        };
        thread.start();

        if (data.wordNumber + 1 >= data.sessionData.totalWordNumber) {
            Toast.makeText(this, "Session completed! Thank you!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        ToastManager.getInstance().toastSavedWord(this);

        String sessionData = new Gson().toJson(data.sessionData);
        Intent intent = new Intent(this, DrawingActivity.class);
        Bundle b = new Bundle();
        b.putString(SESSION_KEY, sessionData);
        b.putInt(WORD_NUMBER_KEY, data.wordNumber + 1); //Your id

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
                Save.getInstance().scanFile(this, path_file);
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


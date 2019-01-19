package it.uniroma1.touchrecorder.data;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by luca on 31/12/17.
 */

public class ToastManager {

    private Toast no_word;
    private Toast next_word;

    private static final ToastManager ourInstance = new ToastManager();

    public static ToastManager getInstance() {
        return ourInstance;
    }

    private ToastManager() {
    }

    public void toastNoWord(Activity activity) {
        resetNoWord();
        no_word = Toast.makeText(activity, "Write a word before!", Toast.LENGTH_SHORT);
        no_word.show();
    }

    public void toastSavedWord(Activity activity) {
        resetSaveWord();
        next_word = Toast.makeText(activity, "Word saved!", Toast.LENGTH_SHORT);
        next_word.show();
    }

    public void resetNoWord() {
        if (no_word != null) {
            no_word.cancel();
        }
    }

    public void resetSaveWord() {
        if (next_word != null) {
            next_word.cancel();
        }
    }
}

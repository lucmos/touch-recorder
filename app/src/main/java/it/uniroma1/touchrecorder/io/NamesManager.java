package it.uniroma1.touchrecorder.io;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import it.uniroma1.touchrecorder.data.DataProvider;
import it.uniroma1.touchrecorder.data.SessionData;
import it.uniroma1.touchrecorder.data.ItemData;

/**
 * Created by luca on 29/12/17.
 */

public class NamesManager {
    private static final String FOLDER_NAME = "TouchRecorder";
    private final static String CONFIG_FILE = "configuration.json";
    private static final String SEPARATOR = "_";

    private static final NamesManager ourInstance = new NamesManager();
    public static NamesManager getInstance() {
        return ourInstance;
    }

    private NamesManager() {
    }

    public static File getUserConfigFileName() {
        return new File(NamesManager.baseDirectory(), CONFIG_FILE);
    }


    public static String getJsonName(ItemData data) {
        return getFileName(data, "json");
    }

    public static String getScreenshotName(ItemData data) {
        return getFileName(data, "png");
    }

    public static String getFileName(ItemData data, String extesion) {
        return String.format(Locale.getDefault(), "%s%s%s%s%s%s%s.%s",
                DataProvider.getInstance().getItemsProvider().getNormalized(data.item_index), SEPARATOR,
                data.sessionData.name, SEPARATOR,
                data.sessionData.surname, SEPARATOR,
                data.item_index, extesion);
    }

    public static File baseDirectory() {

        File path = Environment.getExternalStorageDirectory();
        File baseDirectory = new File(path, FOLDER_NAME);

        // Make sure the Pictures directory exists.
        baseDirectory.mkdirs();
        return baseDirectory;
    }

    public static String normalize(String s){
        return  s.replaceAll("\\s+", ".").toLowerCase();
    }

    public static File sessionDirectory(SessionData data, int item_index, String timestamp)
    {
        File base = baseDirectory();

        String firstFolder = DataProvider.getInstance().getItemsProvider().getNormalized(item_index);
        String secondFolder = normalize(data.name) + SEPARATOR +
                normalize(data.surname) + SEPARATOR + timestamp;
        File sessionDirectory = new File(new File(base, firstFolder), secondFolder);
        sessionDirectory.mkdirs();

        return sessionDirectory;
    }

    public static synchronized void  scanFile(Activity activity, File path) {
        activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(path)));
    }

    public static String getDate() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
        return now.toString();
    }

    @SuppressLint("SimpleDateFormat")
    public static String getShortDate() {
        return  new SimpleDateFormat("dd.MM.yyyy.HH.mm").format(new Date());
    }
}

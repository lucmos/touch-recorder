package it.uniroma1.touchrecorder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.Locale;

import it.uniroma1.touchrecorder.data.SessionData;
import it.uniroma1.touchrecorder.data.WordData;

/**
 * Created by luca on 29/12/17.
 */

public class Save {
    private static final Save ourInstance = new Save();
    private static final String FOLDER_NAME = "TouchRecorder";
    private static final String SEPARATOR = ".";

    public static Save getInstance() {
        return ourInstance;
    }

    private Save() {
    }


    public File saveWordData(final WordData wordData) {
        File p = sessionDirectory( wordData.sessionData);
        final String name = getJsonName(wordData);
        final File path = new File(p, name);

        try (Writer writer = new BufferedWriter(new FileWriter(path))) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(wordData, writer);
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }


    public String getJsonName(WordData data) {
        return getFileName(data, "json");
    }

    public String getScreenshotName(WordData data) {
        return getFileName(data, "png");
    }

    public String getFileName(WordData data, String extesion) {
        return String.format(Locale.getDefault(), "%s.%s.%s.%s.%s",
                data.sessionData.name, data.sessionData.surname, data.sessionData.handwriting, data.wordNumber, extesion);
    }

    public File baseDirectory() {
        File path = Environment.getExternalStorageDirectory();
        File baseDirectory = new File(path, FOLDER_NAME);

        // Make sure the Pictures directory exists.
        baseDirectory.mkdirs();
        return baseDirectory;
    }

    public File sessionDirectory(SessionData data)
    {
        File base = baseDirectory();

        String nameFolder = data.name + SEPARATOR + data.surname;
        String idFolder = data.id + "";
        String modeFolder = data.handwriting.toString();

        File sessionDirectory = new File(new File(new File(base, nameFolder), idFolder), modeFolder);

        sessionDirectory.mkdirs();
        return sessionDirectory;
    }

    public synchronized void  scanFile(Activity activity, File path) {
//        MediaScannerConnection.scanFile(activity,
//                new String[] { path }, null,
//                new MediaScannerConnection.OnScanCompletedListener() {
//                    public void onScanCompleted(String path, Uri uri) {
//                        Log.i("ExternalStorage", "Scanned " + path + ":");
//                        Log.i("ExternalStorage", "-> uri=" + uri);
//                    }
//                });

        activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(path)));

    }

//    public void scanFile(Activity activity, File path) {
//        scanFile(activity, path.toString());
//    }

    public String getDate() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
        return now.toString();
    }

    public void takeScreenshot(Activity activity, File basepath, String name) {
        try {
            // image naming and path  to include sd card  appending name you choose for file
            File imageFile = new File(basepath, name);

            // create bitmap screen capture
            View v1 = activity.getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);


            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.PNG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            scanFile(activity, imageFile);
            bitmap.recycle();
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }



//    void createExternalStoragePublicPicture() {
//        // Create a path where we will place our picture in the user's
//        // public pictures directory.  Note that you should be careful about
//        // what you place here, since the user often manages these files.  For
//        // pictures and other media owned by the application, consider
//        // Context.getExternalMediaDir().
//        File path = Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES);
//        File file = new File(path, "DemoPicture.jpg");
//
//        try {
//            // Make sure the Pictures directory exists.
//            path.mkdirs();
//
//            // Very simple code to copy a picture from the application's
//            // resource into the external file.  Note that this code does
//            // no error checking, and assumes the picture is small (does not
//            // try to copy it in chunks).  Note that if external storage is
//            // not currently mounted this will silently fail.
//            InputStream is = getResources().openRawResource(R.drawable.balloons);
//            OutputStream os = new FileOutputStream(file);
//            byte[] data = new byte[is.available()];
//            is.read(data);
//            os.write(data);
//            is.close();
//            os.close();
//
//            // Tell the media scanner about the new file so that it is
//            // immediately available to the user.
//            MediaScannerConnection.scanFile(this,
//                    new String[] { file.toString() }, null,
//                    new MediaScannerConnection.OnScanCompletedListener() {
//                        public void onScanCompleted(String path, Uri uri) {
//                            Log.i("ExternalStorage", "Scanned " + path + ":");
//                            Log.i("ExternalStorage", "-> uri=" + uri);
//                        }
//                    });
//        } catch (IOException e) {
//            // Unable to create file, likely because external storage is
//            // not currently mounted.
//            Log.w("ExternalStorage", "Error writing " + file, e);
//        }
//    }
//
//    void deleteExternalStoragePublicPicture() {
//        // Create a path where we will place our picture in the user's
//        // public pictures directory and delete the file.  If external
//        // storage is not currently mounted this will fail.
//        File path = Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES);
//        File file = new File(path, "DemoPicture.jpg");
//        file.delete();
//    }
//
//    boolean hasExternalStoragePublicPicture() {
//        // Create a path where we will place our picture in the user's
//        // public pictures directory and check if the file exists.  If
//        // external storage is not currently mounted this will think the
//        // picture doesn't exist.
//        File path = Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES);
//        File file = new File(path, "DemoPicture.jpg");
//        return file.exists();

}

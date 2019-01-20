package it.uniroma1.touchrecorder.io;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import it.uniroma1.touchrecorder.data.ItemData;

public class Saver {

    public static File saveItemData(final ItemData itemData, String timestamp) {
        File p = NamesManager.sessionDirectory(itemData.sessionData, itemData.item_index, timestamp);
        final String name = NamesManager.getJsonName(itemData);
        final File path = new File(p, name);

        try (Writer writer = new BufferedWriter(new FileWriter(path))) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(itemData, writer);
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

    public static void takeScreenshot(Activity activity, File basepath, String name) {
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

            NamesManager.scanFile(activity, imageFile);
            bitmap.recycle();
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }
}

package it.uniroma1.touchrecorder.io;

import android.content.res.Resources;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import it.uniroma1.touchrecorder.R;
import it.uniroma1.touchrecorder.data.Configuration;

public class ConfigurationReader {


    public static Configuration readConfiguration(Resources resources) {
        Gson gson = new GsonBuilder().create();

        File config_file = NamesManager.getUserConfigFileName();
        if (!config_file.exists()) {

            JSONResourceReader reader = new JSONResourceReader(resources, R.raw.configuration);
            Configuration configurationJson = reader.constructUsingGson(it.uniroma1.touchrecorder.data.Configuration.class);
            saveUserConfiguration(configurationJson);
        }

        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader(NamesManager.getUserConfigFileName()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        assert reader != null;
        return gson.fromJson(reader, Configuration.class);
    }

    public static void saveUserConfiguration(Configuration configuration) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (Writer writer = new FileWriter(NamesManager.getUserConfigFileName())) {
            gson.toJson(configuration, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

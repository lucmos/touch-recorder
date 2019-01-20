package it.uniroma1.touchrecorder;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import it.uniroma1.touchrecorder.data.Configuration;
import it.uniroma1.touchrecorder.data.DataProvider;
import it.uniroma1.touchrecorder.data.DeviceData;
import it.uniroma1.touchrecorder.data.Gender;
import it.uniroma1.touchrecorder.data.SessionData;
import it.uniroma1.touchrecorder.io.ConfigurationReader;
import it.uniroma1.touchrecorder.io.NamesManager;

public class AutenticationActivity extends Activity {

    Configuration configurationJson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.autentication_layout);
        ActivityCompat.requestPermissions(AutenticationActivity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    configurationJson = ConfigurationReader.readConfiguration(getResources());
                    DataProvider.createInstance(configurationJson);

                    TextView repetitionsTextView = findViewById(R.id.items_repetition_text);
                    repetitionsTextView.setText(String.valueOf(configurationJson.repetitions));

                    TextView repetitionsLabelView = findViewById(R.id.repetitions_label);
                    repetitionsLabelView.setText(String.valueOf(configurationJson.repetitions_label));
                } else {
                    ActivityCompat.requestPermissions(AutenticationActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            1);
                }
            }
        }
    }

    public void start_recording(View view) {
        EditText nameText = findViewById(R.id.name_text);
        String name = nameText.getText().toString();
        if (name.isEmpty()) {
            Toast.makeText(this, "Please insert a valid name!", Toast.LENGTH_SHORT).show();
            return;
        }

        EditText surnameText = findViewById(R.id.surname_text);
        String surname = surnameText.getText().toString();
        if (surname.isEmpty()) {
            Toast.makeText(this, "Please insert a valid surname!", Toast.LENGTH_SHORT).show();
            return;
        }

        EditText ageText = findViewById(R.id.age_text);
        int age;
        try {
            age = Integer.parseInt(ageText.getText().toString());
        } catch (Exception e) {
            Toast.makeText(this, "Age must be a number!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (age < 1 || age > 100) {
            Toast.makeText(this, "Please insert a valid age!", Toast.LENGTH_SHORT).show();
            return;
        }

        EditText wordsText = findViewById(R.id.items_repetition_text);
        int number_of_items;
        try {
            number_of_items = Integer.parseInt(wordsText.getText().toString());
        } catch (Exception e) {
            Toast.makeText(this, "The item repetition field must be a number!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (number_of_items < 1) {
            Toast.makeText(this, "Please insert a number in the item repetition field!", Toast.LENGTH_SHORT).show();
            return;
        }

//        If changed update configuration
        if (number_of_items != configurationJson.repetitions) {
            configurationJson.repetitions = number_of_items;
            ConfigurationReader.saveUserConfiguration(configurationJson);
        }

        Spinner genderSpinner = findViewById(R.id.gender_spinner);
        Gender gender = Gender.toEnum(genderSpinner.getSelectedItem().toString().toUpperCase());

        DeviceData deviceData = new DeviceData(Build.MODEL, Build.FINGERPRINT,
            getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels,
            getResources().getDisplayMetrics().xdpi, getResources().getDisplayMetrics().ydpi);

        SessionData sessionData = new SessionData(configurationJson, deviceData,
                name, surname, age, gender, NamesManager.getShortDate());
        String sessionData_string = new Gson().toJson(sessionData);

        Intent intent = new Intent(this, DrawingActivity.class);

        Bundle b = new Bundle();
        b.putString(DrawingActivity.SESSION_KEY, sessionData_string);
        b.putInt(DrawingActivity.ITEM_NUMBER_KEY, 0);
        b.putString(DrawingActivity.TIMESTAMP_KEY, sessionData.date);
        intent.putExtras(b); // Put your id to your next Intent

        startActivity(intent);
    }
}

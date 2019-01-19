package it.uniroma1.touchrecorder;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import it.uniroma1.touchrecorder.data.DeviceData;
import it.uniroma1.touchrecorder.data.Gender;
import it.uniroma1.touchrecorder.data.Handwriting;
import it.uniroma1.touchrecorder.data.SessionData;

public class AutenticationActivity extends Activity {

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
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    ActivityCompat.requestPermissions(AutenticationActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            1);
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
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

        EditText idText = findViewById(R.id.id_text);
        int id;
        try {
            id = Integer.parseInt(idText.getText().toString());
        } catch (Exception e) {
            Toast.makeText(this, "Id must be a number!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (id < 0 || id > 100) {
            Toast.makeText(this, "Please insert a valid id!", Toast.LENGTH_SHORT).show();
            return;
        }

        EditText wordsText = findViewById(R.id.word_text);
        int number_of_words;
        try {
            number_of_words = Integer.parseInt(wordsText.getText().toString());
        } catch (Exception e) {
            Toast.makeText(this, "The words field must be a number!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (number_of_words < 1) {
            Toast.makeText(this, "Please insert a number in the words field!", Toast.LENGTH_SHORT).show();
            return;
        }


        Spinner genderSpinner = findViewById(R.id.gender_spinner);
        Gender gender = Gender.toEnum(genderSpinner.getSelectedItem().toString().toUpperCase());

        Spinner handwritingSpinner = findViewById(R.id.handwriting_spinner);
        Handwriting handwriting = Handwriting.toEnum(handwritingSpinner.getSelectedItem().toString().toUpperCase());

//
//        System.out.println();
//        System.out.println();
//        System.out.println(name);
//        System.out.println(surname);
//        System.out.println(age);
//        System.out.println(gender);
//        System.out.println(id);
//        System.out.println(number_of_words);
//        System.out.println(handwriting);
//        System.out.println();
//        System.out.println();

        DeviceData deviceData = new DeviceData(Build.MODEL, Build.FINGERPRINT,
            getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels,
            getResources().getDisplayMetrics().xdpi, getResources().getDisplayMetrics().ydpi);

        SessionData sessionData = new SessionData(deviceData, id,number_of_words, name, surname, age, gender, handwriting);
        String sessionData_string = new Gson().toJson(sessionData);


        Intent intent = new Intent(this, DrawingActivity.class);
        Bundle b = new Bundle();
        b.putString(DrawingActivity.SESSION_KEY, sessionData_string);
        b.putInt(DrawingActivity.WORD_NUMBER_KEY, 0); //Your id

        intent.putExtras(b); //Put your id to your next Intent
        startActivity(intent);
    }
}

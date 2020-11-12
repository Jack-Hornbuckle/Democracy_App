package com.example.project;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class InfoActivity extends AppCompatActivity {


    private static int OTHER = 0;
    private static int MALE = 1;
    private static int FEMALE = 2;
    private static int NATIVE = 1;
    private static int ASIAN = 2;
    private static int BLACK = 3;
    private static int PACIFIC = 4;
    private static int WHITE = 5;

    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        getSupportActionBar().setTitle("Select Your Information");
        sharedPref = getSharedPreferences("myPref",MODE_PRIVATE);
        RadioButton genderButton;
        RadioButton raceButton;
        if (sharedPref.getInt("userSetup", 0) == 1) {
            int gender = sharedPref.getInt("gender", 0);
            int race = sharedPref.getInt("races", 0);
            int age = sharedPref.getInt("age", 0);
            if (gender == MALE) {
                genderButton = findViewById(R.id.genderMale);
                genderButton.setChecked(true);
            } else if (gender == FEMALE) {
                genderButton = findViewById(R.id.genderFemale);
                genderButton.setChecked(true);
            } else if (gender == OTHER) {
                genderButton = findViewById(R.id.genderOther);
                genderButton.setChecked(true);
            }
            if (race == NATIVE) {
                raceButton = findViewById(R.id.raceNative);
                raceButton.setChecked(true);
            } else if (race == ASIAN) {
                raceButton = findViewById(R.id.raceAsian);
                raceButton.setChecked(true);
            } else if (race == BLACK) {
                raceButton = findViewById(R.id.raceBlack);
                raceButton.setChecked(true);
            } else if (race == PACIFIC) {
                raceButton = findViewById(R.id.racePacific);
                raceButton.setChecked(true);
            } else if (race == WHITE) {
                raceButton = findViewById(R.id.raceWhite);
                raceButton.setChecked(true);
            } else if (race == OTHER) {
                raceButton = findViewById(R.id.raceOther);
                raceButton.setChecked(true);
            }
            EditText ageText = findViewById(R.id.ageEditText);
            ageText.setText(Integer.toString(age));
        }
    }

    public void updateInfo(View view)
    {
        RadioGroup gender = findViewById(R.id.genders);
        int genderSelect = gender.getCheckedRadioButtonId();
        RadioButton genderCheck = findViewById(genderSelect);
        RadioGroup race = findViewById(R.id.races);
        int racesSelect = race.getCheckedRadioButtonId();
        RadioButton raceCheck = findViewById(racesSelect);
        EditText age = findViewById(R.id.ageEditText);

        SharedPreferences.Editor edit = sharedPref.edit();
        edit.putInt("userSetup",1);
        edit.apply();

        if(genderCheck.getText().toString().matches("Male")) {
            edit.putInt("gender", MALE);
        } else if(genderCheck.getText().toString().matches("Female")) {
            edit.putInt("gender", FEMALE);
        }  else if(genderCheck.getText().toString().matches("Other")) {
            edit.putInt("gender", OTHER);
        } else {
            Toast.makeText(InfoActivity.this, "Gender Select Error", Toast.LENGTH_LONG).show();
        }
        edit.apply();

        if(raceCheck.getText().toString().matches("American Indian or Alaska Native")) {
            edit.putInt("races", NATIVE);
        } else if(raceCheck.getText().toString().matches("Asian")){
            edit.putInt("races", ASIAN);
        } else if(raceCheck.getText().toString().matches("Black or African American")){
            edit.putInt("races", BLACK);
        } else if(raceCheck.getText().toString().matches("Native Hawaiian or Other Pacific Islander")){
            edit.putInt("races", PACIFIC);
        } else if(raceCheck.getText().toString().matches("White")){
            edit.putInt("races", WHITE);
        } else if(raceCheck.getText().toString().matches("Other")){
            edit.putInt("races", OTHER);
        } else {
            Toast.makeText(InfoActivity.this,"Race Select Error",Toast.LENGTH_LONG).show();
        }
        edit.apply();

        int a =  Integer.parseInt(age.getText().toString());
        edit.putInt("age",a);
        edit.apply();

        String deviceId = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
        edit.putString("id", deviceId);
        edit.apply();
        finish();
    }
}

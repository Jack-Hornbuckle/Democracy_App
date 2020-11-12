package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPref;
    static boolean firstCheck = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Democracy");
        sharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        int userSetup = sharedPref.getInt("userSetup",0);
        if(userSetup == 0)
        {
            Intent intent = new Intent(this,InfoActivity.class);
            startActivity(intent);
        }
    }

    public void onAskPressed(View view){
        Intent intent = new Intent(this,AskActivity.class);
        startActivity(intent);
    }

    public void onAnswerPressed(View view) {
        Intent intent = new Intent(this,QuestionsActivity.class);
        startActivity(intent);
    }

    public void onViewPressed(View view) {
        Intent intent = new Intent(this,ViewActivity.class);
        startActivity(intent);
    }

    public void onInfoPressed(View view) {
        Intent intent = new Intent(this,InfoActivity.class);
        startActivity(intent);
    }
}

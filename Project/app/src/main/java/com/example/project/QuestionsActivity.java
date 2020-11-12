package com.example.project;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class QuestionsActivity extends AppCompatActivity {


    ListView listView;
    Adapter adapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabase = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        getSupportActionBar().setTitle("Current Polls:");
        listView = findViewById(R.id.list1);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> currentQuestions = new ArrayList<>();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    currentQuestions.add(postSnapshot.child("Name").getValue(String.class));
                }
                showList(currentQuestions);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = view.findViewById(R.id.textViewer);
                String question = textView.getText().toString();
                Intent intent = new Intent(getApplicationContext(), DisplayQuestion.class);
                intent.putExtra("question_string", question);
                startActivity(intent);
            }
        });
    }

    private void showList(ArrayList<String> questions) {
        adapter = new Adapter(this, questions);
        listView.setAdapter(adapter);
    }

    public void onBackListClicked(View view) {
        finish();
    }

}
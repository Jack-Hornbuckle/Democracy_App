package com.example.project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewActivity extends AppCompatActivity {

    ListView listView;
    Adapter adapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabase = database.getReference();
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        getSupportActionBar().setTitle("Your Active Questions:");
        listView = findViewById(R.id.list2);
        sharedPref = getSharedPreferences("myPref",MODE_PRIVATE);
        final String id = sharedPref.getString("id", "default_id");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> currentQuestions = new ArrayList<>();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    if(postSnapshot.child("Owner").getValue(String.class).matches(id)) {
                        currentQuestions.add(postSnapshot.child("Name").getValue(String.class));
                    }
                }
                showList(currentQuestions);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
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

    public void onBackViewClicked(View view) {
        finish();
    }
}

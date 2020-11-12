package com.example.project;

import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class ResultsActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabase = database.getReference();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    byte[] data;
    Question question;
    List<Answer> Choices;
    TextView title;
    ListView listView;
    PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Intent intent = getIntent();
        String questionText = intent.getStringExtra("question_title");
        getSupportActionBar().setTitle(questionText);
        listView = findViewById(R.id.AnswersList);

        StorageReference currentQuestion = storageRef.child(questionText);
        currentQuestion.getBytes(1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                data = bytes;
                try {
                    ByteArrayInputStream inp = new ByteArrayInputStream(data);
                    ObjectInputStream our_in = new ObjectInputStream(inp);
                    Object form_quest = our_in.readObject();
                    question = (Question) form_quest;
                    Choices = question.getChoices();
                    ArrayList<String> AnswerList = new ArrayList<>();
                    double totalVotes = 0;
                    for (int i = 0; i < question.getSize(); i++) {
                        totalVotes += Choices.get(i).getTotalVotes();
                    }
                    for (int i = 0; i < question.getSize(); i++) {
                        double percentage = (Choices.get(i).getTotalVotes() / totalVotes) * 100;
                        if (!(percentage > 0)) {
                            percentage = 0;
                        }
                        String input = String.format("%s: %d Votes, %.0f%%",
                                Choices.get(i).getAnswerText(), Choices.get(i).getTotalVotes(), percentage);
                        AnswerList.add(input);
                    }
                    ArrayAdapter adapter = new ArrayAdapter<String>(ResultsActivity.this, R.layout.answer_item, AnswerList);
                    listView.setAdapter(adapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            LayoutInflater layoutInflater = (LayoutInflater) ResultsActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View customView = layoutInflater.inflate(R.layout.popup,null);
                            TextView popupText = customView.findViewById(R.id.popupText);
                            Button closePopupBtn = customView.findViewById(R.id.closePopupBtn);
                            popupText.setText(question.getChoices().get(position).getStats());
                            popupWindow = new PopupWindow(customView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            popupWindow.setElevation(5);
                            popupWindow.showAtLocation(findViewById(R.id.resultsLayout), Gravity.CENTER, 0, 0);
                            closePopupBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    popupWindow.dismiss();
                                }
                            });
                        }
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void onResultCloseClicked(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}

package com.example.project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.List;

public class DisplayQuestion extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabase = database.getReference();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    RadioGroup answers;
    Question question;
    List<Answer> Choices;
    int gender;
    int race;
    int age;
    String userId;
    SharedPreferences sharedPref;
    byte[] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_question);

        sharedPref = getSharedPreferences("myPref",MODE_PRIVATE);
        userId = sharedPref.getString("id","default_id");
        gender = sharedPref.getInt("gender",0);
        race = sharedPref.getInt("races",0);
        age = sharedPref.getInt("age",0);
        Intent intent = getIntent();
        final String questionText = intent.getStringExtra("question_string");
        getSupportActionBar().setTitle(questionText);
        StorageReference currentQuestion = storageRef.child(questionText);
        currentQuestion.getBytes(1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                TextView the_view = findViewById(R.id.question_view);
                data = bytes;
                try {
                    ByteArrayInputStream inp = new ByteArrayInputStream(data);
                    ObjectInputStream our_in = new ObjectInputStream(inp);
                    Object form_quest = our_in.readObject();
                    question = (Question) form_quest;
                    the_view.setText(question.getQuestionTitle());
                    answers = findViewById(R.id.answer_group);
                    Choices = question.getChoices();
                    for (int i = 0; i < question.getSize(); i++) {
                        RadioButton rb = new RadioButton(DisplayQuestion.this);
                        rb.setText(Choices.get(i).getAnswerText());
                        rb.setId(View.generateViewId());
                        answers.addView(rb);
                    }
                    if(userId.matches(question.getOwnerId())){
                        LinearLayout linearLayout = findViewById(R.id.answer_layout);
                        TextView ownerView = new TextView(DisplayQuestion.this);
                        ownerView.setText("This is your Question");
                        linearLayout.addView(ownerView);
                        Button deleteButton = new Button(DisplayQuestion.this);
                        deleteButton.setText("Delete Question");
                        deleteButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mDatabase.child(questionText).removeValue();
                                storageRef.child(questionText).delete();
                                finish();
                            }
                        });
                        linearLayout.addView(deleteButton);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void onVoteClicked(View view) {
        if(!(question.checkVoted(userId))) {
            int answerCheck = answers.getCheckedRadioButtonId();
            if (!(answerCheck > 0)) {
                Toast.makeText(DisplayQuestion.this, "Please Choose an Answer", Toast.LENGTH_LONG).show();
            } else {
                RadioButton our_button = findViewById(answerCheck);
                String the_answer = our_button.getText().toString();
                for (int i = 0; i < question.getSize(); i++) {
                    if (the_answer.equals(Choices.get(i).getAnswerText())) {
                        Choices.get(i).Vote(gender, race, age);
                    }
                }
                question.updateAnswers(Choices);
                question.addVoter(userId);
                try {
                    ByteArrayOutputStream b_out = new ByteArrayOutputStream();
                    ObjectOutput out = new ObjectOutputStream(b_out);
                    out.writeObject(question);
                    byte[] trans_bytes = b_out.toByteArray();
                    StorageReference questionStorage = storageRef.child(question.getQuestionTitle());
                    questionStorage.putBytes(trans_bytes).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            Intent intent = new Intent(DisplayQuestion.this, ResultsActivity.class);
                            intent.putExtra("question_title", question.getQuestionTitle());
                            startActivity(intent);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            Toast.makeText(DisplayQuestion.this,"You have already voted in this poll", Toast.LENGTH_LONG).show();
        }
    }

    public void onResultsClicked(View view) {
        Intent intent = new Intent(DisplayQuestion.this, ResultsActivity.class);
        intent.putExtra("question_title", question.getQuestionTitle());
        startActivity(intent);
    }

    public void onDisplayCloseClicked(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}

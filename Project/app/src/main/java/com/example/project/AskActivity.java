package com.example.project;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AskActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabase = database.getReference();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    List<EditText> EditList;
    EditText QuestionEditText;
    EditText editText1;
    EditText editText2;
    EditText editText3;
    EditText editText4;
    Button PostButton;
    Button AddButton;
    Button CancelButton;
    String QuestionText;
    ProgressDialog pd;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);
        getSupportActionBar().setTitle("Create a Question");
        sharedPref = getSharedPreferences("myPref",MODE_PRIVATE);
        EditList = new ArrayList<>();
        QuestionEditText = findViewById(R.id.Question_Input);
        editText1 = findViewById(R.id.answer1);
        editText2 = findViewById(R.id.answer2);
        editText3 = findViewById(R.id.answer3);
        editText4 = findViewById(R.id.answer4);
        EditList.add(editText1);
        EditList.add(editText2);
        EditList.add(editText3);
        EditList.add(editText4);

        AddButton = findViewById(R.id.AddAnsButton);
        CancelButton = findViewById(R.id.CancelAnsButton);
        AddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EditList.size() < 20) {
                    int tempId = (EditList.get(EditList.size()-1).getId()) + 1;

                    ConstraintLayout this_layout = findViewById(R.id.AskLayout);

                    EditText this_text = new EditText(AskActivity.this);
                    ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 40, 0, 0); //top margins
                    this_text.setLayoutParams(params);
                    this_text.setHint("Enter answer choice");
                    this_text.setId(tempId);
                    this_text.setInputType(InputType.TYPE_CLASS_TEXT);
                    this_layout.addView(this_text);

                    ConstraintSet set = new ConstraintSet();
                    set.clone(this_layout);
                    set.connect(this_text.getId(), ConstraintSet.TOP, EditList.get(EditList.size()-1).getId(), ConstraintSet.BOTTOM);
                    set.connect(AddButton.getId(), ConstraintSet.TOP, this_text.getId(), ConstraintSet.BOTTOM);
                    set.connect(CancelButton.getId(), ConstraintSet.TOP, this_text.getId(), ConstraintSet.BOTTOM);
                    set.applyTo(this_layout);

                    EditList.add(this_text);
                }
            }
        });

        PostButton = findViewById(R.id.post_question);
        PostButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                QuestionText = QuestionEditText.getText().toString();

                if(QuestionText.matches("")) {
                    Toast.makeText(AskActivity.this,"A title is required", Toast.LENGTH_LONG).show();
                } else {
                    Question question = new Question(QuestionText, sharedPref.getString("id", "default_id"));
                    for (int i = 0; i < EditList.size(); i++) {
                        EditText iteration = EditList.get(i);
                        String Ans = iteration.getText().toString();
                        question.add_answer(Ans);
                    }
                    if (question.getSize() > 1) {
                        try {
                            ByteArrayOutputStream b_out = new ByteArrayOutputStream();
                            ObjectOutput out = new ObjectOutputStream(b_out);
                            out.writeObject(question);
                            byte[] trans_bytes = b_out.toByteArray();

                            StorageReference questionStorage = storageRef.child(QuestionText);
                            UploadTask uploadTask = questionStorage.putBytes(trans_bytes);
                            mDatabase.child(QuestionText).child("Name").setValue(QuestionText);
                            mDatabase.child(QuestionText).child("Owner").setValue(sharedPref.getString("id", "default_id"));
                            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    pd = new ProgressDialog(AskActivity.this);
                                    pd.setTitle("Loading ...");
                                    pd.setButton(ProgressDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            pd.dismiss();
                                        }
                                    });
                                    pd.show();
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Intent intent = new Intent(AskActivity.this, DisplayQuestion.class);
                                    intent.putExtra("question_string", QuestionText);
                                    startActivity(intent);
                                }
                            });
                        } catch (Exception e) {
                            Toast.  makeText(AskActivity.this, "Error", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(AskActivity.this, "Need at least 2 choices", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void onCancelAnsClicked(View view) {
        finish();
    }
}

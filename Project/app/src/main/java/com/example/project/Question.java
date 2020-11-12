package com.example.project;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Question implements Serializable {

    String QuestionText;
    List<Answer> Choices;
    String OwnerId;
    List<String> Voters;
    int size;

    public Question(String title, String ownerId) {
        this.QuestionText = title;
        this.Choices = new ArrayList<>();
        this.OwnerId = ownerId;
        this.Voters = new ArrayList<>();
        size = 0;
    }

    public void add_answer(String answerText) {
        if (!(answerText.matches(""))) {
            Answer answer = new Answer(answerText);
            Choices.add(answer);
            size++;
        }
    }

    public void updateAnswers(List<Answer> newData) {
        this.Choices = newData;
    }

    public String getQuestionTitle() { return this.QuestionText; }

    public List<Answer> getChoices() { return this.Choices; }

    public String getOwnerId() { return this.OwnerId; }

    public int getSize() { return this.size; }

    public void addVoter(String voter) {
        this.Voters.add(voter);
    }

    public boolean checkVoted(String voter) {
        boolean out = false;
        for(int i = 0; i < this.Voters.size(); i++) {
            if(this.Voters.get(i).matches(voter)){
                out = true;
            }
        }
        return out;
    }

}

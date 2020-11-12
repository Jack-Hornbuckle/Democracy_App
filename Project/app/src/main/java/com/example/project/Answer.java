package com.example.project;

import java.io.Serializable;

public class Answer implements Serializable {

    /*
    private static int OTHER = 0;
    private static int MALE = 1;
    private static int FEMALE = 2;
    private static int NATIVE = 1;
    private static int ASIAN = 2;
    private static int BLACK = 3;
    private static int PACIFIC = 4;
    private static int WHITE = 5;
    */

    String AnswerText;
    private int totalVotes;
    private int[] totalGender = new int[3];
    private int[] totalRace = new int[6];
    private int[] totalAge = new int[8];

    public Answer(String name) {
        this.AnswerText = name;
        this.totalVotes = 0;
    }

    public void Vote(int gender, int race, int age) {
        totalGender[gender]++;
        totalRace[race]++;
        if(age >= 80) {
            totalAge[7]++;
        } else {
            totalAge[age/10]++;
        }
        totalVotes++;
    }

    public String getAnswerText() { return this.AnswerText; }

    public int getTotalVotes() { return this.totalVotes; }

    public int[] getTotalGender() {
        return totalGender;
    }

    public int[] getTotalRace() {
        return totalRace;
    }

    public int[] getTotalAge() {
        return totalAge;
    }

    public String getStats() {
        String out = String.format("%s\n", this.AnswerText);
        if(this.totalVotes > 0) {
            out = out + String.format("Total Votes: %d\n", this.totalVotes);
            out = out + String.format("\nGender Distribution: \nMale: %d%%\nFemale: %d%%\nOther %d%%\n",
                    (this.totalGender[1] / this.totalVotes) * 100,
                    (this.totalGender[2] / this.totalVotes) * 100,
                    (this.totalGender[0] / this.totalVotes) * 100);
            out = out + String.format("\nRace Distribution: \n" +
                            "American Indian or Alaska Native: %d%%\nAsian: %d%%\nBlack or African American: %d%%\n" +
                            "Native Hawaiian or Other Pacific Islander: %d%%\nWhite: %d%%\nOther: %d%%\n",
                    (this.totalRace[1] / this.totalVotes) * 100,
                    (this.totalRace[2] / this.totalVotes) * 100,
                    (this.totalRace[3] / this.totalVotes) * 100,
                    (this.totalRace[4] / this.totalVotes) * 100,
                    (this.totalRace[5] / this.totalVotes) * 100,
                    (this.totalRace[0] / this.totalVotes) * 100);
            out = out + String.format("\nAge Distribution: \nUnder 10: %d%%\n10-19: %d%%\n20-29: %d%%\n" +
                            "30-39: %d%%\n40-49: %d%%\n50-59: %d%%\n60-69: %d%%\nOver 70: %d%%\n",
                    (this.totalAge[0] / this.totalVotes) * 100,
                    (this.totalAge[1] / this.totalVotes) * 100,
                    (this.totalAge[2] / this.totalVotes) * 100,
                    (this.totalAge[3] / this.totalVotes) * 100,
                    (this.totalAge[4] / this.totalVotes) * 100,
                    (this.totalAge[5] / this.totalVotes) * 100,
                    (this.totalAge[6] / this.totalVotes) * 100,
                    (this.totalAge[7] / this.totalVotes) * 100);
        } else {
            out = out + "This answer choice has no votes.";
        }
        return out;
    }

}

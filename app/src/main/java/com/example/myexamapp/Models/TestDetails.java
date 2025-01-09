package com.example.myexamapp.Models;

import java.io.Serializable;

public class TestDetails implements Serializable {

    private int testDuration;  // Test duration in minutes
    private int totalQuestions;  // Total number of questions in the test
    private int totalMarks;  // Total marks for the test
    private int marksCorrect;  // Marks awarded for a correct answer
    private int marksIncorrect;  // Marks deducted for an incorrect answer

    // Default constructor (required for Firebase)
    public TestDetails() {}

    // Parameterized constructor
    public TestDetails(int testDuration, int totalQuestions, int totalMarks, int marksCorrect, int marksIncorrect) {
        this.testDuration = testDuration;
        this.totalQuestions = totalQuestions;
        this.totalMarks = totalMarks;
        this.marksCorrect = marksCorrect;
        this.marksIncorrect = marksIncorrect;
    }

    // Getters and setters
    public int getTestDuration() {
        return testDuration;
    }

    public void setTestDuration(int testDuration) {
        this.testDuration = testDuration;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public int getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(int totalMarks) {
        this.totalMarks = totalMarks;
    }

    public int getMarksCorrect() {
        return marksCorrect;
    }

    public void setMarksCorrect(int marksCorrect) {
        this.marksCorrect = marksCorrect;
    }

    public int getMarksIncorrect() {
        return marksIncorrect;
    }

    public void setMarksIncorrect(int marksIncorrect) {
        this.marksIncorrect = marksIncorrect;
    }
}

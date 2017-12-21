package ru.mera.testmanager;

import java.util.List;

public class Question {

    private final int numberOfQuestion;
    private final String textOfQuestion;
    private final List<Answer> answers;
    private final DifficultyLevel level;
    private final int id;

    Question(int numberOfQuestion, String textOfQuestion, List<Answer> answers, DifficultyLevel level, int id) {
        this.textOfQuestion = textOfQuestion;
        this.numberOfQuestion = numberOfQuestion;
        this.answers = answers;
        this.level = level;
        this.id = id;
    }

     Question(int numberOfQuestion, String textOfQuestion, List<Answer> answers, int id) {
        this.numberOfQuestion = numberOfQuestion;
        this.textOfQuestion = textOfQuestion;
        this.answers = answers;
        this.level = DifficultyLevel.NORMAL;
        this.id = id;
    }



    int getNumberOfQuestion() {
        return numberOfQuestion;
    }

    String getTextOfQuestion() {
        return textOfQuestion;
    }

    List<Answer> getAnswers() {
        return answers;
    }

    int getId(){
        return id;
    }

    DifficultyLevel getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return "ru.mera.testmanager.Question{" +
                "numberOfQuestion=" + numberOfQuestion +
                ", textOfQuestion='" + textOfQuestion + '\'' +
                ", answers=" + answers +
                ", level=" + level +
                '}';
    }
}

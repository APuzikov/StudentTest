package ru.mera.testmanager;

public class Answer {

    private final String textOfAnswer;
    private final boolean correct;
    private final int id;

    Answer(String textOfAnswer, boolean correct, int id) {
        this.textOfAnswer = textOfAnswer;
        this.correct = correct;
        this.id = id;
    }

    public Answer(String textOfAnswer, boolean correct) {
        this.textOfAnswer = textOfAnswer;
        this.correct = correct;
        id = 0;
    }

    String getTextOfAnswer() {
        return textOfAnswer;
    }

    public int getId() {
        return id;
    }

    boolean isCorrect() {
        return correct;
    }

}

package ru.mera.testmanager;

public class Answer {

    private final String textOfAnswer;
    private final boolean correct;

    Answer(String textOfAnswer, boolean correct) {
        this.textOfAnswer = textOfAnswer;
        this.correct = correct;
    }

    String getTextOfAnswer() {
        return textOfAnswer;
    }

    boolean isCorrect() {
        return correct;
    }

}

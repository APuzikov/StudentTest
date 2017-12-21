package ru.mera.testmanager;

import java.util.ArrayList;
import java.util.List;

public class RandomQuestionsLoader implements QuestionLoader {

    private int countOfQuest;
    private int countOfAnswers;

    public RandomQuestionsLoader(int countOfQuest, int countOfAnswers) {
        this.countOfQuest = countOfQuest;
        this.countOfAnswers = countOfAnswers;
    }

    @Override
    public List<Question> load() {
        List<Question> questions = new ArrayList<>();

        for (int i = 0; i < countOfQuest; i++) {
            List<Answer> answers = new ArrayList<>();

            for (int j = 1; j <= countOfAnswers; j++) {
                answers.add(new Answer("Text of answer" + j, getRandomBoolean()));
            }

            questions.add(new Question(i + 1, "Text " + i, answers, i));

        }
        return questions;
    }

    private boolean getRandomBoolean() {

        return !(Math.random() < 0.5);

    }

}

package ru.mera.testmanager;

public class DefaultQuestionChecker implements QuestionChecker{

    public int checkOfCorrect(String input, Test test, Question question){

        int result = 0;
        String[] str = input.split(",");
        int[] ints = new int[str.length];
        for (int i = 0; i < str.length; i++){
            ints[i] = Integer.parseInt(str[i]);

        }
        boolean correct = true;
        if (ints.length == test.getCountOfcorrect()) {
            for (int i = 0; i < ints.length; i++) {

                correct = correct && question.getAnswers().get(ints[i] - 1).isCorrect();

            }
        } else correct = false;

        if (correct) result = 1;
        return result;
    }
}

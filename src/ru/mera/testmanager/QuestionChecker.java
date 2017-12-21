package ru.mera.testmanager;

public interface QuestionChecker {

    int checkOfCorrect(String input, Test test, Question question) throws LoadException;
}

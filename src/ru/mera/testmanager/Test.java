package ru.mera.testmanager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {


    private List<Question> questions = new ArrayList<>();
    private int minimumPer = 65;
    private int countOfcorrect = 0;
    private int testId = 0;



    public Test(){}

    public Test(int minimumPer){
        this.minimumPer = minimumPer;
    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public int getCountOfcorrect() {
        return countOfcorrect;
    }

    public List<Question> getQuestions(){
        return questions;
    }

    void questionsInit(QuestionLoader questionLoader) throws LoadException{

        //RandomQuestionsLoader randomQuestionsLoader = new RandomQuestionsLoader(countOfQuest, countOfAnswers);
            questions = questionLoader.load();
    }

    String readFromConsole() throws IOException {
        String inputFromConsole;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {

            inputFromConsole = reader.readLine();
            Pattern regex1 = Pattern.compile("^\\d(,\\d)*");
            Matcher m1 = regex1.matcher(inputFromConsole);

            if (!m1.matches()) {
                System.out.println("Неверный ввод");
            } else {
                //System.out.println("Yes");
                break;
            }
        }

        return inputFromConsole;

    }

    void outputQuestionsToConsole(Question question) {
        countOfcorrect = 0;
        System.out.println("Вопрос N " + question.getNumberOfQuestion() + " из " + questions.size());

        System.out.println(question.getTextOfQuestion());

        int index = 1;
        for (Answer answer : question.getAnswers()) {                     //вывод вариантов ответов в консоль
            System.out.println(index + " " + answer.getTextOfAnswer() + " " + answer.isCorrect());
            index++;
            if (answer.isCorrect()) countOfcorrect++;
        }
        System.out.println("Выберите " + countOfcorrect + " ответов");
   }




    void resultsToConsole(int resultOfTest, Student student){

        int percent = calculatePercent(resultOfTest);

        System.out.println("Студент " + student.getName() + " " + percent + "%");

        if (percent >= minimumPer) {
            System.out.println("Тест успешно пройден ");
        } else System.out.println("Тест не пройден");


    }

    int calculatePercent(int resultOfTest) {

        return Math.round((float) resultOfTest/questions.size()*100);
    }
}
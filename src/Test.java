import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.floor;
import static java.lang.Math.random;

public class Test {


    private List<Question> questions = new ArrayList<>();
    //private List<Answer> answers = new ArrayList<>();
    private final int minimumPer = 65;
    private final int countOfQuest = 6;
    private final int countOfAnswers = 5;
    int countOfcorrect = 0;

    public List<Question> getQuestions(){
        return questions;
    }



    void questionsInit() {

        for (int i = 0; i < countOfQuest; i++) {
            List<Answer> answers = new ArrayList<>();

            for (int j = 1; j <= countOfAnswers; j++) {
                answers.add(new Answer("Text of answer" + j, getRandomBoolean()));
            }

            questions.add(new Question(i + 1, "Text " + i, answers ));

        }
    }



    boolean getRandomBoolean() {

        return Math.random() < 0.5 ? false : true;

    }



    String readFromConsole() throws IOException{
        String inputFromConsole;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            inputFromConsole = reader.readLine();
            Pattern regex1 = Pattern.compile("^\\d+(,\\d+)*");
            Matcher m1 = regex1.matcher(inputFromConsole);

            if (!m1.matches()) {

                System.out.println("Неверный ввод");
                continue;
            } else {
                //System.out.println("Yes");
                break;
            }
        }

        return inputFromConsole;

    }

    void outputQuestionsToConsole(Question question, Test test) {
        countOfcorrect = 0;
        System.out.println("Вопрос N " + question.getNumberOfQuestion() + " из " + test.questions.size());
        System.out.println(question.getTextOfQuestion());

        int index = 1;
        for (Answer answer : question.getAnswers()) {                     //вывод вариантов ответов в консоль
            System.out.println(index + " " + answer.getTextOfAnswer() + " " + answer.isCorrect());
            index++;
            if (answer.isCorrect()) countOfcorrect++;
        }
        System.out.println("Выберите " + countOfcorrect + " ответов");

    }

    int checkOfCorrect(String input, Test test, Question question){

        int result = 0;
        String[] str = input.split(",");
        int[] ints = new int[str.length];
        for (int i = 0; i < str.length; i++){
            ints[i] = Integer.parseInt(str[i]);

        }
        boolean correct = true;
        if (ints.length == test.countOfcorrect) {
            for (int i = 0; i < ints.length; i++) {

                correct = correct && question.getAnswers().get(ints[i] - 1).isCorrect();

            }
        } else correct = false;

        if (correct) result = 1;

        return result;
    }


    void resultsToConsole(int resultOfTest, Test test, Student student){

        int percent = test.calculatePercent(resultOfTest, test);

        System.out.println("Студент " + student.getName() + " " + percent + "%");
        System.out.println(resultOfTest);
        if (percent >= test.minimumPer) {
            System.out.println("Тест успешно пройден ");
        } else System.out.println("Тест не пройден");


    }

    void saveResultsOnDisk(int resultOfTest,Test test, Student student) throws IOException{

        BufferedWriter writer = new BufferedWriter(new FileWriter("c:\\results"));
        SimpleDateFormat date = new SimpleDateFormat("dd.MM.yyyy");
        writer.write(date.format(new Date()) + " " + student.getName() + " " + test.calculatePercent(resultOfTest, test));
        writer.close();

    }



    int calculatePercent(int resultOfTest, Test test) {

        return Math.round((float) resultOfTest/test.countOfQuest*100);
    }





}
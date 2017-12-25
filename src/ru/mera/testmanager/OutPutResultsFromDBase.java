package ru.mera.testmanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OutPutResultsFromDBase {

    private Connection connection;
    private String name;
    private int studentId;

    public OutPutResultsFromDBase(Connection connection) {
        this.connection = connection;
    }

    public void outPutStudentTest(String name) throws SQLException, IOException {


        try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM student WHERE name = ?;")){
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                studentId = resultSet.getInt(1);
            } else {
                System.out.println("Студент не найден");

            }
        }

        if (studentId != 0){
            try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM student_test WHERE student_id = ?")) {
                preparedStatement.setInt(1, studentId);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    resultSet.beforeFirst();
                    while (resultSet.next()) {
                        System.out.println("№ теста:" + resultSet.getInt(1) + " Студент: " + name + " Начало теста: " +
                                resultSet.getTimestamp(3) + " Окончание: " + resultSet.getTimestamp(4) + " Результат: " + resultSet.getFloat(5) + "%");
                        System.out.println("");
                    }

                    outPutStudentTestQuestion();

                } else System.out.println("Тестирования ещё не было." );
            }
        }
    }

    public void outPutStudentTestQuestion() throws SQLException, IOException {

        int studentTestId;
        int studentTestQuestionId;
        int questionId;

        System.out.println("Для просмотра вопросов выберите № теста:");
        //System.out.println("id of student --------------------------- " + studentId);
        studentTestId = digitInput();

        try(PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT student_id FROM student_test WHERE id =  ?")) {
            preparedStatement2.setInt(1, studentTestId);
            ResultSet resultSet2 = preparedStatement2.executeQuery();

            if (resultSet2.next() && resultSet2.getInt(1) == studentId) {
                try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM student_test_question WHERE test_id = ?")) {
                    preparedStatement.setInt(1, studentTestId);
                    ResultSet resultSet = preparedStatement.executeQuery();

                    while (resultSet.next()) {
                        studentTestQuestionId = resultSet.getInt(1);
                        questionId = resultSet.getInt(3);
                        try (PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT text_of_question FROM questions where id = ?")) {
                            preparedStatement1.setInt(1, questionId);
                            ResultSet resultSet1 = preparedStatement1.executeQuery();
                            if (resultSet1.next()) {
                                System.out.println("№ вопроса в тесте: " + studentTestQuestionId + "\n" + resultSet1.getString(1));
                            }

                        }
                    }
                }

                outPutStudentTestAnswers();

            }else System.out.println("Тест не найден.");
        }

    }

    public void outPutStudentTestAnswers() throws IOException, SQLException {

        int answerId;
        int studentTestQuestionId;

        System.out.println("Для просмотра выбранных ответов выберите № вопроса в тесте:");
        studentTestQuestionId = digitInput();

        try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT answer_id FROM student_test_answers WHERE student_test_question_id = ?")){

            preparedStatement.setInt(1,studentTestQuestionId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                answerId = resultSet.getInt(1);
                //System.out.println("answer_id-------------------------" + answerId);
                try(PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT * FROM answers WHERE id = ?")){
                    preparedStatement1.setInt(1, answerId);
                    ResultSet resultSet1 = preparedStatement1.executeQuery();
                    while (resultSet1.next()){
                        System.out.println( resultSet1.getString(2) + " - " + resultSet1.getBoolean(3));
                    }
                }
            }

        }

    }

    public boolean isDigits(String input){
        Pattern regex = Pattern.compile("\\d+");
        Matcher matcher = regex.matcher(input);
        return matcher.matches();
    }

    public int digitInput() throws IOException {

        String input;
        int outPut;

        //try(BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))){
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (true){
                input = reader.readLine();
                if (isDigits(input)) {
                    outPut = Integer.parseInt(input);
                    break;
                }
                System.out.println("Неверный ввод.");
            }
        //}
        //reader.close();
        return outPut;
    }

    public void readNameFromConsole() throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Введите ваше имя:");
        while (true){
            name = reader.readLine();
            if (checkName(name)) break;
            System.out.println("Не очень-то похоже на имя, попробуй еще раз.");
        }
        //reader.close();
    }

    private boolean checkName(String name){
        Pattern regex = Pattern.compile("[a-zA-Z]+|[а-яА-Я]+");
        Matcher matcher = regex.matcher(name);
        return matcher.matches();
    }

    public static void main(String[] args) throws Exception{
        TestManager testManager = new TestManager(args[0]);
        OutPutResultsFromDBase outPutResults = new OutPutResultsFromDBase(testManager.getConnection());
        outPutResults.readNameFromConsole();
        outPutResults.outPutStudentTest(outPutResults.name);

    }
}

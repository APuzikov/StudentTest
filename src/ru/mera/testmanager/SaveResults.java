package ru.mera.testmanager;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SaveResults {

    private Student student;
    private Connection connection;

    public SaveResults(Student student, Connection connection) {
        this.student = student;
        this.connection = connection;
    }

    void saveResultToDisk(int resultOfTest) throws IOException{

        PrintWriter writer = new PrintWriter(new FileWriter("C:\\Users\\apuzik\\Documents\\results", true));
        SimpleDateFormat date = new SimpleDateFormat("dd.MM.yyyy");
        writer.println(date.format(new Date()) + " " + student.getName() + " " + resultOfTest);
        writer.close();

    }


    void saveStudentTestQuestion(int idStudentTest, List<Question> questions) throws LoadException {

        try(PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO student_test_question (test_id, question_id) VALUES (?, ?);")) {



            for (Question question : questions) {
                preparedStatement.setInt(1, idStudentTest);
                preparedStatement.setInt(2, question.getId());
                preparedStatement.executeUpdate();
            }

        } catch (SQLException e){
            throw new LoadException("Ошибка коннекта к базе данных", e);
        }
    }

    int saveStudentTest() throws LoadException{
        String name = student.getName();// имя студента
        int studentId;
        int id = -1;

        //получаем текущую дату
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());

        try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM student WHERE name = ?;")) {


            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                //получаем id студента
                studentId = resultSet.getInt("id");

                //делаем запись в таблице student_test
                try(PreparedStatement preparedStatement1 = connection.prepareStatement("INSERT INTO student_test (student_id, test_date, test_result) VALUES (?, ?, ?);")) {
                    preparedStatement1.setInt(1,studentId);
                    preparedStatement1.setTimestamp(2, timestamp);
                    preparedStatement1.setFloat(3, 0.f);
                    preparedStatement1.executeUpdate();

                }

                //получаем id теста
                ResultSet resultSet1 = preparedStatement.executeQuery("SELECT LAST_INSERT_ID();");
                if (resultSet1.next()) id = resultSet1.getInt(1);

            }


        } catch (SQLException e){
            throw new LoadException("Ошибка коннекта к базе данных", e);
        }
        return id;

    }

    void updateStudentTest(int testId, int resultOfTest) throws LoadException {

        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());

        try(PreparedStatement preparedStatement = connection.prepareStatement("UPDATE student_test SET end_date = ?, test_result = ? where id = ?;")){
        preparedStatement.setTimestamp(1, timestamp);
        preparedStatement.setFloat(2,resultOfTest);
        preparedStatement.setInt(3, testId);
        preparedStatement.executeUpdate();

        } catch (SQLException e){
            throw new LoadException("Ошибка коннекта к базе данных", e);
        }

    }

//    void saveStudentTestAnswers(int studentTestQuestionId, int answerId) throws LoadException{
//
//        try(PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO student_test_answers(studen_test_question_id, answer_id) VALUES (?, ?);")){
//            preparedStatement.setInt(1, studentTestQuestionId);
//            preparedStatement.setInt(2, answerId);
//            preparedStatement.executeUpdate();
//
//        }catch (SQLException e){
//            throw new LoadException("Ошибка коннекта к базе данных", e);
//        }
//
//    }

}
package ru.mera.testmanager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionFromDBaseLoader implements QuestionLoader{
    private int countOfQuest;
    private Connection connection;

    public QuestionFromDBaseLoader(int countOfQuest, Connection connection) {
        this.countOfQuest = countOfQuest;
        this.connection = connection;
        //this.pathToQuestionDirectory = pathToQuestionDirectory;
    }

    @Override
    public List<Question> load() throws LoadException{
        List<Question> questions = new ArrayList<>();
        int[] numbersOfQuestion = new int[countOfQuest];

        for (int i = 0; i < countOfQuest; i++) { //генерим массив с номерами вопросов для чтения из файла
            while (true) {
                int number = getRandomNumber();
                if (checkNumber(number, numbersOfQuestion)) {
                    numbersOfQuestion[i] = number;
                    break;
                }
            }
        }

        for (int i = 0; i < countOfQuest; i++) {

            questions.add(buildQuestionFromDbase(numbersOfQuestion[i], i + 1));
        }

        return questions;

    }

    private Question buildQuestionFromDbase(int id, int number) throws LoadException {

        List<Answer> answers = new ArrayList<>();
        String textOfQuestion = "";

        try(Statement statement = connection.createStatement()){

            // читаем текст вопроса
            ResultSet getQuestion = statement.executeQuery("select * from questions where id = " + id +" ;");
            while (getQuestion.next()) {
                textOfQuestion = getQuestion.getString(2);
                //System.out.println(textOfQuestion);
            }

            // записываем ответы в массив
            ResultSet answersFromDB = statement.executeQuery("select * from answers where question_id = " + id +" ;");
            while (answersFromDB.next()){
                //System.out.println(getAnswers.getString(2) + " " + getAnswers.getBoolean(3));
                answers.add(new Answer(answersFromDB.getString(2), answersFromDB.getBoolean(3), answersFromDB.getInt(1)));
            }

        } catch (SQLException e){
            throw new LoadException("Ошибка при чтении из базы данных ", e);
        }

        return new Question(number, textOfQuestion, answers, id);
    }

    private int getRandomNumber() throws LoadException {
        return (int)Math.round(Math.random() * (getCountOfData() - 1)) + 1;
    }

    private boolean checkNumber(int number, int[] numbersOfQuestion){
        boolean check = true;
        for (int i : numbersOfQuestion){

            if (i == number){
                check = false;
            }
        }
        return check;
    }

    private int getCountOfData() throws LoadException{

        int count = 0;

        try(Statement statement = connection.createStatement()){

            //ResultSet resultSet = statement.executeQuery("select count(*) from questions");
            ResultSet resultSet = statement.executeQuery("select count(*) from questions");
            if (resultSet.next()){
            count = resultSet.getInt(1);
            }

        } catch (SQLException e){
                throw new LoadException("Ошибка при чтении из базы данных ", e);
        }

        return count;
    }

//    public static void main(String[] args) throws Exception {
//        QuestionFromDBaseLoader questionFromDBaseLoader = new QuestionFromDBaseLoader(2);
//        System.out.println(questionFromDBaseLoader.getCountOfData());
//        //questionFromDBaseLoader.buildQuestionFromDbase(1, 3);
//    }
}

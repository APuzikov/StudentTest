package ru.mera.testmanager;

import java.sql.*;

public class DBaseQuestionChecker implements QuestionChecker {

    private Connection connection;

    public DBaseQuestionChecker(Connection connection) {
        this.connection = connection;
    }

    @Override
    public int checkOfCorrect(String input, Test test, Question question) throws LoadException {
        int result = 0;
        int studentTestQuestionId = 0;
        String[] str = input.split(",");
        int[] ints = new int[str.length];

        for (int i = 0; i < str.length; i++){
            ints[i] = Integer.parseInt(str[i]);

            if (question.getAnswers().get(ints[i] - 1) != null) {

                try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM student_test_question WHERE test_id = ? AND question_id = ?")){

                preparedStatement.setInt(1,test.getTestId());
                preparedStatement.setInt(2,question.getId());
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) studentTestQuestionId = resultSet.getInt(1);

                } catch (SQLException e){
                    e.printStackTrace();

                }


                //System.out.println(studentTestQuestionId + "   beetwen         ---------------------------------------------------");

                try(PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO student_test_answers(student_test_question_id, answer_id) VALUES (?, ?);"))
                {
                        preparedStatement.setInt(1, studentTestQuestionId);
                    System.out.println(studentTestQuestionId + "-----------------------------------");
                        preparedStatement.setInt(2, question.getAnswers().get(ints[i] - 1).getId());
                    System.out.println(question.getAnswers().get(ints[i] - 1).getId() + "==================================");
                        preparedStatement.executeUpdate();
                    //System.out.println(studentTestQuestionId + "            +++++++++++++++++++++++++++++++++++++++++++++");

                } catch (SQLException e) {
                    e.printStackTrace();

                }
            }
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

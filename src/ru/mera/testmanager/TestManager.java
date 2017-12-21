package ru.mera.testmanager;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class TestManager {

    private Properties prop = new Properties();


    public TestManager(String pathToConfig) throws IOException{
        propertiesLoad(pathToConfig);

    }

    private Properties propertiesLoad(String path) throws IOException{

        try (InputStream input = new FileInputStream(path)) {
            prop.load(input);
        }
      return prop;
    }

    String studentInit() throws IOException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Адрес электронной почты:");
        return reader.readLine();
    }

     Connection getConnection() throws SQLException, ClassNotFoundException {

        Class.forName(prop.getProperty("driver"));
        return DriverManager.getConnection(prop.getProperty("url"), prop.getProperty("username"), prop.getProperty("password"));
    }


    public void startTest(String level) throws LoadException, IOException{

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Адрес электронной почты:");
        String studentEmail = reader.readLine();
        System.out.println("Пароль:");
        String studentPassword = reader.readLine();

        Test test = new Test(Integer.parseInt(prop.getProperty("minimumPer")));
        String input;
        int resultOfTest = 0;
        final int countOfQuest = Integer.parseInt(prop.getProperty("countOfQuest"));

        //test.questionsInit(new RandomQuestionsLoader(countOfQuest, countOfAnswers));
        //test.questionsInit(new QuestionFromFileLoader(countOfQuest, prop.getProperty("pathToQuestionDirectory")));
        try(Connection connection = getConnection()) {
            test.questionsInit(new QuestionFromDBaseLoader(countOfQuest, connection));
            //QuestionChecker questionChecker = new DefaultQuestionChecker();
            QuestionChecker questionChecker = new DBaseQuestionChecker(connection);



            StudentLoaderFromDBase studentLoader = new StudentLoaderFromDBase(connection);
            Student student = studentLoader.loadByEmail(studentEmail);



            if (student == null || !student.getPassword().equals(studentPassword)){
                System.out.println("Студент не найден или неверный пароль");

            } else {
                SaveResults saveResults = new SaveResults(student, connection);
                System.out.println("Студент: " + student.getName());
                System.out.println("Уровень сложности - " + level);
                System.out.println("Нужно ответить правильно на " + prop.getProperty("minimumPer") + "% вопросов");

                int id = saveResults.saveStudentTest();
                test.setTestId(id);

                saveResults.saveStudentTestQuestion(id, test.getQuestions());

                for (Question question : test.getQuestions()) {

                    // вывод вопроса и списка ответов в консоль
                    test.outputQuestionsToConsole(question);

                    //считывание с консоли
                    input = test.readFromConsole();

                    //проверка правильности ответов
                    resultOfTest = resultOfTest + questionChecker.checkOfCorrect(input, test, question);

                }

                //вывод результата тестирования в консоль
                test.resultsToConsole(resultOfTest, student);

                //сохранение результатов тестирования
                //saveResults.saveResultToDisk(test.calculatePercent(resultOfTest));


                saveResults.updateStudentTest(id, test.calculatePercent(resultOfTest));
            }
        } catch (Exception e){
            throw new LoadException("Ошибка коннекта к базе данных", e);
        }

    }
    public static void main(String[] args) throws LoadException{

        DifficultyLevel level = DifficultyLevel.NORMAL;
        //System.out.println(level.name());
        try {
            TestManager testManager = new TestManager(args[0]);

            testManager.startTest(level.getName());
        } catch (IOException e) {
            throw new LoadException("File not found...", e);
        }

    }
}

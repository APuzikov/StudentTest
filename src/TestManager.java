import java.io.IOException;

public class TestManager {

    Student studentInit(){
        Student student = new Student("Ivanov");
        return student;
    }

    public void startTest(Student student) throws IOException {
        Test test = new Test();
        //BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input;
        int resultOfTest = 0;


        test.questionsInit();

        for (Question question : test.getQuestions()) {

            // вывод вопроса и списка ответов в консоль
            test.outputQuestionsToConsole(question, test);

            //считывание с консоли
            input = test.readFromConsole();

            //проверка правильности ответов
            resultOfTest = resultOfTest + test.checkOfCorrect(input, test, question);

        }

        //вывод результатов в консоль
        test.resultsToConsole(resultOfTest, test, student);
        //запись в файл
        //test.saveResultsOnDisk(resultOfTest, test, student);
    }
    public static void main(String[] args) throws IOException {

        TestManager testManager = new TestManager();
        Student student = testManager.studentInit();
        testManager.startTest(student);

    }
}

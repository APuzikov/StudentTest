package ru.mera.testmanager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionFromFileLoader implements QuestionLoader {
    private int countOfQuest;
    private String pathToQuestionDirectory;

    public QuestionFromFileLoader(int countOfQuest, String pathToQuestionDirectory) {
        this.countOfQuest = countOfQuest;
        this.pathToQuestionDirectory = pathToQuestionDirectory;
    }

    @Override
    public List<Question> load() throws  LoadException { //
        List<Question> questions = new ArrayList<>();
        int[] numbersOfQuestion = new int[countOfQuest];
        String fileName;

        for (int i = 0; i < countOfQuest; i++) { //генерим массив с номерами вопросов для чтения из файла
            while (true) {
                int number = getRandomNumber();
                if (checkNumber(number, numbersOfQuestion)) {
                    numbersOfQuestion[i] = number;
                    break;
                }
            }
        }

        for (int i = 0; i < countOfQuest; i++) { // генерим список вопросов
            fileName = pathToQuestionDirectory + "\\test" + numbersOfQuestion[i] + ".txt";
            questions.add(buildQuestionFromFile(fileName, i + 1, numbersOfQuestion[i]));
        }
        return questions;

    }

    private int getRandomNumber(){
        return (int)Math.round(Math.random() * (getCountOfFiles(pathToQuestionDirectory) - 1)) + 1;
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

    private int getCountOfFiles(String path){
        int count = 0;
        File file = new File(path);
        File listFile[] = file.listFiles();
        if (listFile != null) {
            for (File file1 : listFile) {
                if (file1.isFile()) count++;
            }
        }

        return count;
    }

    private Question buildQuestionFromFile(String fileName, int number, int id) throws LoadException{

        StringBuffer buffer = new StringBuffer();
        List<Answer> answers = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {

            boolean flag = false;

            while (reader.ready()) {
                String line = reader.readLine();
                //System.out.println(line);

                if (line.equals("----START ANSWERS----")) {
                    flag = true;

                } else if (!flag) {
                    //продолжаем читать вопрос
                    buffer.append(line).append("\n");

                } else  {
                    //читаем ответы
                    answers.add(buidAnswerFromString(line));
                }
            }
        } catch (IOException e){
            throw new LoadException("Ошибка при чтении файла " + fileName, e);
        }

        return new Question(number, buffer.toString(), answers, id);
    }

    private Answer buidAnswerFromString(String line) {
        String[] data = line.split(",", 2);

        return new Answer(data[1], data[0].equals("1"));
    }

//   public static void main(String[] args) {
//   QuestionFromFileLoader questionFromFileLoader = new QuestionFromFileLoader(5, "C:\\Users\\apuzik\\IdeaProjects\\StudentTest");
//        System.out.println(questionFromFileLoader.getCountOfFiles("C:\\Users\\apuzik\\IdeaProjects\\StudentTest\\questions"));
  //  }

}

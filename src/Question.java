import java.util.List;

public class Question {

    private final int numberOfQuestion;
    private final String textOfQuestion;
    private final List<Answer> answers;

    Question(int numberOfQuestion, String textOfQuestion, List<Answer> answers) {
        this.textOfQuestion = textOfQuestion;
        this.numberOfQuestion = numberOfQuestion;
        this.answers = answers;
    }

    int getNumberOfQuestion() {
        return numberOfQuestion;
    }

    String getTextOfQuestion() {
        return textOfQuestion;
    }

    List<Answer> getAnswers() {
        return answers;
    }

    @Override
    public String toString() {
        return "Question{" +
                "numberOfQuestion=" + numberOfQuestion +
                ", textOfQuestion='" + textOfQuestion + '\'' +
                ", answers=" + answers +
                '}';
    }
}

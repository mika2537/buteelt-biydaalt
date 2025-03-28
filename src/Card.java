public class Card {
    private String question;
    private String answer;
    private int mistakes;

    public Card(String question, String answer, int mistakes) {
        this.question = question;
        this.answer = answer;
        this.mistakes = mistakes;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public int getMistakes() {
        return mistakes;
    }

    public void incrementMistakes() {
        this.mistakes++;
    }
}
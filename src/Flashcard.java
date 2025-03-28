
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Flashcard {
    private static final String HELP_MESSAGE = """
        Usage: flashcard <cards-file> [options]
        Options:
        --help                     Show help message
        --order <order>            Order type: [random, worst-first, recent-mistakes-first] (default: random)
        --repetitions <num>        Number of correct repetitions required (default: 1)
        --invertCards              Swap question and answer
        """;

        public static void main(String[] args) {
            if (args.length == 0 || args[0].equals("--help")) {
                System.out.println(HELP_MESSAGE);
                return;
            }
        
            String filePath = (args.length > 0 && !args[0].equals("--help")) ? args[0] : "/Users/mika/IdeaProjects/buteeltbiydaalt/src/Card.txt";
            String order = "random";
            int repetitions = 1;
            boolean invertCards = false;
        
           
        
            List<Card> cards = loadFlashcards(filePath);
            if (cards == null) return;
        
            switch (order) {
                case "random":
                    Collections.shuffle(cards);
                    break;
                case "recent-mistakes-first":
                    CardOrganizer organizer = new RecentMistakesFirstSorter();
                    cards = organizer.sortCards(cards);
                    break;
                default:
                    System.out.println("Invalid order type: " + order);
                    return;
            }
        
            runFlashcards(cards, repetitions, invertCards);
        }

    private static List<Card> loadFlashcards(String filePath) {
        System.out.println("Loading flashcards from: " + filePath);
        List<Card> cards = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            for (String line : lines) {
                String[] parts = line.split("::");
                if (parts.length == 3) {
                    int mistakes = Integer.parseInt(parts[2]);
                    cards.add(new Card(parts[0], parts[1], mistakes));
                } else {
                    System.out.println("Skipping invalid line: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return null;
        }
        return cards;
    }

    private static void runFlashcards(List<Card> cards, int repetitions, boolean invertCards) {
        Scanner scanner = new Scanner(System.in);
        Map<String, Integer> correctCount = new HashMap<>();
        Map<String, Integer> attemptCount = new HashMap<>();
        boolean allCorrect = true;
    
        for (Card card : cards) {
            correctCount.put(card.getQuestion(), 0);
            attemptCount.put(card.getQuestion(), 0);
        }
    
        while (true) {
            boolean allLearned = true;
            for (Card card : cards) {
                String question = invertCards ? card.getAnswer() : card.getQuestion();
                String answer = invertCards ? card.getQuestion() : card.getAnswer();
    
                attemptCount.put(card.getQuestion(), attemptCount.get(card.getQuestion()) + 1);
    
                if (correctCount.get(card.getQuestion()) >= repetitions) continue;
                allLearned = false;
    
                System.out.println("Question: " + question);
                String userAnswer = scanner.nextLine();
                
    
                // Allow the user to exit the game by typing "exit"
                if (userAnswer.equalsIgnoreCase("exit")) {
                    System.out.println("Exiting the game...");
                    return; // Exit the method and stop the game
                }
    
                if (userAnswer.equalsIgnoreCase(answer)) {
                    correctCount.put(card.getQuestion(), correctCount.get(card.getQuestion()) + 1);
                    System.out.println("Correct!");
                } else {
                    allCorrect = false;
                    System.out.println("Wrong! Correct answer: " + answer);
                }
            }
            if (allLearned) break; // If all cards are learned, exit the loop.
        }
    
        System.out.println("All cards learned!");
    
        // Check achievements
        System.out.println("Achievements Unlocked:");
        if (allCorrect) System.out.println("🏆 CORRECT: All cards were answered correctly in the last round!");
        for (String card : attemptCount.keySet()) {
            if (attemptCount.get(card) > 5) {
                System.out.println("🔄 REPEAT: The card '" + card + "' was answered more than 5 times.");
            }
            if (correctCount.get(card) >= 3) {
                System.out.println("💡 CONFIDENT: The card '" + card + "' was answered correctly at least 3 times.");
            }
        }
    }
}



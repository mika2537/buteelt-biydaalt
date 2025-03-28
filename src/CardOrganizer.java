import java.util.List;

interface CardOrganizer {
    List<Card> sortCards(List<Card> cards);
}

class RecentMistakesFirstSorter implements CardOrganizer {
    @Override
    public List<Card> sortCards(List<Card> cards) {
        cards.sort((c1, c2) -> Integer.compare(c2.getMistakes(), c1.getMistakes()));
        return cards;
    }
}
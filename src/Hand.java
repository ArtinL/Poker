

import java.util.ArrayList;

public class Hand {

    private ArrayList<Card> hand;

    Hand() {
        hand = new ArrayList<Card>();
    }

    void clear() {
        hand.clear();
    }

    ArrayList<Card> getCardList() {
        return hand;
    }

    void addCard(Card c) {
        if (c == null)
            throw new NullPointerException("Can't add a null card to a hand.");
        hand.add(c);
    }

    void removeCard(Card c) {
        hand.remove(c);
    }

    void removeCard(int position) {
        if (position < 0 || position >= hand.size())
            throw new IllegalArgumentException("Position does not exist in hand: "
                    + position);
        hand.remove(position);
        
    }

    int getCardCount() {
        return hand.size();
    }

    Card getCard(int position) {
        if (position < 0 || position >= hand.size())
            throw new IllegalArgumentException("Position does not exist in hand: "
                    + position);
        return hand.get(position);
        
    }

    void sortBySuit() {
        ArrayList<Card> newHand = new ArrayList<Card>();
        while (hand.size() > 0) {
            int pos = 0;
            Card c = hand.get(0);
            for (int i = 1; i < hand.size(); i++) {
                Card c1 = hand.get(i);
                if ( c1.getSuit() < c.getSuit() ||
                        (c1.getSuit() == c.getSuit() && c1.getValue() < c.getValue()) ) {
                    pos = i;
                    c = c1;
                }
            }
            hand.remove(pos);
            newHand.add(c);
        }
        hand = newHand;
    }

    void sortByValue() {
        ArrayList<Card> newHand = new ArrayList<Card>();
        while (hand.size() > 0) {
            int pos = 0;
            Card c = hand.get(0);
            for (int i = 1; i < hand.size(); i++) {
                Card c1 = hand.get(i);
                if ( c1.getValue() < c.getValue() ||
                        (c1.getValue() == c.getValue() && c1.getSuit() < c.getSuit()) ) {
                    pos = i;
                    c = c1;
                }
            }
            hand.remove(pos);
            newHand.add(c);
        }
        hand = newHand;
    }

    boolean hasAce() {
        Card[] handArr = getCardList().toArray(new Card[5]);
        for (Card card : handArr) {
            if (card.getValue() == 1) return true;
        }
        return false;
    }

}

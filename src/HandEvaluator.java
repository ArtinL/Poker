import java.util.ArrayList;

public class HandEvaluator {

    int evaluateScore(Hand hand) {
        Card[] cards = hand.getCardList().toArray(new Card[5]);
        boolean[] scoreArray = new boolean[] {
                pair(cards),
                twoPair(cards),
                threeOfAKind(cards),
                straight(cards),
                flush(cards),
                fullHouse(cards),
                fourOfAKind(cards),
                straightFlush(cards),
                royalFlush(cards)
        };
        for (int i = 8; i >= 0; i--) {
            if (scoreArray[i]) {
                return i+1;
            }
        }
        return 0;
    }

    String getScoreString(int score) {
        switch (score) {
            case 1: return "Pair";
            case 2: return "Two Pair";
            case 3: return "Three of a Kind";
            case 4: return "Straight";
            case 5: return "Flush";
            case 6: return "Full House";
            case 7: return "Four of a Kind";
            case 8: return "Straight Flush";
            case 9: return "Royal Flush";
            default: return "no winning hand";
        }
    }

    Card findHighCard(Hand hand) {
        hand.sortByValue();
        return hand.hasAce() ? hand.getCard(0) : hand.getCard(4);
    }

    // Winning hands
    private boolean pair(Card[] cards) {
        int[] values = valArray(cards);
        int[] numOfEachArray = numOfEach(values);
        return contains(numOfEachArray, 2);
    }

    private boolean twoPair(Card[] cards) {
        int[] values = valArray(cards);
        int[] numEach1 = numOfEach(values);
        int[] numEach2 = numOfEach(numEach1);
        return numEach2[1] == 2;
    }

    private boolean threeOfAKind(Card[] cards) {
        int[] values = valArray(cards);
        int[] numOfEachArray = numOfEach(values);
        return contains(numOfEachArray, 3);
    }

    private boolean straight(Card[] cards) {
        int[] values = valArray(cards);
        int[] numOfEachArray = numOfEach(values, contains(values, 1));
        return checkConsecutive(numOfEachArray);
    }

    private boolean flush(Card[] cards) {
        int[] values = suitArray(cards);
        int[] numOfEachArray = numOfEach(values);
        return contains(numOfEachArray, 5);
    }

    private boolean fullHouse(Card[] cards) {
        int[] values = valArray(cards);
        int[] numOfEachArray = numOfEach(values);
        return contains(numOfEachArray, 3) && contains(numOfEachArray, 2);
    }

    private boolean fourOfAKind(Card[] cards) {
        int[] values = valArray(cards);
        int[] numOfEachArray = numOfEach(values);
        return contains(numOfEachArray, 4) || contains(numOfEachArray, 5);
    }

    private boolean straightFlush(Card[] cards) {
        return straight(cards) && flush(cards);
    }

    private boolean royalFlush(Card[] cards) {
        int[] values = valArray(cards);
        return straight(cards) && flush(cards) && contains(values, 1) && !contains(values, 2);
    }


    // Helper Methods
    private int[] numOfEach(int[] values) {
        int[] numArray = new int[14];
        for (int i = 1; i <= 14; i++) {
            int counter = 0;
            for (int item : values) {
                if (item == (i)) counter++;
            }
            numArray[i-1] = counter;
        }
        return numArray;

    }
    private int[] numOfEach(int[] values, boolean containsOne) {
        int[] numArray = new int[14];
        if (containsOne) numArray[13] = 1;
        for (int i = 1; i <= 13; i++) {
            int counter = 0;
            for (int item : values) {
                if (item == (i)) counter++;
            }
            numArray[i-1] = counter;
        }
        return numArray;

    }

    private boolean contains(int[] numArray, int value) {
        for (int num : numArray) {
            if (num == value) return true;
        }
        return false;
    }

    private boolean checkConsecutive(int[] values) {
        int counter;
        for (int i = 0; i < values.length; i++) {
            if (values[i] == 0) continue;
            counter = 0;
            for (int j = i+1; j < values.length; j++) {
                if (values[j] == 0) break;
                counter++;
                if (counter >= 5 -1) return true;
            }
        }
        return false;
    }

    private int[] valArray(Card[] cards) {
        int[] arr = new int[5];
        for (int i = 0; i < 5; i++) {
            arr[i] = cards[i].getValue();
        }
        return arr;
    }

    private int[] suitArray(Card[] cards) {
        int[] arr = new int[5];
        for (int i = 0; i < 5; i++) {
            arr[i] = cards[i].getSuit();
        }
        return arr;
    }

}

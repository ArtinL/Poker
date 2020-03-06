import java.util.ArrayList;

public class Test {
    static HandEvaluator evaluator = new HandEvaluator();
    static Player[] players = new Player[] {
            new Player("PlayerOne", 20, 0),
            new Player("PlayerTwo", 20, 1),
            new Player("PlayerThree", 20, 2),
            new Player("PlayerFour", 20, 3),
            new Player("PlayerFive", 20, 4)
    };
    static Deck deck = new Deck();
    public static void main(String[] args) {
        deck.shuffle();
        for (int i = 0; i < 5; i++) {
            players[0].draw(deck.dealCard());
            players[1].draw(deck.dealCard());
            players[2].draw(deck.dealCard());
            players[3].draw(deck.dealCard());
            players[4].draw(deck.dealCard());
        }
        for (Player player : players) {
            player.printFullHand();
            System.out.println("\n");
        }
        int j = 0;
        do {
            int[] scores = evaluatePlayerScores();
            ArrayList<Player> highHands = new ArrayList<Player>();
            ArrayList<Player> winners = new ArrayList<Player>();

            int maxScore = max(scores);
            for (int i = 0; i < scores.length; i++) {
                if (scores[i] == maxScore) {
                    highHands.add(players[i]);
                }
            }

            Player[] highHandArr = highHands.toArray(new Player[highHands.size()]);

            Card[] highCardsForAllWinners = findPlayerHighCards(highHandArr);
            int maxCardVal = max(highCardsForAllWinners);
            for (int i = 0; i < highHandArr.length; i++) {
                if (highCardsForAllWinners[i].getValue() == maxCardVal) {
                    winners.add(highHandArr[i]);
                }
            }
            declareWinners(winners.toArray(new Player[winners.size()]));
            j++;
        } while(j < 5);
    }

    private static Card[] findPlayerHighCards(Player[] players) {
        Card[] highCards = new Card[players.length];
        for (int i = 0; i < highCards.length; i++) {
            highCards[i] = evaluator.findHighCard(players[i].getHand());
        }
        return highCards;
    }



    private static int max(int[] arr) {
        int max = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > max)
                max = arr[i];
        }
        return max;
    }



    private static int max(Card[] cards) {
        int max = 0;
        for (Card card : cards) {
            if (card.getValue() > max || card.getValue() == 1)
                max = card.getValue();
        }
        return max;
    }

    private static int[] evaluatePlayerScores() {
        int[] scores = new int[players.length];
        int i = 0;
        for (Player player : players) {
            if (player.outOfGame()) continue;
            int score = evaluator.evaluateScore(player.getHand());
            System.out.println(player.getName() + " has " + evaluator.getScoreString(score));
            scores[i] = score;
            i++;
        }
        return scores;
    }

    private static void declareWinners(Player[] winners) {
        String winnerString = "";
        for (int i = 0; i < winners.length; i++) {
            winnerString += winners[i].getName();
            if (i < winners.length - 1) winnerString += " and ";
        }
        System.out.println("\nThe winner(s): " + winnerString);
    }


}

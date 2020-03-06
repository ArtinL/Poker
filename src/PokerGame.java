import java.util.*;


/**
 -- TODO:
 * DONE ---------------Fix the winning hands and high cards functionality. Right now it works but not consistently---------------
 * DONE ---------------Add bust out functionality when player runs out of money---------------
 * DONE ---------------Handle bad inputs and prevent crashing---------------
 * DONE ---------------Increase the number of community cards to 5 from current 3---------------
    * DONE ---------------Add the ability to choose which 5 cards to evaluate for each player---------------
    OR
    * Design a way for the computer to figure out the best combo
 * DONE ---------------Enhance the betting functionality---------------
    * DONE ---------------Add the ability for players to raise to any amount and require others to call that amount or fold---------------
    * DONE ---------------Add big blind and small blind functionality---------------
    * Add the ability to buy in after busting out
    * Add side pot functionality

 */

public class PokerGame {
    private Deck deck;
    private Player[] players;
    private Pot pot;
    private HandEvaluator evaluator;
    private int roundCounter;
    private int dealerIndex;
    private String cardsString = "";
    private double minimumBet;
    private boolean doBlinds;
    private boolean doBlindsOriginal;
    private int amountOfHighHands;


    
    PokerGame(int numPlayers, double buyIn, double minimumBet, boolean doBlinds) {
        players = new Player[numPlayers];
        for (int i = 0; i < players.length; i++) {
            System.out.print("Enter name for player " + (i + 1) + ": ");
            players[i] = new Player(TextIO.getlnString(), buyIn, i);
        }
        deck = new Deck();
        deck.shuffle();
        pot = new Pot();
        evaluator = new HandEvaluator();
        roundCounter = 0;
        dealerIndex = players.length - 1;
        this.minimumBet = minimumBet;
        this.doBlinds = doBlinds;
        doBlindsOriginal = doBlinds;
    }



    void initNewRound() {
        deck.shuffle();
        for (Player player : players) {
            player.reset();
        }
        pot.clear();
        cardsString = "";
        roundCounter++;
        amountOfHighHands = 0;
        doBlinds = doBlindsOriginal;
        dealerIndex = dealerIndex < players.length - 1 ? dealerIndex + 1 : 0;
    }



    int getRoundCounter() {
        return roundCounter;
    }



    void playRound() {
        System.out.println("Round " + (roundCounter + 1) + "\n");
        System.out.println(players[dealerIndex].getName() + " is the Dealer.\n");


        playersDrawCards();
        playersDrawCards();


        playTurns();
        if (everyoneHasFolded()) {
            endRound(true);
            return;
        }
        for (int i = 0; i < 3; i++) {
            Card communityCard = deck.dealCard();
            playersDrawCards(communityCard);
            cardsString += communityCard.toString() + "\n";
        }
        doBlinds = false;
        playTurns();
        if (everyoneHasFolded()) {
            endRound(true);
            return;
        }
        for (int i = 0; i < 2; i++) {
            if (everyoneIsAllIn()) {
                while (i < 3) {
                    Card communityCard = deck.dealCard();
                    playersDrawCards(communityCard);
                    cardsString += communityCard.toString() + "\n";
                    i++;
                }
                break;
            }
            Card communityCard = deck.dealCard();
            playersDrawCards(communityCard);
            cardsString += communityCard.toString() + "\n";
            System.out.println("New community card has been dealt.\n\n");
            playTurns();
            if (everyoneHasFolded()) {
                endRound(true);
                return;
            }
        }

        if (everyoneHasFolded()) {
            endRound(true);
            return;
        }

        endRound(false);
    }

    private void endRound(boolean foldWin) {
        if (foldWin) {
            System.out.println("Everyone has folded.\n");
            Player winner = notFoldedPlayer();
            System.out.println(winner.getName() + " is the winner.\n\n");
            winner.win(pot.getChips());
            printPlayerChips();
            return;
        }
        for (Player player : players) {
            if (player.outOfGame()) continue;
            getHandInput(player);

        }

        int[] scores = evaluatePlayerScores();
        Player[] winners = findWinners(scores);
        declareWinners(winners);
        double winAmount = (double) (Math.round((pot.getChips() / winners.length) * 100)) / 100;
        for (Player player : winners) {
            player.win(winAmount);
        }

        System.out.println();
        printPlayerChips();
    }



    void printPlayerChips() {
        for (Player player : players) {
            if (player.getChips() == 0) {
                player.bust();
                System.out.println(player.getName() + " has busted out.");
            } else {
                System.out.println(player.getName() + " has a total of " + player.getChips());
            }
        }
    }



    boolean everyoneHasFolded() {
        int foldCounter = 0;
        for (Player player : players) {
            if (player.hasFolded()) foldCounter++;
        }
        return foldCounter == players.length-1;
    }



    Player notFoldedPlayer() {
        for (Player player : players) {
            if (!player.hasFolded()) return player;
        }
        return null;
    }



    boolean isOver() {
        int bustedOutPlayers = 0;
        for (Player player : players) {
            if (player.isBusted()) bustedOutPlayers++;
        }
        return bustedOutPlayers == players.length - 1;
    }



    void end() {
        Player winner = winningPlayer();
        System.out.println("\n\n\nGame over\n" + winner.getName() + " has won the game with $" + winner.getChips() + "!\n\n\n");
    }



    private Player winningPlayer() {
        for (Player player : players) {
            if (!player.isBusted()) return player;
        }
        return null;
    }



    private void playersDrawCards() {
        for (Player player : players) {
            player.draw(deck.dealCard());
        }
    }



    private void playersDrawCards(Card card) {
        for (Player player : players) {
            player.draw(card);
        }
    }



    private boolean everyoneIsAllIn() {
        int allInners = 0;
        for (Player player : players) {
            if (player.hasWentAllIn()) allInners++;
        }
        return allInners >= players.length - 1;
    }



    private void playTurns() {
        int i = 0;
        int timesTheLoopRan = 0;
        while (!allCallsZero() || timesTheLoopRan < players.length) {
            Player player = players[i];
            double lastBet = player.getToCall();
            if (player.hasFolded() || player.hasWentAllIn()) {
                if (i == players.length - 1) i = 0;
                else i++;
                timesTheLoopRan++;
                continue;
            }
            if (!cardsString.equals("")) System.out.println("Community cards:\n" + cardsString);
            System.out.println();
            System.out.print(player.getName() + "'s turn. Please conceal computer\n\n" +
                    pot + "\nBet to call: $" + lastBet + "\n\n" +
                    "You have $" + player.getChips() + "\n\n" +
                    "Do you wish to see your cards? (Y/N): ");
            if (TextIO.getlnBoolean()) player.printHand();

            if (doBlinds && player.getID() == getNextIndex(dealerIndex) && timesTheLoopRan < 1) {
                System.out.println("\nYou are the small blind.\n");
                raise(player, 0, minimumBet/2);
                endTurn();
                if (everyoneHasFolded()) {
                    return;
                }
                if (i == players.length - 1) i = 0;
                else i++;
                timesTheLoopRan++;
                continue;
            } else if (doBlinds && player.getID() == getNextIndex(getNextIndex(dealerIndex)) && timesTheLoopRan < 2) {
                System.out.println("\nYou are the big blind.\n");
                raise(player, 0, minimumBet/2);
                endTurn();
                if (everyoneHasFolded()) {
                    return;
                }
                if (i == players.length - 1) i = 0;
                else i++;
                timesTheLoopRan++;
                continue;
            }

            char input = getInput(lastBet, player);

            switch (input) {
                case 'c':
                    call(player, lastBet);
                    break;

                case 'r':
                    System.out.print("How much would you like to raise the previous bet? ");
                    double amount = TextIO.getlnDouble();
                    if ((amount + player.getToCall()) >= player.getChips())
                        allIn(player);
                    else
                        raise(player, lastBet, amount);
                    break;

                case 'a':
                    allIn(player);
                    break;

                default:
                    System.out.println("You have folded");
                    player.fold();
                    break;

            }


            endTurn();
            if (everyoneHasFolded()) {
                return;
            }
            if (i == players.length - 1) i = 0;
            else i++;
            timesTheLoopRan++;
        }
    }



    private void call(Player player, double lastBet) {
        player.call();
        pot.addChips(lastBet);
        String response = lastBet == 0 ? "You checked" : "You called $" + lastBet;
        System.out.println(response);
        System.out.println("You have $" + player.getChips());
    }



    private void raise(Player player, double lastBet, double amount) {
        player.raise(amount);
        raiseForEveryone(amount, player);
        pot.addChips(lastBet + amount);
        String responseCallString = lastBet == 0 ? "Y" : "You called $" + lastBet + " and y";
        System.out.println(responseCallString + "ou raised the stakes by $" + amount);
        System.out.println("You have $" + player.getChips());
    }



    private void allIn(Player player) {
        System.out.println("You have gone all in");
        pot.addChips(player.getChips());
        raiseForEveryone(player.getChips(), player);
        player.allIn();
    }




    private boolean allCallsZero() {
        for (Player player : players) {
            if (player.getToCall() != 0) return false;
        }
        return true;
    }



    private void raiseForEveryone(double amount, Player currentPlayer) {
        for (Player player : players) {
            if (player.equals(currentPlayer) || player.outOfGame() || player.hasWentAllIn()) continue;
            player.addToCall(amount);
        }
    }



    private char getInput(double lastBet, Player player) {
        char input;
        do {
            String response;
            if (lastBet == 0) {
                response = "Check (c), Raise (r), or Fold (f)? ";
            } else if (lastBet >= player.getChips()) {
                response = "All in (a), or Fold (f)?";
            } else {
                response = "Call  $" + player.getToCall() + " (c), Raise (r), or Fold (f)? ";
            }
            System.out.print("\n" + response);
            try {
                input = ((TextIO.getlnString()).toLowerCase()).charAt(0);
            } catch (StringIndexOutOfBoundsException e) {
                input = 0;
                System.out.println("Please provide input");
            }
        } while (!isValid(input));
        return input;
    }



    private boolean isValid(char input) {
        return input == 'c' || input == 'r' || input == 'f' || input == 'a';

    }



    private void endTurn() {

        System.out.print("\nPress Enter to end turn  ");
        TextIO.getlnString();
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }

    }



    int getNextIndex(int index) {
        return index == players.length - 1 ? 0 : index + 1;

    }



    private void getHandInput(Player player) {
        System.out.println(player.getName() + ", these are the cards you have available: ");
        Card[] playerHandArr = player.getHandAsList().toArray(new Card[7]);
        for (int i = 0; i < playerHandArr.length; i++) {
            if (i == 2) System.out.println();
            System.out.println((i+1) + ". " + playerHandArr[i]);
        }
        int[] cardIndices;
        System.out.println("\nWhich 5 cards would you like to choose to evaluate? \n" +
                "--Write the five card indices separated by commas ONLY e.g. 1,2,7\n" +
                "--Do not repeat cards and don't put in a number out of bounds\n");
        System.out.print("Enter your cards: ");
        while (true) {
            String input = TextIO.getlnString();
            try {
                cardIndices = splitArray(input);

            } catch(NumberFormatException err) {
                System.out.print("Bad input. Try again: ");
                continue;
            }
            if (cardIndices.length != 5) {
                System.out.print("Please select only 5 cards. Try again: ");
                continue;
            }
            int[] numEachCardChosen = numOfEach(cardIndices);
            if (containsAtLeast(numEachCardChosen, 2)) {
                System.out.print("Please select only one of each card. Try again: ");
                continue;
            }
            if (max(cardIndices) > 7) {
                System.out.print("Out of bounds. Try again: ");
            }
            break;
        }

        System.out.println("Your cards are: ");
        Hand resultHand = new Hand();
        for (int i = 0; i < 5; i++) {
            resultHand.addCard(playerHandArr[cardIndices[i]-1]);
            System.out.println(playerHandArr[cardIndices[i]-1]);
        }
        endTurn();
        player.setResultHand(resultHand);
    }



    private int[] splitArray(String input) {
        String[] strArr = input.split(",");
        int[] intArr = new int[strArr.length];

        for (int i = 0; i < strArr.length; i++) {
            intArr[i] = Integer.parseInt(strArr[i]);
        }

        return intArr;
    }



    private int[] evaluatePlayerScores() {
        int[] scores = new int[players.length];
        int i = 0;
        for (Player player : players) {
            if (player.outOfGame()) continue;
            int score = evaluator.evaluateScore(player.getResultHand());
            System.out.println(player.getName() + " has " + evaluator.getScoreString(score));
            scores[i] = score;
            i++;
        }
        return scores;
    }



    private Player[] findWinners(int[] scores) {
        ArrayList<Player> highHands = new ArrayList<Player>();
        ArrayList<Player> winners = new ArrayList<Player>();

        int maxScore = max(scores);
        for (int i = 0; i < scores.length; i++) {
            if (scores[i] == maxScore && !players[i].outOfGame()) {
                highHands.add(players[i]);
                amountOfHighHands++;
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
        return winners.toArray(new Player[winners.size()]);
    }



    private Card[] findPlayerHighCards(Player[] players) {
        Card[] highCards = new Card[players.length];
        for (int i = 0; i < highCards.length; i++) {
            highCards[i] = evaluator.findHighCard(players[i].getResultHand());
        }
        return highCards;
    }



    private int max(int[] arr) {
        int max = 0;
        for (int value : arr) {
            if (value > max) max = value;
        }
        return max;
    }



    private int max(Card[] cards) {
        int max = 0;
        for (Card card : cards) {
            if (card.getValue() > max || card.getValue() == 1)
                max = card.getValue();
        }
        return max;
    }



    private boolean containsAtLeast(int[] numArray, int value) {
        for (int num : numArray) {
            if (num >= value) return true;
        }
        return false;
    }



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



    private void declareWinners(Player[] winners) {
        String winnerString = "";
        for (int i = 0; i < winners.length; i++) {
            winnerString += winners[i].getName();
            if (i < winners.length - 1) winnerString += " and ";
        }
        System.out.print("\nThe winner(s): " + winnerString);
        if (amountOfHighHands > 1) System.out.println(" with high cards");
    }


}

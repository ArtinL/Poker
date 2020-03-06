public class PokerGameRunner {
    public static void main(String[] args) {



        do {
            int numPlayers = getPlayerInput();
            double buyIn = getMoneyInput();
            System.out.print("What is the minimum bet? (0 if you want to play without blinds):  $");
            double minBet = TextIO.getlnDouble();
            boolean doBlind = minBet != 0;
            PokerGame game = new PokerGame(numPlayers, buyIn, minBet, doBlind);

            do {
                System.out.println("\n\n");
                game.playRound();

                System.out.print("\nStart next round? (Y/N): ");
                if (!TextIO.getlnBoolean()) break;

                game.initNewRound();

            } while (!game.isOver());

            game.end();
            System.out.print("Play new game? (Y/N): ");

        } while(TextIO.getlnBoolean());

        System.out.println("Thank you for playing Poker.\nGoodbye!");
    }

   

    static int getPlayerInput() {
        int numPlayers;
        do {
            System.out.print("How many people are playing? (from 2 - 8 people): ");
            numPlayers = TextIO.getlnInt();
        } while(numPlayers < 2 || numPlayers > 8);
        return numPlayers;
    }

    static double getMoneyInput() {
        double buyIn;
        do {
            System.out.print("What is the buy-in for the game? (more than $5) $");
            buyIn = TextIO.getlnInt();
        } while (buyIn < 5.0);
        return buyIn;
    }
}

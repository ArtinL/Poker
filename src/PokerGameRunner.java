public class PokerGameRunner {
    public static void main(String[] args) {

        //printWelcomeAndRules(); //Comment this out if you need to.

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

    static void printWelcomeAndRules() {
        System.out.println("Hello and welcome to Poker!\n" +
                "\n" +
                "If you are unfamiliar with the game of poker, switch over to Chrome and take a minute to read the WikiHow article I have open\n" +
                "I have also made a guide on how to play my version of the game on the paper next to the laptop. Refer to it as you are playing\n\n" +
                "Please note that I made some slight modifications to the actual game of Texas Hold'em because the whole game is pretty big\n" +
                "Minus a few things (like side pots) I had to cut out, the game is fully playable and fully functional.\n\n" +
                "Anyway, I hope by this point you've at least somewhat familiarized yourselves with the game.\n" +
                "There are no stakes involved, so you only need to know how the basics in order to play this\n\n" +
                "Also, this text is being printed by a single subroutine at the top of the PokerGameRunner class in main. You can comment it out if you want\n\n" +
                "Anyway, lets get started\n\n");
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

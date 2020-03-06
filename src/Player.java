import java.util.ArrayList;

public class Player {
    public final int IDENTITY;
    private double chips;
    private double toCall;
    private Hand hand = new Hand();
    private String name;
    private boolean busted;
    private boolean blind;
    private boolean hasFolded;
    private boolean wentAllIn;
    private Hand resultHand = new Hand();


    Player(String name, double chips, int identity) {
        this.name  = name;
        this.chips = chips;
        toCall = 0;
        hasFolded = false;
        IDENTITY = identity;
        busted = false;
    }

    String getName() {
        return name;
    }

    Hand getHand() {
        return hand;
    }

    ArrayList<Card> getHandAsList() {
        return hand.getCardList();
    }

    void printHand() {
        System.out.println("\n" + this.hand.getCard(0).toString() + "\n" + this.hand.getCard(1).toString());
    }

    void reset() {
        hand.clear();
        resultHand.clear();
        if (!busted) {
            hasFolded = false;
            wentAllIn = false;
        }
        toCall = 0;
        blind = false;
    }

    void draw(Card c) {
        hand.addCard(c);
    }

    void addToCall(double amount) {
        toCall += amount;
    }

    double getToCall() {
        return toCall;
    }

    void call() {
        if (toCall > chips) {
            throw new IllegalArgumentException();
        }
        chips -= toCall;
        toCall = 0;
    }

    void raise(double raiseAmount) {
        call();
        chips -= raiseAmount;
    }

    void fold() {
        hasFolded = true;
        toCall = 0;
    }

    void allIn() {
        chips = 0;
        toCall = 0;
        wentAllIn = true;
    }

    boolean hasWentAllIn() {
        return wentAllIn;
    }

    void bust() {
        busted = true;
    }

    boolean isBusted() {
        return busted;
    }

    void setBlind() {
        blind = true;
    }

    boolean isBlind() {
        return blind;
    }

    void win(double amount) {
        chips += amount;
    }

    double getChips() {
        return chips;
    }


    boolean hasFolded() {
        return hasFolded;
    }

    int getID() {
        return IDENTITY;
    }

    boolean outOfGame() {
        return busted || hasFolded;
    }

    // for testing
    void printFullHand() {
        for (int i = 0; i < 5; i++) {
            System.out.println(hand.getCard(i));
        }
    }

    Hand getResultHand() {
        return resultHand;
    }

    void setResultHand(Hand resultHand) {
        this.resultHand = resultHand;
    }

    @Override
    public String toString() {
        return name + ", $" + chips;
    }

    @Override
    public boolean equals(Object player) {
        return ((Player)player).IDENTITY == this.IDENTITY;
    }
}

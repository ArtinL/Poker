public class Pot {
    private double chips;

    Pot() {
        chips = 0;
    }

    void addChips(double amount) {
        //if (amount < lastBet) throw new IllegalArgumentException();
        chips += amount;
    }



    void clear() {
        chips = 0;
    }



    double getChips() {
        return chips;
    }

    @Override
    public String toString() {
        return "Amount in pot: $" + chips;
    }
}

public class Deck {

    private Card[] deck;
    private int cardsUsed;

    public Deck() {
        this(false);
    }

    public Deck(boolean includeJokers) {
        if (includeJokers)
            deck = new Card[54];
        else
            deck = new Card[52];
        int cardCt = 0;
        for(int currentSuit = 0; currentSuit <= 3; currentSuit++) {
        		for(int currentValue = 1; currentValue <= 13; currentValue++) {
        			deck[cardCt] = new Card(currentValue, currentSuit);
        			cardCt++;
        		}
        }		
        if (includeJokers) {
            deck[52] = new Card(1,Card.JOKER);
            deck[53] = new Card(2,Card.JOKER);
        }
        cardsUsed = 0;
    }

    public void shuffle() { 	
        cardsUsed = 0;
        int firstRandI;
        int secondRandI;
        
        for (int i = 0; i < 100; i++) {
            firstRandI = (int)(Math.random()*deck.length);
            secondRandI = (int)(Math.random()*deck.length);
            Card temp;
            temp = deck[firstRandI];
            deck[firstRandI] = deck[secondRandI];
            deck[secondRandI] = temp;
        }

    }

    public int cardsLeft() { 
        return deck.length - cardsUsed;
    }

    public Card dealCard() {
        cardsUsed++;
        if (cardsUsed > deck.length) throw new IllegalStateException("Deck out of cards");
        return deck[cardsUsed - 1];
    }

    public boolean hasJokers() {
        return (deck.length == 54);
    }
    
    public String toString() {
    		String str = "";
    		for(int i = 0; i < deck.length; i++) {
    			str += deck[i].toString();
    			if(i == deck.length - 1)
    				str += ".";
    			else
    				str += ", ";
    			if(i > 0 && i % 4 == 0)
    				str += "\n";
    		}
    		return str;
    }

}

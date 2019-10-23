package agents;
import loveletter.*;
import java.util.*;

/**
 * An interface for representing an agent in the game Love Letter
 * All agent's must have a 0 parameter constructor
 * */
public class Agent22482826 implements Agent{

    private Random rand;
    private State current;
    private int myIndex;
    private HashMap<Card, Double> map;

    //0 place default constructor
    public Agent22482826()
    {
        rand  = new Random();
        map = new HashMap<Card ,Double>();
    }

    /**
     * Reports the agents name
     * */
    public String toString()
    {
        return "Agent22482826";
    }

    /**
     * Method called at the start of a round
     * @param start the starting state of the round
     **/
    public void newRound(State start)
    {
        current = start;
        myIndex = current.getPlayerIndex();
    }

    /**
     * Method called when any agent performs an action. 
     * @param act the action an agent performs
     * @param results the state of play the agent is able to observe.
     * **/
    public void see(Action act, State results)
    {
        current = results;
    }

    /**
     * Perform an action after drawing a card from the deck
     * @param c the card drawn from the deck
     * @return the action the agent chooses to perform
     * @throws IllegalActionException when the Action produced is not legal.
     * */
    public Action playCard(Card c){
        Action act = null;
        Card play;
        probability();
        play = chooseCard(current.getCard(myIndex), c);
        Card chosen = select();
        int target = choosePlayer(current);
        System.out.println(target);
        System.out.println(play.toString() + " ################");
        if (chosen == null)
        {
            System.out.println("Guard");
        }
        else
        {
            System.out.println(chosen.toString());
        }
        while(!current.legalAction(act, c))
        {
            if (current.eliminated(target) == true)
            {
                for (int i = 0; i < current.numPlayers(); i++)
                {
                    if( current.eliminated(i) == false && i != myIndex)
                    {
                        target = i;
                    }
                }
            }
            //System.out.println(current.legalAction(act,c));
            {
                try
                {
                    switch(play){
                        case GUARD: // choose a player and guess a card, If correct they get knocked out
                        if (chosen == null)
                        {
                            act = Action.playGuard(myIndex, target, Card.values()[rand.nextInt(7)+1]);
                            break;
                        }
                        act = Action.playGuard(myIndex, target, chosen);
                        break;
                        case PRIEST: // peek a player's card
                        act = Action.playPriest(myIndex, target);
                        break;
                        case BARON: // compare cards with player, lowest player is out
                        act = Action.playBaron(myIndex, target);
                        break;
                        case HANDMAID: // gives immunity to all effects until next round
                        act = Action.playHandmaid(myIndex);
                        break;
                        case PRINCE: // choose a player (or ourself) to discard one card (of our choice), then they draw one more from deck
                        if (current.handmaid(target) == true)
                        {
                            act = Action.playPrince(myIndex, myIndex);
                            break;
                        }
                        act = Action.playPrince(myIndex, target);
                        break;
                        case KING: // choose a player and swap cards with them
                        act = Action.playKing(myIndex, target);
                        break;
                        case COUNTESS: // if prince or king is held, countess must be played. Else, CAN be played
                        act = Action.playCountess(myIndex);
                        break;
                        default:
                        act = null;//never play princess
                    }
                }catch(IllegalActionException e){
                    System.out.println("Illegal move!");/*do nothing*/}
                    //System.out.println(current.legalAction(act,c));
            }
        }
        return act;
    }

    /**
     * Given a state object, nominate a valid player to target, with criteria of player with most wins
     * @param playerState the state in which a player is nominated
     * @return index of the nominated player
     */
    public int choosePlayer( State playerState)
    {
        int target = 0;
        int max = 0;
        for (int i = 0; i < playerState.numPlayers(); i++)
        {
            if( playerState.eliminated(i) == false && i != myIndex && playerState.handmaid(i) == false)
            {
                if ( max <= playerState.score(i))
                {
                    max = playerState.score(i);
                    target = i;
                }
            }
        }
        return target;
    }

    /**
     * Given 2 cards, choose which card to play
     * @param hand the card that is currently in the player's hand
     * @param hand the card that was drawn from deck
     * @return the chosen card
     */
    public Card chooseCard(Card hand, Card drawn)
    {
        if ( hand.value() == 4)
        {
            return hand;
        }
        if (drawn.value() == 4)
        {
            return drawn;
        }
        if (hand.value() == 8)
        {
            return drawn;
        }
        if (drawn.value() == 8)
        {
            return hand;
        }
        if (drawn.value() == 7)
        {
            if (hand.value() == 5 || hand.value() == 6)
            {
                return drawn;
            }
            else
            {
                return hand;
            }
        }
        if (hand.value() == 7)
        {
            if (drawn.value() == 5 || drawn.value() == 6)
            {
                return hand;
            }
            else
            {
                return drawn;
            }
        }
        if (hand.value() <= drawn.value())
        {
            return hand;
        }
        else
        {
            return drawn;
        }
    }

    /**
     * Obtains the card with the highest probability of being drawn
     * @return the card with the highest probability
     */
    public Card select()
    {
        Card chosen = null;
        double max = 0.0;
        for (Card cc : map.keySet())
        {
            if (cc == Card.GUARD)
            {
                continue;
            }
            else
            {
                double value = map.get(cc);
                if (max <= value)
                {
                    max = value;
                    chosen = cc;
                }
            }
        }
        return chosen;
    }

    /**
     * Given the current state of the game, calculate the probability of drawing a card from a pile of unseen/unused cards, not including this agent cards
     */
    public void probability()
    {
        int count;
        map.clear();
        ArrayList<Card> remaining = new ArrayList<Card>(Arrays.asList(current.unseenCards()));
        remaining.remove(current.getCard(myIndex)); // Remove our cards from the pile
        for (int i = 0; i < remaining.size(); i++)
        {
            count = 1;
            Card c = remaining.get(i);
            for (int j = i + 1; j < remaining.size(); j++)
            {
                if (remaining.get(i) == remaining.get(j))
                {
                    count += 1;
                }
                else
                {
                    i = j - 1;
                    break;
                }
            }
            // update probability
            map.put(c, (double) count / (double) remaining.size());//probability of choosing this card from unseen cards
        }
        //printer(map);
    }

    public void printer(HashMap<Card, Double> map)
    {
        double total = 0.0;
        for (Card card: map.keySet())
        {
            String key = card.toString();
            double value = Double.valueOf(map.get(card));
            System.out.println(key + " " + value); 
            total += value;
        }
        System.out.println(total);
    }
}
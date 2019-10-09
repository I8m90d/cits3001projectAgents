package agents;
import loveletter.*;
import java.util.Random;

/**
 * Write a description of class Agent22112348 here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Agent22112348 implements Agent{
    private Random rand;
    private State current;
    private int myIndex;

    //0 place default constructor
    public Agent22112348(){
        rand  = new Random();
    }

    public String toString(){return "Rando";}

    public void newRound(State start){
        current = start;
        myIndex = current.getPlayerIndex();
    }

    public void see(Action act, State results){
        current = results;
    }

    public Action playCard(Card c){
        Action act = null;
        Card play;
        while(!current.legalAction(act, c)){
            if(rand.nextDouble()<0.5) play= c;
            else play = current.getCard(myIndex);
            int target = rand.nextInt(current.numPlayers());
            try{
                switch(play){
                    case GUARD:
                        act = Action.playGuard(myIndex, target, Card.values()[rand.nextInt(7)+1]);
                        break;
                    case PRIEST:
                        act = Action.playPriest(myIndex, target);
                        break;
                    case BARON:  
                        act = Action.playBaron(myIndex, target);
                        break;
                    case HANDMAID:
                        act = Action.playHandmaid(myIndex);
                        break;
                    case PRINCE:  
                        act = Action.playPrince(myIndex, target);
                        break;
                    case KING:
                        act = Action.playKing(myIndex, target);
                        break;
                    case COUNTESS:
                        act = Action.playCountess(myIndex);
                        break;
                    default:
                        act = null;//never play princess
                }
            }catch(IllegalActionException e){/*do nothing*/}
        }
    
        return act;
    }
}

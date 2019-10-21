package agents;
import loveletter.*;
import java.util.Random;

/**
 * Write a description of class Agent22112348 here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class BasicAgent implements Agent{
    private Random rand;
    private State current;
    private int myIndex;

    //0 place default constructor
    public BasicAgent(){
        rand  = new Random();
    }

    public String toString(){return "Basic";}

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
        Card lastcard = null;
        play = current.getCard(myIndex);
        
        while(!current.legalAction(act, c)){
            if(play.value() == 8){
                play = c;
            }else{
                if(play.value() == 7 && c.value() <= 4){
                    play = c;
                }else if(c.value() == 7 && play.value() > 4){
                    play = c;
                }else if(play.value() != 7){
                    if(play.value() > c.value()){
                        play = c;
                    }
                }
            }
            
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

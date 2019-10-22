package agents;
import loveletter.*;
import java.util.Random;
import java.util.Iterator;
import java.lang.Math;

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
    private int ownCard = 0;
    private int newCard = 0;
    
    //0 place default constructor
    public Agent22112348(){
        rand  = new Random();
    }

    public String toString(){return "Viet";}

    public void newRound(State start){
        current = start;
        myIndex = current.getPlayerIndex();
    }

    public void see(Action act, State results){
        current = results;
    }

    public Action playCard(Card c){
        Action act = null;
        Card play = current.getCard(myIndex);
        
        ownCard = play.value();
        newCard = c.value();
        
        int target = -1;
        int guessCard = 0;
        // Never play princess
        double bestGuess = 0;
        int bestTarget = -1;
        int bestCard = 0;
        if(play.value() == 8){
            play = c;
            switch(play){
                    case GUARD:
                        bestGuess = 0;
                        bestTarget = -1;
                        bestCard = 0;
                        // Check if you play priest last turn
                        for(int i = 0; i < current.numPlayers(); i++){
                            // Check for not self, player not eliminated and not have handmaid protection
                            if(i == myIndex || current.eliminated(i) || current.handmaid(i)){
                                continue;
                            }
                            
                            // Check if i play priest last turn and i can still see their cards(in case they play king or prince)
                            if(current.getCard(i) != null){
                                bestCard = current.getCard(i).value();
                                bestTarget = i;
                                break;
                            }
                            
                            // Check if they last play countess, prioritize them
                            if(playLast(i) == 7){
                                double tempGuess = 0;
                                for(int theCard = 5; theCard < 9; theCard++){
                                    if(unplayedProb(theCard) > tempGuess){
                                        tempGuess = unplayedProb(theCard);
                                        bestCard = theCard;
                                    }
                                }
                                if(tempGuess > 0){
                                    break;
                                }
                            }
                            
                            // Start comparing the possible target and their card possibilities
                            for(int theCard = 2; theCard < 9; theCard++){
                                // Can not guess guard
                                if(unplayedProb(theCard) > bestGuess){
                                    bestGuess = unplayedProb(theCard);
                                    bestTarget = i;
                                    bestCard = theCard;
                                }
                            }
                        }
                        target = bestTarget;
                        guessCard = bestCard;
                        break;
                    case PRIEST:
                        // Choose the player without hand maid
                        for(int i = 0; i < current.numPlayers(); i++){
                            // Check for not self, player not eliminated and not have handmaid protection
                            if(i == myIndex || current.eliminated(i) || current.handmaid(i)){
                                continue;
                            }
                            
                            target = i;
                        }
                        break;
                    case BARON: 
                        bestGuess = 0;
                        bestTarget = -1;
                        bestCard = 0;
                        for(int i = 0; i < current.numPlayers(); i++){
                            // Check for not self, player not eliminated and not have handmaid protection
                            if(i == myIndex || current.eliminated(i) || current.handmaid(i)){
                                continue;
                            }
                            
                            // Check for if last play priest
                            if(current.getCard(i) != null){
                                if(current.getCard(i).value() < 3){
                                    bestTarget = i;
                                    break;
                                }
                            }
                            
                            // They last play countess means they prob have high card
                            if(playLast(i) == 7){
                                continue;
                            }
                            
                            bestTarget = i;
                        }
                        
                        target = bestTarget;
                        break;
                    case HANDMAID:
                        break;
                    case PRINCE:  
                        bestGuess = 0;
                        bestTarget = -1;
                        bestCard = 0;
                        for(int i = 0; i < current.numPlayers(); i++){
                            // Check for not self, player not eliminated and not have handmaid protection
                            if(i == myIndex || current.eliminated(i) || current.handmaid(i)){
                                continue;
                            }
                            
                            if(playLast(i) == 7){
                                bestTarget = i;
                                break;
                            }else{
                                if(playLast(i) == 3){
                                    bestTarget = i;
                                    bestCard = 3;
                                    //Not break incase countess
                                }else if(playLast(i) == 6 && bestCard != 3){
                                    bestTarget = i;
                                    // Not break incase countess or baron
                                }
                            }
                        }
                        
                        target = bestTarget;
                        break;
                }
        }else if(c.value() == 8){
            play = play;
            switch(play){
                case GUARD:
                    bestGuess = 0;
                    bestTarget = -1;
                    bestCard = 0;
                    // Check if you play priest last turn
                    for(int i = 0; i < current.numPlayers(); i++){
                        // Check for not self, player not eliminated and not have handmaid protection
                        if(i == myIndex || current.eliminated(i) || current.handmaid(i)){
                            continue;
                        }
                        
                        // Check if i play priest last turn and i can still see their cards(in case they play king or prince)
                        if(current.getCard(i) != null){
                            bestCard = current.getCard(i).value();
                            bestTarget = i;
                            break;
                        }
                        
                        // Check if they last play countess, prioritize them
                        if(playLast(i) == 7){
                            double tempGuess = 0;
                            for(int theCard = 5; theCard < 9; theCard++){
                                if(unplayedProb(theCard) > tempGuess){
                                    tempGuess = unplayedProb(theCard);
                                    bestCard = theCard;
                                }
                            }
                            if(tempGuess > 0){
                                break;
                            }
                        }
                        
                        // Start comparing the possible target and their card possibilities
                        for(int theCard = 2; theCard < 9; theCard++){
                            // Can not guess guard
                            if(unplayedProb(theCard) > bestGuess){
                                bestGuess = unplayedProb(theCard);
                                bestTarget = i;
                                bestCard = theCard;
                            }
                        }
                    }
                    target = bestTarget;
                    guessCard = bestCard;
                    break;
                case PRIEST:
                    // Choose the player without hand maid
                    for(int i = 0; i < current.numPlayers(); i++){
                        // Check for not self, player not eliminated and not have handmaid protection
                        if(i == myIndex || current.eliminated(i) || current.handmaid(i)){
                            continue;
                        }
                        
                        target = i;
                    }
                    break;
                case BARON: 
                    bestGuess = 0;
                    bestTarget = -1;
                    bestCard = 0;
                    for(int i = 0; i < current.numPlayers(); i++){
                        // Check for not self, player not eliminated and not have handmaid protection
                        if(i == myIndex || current.eliminated(i) || current.handmaid(i)){
                            continue;
                        }
                        
                        // Check for if last play priest
                        if(current.getCard(i) != null){
                            if(current.getCard(i).value() < 3){
                                bestTarget = i;
                                break;
                            }
                        }
                        
                        // They last play countess means they prob have high card
                        if(playLast(i) == 7){
                            continue;
                        }
                        
                        bestTarget = i;
                    }
                    
                    target = bestTarget;
                    break;
                case HANDMAID:
                    break;
                case PRINCE:  
                    bestGuess = 0;
                    bestTarget = -1;
                    bestCard = 0;
                    for(int i = 0; i < current.numPlayers(); i++){
                        // Check for not self, player not eliminated and not have handmaid protection
                        if(i == myIndex || current.eliminated(i) || current.handmaid(i)){
                            continue;
                        }
                        
                        if(playLast(i) == 7){
                            bestTarget = i;
                            break;
                        }else{
                            if(playLast(i) == 3){
                                bestTarget = i;
                                bestCard = 3;
                                //Not break incase countess
                            }else if(playLast(i) == 6 && bestCard != 3){
                                bestTarget = i;
                                // Not break incase countess or baron
                            }
                        }
                    }
                    
                    target = bestTarget;
                    break;
                }
        }else{
            // Check for force actions
            if(play.value() == 7 && c.value() > 4){
                play = play;
            }else if(c.value() == 7 && play.value() > 4){
                play = c;
            }else if(play.value() == c.value()){
                switch(play){
                    case GUARD:
                        bestGuess = 0;
                        bestTarget = -1;
                        bestCard = 0;
                        // Check if you play priest last turn
                        for(int i = 0; i < current.numPlayers(); i++){
                            // Check for not self, player not eliminated and not have handmaid protection
                            if(i == myIndex || current.eliminated(i) || current.handmaid(i)){
                                continue;
                            }
                            
                            // Check if i play priest last turn and i can still see their cards(in case they play king or prince)
                            if(current.getCard(i) != null){
                                bestCard = current.getCard(i).value();
                                bestTarget = i;
                                break;
                            }
                            
                            // Check if they last play countess, prioritize them
                            if(playLast(i) == 7){
                                double tempGuess = 0;
                                for(int theCard = 5; theCard < 9; theCard++){
                                    if(unplayedProb(theCard) > tempGuess){
                                        tempGuess = unplayedProb(theCard);
                                        bestCard = theCard;
                                    }
                                }
                                if(tempGuess > 0){
                                    break;
                                }
                            }
                            
                            // Start comparing the possible target and their card possibilities
                            for(int theCard = 2; theCard < 9; theCard++){
                                // Can not guess guard
                                if(unplayedProb(theCard) > bestGuess){
                                    bestGuess = unplayedProb(theCard);
                                    bestTarget = i;
                                    bestCard = theCard;
                                }
                            }
                        }
                        target = bestTarget;
                        guessCard = bestCard;
                        break;
                    case PRIEST:
                        // Choose the player without hand maid
                        for(int i = 0; i < current.numPlayers(); i++){
                            // Check for not self, player not eliminated and not have handmaid protection
                            if(i == myIndex || current.eliminated(i) || current.handmaid(i)){
                                continue;
                            }
                            
                            target = i;
                        }
                        break;
                    case BARON: 
                        bestGuess = 0;
                        bestTarget = -1;
                        bestCard = 0;
                        for(int i = 0; i < current.numPlayers(); i++){
                            // Check for not self, player not eliminated and not have handmaid protection
                            if(i == myIndex || current.eliminated(i) || current.handmaid(i)){
                                continue;
                            }
                            
                            // Check for if last play priest
                            if(current.getCard(i) != null){
                                if(current.getCard(i).value() < 3){
                                    bestTarget = i;
                                    break;
                                }
                            }
                            
                            // They last play countess means they prob have high card
                            if(playLast(i) == 7){
                                continue;
                            }
                            
                            bestTarget = i;
                        }
                        
                        target = bestTarget;
                        break;
                    case HANDMAID:
                        break;
                    case PRINCE:  
                        bestGuess = 0;
                        bestTarget = -1;
                        bestCard = 0;
                        for(int i = 0; i < current.numPlayers(); i++){
                            // Check for not self, player not eliminated and not have handmaid protection
                            if(i == myIndex || current.eliminated(i) || current.handmaid(i)){
                                continue;
                            }
                            
                            if(playLast(i) == 7){
                                bestTarget = i;
                                break;
                            }else{
                                if(playLast(i) == 3){
                                    bestTarget = i;
                                    bestCard = 3;
                                    //Not break incase countess
                                }else if(playLast(i) == 6 && bestCard != 3){
                                    bestTarget = i;
                                    // Not break incase countess or baron
                                }
                            }
                        }
                        
                        target = bestTarget;
                        break;
                }
            }else{
                // General action
                // No combination of countess and other card with value greater than 4
                // No dupp cards
                switch(play){
                    case GUARD:
                        switch(c){
                            case PRIEST:
                                
                                break;
                            case BARON:  
                                bestGuess = 0;
                                bestTarget = -1;
                                bestCard = 0;
                                // Check if you play priest last turn
                                for(int i = 0; i < current.numPlayers(); i++){
                                    // Check for not self, player not eliminated and not have handmaid protection
                                    if(i == myIndex || current.eliminated(i) || current.handmaid(i)){
                                        continue;
                                    }
                                    
                                    // Check if i play priest last turn and i can still see their cards(in case they play king or prince)
                                    if(current.getCard(i) != null){
                                        bestCard = current.getCard(i).value();
                                        bestTarget = i;
                                        break;
                                    }
                                    
                                    // Check if they last play countess, prioritize them
                                    if(playLast(i) == 7){
                                        double tempGuess = 0;
                                        for(int theCard = 5; theCard < 9; theCard++){
                                            if(unplayedProb(theCard) > tempGuess){
                                                tempGuess = unplayedProb(theCard);
                                                bestCard = theCard;
                                            }
                                        }
                                        if(tempGuess > 0){
                                            break;
                                        }
                                    }
                                    
                                    // Start comparing the possible target and their card possibilities
                                    for(int theCard = 2; theCard < 9; theCard++){
                                        // Can not guess guard
                                        if(unplayedProb(theCard) > bestGuess){
                                            bestGuess = unplayedProb(theCard);
                                            bestTarget = i;
                                            bestCard = theCard;
                                        }
                                    }
                                }
                                target = bestTarget;
                                guessCard = bestCard;
                                break;
                            case HANDMAID:
                                break;
                            case PRINCE:  
                                break;
                            case KING:
                                break;
                            case COUNTESS:
                                break;
                        }
                        break;
                    case PRIEST:
                        switch(c){
                            case GUARD:
                                break;
                            case BARON:  
                                break;
                            case HANDMAID:
                                break;
                            case PRINCE:  
                                break;
                            case KING:
                                break;
                            case COUNTESS:
                                break;
                        }
                        break;
                    case BARON: 
                        switch(c){
                            case GUARD:
                                break;
                            case PRIEST:
                                break;
                            case HANDMAID:
                                break;
                            case PRINCE:  
                                break;
                            case KING:
                                break;
                            case COUNTESS:
                                break;
                        }
                        break;
                    case HANDMAID:
                        switch(c){
                            case GUARD:
                                break;
                            case PRIEST:
                                break;
                            case BARON:  
                                break;
                            case PRINCE:  
                                break;
                            case KING:
                                break;
                            case COUNTESS:
                                break;
                        }
                        break;
                    case PRINCE:  
                        switch(c){
                            case GUARD:
                                break;
                            case PRIEST:
                                break;
                            case BARON:  
                                break;
                            case HANDMAID:
                                break;
                            case KING:
                                break;
                        }
                        break;
                    case KING:
                        switch(c){
                            case GUARD:
                                break;
                            case PRIEST:
                                break;
                            case BARON:  
                                break;
                            case HANDMAID:
                                break;
                            case PRINCE:  
                                break;
                        }
                        break;
                    case COUNTESS:
                        switch(c){
                            case GUARD:
                                break;
                            case PRIEST:
                                break;
                            case BARON:  
                                break;
                            case HANDMAID:
                                break;
                        }
                        break;
                }
            }
        }
        
        boolean fail = false;
        while(!current.legalAction(act, c)){
            if(target == -1 || fail){
                target = rand.nextInt(current.numPlayers());
            }
            
            try{
                if(guessCard == 0 || fail){
                    guessCard = rand.nextInt(7) + 2;
                }
                System.out.println("Guess: " + guessCard);
                switch(play){
                    case GUARD:
                        act = Action.playGuard(myIndex, target, Card.values()[guessCard - 1]);
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
            }catch(IllegalActionException e){
                fail = true;
            }
            fail = true;
        }
    
        return act;
    }
    
    private int numUnplayed(){
        return current.unseenCards().length;
    }
    
    // How likely a specific card is on any player(not this) hand
    private double unplayedProb(int cardValue){
        Card[] cards = current.unseenCards();
        int sumLeft = cards.length - 2;
        int numLeft = 0;
        
        if(ownCard == cardValue){
            numLeft--;
        }
        if(newCard == cardValue){
            numLeft--;
        }
        
        for(Card each : cards){
            if(each.value() == cardValue){
                numLeft++;
            }
        }
        
        return ((double)numLeft / (double)sumLeft);
    }
    
    // How likely a specific player(except this) encounter the card
    private double playerCardProb(int cardValue, int playerIndex){
        Iterator<Card> discardList = current.getDiscards(playerIndex);
        int numDiscard = 0;
        int numInit = 0;
        
        while(discardList.hasNext()){
            Card currentCard = discardList.next();
            if(currentCard.value() == cardValue){
                numDiscard++;
                numInit = currentCard.count();
            }
        }
        
        double prob = unplayedProb(cardValue) * Math.pow(((double)1 / (double)numInit), numDiscard);
        
        return prob;
    }
    
    private int playLast(int playerIndex){
        Iterator<Card> discardList = current.getDiscards(playerIndex);
        Card lastcard = null;
        while(discardList.hasNext()){
            lastcard = discardList.next();
        }
        
        if(lastcard == null){
            return -1;
        }
        return lastcard.value();
    }
}

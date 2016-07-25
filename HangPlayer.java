package hangmanCS401GUI;

import java.util.ArrayList;
/**
 * University of Pittsburgh
 * CS401 Java Development
 * Fall 2014
 * @author William O'Toole
 */
public class HangPlayer {
    
    private ArrayList<HangPlayer> p;
    // a globla string to hold the current player name
    private static String currentPlayer;
    // global viariables to count the wins and losses
    private static int winCount = 0;
    private static int lossCount = 0;
    // global count to hold the amount of words in the file.
    private static int masterCount = 0;
    private String name;
    private int wins;
    private int losses;
    
    public HangPlayer(String pName, int numWins, int numLosses){
        
        name = pName;
        wins = numWins;
        losses = numLosses;
     
    }
    public HangPlayer(){
        
    }
    
    public String getName(){
        
        return name;
    }
    
    public int getWins(){
        return wins;
    }
    
    public int getLosses(){
        return losses;
    }

    public String toString()
	{
            StringBuffer B = new StringBuffer();
           B.append(name + "\n");
            B.append(wins + "\n");
            B.append(losses + "\n");			
            return B.toString();
	}
    
     public HangPlayer findPlayer(String title) {
        String current;
        current = title;
        int loopCount=0;    
        int pIndex=0;
        boolean found=true;
    
        for (int i = 0; i < p.size(); i++) {                       
            HangPlayer pyrName = p.get(i);
            
            if ( pyrName.getName().equalsIgnoreCase(currentPlayer)){
                pIndex = i;
                winCount=pyrName.getWins();
                lossCount=pyrName.getLosses();
                found = true;
                loopCount++;
                return p.get(pIndex);
                         
                }else{
                    found = false;
                    pIndex=i;
                    loopCount++;
                }       
            }     
        if(!found){
           HangPlayer newPlayer = new HangPlayer(currentPlayer, winCount, lossCount);
           p.add(newPlayer);
           pIndex++;
           
        }
        return p.get(pIndex);
    }
}
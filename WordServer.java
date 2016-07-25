package hangmanCS401GUI;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class WordServer{

    private String []words;
    private int min=0;
    private int max=0;
    private int count=0;
    private static int countWords =0;
             
    public String [] getWordsArray(){
        return null;
    }
    
    public static int getCountWords(){
        return countWords;
    } 
    
    public String getNextWord() {
        String nothing = "";
        while(count<words.length){
	        String aWord = words[count];
	        count++;
	        countWords++;
	        return aWord; 
        }      
        return nothing;  
    }

    public void loadWords(Scanner fScan) throws IOException{

        max = Integer.parseInt(fScan.nextLine());

        words = new String[max];

        for (int i=0; i<max;i++){

        String loadedWord = fScan.nextLine();

        words[i]=loadedWord;

        }
         shuffleArray(words);
    } 

    public static void shuffleArray(String [] array) {

    	Random rand = new Random();

        for (int i = array.length-1; i > 0; i--) {
        	int index = rand.nextInt(i+1);
        	// Simple swap
        	String a = array[index];
        	String b = array[i];
        	array[index] = b;
        	array[i] = a;
        }
    }  
}




package hangmanCS401GUI;

import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 * University of Pittsburgh
 * CS401 Java Development
 * Fall 2014
 * @author William O'Toole
 * 
 * NOTE: This program is not complete. I decided to not complete it because I have a completed a very similar project hangmanIS1017
 */
public class Assig4 {

	private JMenuBar mBar;
	private JMenu menu;
	private JMenuItem miNewGame, miLoad, miSave, miQuit;


	// Window    
	private JFrame theFrame;

	private buildPanel drawPanel;  // JPanel to "hold" the figure.  See comments in the

	// MyPanel class below

	private JPanel controlPanel, mainInfoPanel, guessPanel;	// JPanel to hold the buttons
	private JPanel infoPanel,infoPanel2;


	private JButton btnGuess, btnQuitWord, btnQuitPlayer;

	// See JTextArea in Java API
	private JLabel lblPlayer,lblWins, lblLosses, lblName, lblNumW, lblNumL;
	private JLabel lblGameStatus,lblStatus, lblTips, lblAtip;
	private static JLabel lblCorrectGuess,lblCArray, lblWrongGuess,lblWArray;
	private JLabel lblGuessCount,lblNumC;

	private JTextArea info;

	private Container thePane;

	private MenuListener mListener;
	private ControlListener cListener;

	// decalring HangPlayer variable for the current player
	private HangPlayer current;
	// declaring an ArrayList to hold all the players
	private static ArrayList<HangPlayer> p;
	// a globla string to hold the current player name
	private static String currentPlayer;
	// global viariables to count the wins and losses
	private static int winCount = 0;

	private static int lossCount = 0;
	// global count to hold the amount of words in the file.
	private static int masterCount = 0;


	private String fileName;
	private HangFigure f;
	private Random R;
	private boolean gameOver;

	private String guesses;
	private int badGuesses;

	private static String gl;
	private static boolean loaded=false;    
	private static boolean wordsLeft = true;    
	private int gamesPlayed=0;
	private static boolean quitGame = false;   
	private static boolean previousGuess = false;
	private static char[] guessedArray=new char[20]; 
	private int SENTENAL = 1;
	private static boolean endGame=false;
	private static StringBuilder tArray;
	private static StringBuilder maskedWord;

	public Assig4() throws IOException {
		final int WINDOW_H = 400;
		final int WINDOW_W = 600;

		// MENU BAR BUILD
		mListener = new MenuListener();
		mBar = new JMenuBar();
		//Build the first menu.
		menu = new JMenu("Game");
		menu.setMnemonic(KeyEvent.VK_G);
		menu.getAccessibleContext().setAccessibleDescription(
				"The only menu in this program that has menu items");
		mBar.add(menu);

		// The group of Menu Items
		// New Game Menu Option
		miNewGame = new JMenuItem("New Game");
		miNewGame.setMnemonic(KeyEvent.VK_N);
		miNewGame.addActionListener(mListener);       
		menu.add(miNewGame);

		// Select File to Load Menu Option
		miLoad = new JMenuItem("Load");
		miNewGame.setMnemonic(KeyEvent.VK_L);
		miLoad.addActionListener(mListener);
		menu.add(miLoad);
		miLoad.setEnabled(false);
		// Save Menu Option
		miSave = new JMenuItem("Save");
		miNewGame.setMnemonic(KeyEvent.VK_S);
		miSave.addActionListener(mListener);
		menu.add(miSave);
		miSave.setEnabled(false);


		// Close Menu Option
		miQuit = new JMenuItem("Quit Program");
		miNewGame.setMnemonic(KeyEvent.VK_Q);
		miQuit.addActionListener(mListener);
		menu.add(miQuit);


		//FRAME BUILD
		//FRAME BUILD          


		theFrame = new JFrame("HangMan Game");
		theFrame.setTitle("HANGMAN GAME"); 

		drawPanel = new buildPanel(300,400);

		theFrame.add(drawPanel, BorderLayout.CENTER);


		theFrame.setVisible(true);  // Make the window visible on the screen.
		theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		theFrame.setJMenuBar(mBar);
		R = new Random();

		controlPanel = new JPanel();
		controlPanel.setLayout(new GridLayout(3, 1));
		controlPanel.setVisible(true);

		mainInfoPanel = new JPanel();
		mainInfoPanel.setLayout(new GridLayout(2, 1));
		infoPanel = new JPanel();
		infoPanel.setLayout(new GridLayout(3, 1));
		infoPanel2 = new JPanel();
		infoPanel2.setLayout(new GridLayout(3,1));

		guessPanel = new JPanel();
		guessPanel.setLayout(new GridLayout(3, 2));
		//	info = new JTextArea();
		//	info.setColumns(40);
		//	infoPanel.add(info);



		lblCorrectGuess = new JLabel("The Word: ",SwingConstants.RIGHT);
		lblWrongGuess = new JLabel("Incorrect Letters: ",SwingConstants.RIGHT);
		lblGuessCount = new JLabel("WRONG GUESSES LEFT: ",SwingConstants.RIGHT);
		lblCArray = new JLabel("",SwingConstants.LEFT);
		lblWArray = new JLabel("",SwingConstants.LEFT);
		lblNumC = new JLabel("",SwingConstants.LEFT);

		guessPanel.add(lblCorrectGuess);
		guessPanel.add(lblCArray);
		guessPanel.add(lblWrongGuess);
		guessPanel.add(lblWArray);
		guessPanel.add(lblGuessCount);
		guessPanel.add(lblNumC);


		lblPlayer = new JLabel("Player: ",SwingConstants.RIGHT);       
		lblWins = new JLabel ("Wins: ",SwingConstants.RIGHT);
		lblLosses = new JLabel("Losses: ",SwingConstants.RIGHT);

		lblName = new JLabel("???",SwingConstants.LEFT);
		lblNumW = new JLabel("?",SwingConstants.LEFT); 
		lblNumL = new JLabel("?",SwingConstants.LEFT);

		infoPanel.add(lblPlayer);
		infoPanel.add(lblName);
		infoPanel.add(lblWins);
		infoPanel.add(lblNumW);
		infoPanel.add(lblLosses);
		infoPanel.add(lblNumL);

		lblGameStatus = new JLabel("Game Status: ");        
		lblStatus = new JLabel("Not Started");      
		lblTips = new JLabel ("Tips: ");
		lblAtip = new JLabel ("Select New Game in the Menu Bar");

		infoPanel2.add(lblGameStatus);
		infoPanel2.add(lblStatus);
		infoPanel2.add(lblTips);
		infoPanel2.add(lblAtip);
		// btnNewWord, btnQuitWord, btnQuitPlayer
		cListener = new ControlListener();
		// BUTTONS
		btnGuess = new JButton("Guess a Letter!");
		btnGuess.addActionListener(cListener);
		controlPanel.add(btnGuess);
		btnGuess.setEnabled(false);

		btnQuitWord = new JButton("Quit Word");
		btnQuitWord.addActionListener(cListener);       
		controlPanel.add(btnQuitWord);
		btnQuitWord.setEnabled(false);


		btnQuitPlayer = new JButton("Quit Player");
		btnQuitPlayer.addActionListener(cListener);
		controlPanel.add(btnQuitPlayer);
		btnQuitPlayer.setEnabled(false);


		mainInfoPanel.add(infoPanel2);
		mainInfoPanel.add(infoPanel);
		theFrame.add(mainInfoPanel, BorderLayout.NORTH);
		theFrame.add(controlPanel, BorderLayout.EAST);
		theFrame.add(drawPanel, BorderLayout.CENTER);
		theFrame.add(guessPanel, BorderLayout.SOUTH);


		infoPanel.setVisible(true);
		infoPanel2.setVisible(true);
		guessPanel.setVisible(true);

		drawPanel.repaint();

		theFrame.setResizable(false);  // Don't let the user resize the window.
		theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // End the program if the user closes the window.
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();  // The width/height of the screen.
		theFrame.setLocation( (screen.width - theFrame.getWidth())/3, 
				(screen.height - theFrame.getHeight())/3 );  // Position window in the center of screen.
		theFrame.setVisible(true);

		theFrame.pack();

		loaded =false;
		loadPlayers();

		checkGame();


		//findPlayer(currentPlayer);

	}

	private void checkGame() {
		//        if(currentPlayer)
		gameOver = false;

		btnGuess.setEnabled(false);
		btnQuitWord.setEnabled(false);
		btnQuitPlayer.setEnabled(false);

		//int index = (int)(Math.random()*wordlist.getWordCount());
		//word = wordlist.removeWord(index);
		//        
		//        word = word.toUpperCase();
		//        message = "The word has " + word.length() + " letters.  Let's play Hangman!";
	}

	//    private boolean wordIsComplete() {
	//        for (int i = 0; i < word.length(); i++) {
	//                char ch = word.charAt(i);
	//                if ( guesses.indexOf(ch) == -1 ) {
	//                        return false;
	//                }
	//        }
	//        return true;
	//    }


	//    public class MyFrame extends JFrame{
	//    
	//        private Container contents;
	//        
	//        public MyFrame(){
	//            contents = getContentPane();  
	//            contents.setLayout(new GridLayout(2,2));
	//        }
	//
	//    }

	class buildPanel extends JPanel
	{
		private int width, height;
		public buildPanel(int w, int h)
		{
			width = w;
			height = h;
		}

		// This method is implicitly called through the JFrame to see how much
		// space the JPanel needs.  Otherwise, the JPanel will be given a default,
		// very small size.
		public Dimension getPreferredSize()
		{
			return new Dimension(width, height);
		}

		// This method is implictly called every time the panel must be refreshed.
		// It will happen automatically if the window is minimized and then opened
		// again, or if it is covered and then exposed.  We can also request that it
		// be done at other times by calling the repaint() method, as shown above.
		public void paintComponent (Graphics g) // Pass the graphics object
		// to the Panel so it can
		// draw its shapes
		{
			super.paintComponent(g);         // don't forget this line!
			Graphics2D g2d = (Graphics2D) g; // don't forget this line either!
			if (f != null)		// Note that we are calling the draw() method in
				f.draw(g2d);	// in the HangFigure class and passing the
			// Graphics2D object into it.  This way, our JPanel
			// does not have to know the specifics of how to draw the
			// figure -- it only has to know that the figure has a
			// draw() method.  See the draw() method in HangFigure for
			// more details.
		}
	}


	private class MenuListener implements ActionListener{

		public void actionPerformed(ActionEvent e){

			if(e.getSource()== miNewGame){
				currentPlayer=JOptionPane.showInputDialog("Please enter your name.");               
				findPlayer(currentPlayer);
				lblAtip.setText("Load Words from the Menu");
				lblAtip.setForeground(Color.RED);
				miLoad.setEnabled(true);
				miNewGame.setEnabled(false);
			}
			if(e.getSource()== miLoad){
				//fileName=JOptionPane.showInputDialog("Please enter file name. \nDefault File Name: consoleHangmanWords.txt");    
				try {
					playGame("consoleHangmanWords.txt");
					//playGame(fileName);
				} catch (IOException ex) {                    
					JOptionPane.showMessageDialog(null, "File Not Found");                       
				}

			}

			if(e.getSource()== miSave){
				saveData();
			}

			if(e.getSource()== miQuit){
				int reply = JOptionPane.showConfirmDialog(null,
						"Do you really want to give up?", "Quit", JOptionPane.YES_NO_OPTION);
				if (reply == JOptionPane.YES_OPTION){
					//saveData();
					System.exit(0);               
				}   
			}                
		}
	}

	private class ControlListener implements ActionListener {
		// btnNewWord, btnQuitWord, btnQuitPlayer
		public void actionPerformed(ActionEvent e)
		{

			if(e.getSource()== btnGuess){
				gl=JOptionPane.showInputDialog("GUESS A LETTER!");    
			}

			if(e.getSource()== btnQuitWord){
				int reply = JOptionPane.showConfirmDialog(null,
						"Quit this Word?", "Quit", JOptionPane.YES_NO_OPTION);
				if (reply == JOptionPane.YES_OPTION){

				}       
			}

			if(e.getSource()== btnQuitPlayer){
				int reply = JOptionPane.showConfirmDialog(null,
						"Quit this Player?", "Quit", JOptionPane.YES_NO_OPTION);
				if (reply == JOptionPane.YES_OPTION){

				}                    
			}    
		}
	}

	public void playGame(String fName) throws IOException{
		File file = new File("consoleHangmanWords.txt");  
		//File file = new File(fName);
		WordServer w = new WordServer();

		Scanner fScan = new Scanner(file);

		w.loadWords(fScan);

		if(wordsLeft){
			btnGuess.setEnabled(true); 
			btnQuitWord.setEnabled(true);
			btnQuitPlayer.setEnabled(true);

			String word = w.getNextWord();
			System.out.println(word);
			subGame(word);

		}else{
			JOptionPane.showMessageDialog(null, "NO MORE WORDS LEFT!");
			lblStatus.setText("NO MORE WORDS LEFT!");
		}    

	}
	public static void subGame(String w){

		int actionCounter=0;
		int wrongCount=0;
		boolean guessing= true;  

		String word = w;
		//System.out.println(word);
		char[] wordArray = word.toCharArray();
		char [] copyArray = copyArray(word);
		char [] guessArray = new char[word.length()+8];
		char mask = '_';       
		Arrays.fill(copyArray,mask);
		Arrays.fill(guessedArray, '-');
		JOptionPane.showMessageDialog(null, "Here is your word"); 

		while(guessing){

			boolean contains = false;
			tArray = new StringBuilder(Arrays.toString(guessedArray));
			maskedWord = new StringBuilder(Arrays.toString(copyArray));
			lblCArray.setText(String.valueOf(maskedWord)); 
			lblWArray.setText(String.valueOf(tArray));

			char a = gl.charAt(0);
			char c = Character.toUpperCase(a);			

			if(c=='1'){
				quitGame=false;
				if(actionCounter==0){
					break;    
				}else{
					lossCount++;
					break;   
				}

			}
			searchGuess(guessArray,c);
			if(previousGuess){
				JOptionPane.showMessageDialog(null,"Already Guessed that Letter!");
			}else{
				actionCounter++;
				guessArray[actionCounter]=c;
				for(int i=0; i< wordArray.length; i++){

					char val = wordArray[i];    
					if (c == val) {                     
						copyArray[i] = c;
						contains = true; 
						lblCArray.setText(String.valueOf(maskedWord)); 
						lblWArray.setText(String.valueOf(tArray));
					}          
				}
				if(contains){
					if(Arrays.equals(copyArray, wordArray)){
						guessing=false;
						winCount++;                      
						Arrays.fill(guessedArray, '-');                     
					}
				}else{
					if(wrongCount>5){
						guessing = false;
						lossCount++;

						Arrays.fill(guessedArray, '-');

					}else{

						wrongCount++;
					}    
				}               
			}           
		}         
	}
	public static char [] copyArray(String c){
		String a=c;
		char [] copyArray = new char [a.length()];        
		return copyArray;       
	}
	public static void searchGuess(char [] a, char c){ 

		boolean contains = false;      
		guessedArray = a;

		for(int i=0; i< a.length; i++){
			char val = a[i];            
			if (c == val) {                     
				guessedArray[i] = c;
				contains = true;                       
			}          
		}
		if(contains){

			previousGuess = true;    
		}else{
			previousGuess =false;
		} 

	}


	private void loadPlayers() throws IOException {

		Scanner scan = new Scanner(new FileInputStream("consoleHangmanPlayers.txt"));        
		p = new ArrayList<>();

		while(scan.hasNextLine()){
			String plyrName = scan.nextLine();          
			int plyrWins = Integer.parseInt(scan.nextLine());
			int plyrLosses = Integer.parseInt(scan.nextLine());           
			HangPlayer temp = new HangPlayer(plyrName,plyrWins,plyrLosses);             
			p.add(temp);
		}
	}

	public HangPlayer findPlayer(String cp) {
		currentPlayer = cp;    
		int loopCount = 0;    
		int pIndex = 0;
		boolean found=true;

		for (int i = 0; i < p.size(); i++) {                       
			HangPlayer pyrName = p.get(i);           
			if ( pyrName.getName().equalsIgnoreCase(cp)){
				pIndex = i;
				winCount=pyrName.getWins();
				lossCount=pyrName.getLosses();
				found = true;
				lblName.setText(currentPlayer);
				lblNumW.setText(String.valueOf(winCount));
				lblNumL.setText(String.valueOf(lossCount));
				loopCount++;
				break;         
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
			lblName.setText(currentPlayer);
			lblNumW.setText(String.valueOf(winCount));
			lblNumL.setText(String.valueOf(lossCount));
		}
		return current;
	}

	private void saveData()
	{
		try
		{       
			PrintWriter P = new PrintWriter("consoleHangmanPlayers.txt");
			for(int i =0; i<p.size(); i++){
				HangPlayer player =p.get(i);

				P.println(player.getName());
				P.println(player.getWins());
				P.println(player.getLosses());
			}
			P.close();
		}
		catch (IOException e){
			System.out.println("Error File consoleHangmanPlayers.txt ");
		}      
	}  


	public static void main(String[] args) throws IOException {
		new Assig4();
	} 

}
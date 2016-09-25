package Jeopardy_GUI;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


public class Play {
	private  String[] categories = new String[5]; 
	private  int[] points = new int[5];
	private	Question[][] board = new Question[5][5];
	private int fjCount = 0;
	private int normalQCount = 0;
	private int questionAnsweredCount = 0;
	private finalQuestion fjQ; 
	private Team[] teams;
	private int[] bets; //money betting index match each team
	private int teamNum;
	private boolean fileCorrect;
	
	
	
	
	public Play(String filename, int numTeam, String[] teamName){
		teamNum = numTeam;
		if(openFile(filename)){
			teamSetup( teamNum, teamName);
			//gameStart();
		}
		//else tell them the file is not parsed
	}
	
	public Play(String filename){
		fileCorrect = openFile(filename);
	}
	
	//getter's
	public Team[] getTeams(){
		return teams;
	}
	
	public finalQuestion getFinalQ(){
		return fjQ;
	}
	
	public Question[][] getQuestionsData(){
		return board;
	}
	
	public int[] getPoints(){
		return points;
	}
	
	public String[] getCategories(){
		return categories;
	}
	
	public boolean checkFormat(){
		return fileCorrect;
	}
	
	
	
	private boolean openFile(String filename){
		FileReader fr = null;
		boolean opened = false;
		try{
			fr = new FileReader(filename);
			opened = parse(fr);
		}catch(FileNotFoundException fnfe){
			System.out.println("FileNotFoundException: " + fnfe.getMessage());
			return false;
		}finally{
			if(fr != null){

				try{
					fr.close();
				}catch(IOException ioe){
					System.out.println("IOException: " + ioe.getMessage());
					return false;
				}
			}
		}
		
		return opened;
	}
	
	private boolean parse(FileReader fr){
		BufferedReader br = null;
		boolean parsed = false;
		try{
			br = new BufferedReader(fr);
			//get categories
			String line;
			line = br.readLine();
			String delims = "[:]+";
			categories = line.split(delims);
			arrayTakeOffSpace(categories);
			//get points
			line = br.readLine();
			String[] buffStr = line.split(delims);
			//translate it into int
			if(buffStr.length != 5) return false;
			for(int i = 0 ; i < buffStr.length; i++){
				int num = Integer.parseInt(buffStr[i]);
				points[i] = num;
			}
			parsed = parseQuestion(br);
			
		}catch(IOException ioe){
			System.out.println("IOException: " + ioe.getMessage());
			return false;
		}finally{
			if(br != null){
				try{
					br.close();
				}catch(IOException ioe){
					System.out.println("IOException: " + ioe.getMessage());
				}
			}
		}
		
		return parsed;
	}
	
	private boolean parseQuestion(BufferedReader br){
		String line;
		String delims = "[:]+";
		try{
			while((line = br.readLine())!=null){
				String input[] = line.split(delims);
				if(input.length <4 ){ //
					System.out.println("At lease one question format not correct");
					System.exit(0); //eject if format not correct aka no q and ans
				}
				input[1] = stringMinusSpace(input[1]);
				input[2] = stringMinusSpace(input[2]);
				String category = input[1];
				String points = input[2];
				
				//fj check
				if(category.equalsIgnoreCase("FJ")){
					boolean fjValid = processFJ(category);
					if(fjValid){
						String Q = input[2];
						String A = input[3];
						fjQ = new finalQuestion(0, Q, A);
					}
					else{
						return false;
					}
				}
				else{
					//process other questions 
					boolean categoryValid = categoryCheck(category);
					boolean pointValid = pointsCheck(points);
					if(input.length!=5){
						System.out.println("At lease one question format not correct");
						System.exit(0); //eject if format not correct aka no q and ans
					}
					
					if(pointValid && categoryValid){
						String Q = input[3];
						String A = input[4];
						int pointInt = Integer.parseInt(points);
						int catIdx = findCatIndex(category);
						int ptIdx = findPointIndex(pointInt);
						if(board[catIdx][ptIdx]== null){
							normalQCount ++;
							Question q = new Question(pointInt,Q,A);
							board[catIdx][ptIdx] = q;
						}
						else{
							System.out.println("Error: duplicate points of a question in a category");
							return false;
						}
						
					}
					else{
						//message typed in the helper function already
						return false;
					}
				}			
				
			}
			//final check num of Q fjQ 
			return checkNumQuestions();
			

		}catch(IOException ioe){
			System.out.println("Error: Did not parse successfully.");
			return false;
		}
	}
	
	private String stringMinusSpace(String s){
		String noSpace = "";
		for(int i=0; i < s.length(); i++){
			char c = s.charAt(i);
			if(c!=32) noSpace = noSpace + c; //32 is space
		}
		return noSpace;
	}
	
	private void arrayTakeOffSpace(String[] arry){
		for(int i = 0 ; i < arry.length; i++){
			arry[i] = stringMinusSpace(arry[i]);
		}
	}
	
	private boolean checkNumQuestions(){
		boolean rightNum = (normalQCount == 25 && fjCount == 1 );
		if(rightNum) return true;
		else{
			System.out.println("The number of question is not correct." );
			return false;
		}
	}
	
	private int findCatIndex(String s){
		for(int i = 0; i <categories.length; i++){
			if(categories[i].equalsIgnoreCase(s)){
				return i ; 
			}
		}
		return -1;
	}
	
	private int findPointIndex(int p){
		for(int i = 0 ; i <points.length; i++){
			if(points[i]==p){
				return i;
			}
		}
		return -1;
	}
	
	private boolean categoryCheck(String category){
		boolean foundCategory = false;
		if(findCatIndex(category)!=-1){
			foundCategory = true;
		}
		//check category
		if(!foundCategory){
			System.out.println("Error: some category does not exist in the database");
			System.exit(0);
		}
		
		
		return true;
		
	}
	
	
	
	private boolean pointsCheck(String point){
		//check if it is numerical
		boolean isNumber = true;	
		for (int i = 0; i < point.length(); i++) {
	            if(point.charAt(i) < 48 || point.charAt(i) > 57) {
	                isNumber = false;
	                break;
	            }
	    }
				
		if(!isNumber){
			System.out.println("Input file format not correct: not numerical val second column in the text file");
			System.exit(0);
			
		}
		//check if points exist in the database
		int pointsInLine = Integer.parseInt(point);
		if(findPointIndex(pointsInLine)!= -1){
			return true;
		}
		System.out.println("Error: question's point val not existed in the database");
		System.exit(0);
		return false;
		
		
	}
	
	private boolean processFJ(String category){
		if(fjCount == 0){
			fjCount ++;
			return true;
		}
		else{
			System.out.println("Error: More than one fj in the textfile");
			return false;
		}
	}
	
	
	private void teamSetup(int teamNum, String[] teamName){	
		teams = new Team[teamNum];
		bets = new int[teams.length];
		for(int i = 0 ; i < teamNum; i ++){
			teams[i] = new Team(teamName[i]);
		}
		
		
		
	}
	private void gameStart(){
		//choose a random team and start playing
		Random randomGenerator = new Random();
		int ranNum = randomGenerator.nextInt(teamNum);
		System.out.println("The team to go first will be " + teams[ranNum].getName());
		gameHelper(ranNum); //process all 25 questions until it's over
		finalRound();
		printFinalWinner();
		
		
	}
	
	private void readReplayCommand(String command){
		boolean replay = command.equalsIgnoreCase("replay");
		if(replay){
			System.out.println("**Restart the game with the same teams");
			resetTheGame();
			gameStart();
		}
	}
	
	public void resetTheGame(){
		//clean up the old data
		fjCount = 0;
		questionAnsweredCount = 0;
		for(int i = 0 ; i< 5; i++){
			for(int j = 0 ; j < 5 ; j++){
				board[i][j].reset();
			}
		}
		for(int i = 0 ; i < teamNum; i ++){
			teams[i].reset();
			bets[i] = 0; 
		}
	}
	
	private int getPositiveInt(){
		Scanner sc =  new Scanner(System.in);
		int num = 0;
		do{
			if(sc.hasNextInt()){ // if it is an int don't for sure it's not "exit" 
				num = sc.nextInt();
				if(num<=0){
					System.out.println("Please give a valid postive integer");
				}
				sc.nextLine(); // get rid of the return char
			}
			else{
				String command = sc.nextLine();
				readExitCommand(command);
				readReplayCommand(command);
				System.out.println("Please give a valid postive integer");
			}
		} while(num<=0);		
		return num;

	}
	
	private int getCategory(){
		System.out.print(" | ");
		for(int i = 0 ; i < categories.length; i++){
			System.out.print(categories[i] + " | " );
		}
		Scanner sc = new Scanner(System.in);
		int categoryIdx = -1;
		do{
			String category = sc.nextLine();
			readExitCommand(category);
			readReplayCommand(category);
			categoryIdx = findCatIndex(category);
			if(categoryIdx == -1) System.out.println("Cannot find that category try again.");
		}while(categoryIdx == -1);
		
		
		return categoryIdx; 
	}
	
	private int getPointsIndex(){
		Scanner sc = new Scanner(System.in);
		System.out.print(" | ");
		for(int i = 0 ; i < points.length; i++){
			System.out.print(points[i] + " | " );
		}
		int pointIdx = -1;
		do{	
			if(sc.hasNextInt()){ // if it is an int don't for sure it's not "exit" 
				int point  = sc.nextInt();
				if(point <=0){
					System.out.println("Please give a valid postive integer");
				}
				sc.nextLine(); // get rid of the return char
				pointIdx = findPointIndex(point);
			}
			else{
				String command = sc.nextLine();
				readExitCommand(command);
				readReplayCommand(command);
			}
		
			if(pointIdx==-1) System.out.println("Cannot find such dollar value in the database. Try again");
		}while(pointIdx == -1);
		return pointIdx;
	}
	
	private void gameHelper(int ranNum){
		//base case when the game is over move on to final jeopardy question!!
		if(questionAnsweredCount>=25){
			return;
		}
		System.out.println("It is " + teams[ranNum].getName() + "'s turn to choose a question");
		//find category
		System.out.println("Please choose a category you would like to answer: ");
		int categoryIdx  = getCategory();
		//find points
		System.out.println("Please enter the dollar value of the question you wish to answer");
		int pointIdx = getPointsIndex();
		
		Scanner sc = new Scanner(System.in);
		//now check if the question is answer knowing dollar and cat is valid
		boolean answered = board[categoryIdx][pointIdx].answeredYet();
		if(!answered){
			int point = points[pointIdx];
			System.out.println(board[categoryIdx][pointIdx].getQeustion());
			System.out.println("Enter your answer. Remember to pose it as a question.");
			String ans = sc.nextLine();
			readExitCommand(ans); // check if the user wants to exit any point
			readReplayCommand(ans);
			board[categoryIdx][pointIdx].answerQuestion(ans); //ask Q
			if(!board[categoryIdx][pointIdx].getRight()&&board[categoryIdx][pointIdx].getWarned()){
				//not in question format. give one more chance
				ans = sc.nextLine();
				readExitCommand(ans);
				readReplayCommand(ans);
				board[categoryIdx][pointIdx].answerQuestion(ans);
			}
			if(board[categoryIdx][pointIdx].getRight()){
				System.out.println("You answered it right! " + point + " will be added to your total");
				questionAnsweredCount+= 10;
				teams[ranNum].addDollars(point);
				printDollars();
			}
			else{
				System.out.println("Wrong Answer!");
				questionAnsweredCount++; 
				teams[ranNum].minusDollars(point);
				printDollars();
			}
		}
		else{
			System.out.println("Question already answered. Choose it again");
			gameHelper(ranNum); //same team choose a question again
		}
		int nextIdx = (ranNum+1)%(teams.length);
		gameHelper(nextIdx);
	}
	
	
	private void printDollars(){
		System.out.println("Here are the updated scores");
		System.out.println("-----------------------------------------------------");
		for(int i = 0 ; i < teams.length; i++){
			System.out.print("|\t" + teams[i].getName() + "\t");
		}
		System.out.println("|");
		for(int i = 0 ; i < teams.length; i++){
			System.out.print("|\t" + teams[i].getDollars() + "\t");
		}
		System.out.println("|");
		System.out.println("-----------------------------------------------------");
		
	}
		
	private void finalRound(){
		System.out.println("Now that all the questions have been chosen. It is time for final"
				+ " Jeopardy");
		for(int i = 0 ; i < teams.length; i++){
			//iterate thru each team
			if(teams[i].getDollars() >0){ //eligible to bet 
				bets[i] = getBet(teams[i], i);
			}
			else{ // if it is not eligible make it 0 by default
				System.out.println("Team " + teams[i].getName() + ", you DO NOT have money to bet so we have to"
						+ " SKIP you.");
				bets[i] = 0;
			}
		}
		finalQuestionAsk();
	}
	
	public ArrayList<Integer> getFinalCandidates(){
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i = 0 ; i < teams.length; i ++){
			if(teams[i].getDollars()>0){
				list.add(i);
			}
		}
		
		return list;
	}
	
	private int getBet(Team t, int teamIdx){
		System.out.println("Team " + t.getName() + ", please give give a dollar amount from your total that"
				+ "you would like to bet");
		int bet = 0;
		do{
			bet = getPositiveInt();
			if(bet > t.getDollars()) System.out.println("Your total is less than your bet. Try again.");
		}while(bet > t.getDollars());
		return bet;
	}
	
	private void finalQuestionAsk(){
		System.out.print("The question is:   ");
		System.out.println(fjQ.getQeustion());
		boolean[] right = new boolean[teams.length];
		Scanner sc = new Scanner(System.in);
		for(int i = 0 ; i < teams.length; i++){
			right[i] =false; 
			if(bets[i]!=0){ //bets index match with the team index
				System.out.println("Team " + teams[i].getName() + ", please enter your answer.");
				String ans = sc.nextLine();
				readExitCommand(ans);
				readReplayCommand(ans);
				boolean getRight = fjQ.answerFJQuestion(ans);
				if(getRight){
					right[i] = true;
					teams[i].addDollars(bets[i]);
				}
				else{
					teams[i].minusDollars(bets[i]);
				}
			}
		}
		//display who got it right
		//you want to display at the end
		for(int i = 0 ; i < right.length; i++){
			if(right[i]){
				System.out.println("Team " + teams[i].getName() + " got it CORRECT");
			}
			
		}
	}
	
	private void readExitCommand(String command){
		boolean exit = command.equalsIgnoreCase("exit");
		if(exit)  System.exit(0);
	}
	
	private void printFinalWinner(){
		ArrayList<Integer> winners = new ArrayList<Integer>();
		int max = teams[0].getDollars(); 
		//find max
		for(int i=0; i < teams.length; i++){
			if(teams[i].getDollars() > max){
				max = teams[i].getDollars();
			}
		}
		
		if(max >0){
			for(int i = 0 ; i < teams.length; i++){
				if(max == teams[i].getDollars()){ //add the winner team index in
					winners.add(i);
				}
			}
			System.out.print("And the winner");
			if(winners.size()>1) System.out.print("s are ");
			else System.out.print(" is ");
			
			for(int i = 0 ; i < winners.size(); i++){
				System.out.print(teams[winners.get(i)].getName() + " ");
			}
		}else{
			System.out.println("No one wins the game.");
		}
		
		System.exit(0);
		
	}
	
	public ArrayList<Integer> getWinners(){
		ArrayList<Integer> winners = new ArrayList<Integer>();
		int max = teams[0].getDollars(); 
		//find max
		for(int i=0; i < teams.length; i++){
			if(teams[i].getDollars() > max){
				max = teams[i].getDollars();
			}
		}
		if(max>0){
			for(int i = 0 ; i < teams.length; i++){
				if(max == teams[i].getDollars()){ //add the winner team index in
					winners.add(i);
				}
			}
		}
		
		return winners;  //winners will be empty if no one wins
	}
	
	
	
}


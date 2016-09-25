package Jeopardy_GUI;

public class Question {
	int points;
	String question;
	String answer;
	boolean asked = false;
	boolean warned = false;
	boolean gotRight = false;
	boolean correctFormat = false;
	String[] w = new String[] {"who", "what", "where", "when", "which"};
	String[] v = new String[] {"is", "are", "am" , "was" , "were"};
	String ansArray[];
	int mistakeMade = 0;
	public Question(int points, String question, String answer){
		this.points = points;
		this.question = question;
		this.answer = answer;
	}
	
	public boolean getRight(){
		return gotRight;
	}
	
	public boolean correctFormat(){
		return correctFormat;
	}
	
	public boolean getWarned(){
		return warned;
	}
	public void reset(){
		gotRight = false;
		warned = false;
		asked = false;
	}
	
	public void setPoints(int newPoint){
		points = newPoint;
	}
	public int getPoints(){
		return points;
	}
	public String getQeustion(){
		return question;
	}
	public void answerQuestion(String ans){
		asked = true;
		//stub for format what/who/where is/are
		String delims = "[ ]+";
		ansArray = ans.split(delims);
		String[] rightAnswerArry = answer.split(delims);
		if(checkQuestionFormat(ansArray)){
			correctFormat = true;
			if(checkKeywords(ansArray, rightAnswerArry)){
				gotRight = true;
			}
			//if pass the format but key words fail then that means it just got wrong
			return;
		}
		else{
			//only get warned when didn't pass the question format first time.
			mistakeMade++;
			if(mistakeMade == 2){
				warned = true;
				//System.out.println("Please phrase the answer in a question form. You got one more chance.");
				return;
			}
		}
		
	
		
		
	}
	
	public boolean checkKeywords(String[] ansArray, String[] rightAnswerArry){
		String userAns ="";
		String rightAns ="";
		for(int i = 2; i < ansArray.length; i++){ //skip the first two words
			userAns += ansArray[i] + " ";
		}
		for(int j = 0 ; j < rightAnswerArry.length; j++){
			rightAns += rightAnswerArry[j] + " ";
		}

		return (userAns.equalsIgnoreCase(rightAns));
	}
	
	public boolean checkQuestionFormat(String[] ansArray){
		for(int i = 0 ; i < w.length; i++){
			if(w[i].equalsIgnoreCase(ansArray[0])){
				for(int j = 0 ; j< v.length; j++){
					if(v[j].equalsIgnoreCase(ansArray[1])){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public String getAnswer(){
		return answer;
	}
	
	public boolean answeredYet(){
		return asked;
	}
}

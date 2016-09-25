package Jeopardy_GUI;

public class finalQuestion extends Question{
	public finalQuestion(int points, String question, String answer){
		super(points, question,  answer);
	}
	
	
	public boolean answerFJQuestion(String ans){
		String delims = "[ ]+";
		ansArray = ans.split(delims);
		String[] rightAnswerArry = answer.split(delims);
		if(checkQuestionFormat(ansArray)){
			if(checkKeywords(ansArray, rightAnswerArry)){
				return true;
			}
		}
		return false;
	}
}

package Jeopardy_GUI;
public class Team {
	private int dollars = 0; //zero by default
	private String name;
	public Team(String name){
		dollars = 0;
		this.name = name;
		
	}
	
	public void reset(){
		dollars = 0;
	}
	public String getName(){
		return name;
	}
	
	public int getDollars(){
		return dollars;
	}
	
	public void addDollars(int addition){
		dollars += addition;
	}
	
	public void minusDollars(int subtraction){
		dollars -= subtraction;
	}
	
	public void setDollars(int newDollars){
		dollars = newDollars;
	}
}	


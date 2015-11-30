package client;



import java.util.Vector;

public class Post {
	private String recipeName, username;
	private int numberOfReheats;
	private String[] instructionList;
	private String[] ingredientList;
	private String date;
	public Post(String recipeName, String username, int numberOfReheats, String [] ingredientList, String[]instructionList, String date) {
		this.date = date;
		this.recipeName = recipeName;
		this.username = username;
		//or is it = number of reheats?
		this.numberOfReheats = numberOfReheats;
		this.instructionList = instructionList;
		this.ingredientList = ingredientList;
		
	}
	public String getDate() {
		
		return date;
	}
	public String getUsername() { 
		return username;
	}
	public String getName() { 
		return recipeName;
	}
	public String[] getIngredientList() {
		return ingredientList;
	}
	public String[] getInstructionList() {
		return instructionList;
	}
	public int getNumberOfReheats() {
		return numberOfReheats;
	}
	public String getParsedDate(){
		boolean AM = true;
		String year = date.substring(0, 4);
		String month = date.substring(4,6);
		String day = date.substring(6, 8);
		String hour = date.substring(8, 10);
		int hourInt = Integer.parseInt(hour);
		if(hourInt>12){
			hourInt -= 12;
			hour = hourInt + "";
			AM = false;
			
		}
		String minute = date.substring(10, 12);
		String total = month + " "+ day + ", " + year + " " + hour + ":" + minute;
		if(AM == true){
			total +=" PM";
		}
		else{
			total += " AM";
		}
		System.out.println(month);
		return total;
	}
}

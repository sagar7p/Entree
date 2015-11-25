package profile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import client.Post;
import server.SQLDriver;

public class PersonalPage {
	//main data 
	private ArrayList<String> groceryList;
	private ArrayList<String> followersList;
	private ArrayList<String> followingList;
	private ArrayList<Post> recipeList;
	private String username;
	private SQLDriver sqlD;
	private String firstname;
	private String lastname;
	private String food;
	public int numOfPosts = 0;
	
	
	public PersonalPage(String user) {	
		sqlD = new SQLDriver();
		this.username = user;
		recipeList = new ArrayList<Post>();
		sqlD.connect();
		String[] recipes = sqlD.addItem(user,null,1);
		String [] followers = sqlD.addItem(user,null,2);
		String [] following = sqlD.addItem(user,null,3);
		String [] groceries = sqlD.addItem(user,null,4);
		if (recipes != null) {
			for(int i = 0; i < recipes.length; i++) {			
				recipeList.add(sqlD.getRecipe(recipes[i]));
			}
		}
		if(followers != null) {
			followersList = new ArrayList<String>(Arrays.asList(sqlD.addItem(user,null,2)));
		}
		else {
			followersList = new ArrayList<String>();
		}
		if(following != null) {
			followingList = new ArrayList<String>(Arrays.asList(sqlD.addItem(user,null,3)));
		}
		else {
			followingList = new ArrayList<String>();
		}
		if(groceries != null) {
			groceryList = new ArrayList<String>(Arrays.asList(sqlD.addItem(user,null,4)));
		}
		else {
			groceryList = new ArrayList<String>();
		}
		
		String [] data = sqlD.getUserInfo(user);
		if(data != null) {
			firstname = data[0];
			lastname = data[1];
			food = data[2];
		}
		
		sqlD.stop();
		
	}
	
	public void createPost(Post p) {
		sqlD.connect();
		recipeList.add(p);
		sqlD.addRecipe(p.getName(), p.getUsername(), p.getIngredientList(), p.getInstructionList(), p.getNumberOfReheats(), false, p.getDate());
		sqlD.stop();
	}
	
	//GUI population

	public String getUsername() {
		// TODO Auto-generated method stub
		return username;
	}
	
	public Deque<Post> searchRecipe(String s) {
//		return SEARCHPOSTS(); !(*%#^&@($
		return null;
	}
	public List<String> getGroceryList() {
		return groceryList;
	}
	public List<String> getFollowers() {
		return followersList;
	}
	public List<String> getFollowing() {
		return followingList;
	}
	public List<Post> getPosts() {
		return recipeList;
	}
	public int getNumPosts() {
		return recipeList.size();
	}
	public int getNumFollowing() {
		return followersList.size();
	}
	public Post getPostByName(String name) {
		for(int i = 0; i < recipeList.size(); i++) {
			if(recipeList.get(i).getName().equals(name))
				return recipeList.get(i);
		}
		return null;
	}
	public String getFavoriteFood() {
		return food;
	}
	public String getFullName() {
		return firstname + " " + lastname;
	}
	public void addRecipe(Post post) {
		recipeList.add(post);
	}
	
	
	public Post getNumOfPosts(Vector<PersonalPage> allProfiles) {
		sqlD.connect();
		String[] recipes = sqlD.addItem(username,null,1);
		if(recipeList.size() < recipes.length) {
			Post post = sqlD.getRecipe(recipes[recipes.length - 1]);
			sqlD.stop();
			recipeList.add(post);
			return post;
		}
		List<String> followers = getFollowing();
		if (!followers.isEmpty()) {	
			for (int i = 0; i < followers.size(); i++) {
				recipes = sqlD.addItem(followers.get(i),null,1);
				if(allProfiles.get(i).getPosts().size() < recipes.length) {
					Post post = sqlD.getRecipe(recipes[recipes.length - 1]);
					allProfiles.get(i).addRecipe(post);
					sqlD.stop();
					return post;
				}
			}
		}
		sqlD.stop();
		return null;
	}


}

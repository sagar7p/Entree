package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.mysql.jdbc.Driver;

import client.Post;
import profile.PersonalPage;



public class SQLDriver {
	private Connection con;
	private final static String addRecipe = "INSERT INTO Recipes(Title, Username, Ingredients,Instructions,Reheats,Featured,Timeslot) VALUES(?,?,?,?,?,?,?)";
	private final static String getRecipe = "SELECT * FROM Recipes WHERE Title=?";
	private final static String addHeat = "UPDATE Recipes SET Reheats=Reheats+1 WHERE Title=?";
	private final static String addUser = "INSERT INTO Users(Username,Email,Pass,Food,Firstname,Lastname) VALUES(?,?,?,?,?,?)";
	private final static String getUserData = "SELECT * FROM Users WHERE Username=?";
	private final static String addRecipeToUser = "UPDATE Users SET Recipes=? WHERE Username=?";
	private final static String addFollowerToUser = "UPDATE Users SET Followers=? WHERE Username=?";
	private final static String addFollowingToUser = "UPDATE Users SET Following=? WHERE Username=?";
	private final static String addGroceryItem = "UPDATE Users SET Grocery=? WHERE Username=?";
	private final static String getFeaturedRecipes = "SELECT * FROM Recipes WHERE Featured=TRUE";
	private final static String doneShopping = "UPDATE Users SET Grocery=? WHERE Username=?";
	private final static String getAllRecipes = "SELECT * FROM Recipes";
	private final static String getAllUsers = "SELECT * FROM Users";
	

	

	
	
	public SQLDriver() {
		try {
			new Driver();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void connect() {
		try {
//           con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Entree?user=root&password=");
			con = DriverManager.getConnection("jdbc:mysql://entree-repo.cqemh2gaogyy.us-west-2.rds.amazonaws.com:3306/Entree?user=kevin&password=pass");
		} catch (SQLException e) {
			System.out.println("Could not connect");
			try {
				con = DriverManager.getConnection("jdbc:mysql://entree-repo.cqemh2gaogyy.us-west-2.rds.amazonaws.com:3306/Entree?user=kevin&password=pass");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
 	}
	
	public void stop() {
		try {con.close();} catch (SQLException e) {e.printStackTrace();}
	}
	
	//add recipe to database
	public void addRecipe(String title, String user, String[] ingredients, String[] instructions, int reheats, boolean featured, String time) {
		try {
			String updatedIngredients = Arrays.toString(ingredients);
			updatedIngredients = updatedIngredients.substring(1, updatedIngredients.length()-1);
			String updatedInstructions = "";
			for(int i = 0; i < instructions.length; i++) {
				if(i != (instructions.length - 1)) {
					updatedInstructions += instructions[i] + "|";
				}
				else {
					updatedInstructions += instructions[i];
				}
			}
			PreparedStatement ps = con.prepareStatement(addRecipe);
			ps.setString(1, title);
			ps.setString(2, user);
			ps.setString(3, updatedIngredients);
			ps.setString(4, updatedInstructions);
			ps.setInt(5, reheats);
			ps.setBoolean(6, featured);
			ps.setString(7, time);
			ps.executeUpdate();
			addItem(user, title, 1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//get an actual Post Object
	public Post getRecipe(String name) {
		try {
			PreparedStatement ps = con.prepareStatement(getRecipe);
			ps.setString(1, name);
			ResultSet set = ps.executeQuery();
			if(set.next()) {
				String [] ingredients = set.getString(3).split("[,]");
				String [] instructions = set.getString(4).split("[|]");
				Post recipe = new Post(set.getString(1), set.getString(2), set.getInt(5), ingredients,instructions,set.getString(7));
				return recipe;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;		
	}
	
	//add user to database
	public void addUser(String username, String email, String pass, String food, String first, String last) {
		try {
			int hash = pass.hashCode();
			PreparedStatement ps = con.prepareStatement(addUser);
			ps.setString(1, username);
			ps.setString(2, email);
			ps.setInt(3, hash);
			ps.setString(4, food);
			ps.setString(5, first);
			ps.setString(6, last);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//add heat to the database
	public void addHeat(String Recipe, String user) {
		try {
			System.out.println("Heat added");
			PreparedStatement ps = con.prepareStatement(addHeat);
			ps.setString(1, Recipe);
			addItem(user,Recipe,1);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	private ResultSet getUserData(String user) {
		try {
			PreparedStatement ps = con.prepareStatement(getUserData);
			ps.setString(1, user);
			ResultSet result = ps.executeQuery();
			if(result.next())
				return result;
		} catch(SQLException e) {e.printStackTrace();}
		return null;
	}
	
	public String[] getUserInfo(String user) {
		ResultSet set = getUserData(user);
		if(set != null) {
			String[] data = null;
			try {
				data = new String[]{set.getString(9),set.getString(10),set.getString(4)};
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return data;
		}
		return null;
	}
	
	public String[] addItem(String username, String item, int type) {
		ResultSet set = getUserData(username);
		String sql = null;
		if(type == 1) {
			sql = addRecipeToUser;
		}
		else if(type == 2) {
			sql = addFollowerToUser;
		}
		else if(type == 3) {
			sql = addFollowingToUser;
		}
		else if(type == 4){
			sql = addGroceryItem;
		}
		try {
			//find user and add to his list
			if(set != null) {
				if(item != null && sql != null) {
					PreparedStatement ps2 = con.prepareStatement(sql);
					ps2.setString(2, username);
					String list = set.getString(type + 4);
					if(list == null)
						list = "";			
					
					if(list.indexOf(0) == ' ') {
						list = list.substring(1);
					}
					if(list.indexOf(list.length() -1) == ' ') {
						list = list.substring(0,list.length() -1);
					}
					if(!list.toLowerCase().contains(item.toLowerCase())) {
						if(list == "")
							list = item;
						else
							list += ","+item;
						ps2.setString(1, list);	
						ps2.executeUpdate();
						if(type == 3) {
							System.out.println("Adding new item");
							addItem(item,username,2);
						}
					}
				}
				String list = set.getString(type + 4);

				if(list != null) {
					if(list.indexOf(',') == -1) {
						System.out.println(list);
						return new String[]{list};
					}
					String [] all = list.split("[,]");
					return all;
				}
			}
			
		} catch(SQLException e) {e.printStackTrace();}
		return null;
	}
	
	//done shopping clear grocery list
	public void doneShopping(String username){
		try {
			PreparedStatement ps = con.prepareStatement(doneShopping);
			ps.setString(1,"");
			ps.setString(2, username);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	//authenticate
	public boolean authenticate(String username, String password) {
		try {
			PreparedStatement ps = con.prepareStatement(getUserData);
			ps.setString(1, username);
			ResultSet set = ps.executeQuery();
			if(set.next()) {
				int hash = password.hashCode();
				if(set.getInt(3) == hash)
					return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;	
	}

	//get all featured
	public String[] getAllFeaturedRecipes() {
		try {
			PreparedStatement ps = con.prepareStatement(getFeaturedRecipes);
			ResultSet set = ps.executeQuery();
			String[] recipes = new String[25];
			int counter = 0;
			while(set.next()) {
				recipes[counter] = set.getString(1);
				counter++;
			}
			return recipes;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	//get recipes
	public Set<Post> getAllRecipesFromIngredients(String ingredients) {
		Set<String> allRecipes = new HashSet<String>();
		Set<Post> allPosts = new HashSet<Post>();
		String[] allIngredients= ingredients.split(" ");
		for(int i = 0; i < allIngredients.length; i++) {
			allRecipes.addAll(getRecipesFromIngredient(allIngredients[i]));
		}
		for(String name : allRecipes) {
			allPosts.add(getRecipe(name));
		}
		return allPosts;
	}
	
	//get recipes as set
	private Set<String> getRecipesFromIngredient(String ingredient) {
		Set<String> recipe = new HashSet<String>();
		try {
			PreparedStatement ps = con.prepareStatement(getAllRecipes);
			ResultSet set = ps.executeQuery();
			int rows = 0;
			if (set.last()) {
			    rows = set.getRow();
			    // Move to beginning
			    set.beforeFirst();
			}
			String[] recipes = new String[rows];
			int counter = 0;
			while(set.next()) {
				recipes[counter] = set.getString(1);
				counter++;
			}
			for(int i = 0; i < counter; i++) {
				Post post = getRecipe(recipes[i]);
				if(recipes[i].toLowerCase().contains((ingredient.toLowerCase())))
					recipe.add(recipes[i]);
				String [] ingredients = post.getIngredientList();
				for(int j = 0; j < ingredients.length; j++) {
					if(ingredients[j].toLowerCase().contains((ingredient.toLowerCase()))) {
						recipe.add(recipes[i]);
					}
				}
			}
			return recipe;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	//public method to get personal Pages
	public Set<PersonalPage> searchAllUsers(String usernames) {
		Set<String> allUsers = new HashSet<String>();
		Set<PersonalPage> allUserName = new HashSet<PersonalPage>();
		String[] allnames= usernames.split(" ");
		for(int i = 0; i < allnames.length; i++) {
			allUsers.addAll(searchUser(allnames[i]));
		}
		for(String name : allUsers) {
			allUserName.add(new PersonalPage(name));
		}
		return allUserName;
	}
	
	
	//
	private Set<String> searchUser(String username) {
		Set<String> users = new HashSet<String>();
		try {
			PreparedStatement ps = con.prepareStatement(getAllUsers);
			ResultSet set = ps.executeQuery();
			int rows = 0;
			if (set.last()) {
			    rows = set.getRow();
			    // Move to beginning
			    set.beforeFirst();
			}
			if(rows == 0)
				return null;
			String[] usernames= new String[rows];
			int counter = 0;
			while(set.next()) {
				usernames[counter] = set.getString(1);
				counter++;
			}
			for(int i = 0; i < counter; i++) {
				if(usernames[i].toLowerCase().contains(username.toLowerCase())) {
					if(!usernames[i].equals("guest"))
						users.add(usernames[i]);
				}
			}
			return users;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
			
	}
	
	//unfollow someone
	public boolean unFollow(String user, String target, boolean checking) {
		String [] following = addItem(user, null, 3);
		ArrayList<String> listOfFollowing = new ArrayList<String>(Arrays.asList(following));
		if(checking) {
			return listOfFollowing.contains(target);
		}
		boolean exists =  listOfFollowing.remove(target);
		following = listOfFollowing.toArray(new String[listOfFollowing.size()]);
		if(exists) {
			String [] followers = addItem(target, null, 2);
			ArrayList<String> listOfFollowers = new ArrayList<String>(Arrays.asList(followers));
			listOfFollowers.remove(user);
			followers = listOfFollowers.toArray(new String[listOfFollowers.size()]);
			PreparedStatement ps3;
			try {
				ps3 = con.prepareStatement(addFollowerToUser);
				ps3.setString(2, target);
				ps3.setString(1, Arrays.toString(followers).substring(1, Arrays.toString(followers).length() - 1));
				ps3.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}
		try {
			PreparedStatement ps2 = con.prepareStatement(addFollowingToUser);
			ps2.setString(2, user);
			ps2.setString(1, Arrays.toString(following).substring(1, Arrays.toString(following).length() - 1));
			ps2.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return exists;	
		
	}
	
	public boolean isRecipe(String username, String item, HashMap<String,Post> name) {
		try {
			ResultSet set = getUserData(username);
			String list = set.getString(5);
			String [] recipes = list.split("[,]");
			
			for(int i = 0; i < recipes.length; i++) {
				if(recipes[i].replaceAll("\\s", "").equals(item.replaceAll("\\s", "")))
					return true;			
			}
			for(String key : name.keySet()) {
				System.out.println(key + " "+ item);
				if(key.replaceAll("\\s", "").equals(item.replaceAll("\\s", "")))
					return true;
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	
	
}

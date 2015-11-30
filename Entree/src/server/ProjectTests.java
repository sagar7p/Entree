package server;

import java.util.HashSet;
import java.util.List;
import java.util.Vector;

import client.Post;
import profile.PersonalPage;

public class ProjectTests {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SQLTest();
		//PersonalClassTest();
	}
	
	private static void SQLTest() {
		SQLDriver msql = new SQLDriver();
		Vector<String> ingredients = new Vector<String>();
		ingredients.addElement("milk");
		ingredients.add("water");
		Vector<String> instructions = new Vector<String>();
		instructions.add("Mix it");
		instructions.add("Drink it");
		msql.connect();
		//PersonalPage profile = new PersonalPage("@matt");
		/*msql.addRecipe("Soup", "@sagar", ingredients, instructions, 170, true);
		System.out.println(msql.getRecipe("Muffins").getNumberOfReheats());
		msql.addUser("@sahil", "sahil@email.com", "pass4", "chicken");
		System.out.println(msql.addItem("@sagar", "Cheese", 1)[1]);
		System.out.println(Arrays.toString(msql.addItem("@kevin", "@sahil", 2)));
		System.out.println(Arrays.toString(msql.addItem("@sahil", "@kevin", 3)));
		System.out.println(Arrays.toString(msql.addItem("@matt", "Salsa", 4)));
		System.out.println(Arrays.toString(msql.addItem("@sagar", null, 1)));
		System.out.println(Arrays.toString(msql.addItem("@kevin", null, 2)));
		System.out.println(Arrays.toString(msql.addItem("@sahil", null, 3)));
		System.out.println(Arrays.toString(msql.addItem("@matt", null, 4)));*/
		/*for(int i = 0; i < profile.getPosts().size(); i++) {
			   System.out.println(profile.getPosts().get(i).getName());
		   }*/
		/*HashSet<Post> recipes= (HashSet<Post>) msql.getAllRecipesFromIngredients("tacos");
		for(Post recipe : recipes) {
			System.out.println(recipe.getName());
		}*/
		/*String [] data = msql.getUserInfo("@sagar");
		for(int i =0; i < 3; i++) {
			System.out.println(data[i]);
		}*/
		/*Set<PersonalPage> pages = msql.searchAllUsers("@sag @kevi @matt @sa");
		for(PersonalPage page : pages ) {
			System.out.println(page.getUsername());
		}*/
		/*PersonalPage profile1 = new PersonalPage("@");
		System.out.println(profile1.getFollowing());*/
		//msql.addItem("@sagar", "@sahil", 3);
		//System.out.println(msql.unFollow("@sagar", "@kevin", true));
		System.out.println(msql.isRecipe("sagar", "Turkey Gravy"));
		msql.stop();
	}
	
	private static void PersonalClassTest() {
		PersonalPage profile = new PersonalPage("@sagar");
		List<Post> list = profile.getPosts();
		for(int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i).getName());
		}
	}
	

}

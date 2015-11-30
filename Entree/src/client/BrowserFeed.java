package client;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Vector;

import javax.swing.JFrame;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.JSValue;
import com.teamdev.jxbrowser.chromium.dom.By;
import com.teamdev.jxbrowser.chromium.dom.DOMDocument;
import com.teamdev.jxbrowser.chromium.dom.DOMElement;
import com.teamdev.jxbrowser.chromium.dom.DOMNode;
import com.teamdev.jxbrowser.chromium.dom.events.DOMEvent;
import com.teamdev.jxbrowser.chromium.dom.events.DOMEventListener;
import com.teamdev.jxbrowser.chromium.dom.events.DOMEventType;
import com.teamdev.jxbrowser.chromium.events.FinishLoadingEvent;
import com.teamdev.jxbrowser.chromium.events.LoadAdapter;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;

import profile.PersonalPage;
import server.SQLDriver;

public class BrowserFeed extends Thread {
	private Map<String, String> monthMap;
	private BrowserPersonal currUserPersonalPage;
	private PersonalPage profile;
	private Browser browser;
	private Vector<PersonalPage> allProfiles;
	private SQLDriver sq;
	
	 //public static void main(String[] args) { new BrowserFeed("@sagar"); }
	 

	public BrowserFeed (String username) {

		try {
			for (File file : (new File(".")).getCanonicalFile().listFiles()) {
				System.out.println(file.getAbsolutePath());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		allProfiles = new Vector<PersonalPage>();
		monthMap = new HashMap<String, String>();
		monthMap.put("01", "January");
		monthMap.put("02", "February");
		monthMap.put("03", "March");
		monthMap.put("04", "April");
		monthMap.put("05", "May");
		monthMap.put("06", "June");
		monthMap.put("07", "July");
		monthMap.put("08", "August");
		monthMap.put("09", "September");
		monthMap.put("10", "October");
		monthMap.put("11", "November");
		monthMap.put("12", "December");
		profile = new PersonalPage(username);
		System.out.println(profile.getFollowers());
		sq = new SQLDriver();
		sq.connect();
		browser = new Browser();
		BrowserView browserView = new BrowserView(browser);
		JFrame frame = new JFrame("Entree");
		frame.add(browserView, BorderLayout.CENTER);
		frame.setSize(frame.getMaximumSize());
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		if(!username.equals("@guest")) {
			try {
				browser.loadURL("file://" + (new File(".")).getCanonicalFile().getParent() + File.separator + "index.html");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			try {
				browser.loadURL("file://" + (new File(".")).getCanonicalFile().getParent() + File.separator + "guest.html");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// logout and person's name shows up in the upper right hand corner
		browser.addLoadListener(new LoadAdapter() {
			@Override
			public void onFinishLoadingFrame(FinishLoadingEvent event) {
				if (event.isMainFrame()) {
					DOMDocument document = event.getBrowser().getDocument();
					DOMElement logout = document.findElement(By.className("logout"));
					
					DOMElement id = document.findElement(By.className("id"));
					id.setInnerHTML("@"+username);

					// attempting to navigate by clicking
					logout.addEventListener(DOMEventType.OnClick, new DOMEventListener() {
						public void handleEvent(DOMEvent event) {
							new BrowserLogin();
						}
					}, false);
				}
			}
		});
	
			//Load main page and link to main page
			browser.addLoadListener(new LoadAdapter() {
				@Override
				public void onFinishLoadingFrame(FinishLoadingEvent event) {
					if (event.isMainFrame()) {
						DOMDocument document = event.getBrowser().getDocument();
						DOMElement brand = document.findElement(By.className("brand"));
						
						// attempting to navigate by clicking
						brand.addEventListener(DOMEventType.OnClick, new DOMEventListener() {
							public void handleEvent(DOMEvent event) {
								new BrowserFeed(username);
							}
						}, false);
						
						
						
					}
				}
			});
		
		
		//search bar
		browser.addLoadListener(new LoadAdapter() {
			@Override
			public void onFinishLoadingFrame(FinishLoadingEvent event) {
				if (event.isMainFrame()) {
					DOMDocument document = event.getBrowser().getDocument();
					DOMElement searchquery = document.findElement(By.id("searchquery"));

					searchquery.addEventListener(DOMEventType.OnClick, new DOMEventListener() {
						public void handleEvent(DOMEvent event) {
							JSValue searchtext = browser
									.executeJavaScriptAndReturnValue("document.getElementById('searchtext').value");
							// call search on searchtext
							if (searchtext.getString().substring(0, 1).equals("@")) {
								
								//USER NAME
								Set<PersonalPage> toAdd = sq.searchAllUsers(searchtext.getString().substring(1));
								List<DOMElement> divs = document.findElements(By.tagName("div"));
								if (!toAdd.isEmpty()) {
									for (DOMElement div : divs) {
										if (div.getAttribute("id").equals("userinsertionpoint")) {
											div.setInnerHTML("");
											for (PersonalPage p : toAdd) {
												div.setInnerHTML(addUserResult(p) + div.getInnerHTML());
											}
										}
									}
									browser.executeJavaScript(updatePicture());
									
									List<DOMElement> connectToProfiles = document.findElements(By.className("toprofile"));
									for (DOMElement connectToProfile: connectToProfiles ){
										connectToProfile.addEventListener(DOMEventType.OnClick, new DOMEventListener() {
											public void handleEvent(DOMEvent event) {
												System.out.println(" I JUST CLICKED ON THIS PERSON: "+ connectToProfile.getAttribute("value"));
												new BrowserPersonal2(username, connectToProfile.getAttribute("value"));
											}
										}, false);
									}
								} else {
									for (DOMElement div : divs) {
										if (div.getAttribute("id").equals("userinsertionpoint")) {
											div.setInnerHTML("<p>Can't find the user you were searching for :( </p>");
										}
									}
								}
								DOMNode link = document.findElement(By.id("openuserresults"));
								if (link != null) {
									link.click();
								}
							} else {
								Set<Post> toAdd = sq.getAllRecipesFromIngredients(searchtext.getString());
								List<DOMElement> divs = document.findElements(By.tagName("div"));
								if (!toAdd.isEmpty()) {
									for (DOMElement div : divs) {
										if (div.getAttribute("id").equals("thumbnailinsertionpoint")) {
											div.setInnerHTML("");
											for (Post p : toAdd) {
												System.out.println(p.getName());
												div.setInnerHTML(addSearchResult(p) + div.getInnerHTML());
											}
										}
									}
									browser.executeJavaScript(updatePicture());
								} else {
									for (DOMElement div : divs) {
										if (div.getAttribute("id").equals("thumbnailinsertionpoint")) {
											div.setInnerHTML("<p>These were not the droids you are looking for:(</p>");
										}
									}
								}
								DOMNode link = document.findElement(By.id("opensearchresults"));
								if (link != null) {
									link.click();
								}
							}
						}
					}, false);
				}
			}
		});
		
		//navigation to personal page & update feeds
		browser.addLoadListener(new LoadAdapter() {
			@Override
			public void onFinishLoadingFrame(FinishLoadingEvent event) {
				if (event.isMainFrame()) {

					DOMDocument document = event.getBrowser().getDocument();

					// navigate to personal page
					DOMElement navigate = document.findElement(By.className("Navigate"));

					// attempting to navigate by clicking
					navigate.addEventListener(DOMEventType.OnClick, new DOMEventListener() {
						public void handleEvent(DOMEvent event) {
							if(!username.equals("@guest")) {
								currUserPersonalPage = new BrowserPersonal(username);
							}
							
						}
					}, false);

					List<DOMElement> divs = document.findElements(By.tagName("div"));
					for (DOMElement div : divs) {
						if (div.getAttribute("class").equals("feed") && !username.equals("@guest")) {
							HashMap<String,Post> name = new HashMap<String,Post>();
							for (int i = 0; i < profile.getPosts().size(); i++) {
								// System.out.println(profile.getPosts().get(i).getName());
								Post retrievedPost = profile.getPosts().get(i);
								if (retrievedPost != null) {
									name.put(retrievedPost.getName(),retrievedPost);
								}
							}
							List<String> followers = profile.getFollowing();
							if (!followers.isEmpty()) {
								
							
								for (int i = 0; i < followers.size(); i++) {
									PersonalPage pp = new PersonalPage(followers.get(i));
									allProfiles.add(pp);
									for (int j = 0; j < pp.getPosts().size(); j++) {
										Post p = pp.getPosts().get(j);
										if (p!=null) {
											name.put(p.getName(),p);	
										}
									}
								}
								if(name.size() > 0) {
									profile.numOfPosts = name.size();
									PriorityQueue<Post> orderedPosts = new PriorityQueue<Post>(name.size(), new PostComparator());
									for(String key : name.keySet()) {
										orderedPosts.add(name.get(key));
									}
									while (!orderedPosts.isEmpty()) {
										Post p = orderedPosts.remove();
										div.setInnerHTML(addPostDiv(p) + div.getInnerHTML());
										div.setInnerHTML(addPostModal(p) + div.getInnerHTML());
									}
									
									List<DOMElement> connectToProfiles = document.findElements(By.className("toprofile"));
									for (DOMElement connectToProfile: connectToProfiles ){
										connectToProfile.addEventListener(DOMEventType.OnClick, new DOMEventListener() {
											public void handleEvent(DOMEvent event) {
												System.out.println(" I JUST CLICKED ON THIS PERSON: "+ connectToProfile.getAttribute("value"));
												new BrowserPersonal2(username, connectToProfile.getAttribute("value"));
											}
										}, false);
									}
								}
							}
						} else if (div.getAttribute("class").equals("featured")) {
							String[] featuredPosts = sq.getAllFeaturedRecipes();
							for (int i = 0; i < featuredPosts.length; i++) {
								if (featuredPosts[i] != null) {
									Post p = sq.getRecipe(featuredPosts[i]);

									div.setInnerHTML(addFeaturedPostDiv(p) + div.getInnerHTML());
									div.setInnerHTML(addPostModal(p) + div.getInnerHTML());

								}
							}
						}
					}

				}
			}

			class PostComparator implements Comparator<Post> {
				@Override
				public int compare(Post x, Post y) {
					// Assume neither string is null. Real code should
					// probably be more robust
					// You could also just return x.length() - y.length(),
					// which would be more efficient.
					if (Double.parseDouble(x.getDate()) < Double.parseDouble(y.getDate())) {
						return -1;
					}
					if (Double.parseDouble(x.getDate()) > Double.parseDouble(y.getDate())) {
						return 1;
					}
					return 0;
				}
			}
			
		});
		
		browser.addLoadListener(new LoadAdapter() {
			@Override
			public void onFinishLoadingFrame(FinishLoadingEvent event) {
				if (event.isMainFrame()) {
					runThread();
					browser.executeJavaScript(updatePicture());
					// web page is loaded completely including all frames
				}
			}
		});
		browser.addLoadListener(new LoadAdapter() {
			public void onFinishLoadingFrame(FinishLoadingEvent event) {
				if (event.isMainFrame()) {
					DOMDocument document = event.getBrowser().getDocument();
					addNewHeat(document);
				}
			}
		});
		
	}
	
	public void runThread() {
		System.out.println("Thread is running");
		this.start();
	}

	public String addPostDiv(Post p) {
		String date = p.getParsedDate();
		String month = date.substring(0, 2);
		if (monthMap == null) {
			System.out.println("null");
		}
		System.out.println("month:" + month + " " + monthMap.get(month));
		String rest = date.substring(2);
		String parsedDate = monthMap.get(month) + rest;
		String html = "<div class='thumbnail'>" + "<img class='foodimage'src=''  value='"
				+ p.getName() + "'>" + "<div class='caption' id = '" + p.getName().replaceAll("\\s", "") + "post'>" + "<h3>" + p.getName() + "</h3>" + "<p>" +"<a href='#' value='"+p.getUsername()+"' class='toprofile'>"
				+ "@" + p.getUsername() + "</a>" + "</p>" + "<p><time>" + parsedDate + "</time></p>" + "<p><i>"
				+ p.getNumberOfReheats() + "<span class = 'glyphicon glyphicon-fire'></span></i></p>" + "<p>"
				+ "<p class='btn btn-primary reheat' role='button' name='" + p.getName() + "'><span class ='"
				+ "glyphicon glyphicon-fire' ></span>&nbsp;Reheat</p>"
				+ "<a href='#' class='btn btn-default foodinfo' role='button' data-toggle='modal' data-target='#"
				+ p.getName().replaceAll("\\s", "") + "'>More</a>" + "</p>" + "</div>" + "</div>";
		return html;
	}

	public String addFeaturedPostDiv(Post p) {
		String html = "<div class='thumbnail'>" + "<img class='foodimage' src='img/yum5.jpg' alt='sampleimage' value='"
				+ p.getName() + "'>" + "<div class='caption'>" + "<p>"
				+ "<a href='#' class='btn btn-default btn-block foodinfo' role='button' data-toggle='modal' data-target='#"
				+ p.getName().replaceAll("\\s", "") + "'>More</a>" + "</p>" + "</div>" + "</div>";
		return html;

	}

	public String addPostModal(Post p) {
		String html = "<div id='" + p.getName().replaceAll("\\s", "") + "' class='modal fade' role='dialog'>"
				+ "<div class='modal-dialog'>" + "<!-- Modal content-->" + "<div class='modal-content'>"
				+ " <div class='modal-header'>"
				+ "   <button type='button' class='close' data-dismiss='modal'>&times;</button>"
				+ "   <h4 class='modal-title'>" + p.getName() + "</h4>" + " </div>" + " <div class='modal-body'>"
				// substitute the turkey.jpg with whatever the post image
				// actually is
				+ "   <img class='foodimage' src='" + "img/turkey.jpg" + "' width='100%' value='" + p.getName() + "'>"
				+ "   <hr>" + "   <h3>Ingredients</h3>" + "   <div class='list-group ingredients'>";

		for (int i = 0; i < p.getIngredientList().length; i++) {
			html += "<a href='#' class='list-group-item'><input type='checkbox' aria-label='...'>&nbsp;&nbsp;"
					+ p.getIngredientList()[i] + "</a>";
		}

		html += "</div>" + "<h3>Instructions</h3>" + "<ol class='list-group instructions'>";

		for (int i = 0; i < p.getInstructionList().length; i++) {
			html += "<li class='list-group-item'>" + p.getInstructionList()[i] + "</li>";
		}
		html += "</ol>" + "</div>" + "<div class='modal-footer'>"
				+ "<button type='button' class='btn btn-default' data-dismiss='modal'>Close</button>" + "</div>"
				+ "</div>" + "</div>" + "</div>";
		return html;
	}

	public String updatePicture() {
		String parse = "$('.foodimage').each(function(index) {var foodimage = $(this);console.log(foodimage);var Recipe = Parse.Object.extend('Recipe');var query = new Parse.Query(Recipe);query.equalTo('Name', $(this).attr('value'));"
				+ "query.find({success: function(results) {var recipe = results[0];var photo = recipe.get('File');foodimage.attr('src',photo.url());foodimage.css('width','100%');},error: function(error) {console.log('error')}});});";
		return parse;
	}
	
	public static String addUserResult(PersonalPage p) {
		String username = p.getUsername().substring(1, p.getUsername().length()).replace("\\s", "");
		String html = "<div id='"+username+"' class='panel panel-default'>"
				+ "<div class='panel-heading' data-toggle='collapse' href='#"+username+"-user'>"
				+ "<h4 class='panel-title'><a>"+"@"+p.getUsername()+"</a></h4>" + "</div>"
			    + "<div id='"+username+"-user' class='panel-collapse collapse'>"
			    + "<div class='panel-body'>"
			    + "<div class='container'>"
			    + "<a href='#' value='"+p.getUsername()+"' class='toprofile'><img class='thumbnail pull-left media-object' style='width:80px; border-radius:' src='img/default.png'></a>"
			    + "<div class='media-body' style='padding-left: 20px; padding-top:20px;'>"
			    + "<a href='#' value='"+p.getUsername()+"' class='toprofile'><h4 class='media-heading'>" + p.getFullName() +"</h4></a>"
			    + "<p><strong>Favorite food: </strong>" + p.getFavoriteFood()+ "</p>"
			    + "<p><span class='label label-info'>" + p.getNumPosts() + " Posts</span> <span class='label label-warning'>" + p.getNumFollowing() +" followers</span></p>"
			    + "</div></div></div></div></div>";
		return html;
	}

	public static String addSearchResult(Post p) {
		String html = "<div class='thumbnail'>"
				// add new image!
				+ "<img class='foodimage' src='img/yum1.jpg' alt='sampleimage' value='"+p.getName()+"' id='test'>"
				+ "<div class='caption'>" + "<h3>" + p.getName() + "</h3>" + "<p><a href='#'>" + "@"+p.getUsername()
				+ "</a></p>" + "<div class='panel-group'>" + "<div class='panel panel-default'>"
				+ "<div class='panel-heading' data-toggle='collapse' href='#" + p.getName().replaceAll("\\s", "")
				+ "-result'" + "<h4 class='panel-title'><a>More</a></h4></div>" + "<div id='"
				+ p.getName().replaceAll("\\s", "") + "-result' class='panel-collapse collapse'>"
				+ "<div class='panel-body' style='padding: 20px;'>" + "<h3 style='margin-top: 0px'>Ingredients</h3>"
				+ "<div class='list-group ingredients'>";
		for (int i = 0; i < p.getIngredientList().length; i++) {
			html += "<a href='#' class='list-group-item'><input type='checkbox' aria-label='...'>&nbsp;&nbsp;"
					+ p.getIngredientList()[i] + "</a>";
		}
		html += "</div><hr>" + "<h3>Instructions</h3>" + "<ol class='list-group instructions'>";
		for (int i = 0; i < p.getInstructionList().length; i++) {
			html += "<li class='list-group-item'>" + p.getInstructionList()[i] + "</li>";
		}
		html += "</ol></div></div></div></div></div></div>";
		return html;
	}
	
	public void addNewHeat(DOMDocument document) {
		List<DOMElement> buttons = document.findElements(By.className("reheat"));
		for (DOMElement button : buttons) {
			button.addEventListener(DOMEventType.OnClick, new DOMEventListener() {
				public void handleEvent(DOMEvent event) {
					String postName = button.getAttribute("name");
					sq.addHeat(postName,profile.getUsername());
					Post p = sq.getRecipe(postName);
					String date = p.getParsedDate();
					String month = date.substring(0, 2);
					if (monthMap == null) {
						System.out.println("null");
					}
					String rest = date.substring(2);
					String parsedDate = monthMap.get(month) + rest;
					List<DOMElement> divs = document.findElements(By.tagName("div"));
					for(DOMElement div: divs){
						if(div.getAttribute("id").equals(p.getName().replaceAll("\\s", "")+"post")){
							System.out.println(p.getNumberOfReheats());
							System.out.println(div.getInnerHTML());
							div.setInnerHTML("<h3>" + p.getName() + "</h3>" + "<p>"
									+ "@" + p.getUsername() + "</p>" + "<p><time>" + parsedDate + "</time></p>" + "<p><i>"
									+ p.getNumberOfReheats() + "<span class = 'glyphicon glyphicon-fire'></span></i></p>" + "<p>"
									+ "<p class='btn btn-primary reheat' role='button' name='" + p.getName() + "post'><span class ='"
									+ "glyphicon glyphicon-fire' ></span>&nbsp;Reheat</p>"
									+ "<a href='#' class='btn btn-default foodinfo' role='button' data-toggle='modal' data-target='#"
									+ p.getName().replaceAll("\\s", "") + "'>More</a>" + "</p>");
							break;
						}
						
					}
					
				}
			}, false);

		}
	}
	
	public void run() {
		System.out.println("new thread");
		while(true) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Post post = profile.getNumOfPosts(allProfiles); 
			if (post != null && !sq.isRecipe(profile.getUsername(), post.getName())) {
				System.out.println("recipe added: " + post.getName());
				DOMDocument document = browser.getDocument();
				DOMElement div = document.findElement(By.className("feed"));
				div.setInnerHTML(addPostDiv(post) + div.getInnerHTML());
				addNewHeat(document);
				browser.executeJavaScript(updatePicture());
				for(int i = 0; i< allProfiles.size(); i++) {
					if(allProfiles.get(i).getUsername() == post.getUsername()) {
						allProfiles.remove(i);
						allProfiles.add(i, new PersonalPage(post.getUsername()));
					}
				}
				System.out.println(post.getUsername() + " " + profile.getUsername() + " " + post.getName());
			}
			
		}
	}
}

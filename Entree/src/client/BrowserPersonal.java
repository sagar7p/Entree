package client;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.CloseStatus;
import com.teamdev.jxbrowser.chromium.FileChooserMode;
import com.teamdev.jxbrowser.chromium.FileChooserParams;
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
import com.teamdev.jxbrowser.chromium.swing.DefaultDialogHandler;

import profile.PersonalPage;
import server.SQLDriver;

public class BrowserPersonal{
	private PersonalPage profile;
	private SQLDriver sq;
	private Browser browser;
	
	private String username;
	private Map<String, String> monthMap;
	{
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
	}
  /* public static void main(String[] args) {
	   new BrowserPersonal("@kevin");
   }*/
	
   public BrowserPersonal(String username) {
	   try {
		   for(File file : (new File(".")).getCanonicalFile().listFiles()) {
	    		System.out.println(file.getAbsolutePath());
	    	}
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   this.username = username;
	   profile = new PersonalPage(username);
	   sq = new SQLDriver();
	   sq.connect();
       browser = new Browser();
       BrowserView browserView = new BrowserView(browser);
       JFrame frame = new JFrame("Entree");
       frame.add(browserView, BorderLayout.CENTER);
       frame.setSize(frame.getMaximumSize());
       frame.setVisible(true);
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
       
       browser.setDialogHandler(new DefaultDialogHandler(browserView) {
           @Override
           public CloseStatus onFileChooser(FileChooserParams params) {
               if (params.getMode() == FileChooserMode.Open) {
                   JFileChooser fileChooser = new JFileChooser();
                   int returnValue = fileChooser.showOpenDialog(
                           browserView);
                   if (returnValue == JFileChooser.APPROVE_OPTION) {
                       File selectedFile = fileChooser.getSelectedFile();
                       params.setSelectedFiles(selectedFile.getAbsolutePath());
                       return CloseStatus.OK;
                   }
               }
               return CloseStatus.CANCEL;
           }
       });
       
     //Load main page
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
       
     //logout and you are signed in as yourself
       browser.addLoadListener(new LoadAdapter() {
    	   @Override
    	   public void onFinishLoadingFrame(FinishLoadingEvent event) {
    		   if(event.isMainFrame()) {
    			   DOMDocument document = event.getBrowser().getDocument();
    			   DOMElement logout = document.findElement(By.className("logout"));
    			   
    			   DOMElement id = document.findElement(By.className("id"));
				   id.setInnerHTML("@" + username);
    			   
    			   //attempting to navigate by clicking
    			   logout.addEventListener(DOMEventType.OnClick, new DOMEventListener() {
    				   public void handleEvent(DOMEvent event) {
    					   new BrowserLogin();
    				   }
    			   }, false);
    		   }
    	   }
       });
       
       browser.addLoadListener(new LoadAdapter() {
    	   @Override
    	   public void onFinishLoadingFrame(FinishLoadingEvent event) {
    		   if(event.isMainFrame()) {
    			   DOMDocument document = event.getBrowser().getDocument();
    			   DOMElement button = document.findElement(By.id("submit"));

    			   button.addEventListener(DOMEventType.OnClick, new DOMEventListener() {
    				   public void handleEvent(DOMEvent event2) {
    					   JSValue recipe = browser.executeJavaScriptAndReturnValue("document.getElementById('recipename').value");
    					   JSValue ingredients = browser.executeJavaScriptAndReturnValue("document.getElementById('ingredients').value");
    					   JSValue instructions = browser.executeJavaScriptAndReturnValue("document.getElementById('instructions').value");
    					   profile.createPost(new Post(recipe.getString(), profile.getUsername(), 0, ingredients.getString().split("[,]"), instructions.getString().split("\\n"),new SimpleDateFormat("yyyyMMddhhmmss").format(new Date())));	
    					   addRecipeToHTML(event,false);
    					   addActionListener(document);
    				   }
    			   }, false);
    		   }
    	   }
       });
       browser.addLoadListener(new LoadAdapter() {
           @Override
           public void onFinishLoadingFrame(FinishLoadingEvent event) {
               if (event.isMainFrame()) {
                   addRecipeToHTML(event,true);
               }
           }
       });
       
       browser.addLoadListener(new LoadAdapter() {
    	    @Override
    	    public void onFinishLoadingFrame(FinishLoadingEvent event) {
    	        if (event.isMainFrame()) {
    	            browser.executeJavaScript(updatePicture());
    	            System.out.println("Javascript executed");
    	            // web page is loaded completely including all frames
    	        }
    	    }
    	});
 //search bar
       browser.addLoadListener(new LoadAdapter(){
     	  public void onFinishLoadingFrame(FinishLoadingEvent event){
     	  if(event.isMainFrame()){
     	  DOMDocument document = event.getBrowser().getDocument();
                    List<DOMElement> buttons = document.findElements(By.className("doneshopping"));
                    for (DOMElement button : buttons) {
                 	  button.addEventListener(DOMEventType.OnClick, new DOMEventListener() {
                 		  public void handleEvent(DOMEvent event) {
                 			  sq.doneShopping(profile.getUsername());
                 			  DOMElement list = document.findElement(By.id("groceryList"));
					     	  for(int i = 0; i < profile.getGroceryList().size(); i++){				
					     		  list.setInnerHTML("");				
					     	  }
					     }				
					}, false);
                 }					
			}				
		}

        });
       
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
 												div.setInnerHTML(BrowserFeed.addUserResult(p) + div.getInnerHTML());
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
 												div.setInnerHTML(BrowserFeed.addSearchResult(p) + div.getInnerHTML());
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
       browser.addLoadListener(new LoadAdapter(){
    	   @Override
    	   public void onFinishLoadingFrame(FinishLoadingEvent event){
    		   if(event.isMainFrame()){
    			   List<String> groceryList = profile.getGroceryList();
    			   DOMDocument document = event.getBrowser().getDocument();
                   List<DOMElement> divs = document.findElements(By.tagName("ul"));

    			   for (DOMElement div : divs) {
                	   if(div.getAttribute("class").equals("list-group grocery")) {
                		   // grab all the ingredients and
                		   //iterate through the for loop and add it to the recipe item
                		   for(int i = 0; i < groceryList.size(); i++){
                			   if (!groceryList.get(i).isEmpty()) {
                				   div.setInnerHTML(addGroceryDiv(groceryList.get(i)) + div.getInnerHTML());
                			   }
                				   
                		   }
                	   }
    			   }
                	   
    		   }
    	   }
       });
       browser.addLoadListener(new LoadAdapter(){
    	   public void onFinishLoadingFrame(FinishLoadingEvent event){
    		   if(event.isMainFrame()){
    			   DOMDocument document = event.getBrowser().getDocument();
                  
    		   }
    	   }
       });
       browser.addLoadListener(new LoadAdapter(){
    	   public void onFinishLoadingFrame(FinishLoadingEvent event){
    		   if(event.isMainFrame()){
    			   DOMDocument document = event.getBrowser().getDocument();
                   List<DOMElement> buttons = document.findElements(By.className("btn-plus"));
                   for (DOMElement button : buttons) {
                	   button.addEventListener(DOMEventType.OnClick, new DOMEventListener() {
    				   public void handleEvent(DOMEvent event) {
    					   JSValue newItem = browser.executeJavaScriptAndReturnValue("document.getElementById('addField').value");
    					   String newItemString = newItem.getString();
    					   if(newItemString == null || newItemString.equals("")){
    						   return;
    					   }
    					   sq.addItem(profile.getUsername(), newItemString, 4);
    					   DOMElement list = document.findElement(By.id("groceryList"));
    					   list.setInnerHTML(list.getInnerHTML() + "<li class='list-group-item'>"+ newItemString + "</li>");
    					   browser.executeJavaScript("document.getElementById('addField').value = ''");
    					   
    				   }
    			   }, false);
                	  
                   }
    		   }
    	   }
       });
       
       browser.addLoadListener(new LoadAdapter(){
    	   public void onFinishLoadingFrame(FinishLoadingEvent event){
    		   if(event.isMainFrame()){
    			   DOMDocument document = event.getBrowser().getDocument();

    			   addActionListener(document);
    		   }
    	   }
       });
     
  

      try {
  	    browser.loadURL("file://" + (new File(".")).getCanonicalFile().getParent() + File.separator+ "personal3.html");
       
	    //browser.loadURL("file://" + (new File(".")).getCanonicalFile().getParent() + File.separator+ "index.html");
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
       

   }
   
   public void addActionListener(DOMDocument document) {
	   List<DOMElement> buttons = document.findElements(By.className(("addtogrocery")));
	   for(DOMElement addButton : buttons){
			addButton.addEventListener(DOMEventType.OnClick, new DOMEventListener() {
				public void handleEvent(DOMEvent event){
					System.out.println("ADded to grocery");
					String postName = addButton.getAttribute("grocery");
					Post p = sq.getRecipe(postName);
					for(int i = 0; i < p.getIngredientList().length; i++){
						String[] currList =sq.addItem(profile.getUsername(), p.getIngredientList()[i], 4);
						String[] afterList = sq.addItem(profile.getUsername(), null, 4);
						if(currList.length != afterList.length){
							DOMElement list = document.findElement(By.id("groceryList"));
	    					list.setInnerHTML(list.getInnerHTML() + "<li class='list-group-item'>"+ p.getIngredientList()[i] + "</li>");
						}
					}				
				}
			}, false);
	   }
   }
   
   public String addPostDiv(Post p) {
	   String date = p.getParsedDate();
		String month = date.substring(0, 2);
		String rest = date.substring(2);
		String parsedDate = monthMap.get(month) + rest;
	   String html = "<div class='thumbnail'>" 
                     + "<img class='foodimage' src='' value='"+p.getName()+"'>"
                     + "<div class='caption'>"
                     + "<h3>"+p.getName()+"</h3>"
                     + "<p>"+"<a href='#' value='"+p.getUsername()+"' class='toprofile'>"
     				 + "@" + p.getUsername() + "</a>"+"</p>"
     				 + "<p><time>" + parsedDate + "</time></p>" + "<p><i>"
     				 + p.getNumberOfReheats() + "<span class = 'glyphicon glyphicon-fire'></span></i></p>"
                     +     "<p>"
                     +      "<p class='btn btn-primary addtogrocery' role='button' grocery ='" + p.getName()  +"'>Add to Grocery</p>"
                     +       "<a href='#' class='btn btn-default' role='button' data-toggle='modal' data-target='#"+p.getName().replaceAll("\\s","")+"'>More</a>"                     +    "</p>"
                     + "</div>"
                    + "</div>";
	   return html;
   }
   
   public String addGroceryDiv(String name){
	   String html = "<li class='list-group-item'>" +name + "</li>";
	   return html;
   }
   
   public String addPostModal(Post p) {
       String html = "<div id='" + p.getName().replaceAll("\\s","") + "' class='modal fade' role='dialog'>"		+ "<div class='modal-dialog'>"
		+ "<!-- Modal content-->"
		+ "<div class='modal-content'>"
		+ "	<div class='modal-header'>"
		+ "		<button type='button' class='close' data-dismiss='modal'>&times;</button>"
		+ "		<h4 class='modal-title'>"+p.getName()+"</h4>"
		+ "	</div>"
		+ "	<div class='modal-body'>"
		//substitute the turkey.jpg with whatever the post image actually is
		+ "		<img class='foodimage' src='" + "img/turkey.jpg" + "' width='100%' value='"+p.getName()+"'>"
		+ "		<hr>"
		+ "	 	<h3>Ingredients</h3>"
		+ "		<div class='list-group ingredients'>";

	   for (int i=0; i<p.getIngredientList().length; i++) {
		   html += "<a href='#' class='list-group-item'><input type='checkbox' aria-label='...'>&nbsp;&nbsp;" + p.getIngredientList()[i] + "</a>";
	   }
	   
	   html += "</div>"
				+ "<h3>Instructions</h3>"
				+ "<ol class='list-group instructions'>";
	   
	   for (int i=0; i<p.getInstructionList().length; i++) {
			html += "<li class='list-group-item'>" + p.getInstructionList()[i] + "</li>";
		}
		html += "</ol>"
			+ "</div>"
			+ "<div class='modal-footer'>"
			+ "<button type='button' class='btn btn-default' data-dismiss='modal'>Close</button>"
			+ "</div>"
			+ "</div>"
			+ "</div>"
			+ "</div>";
	   return html;
   }
   
   public void addRecipeToHTML(FinishLoadingEvent event, Boolean all) {
	   DOMDocument document = event.getBrowser().getDocument();
       List<DOMElement> divs = document.findElements(By.tagName("div"));
       for (DOMElement div : divs) {
    	   if(div.getAttribute("class").equals("feed")) {
    		   if(all) {
	    		   for(int i = 0; i < profile.getPosts().size(); i++) {
	    			   System.out.println(profile.getPosts().get(i).getName());
	    			   div.setInnerHTML(addPostDiv(profile.getPosts().get(i)) + div.getInnerHTML());
	    			   div.setInnerHTML(addPostModal(profile.getPosts().get(i)) + div.getInnerHTML());
	    		   }
    		   }
    		   else {
    			   div.setInnerHTML(addPostDiv(profile.getPosts().get(profile.getPosts().size() -1)) + div.getInnerHTML());
    			   div.setInnerHTML(addPostModal(profile.getPosts().get(profile.getPosts().size() -1)) + div.getInnerHTML());
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
    		   return;
    	   }
       }
   }
   
   public  String updatePicture() {
	     String parse = "$('.foodimage').each(function(index) {var foodimage = $(this);console.log(foodimage);var Recipe = Parse.Object.extend('Recipe');var query = new Parse.Query(Recipe);query.equalTo('Name', $(this).attr('value'));"
	           +"query.find({success: function(results) {var recipe = results[0];var photo = recipe.get('File');foodimage.attr('src',photo.url());foodimage.css('width','100%');},error: function(error) {console.log('error')}});});";
	     return parse;
	}
   
   
}
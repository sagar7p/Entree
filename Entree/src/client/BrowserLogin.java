package client;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.JSValue;
import com.teamdev.jxbrowser.chromium.dom.By;
import com.teamdev.jxbrowser.chromium.dom.DOMDocument;
import com.teamdev.jxbrowser.chromium.dom.DOMElement;
import com.teamdev.jxbrowser.chromium.dom.events.DOMEvent;
import com.teamdev.jxbrowser.chromium.dom.events.DOMEventListener;
import com.teamdev.jxbrowser.chromium.dom.events.DOMEventType;
import com.teamdev.jxbrowser.chromium.events.FinishLoadingEvent;
import com.teamdev.jxbrowser.chromium.events.LoadAdapter;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;

import profile.PersonalPage;
import server.SQLDriver;

public class BrowserLogin {
	private PersonalPage personal;
	private static Browser browser;
	private BrowserView browserView;
	private static BrowserLogin browsermain;
	private static SQLDriver driver;

	public BrowserLogin() {
		browser = new Browser();
		browserView = new BrowserView(browser);
		JFrame frame = new JFrame("Log in to Entree");

		// SQL Driver start up
		driver = new SQLDriver();
		driver.connect();

		frame.add(browserView, BorderLayout.CENTER);
		frame.setSize(frame.getMaximumSize());
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		try {
			browser.loadURL("file://" + (new File(".")).getCanonicalFile().getParent() + File.separator + "login.html");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// register new user
		browser.addLoadListener(new LoadAdapter() {
			@Override
			public void onFinishLoadingFrame(FinishLoadingEvent event) {
				if (event.isMainFrame()) {
					
					
					String lastName = null;
					String email = null;
					String userName = null;
					String password = null;
					String favFood = null;
					
					DOMDocument document = event.getBrowser().getDocument();

					//launch modal with a form to enter information --> done in HTML/CSS
					//after clicking ok
					DOMElement button3 = document.findElement(By.className("registerNOW"));
					//clicking submit/register
					button3.addEventListener(DOMEventType.OnClick, new DOMEventListener() {

						@Override
						public void handleEvent(DOMEvent event) {
							String firstName = browser.executeJavaScriptAndReturnValue("document.getElementById('first').value").getString();
							String lastName = browser.executeJavaScriptAndReturnValue("document.getElementById('last').value").getString();
							String email = browser.executeJavaScriptAndReturnValue("document.getElementById('email').value").getString();
							String user = browser.executeJavaScriptAndReturnValue("document.getElementById('username').value").getString();
							String password = browser.executeJavaScriptAndReturnValue("document.getElementById('password').value").getString();
							String favFood = browser.executeJavaScriptAndReturnValue("document.getElementById('food').value").getString();
							
							//populate database with appropriate information
							System.out.println("attempting to add user to database");
							driver.addUser(user, email, password, favFood, firstName, lastName);
							//driver.addUser("@arthur", "arthur@email.com", "arthur", "ants", "Arthur", "Read");
							//automatically login as that user
							new BrowserFeed(user);
							
						}
						
					}, false);		
				}
			}
		});

		// guest mode
		browser.addLoadListener(new LoadAdapter() {
			@Override
			public void onFinishLoadingFrame(FinishLoadingEvent event) {
				if (event.isMainFrame()) {
					DOMDocument document = event.getBrowser().getDocument();
					DOMElement button1 = document.findElement(By.className("guestmode"));

					// attempting to navigate by clicking
					button1.addEventListener(DOMEventType.OnClick, new DOMEventListener() {
						public void handleEvent(DOMEvent event) {
							new BrowserFeed("@guest");
						}
					}, false);
				}
			}
		});

		// normal login
		browser.addLoadListener(new LoadAdapter() {
			@Override

			public void onFinishLoadingFrame(FinishLoadingEvent event) {
				if (event.isMainFrame()) {
					DOMDocument document = event.getBrowser().getDocument();
					DOMElement button = document.findElement(By.className("signin"));

					Map<String, String> attributes = button.getAttributes();
					button.addEventListener(DOMEventType.OnClick, new DOMEventListener() {

						public void handleEvent(DOMEvent event) {

							JSValue check1 = browser
									.executeJavaScriptAndReturnValue("document.getElementById('inputEmail').value");
							String usernameText = check1.getString();
							System.out.println("The username you entered was " + usernameText);

							JSValue check2 = browser
									.executeJavaScriptAndReturnValue("document.getElementById('inputPassword').value");
							String passwordText = check2.getString();
							System.out.println("The username you entered was " + passwordText);

							System.out.println("Attempting to authenticate"); // after
																				// clicking

							boolean authenticated = driver.authenticate(usernameText, passwordText);
							if (authenticated) {
								System.out.println("you successully logged in");

								List<DOMElement> divs = document.findElements(By.className("Error"));

								for (DOMElement div : divs) {
									div.setInnerHTML("welcome " + usernameText);
									// launch respective
								}

								// load main page with person logged in
								new BrowserFeed(usernameText);

							} else {
								System.out.println("user authentication failed, try again");
								// clear text fields
								// print out to screen that user authentication
								// failed
								List<DOMElement> divs = document.findElements(By.className("Error"));

								for (DOMElement div : divs) {
									div.setInnerHTML("user authentication failed, try again");
								}

							}

							for (String attrName : attributes.keySet()) {
								System.out.println(attrName + "=" + attributes.get(attrName));
							}
							/*
							 * try { browser.loadURL("file://" + (new
							 * File(".")).getCanonicalFile().getParent() +
							 * File.separator+ "login.html"); }catch
							 * (IOException e) { // TODO Auto-generated catch
							 * block e.printStackTrace(); }
							 */

						}
					}, false);
				}
			}
		});
	}

	public static void main(String[] args) {

		browsermain = new BrowserLogin();

	}
}

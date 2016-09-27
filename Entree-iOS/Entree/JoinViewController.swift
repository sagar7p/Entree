//
//  JoinViewController.swift
//  Entree
//
//  Created by Sagar Punhani on 12/20/15.
//  Copyright © 2015 Sagar Punhani. All rights reserved.
//

import UIKit
import Parse

class JoinViewController: UIViewController {

    @IBOutlet var textFields: [UITextField]!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        title = "Register"

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func joinButton(sender: UIButton) {
        let user = PFUser()
        user.username = textFields[2].text
        user.password = textFields[3].text
        user.email = textFields[4].text
        user["first"] = textFields[0].text
        user["last"] = textFields[1].text
        user["food"] = textFields[5].text
        
        user.signUpInBackgroundWithBlock { (succeeded, error) -> Void in
            if(succeeded) {
                PFUser.logInWithUsernameInBackground(self.textFields[2].text!, password: self.textFields[3].text!) { (user, error) -> Void in
                    if(user != nil) {
                        
                        self.performSegueWithIdentifier("register", sender: nil)
                    } else {
                        print("gg")
                    }
                }
            }
        }
        
    }

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}

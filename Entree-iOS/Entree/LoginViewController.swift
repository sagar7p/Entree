//
//  LoginViewController.swift
//  Entree
//
//  Created by Sagar Punhani on 12/20/15.
//  Copyright © 2015 Sagar Punhani. All rights reserved.
//

import UIKit
import Parse



class LoginViewController: UIViewController {
    
    
    @IBOutlet weak var userField: UITextField!
    
    @IBOutlet weak var passField: UITextField!

    override func viewDidLoad() {
        super.viewDidLoad()
            

        // Do any additional setup after loading the view.
    }
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        let user = PFUser.currentUser()
        if user != nil {
            self.performSegueWithIdentifier("login", sender: nil)
        }
        
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func LoginButton(sender: UIButton) {
        PFUser.logInWithUsernameInBackground(userField.text!, password: passField.text!) { (user, error) -> Void in
            if(user != nil) {
                self.performSegueWithIdentifier("login", sender: nil)
            } else {
                print("gg")
            }
        }
    }
    
    @IBAction func RegisterButton(sender: UIButton) {        
        
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

//
//  ProfileTableViewController.swift
//  Entree
//
//  Created by Sagar Punhani on 1/28/16.
//  Copyright © 2016 Sagar Punhani. All rights reserved.
//

import UIKit
import Parse

class ProfileTableViewController: UITableViewController {

    @IBOutlet weak var profilePicture: UIImageView!
    
    var user = PFUser.currentUser()
    
    @IBOutlet weak var name: UILabel!
    
    @IBOutlet weak var username: UILabel!
    
    @IBOutlet weak var email: UILabel!
    
    @IBOutlet weak var food: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        title = "My Profile"
        profilePicture!.layer.borderWidth = 1
        profilePicture!.layer.masksToBounds = false
        profilePicture!.layer.borderColor = UIColor.blackColor().CGColor
        profilePicture!.layer.cornerRadius = profilePicture!.frame.height/2
        profilePicture!.clipsToBounds = true
        name.text = "\(user!["first"] as! String) \(user!["last"] as! String)"
        username.text = "@\(user!["username"] as! String)"
        email.text = user!["email"] as? String
        food.text = user!["food"] as? String

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    // MARK: - Table view data source

    override func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        // #warning Incomplete implementation, return the number of sections
        return 2
    }

    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        if section == 0 {
            return 4
        } else {
            return 1
        }
    }

    
    /*override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier("name", forIndexPath: indexPath)
    
        // Configure the cell...

        return cell
    }*/
    

    /*
    // Override to support conditional editing of the table view.
    override func tableView(tableView: UITableView, canEditRowAtIndexPath indexPath: NSIndexPath) -> Bool {
        // Return false if you do not want the specified item to be editable.
        return true
    }
    */

    /*
    // Override to support editing the table view.
    override func tableView(tableView: UITableView, commitEditingStyle editingStyle: UITableViewCellEditingStyle, forRowAtIndexPath indexPath: NSIndexPath) {
        if editingStyle == .Delete {
            // Delete the row from the data source
            tableView.deleteRowsAtIndexPaths([indexPath], withRowAnimation: .Fade)
        } else if editingStyle == .Insert {
            // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
        }    
    }
    */

    /*
    // Override to support rearranging the table view.
    override func tableView(tableView: UITableView, moveRowAtIndexPath fromIndexPath: NSIndexPath, toIndexPath: NSIndexPath) {

    }
    */

    /*
    // Override to support conditional rearranging of the table view.
    override func tableView(tableView: UITableView, canMoveRowAtIndexPath indexPath: NSIndexPath) -> Bool {
        // Return false if you do not want the item to be re-orderable.
        return true
    }
    */

    
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
        if segue.identifier == "logout" {
            PFUser.logOut()
        }
    }
    

}

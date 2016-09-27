//
//  GroceryTableViewController.swift
//  Entree
//
//  Created by Sagar Punhani on 1/10/16.
//  Copyright © 2016 Sagar Punhani. All rights reserved.
//

import UIKit
import Parse

class GroceryTableViewController: UITableViewController, UITextFieldDelegate {
    
    var foodImage = ImageUtil.RBSquareImage(UIImage(named: "food")!)
    
    var allImages = [String:UIImage]()
    
    @IBOutlet weak var addGroceryField: UITextField!
    
    @IBOutlet weak var addButton: UIButton!
    
    var subtitles : [String:String]{
        get {
            if let recipes = NSUserDefaults.standardUserDefaults().dictionaryForKey(ImageUtil.groceryKey) {
                return recipes as! [String:String]
            } else {
                return [String:String]()
            }
        } set {
            NSUserDefaults.standardUserDefaults().setValue(newValue, forKey: ImageUtil.groceryKey)
        }
    }
    
    var user = PFUser.currentUser()
    
    var groceryList : [String] {
        get {
            if let grocerys = user!["grocery"] as? [String] {
                return grocerys
            } else {
                return [String]()
            }
            
        }
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        title = "Grocery List"
        addButton.setImage(UIImage(named: "click"), forState: .Highlighted)
        addGroceryField.delegate = self
        let tapper = UITapGestureRecognizer(target: self, action: #selector(GroceryTableViewController.handleTap))
        tapper.cancelsTouchesInView = false
        self.view.addGestureRecognizer(tapper)
        allImages["Custom"] = foodImage
        
        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem()
    }
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        refresh()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    // MARK: - Table view data source

    override func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        // #warning Incomplete implementation, return the number of sections
        return 1
    }

    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return groceryList.count
    }

    
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier("groceryitem", forIndexPath: indexPath)
        //self.rowWidth is the desired Width
        //self.rowHeight is the desired height
        let size : CGFloat = 50
        cell.textLabel!.text = groceryList[indexPath.row]
        cell.textLabel?.lineBreakMode = .ByWordWrapping
        cell.textLabel?.numberOfLines = 0
        if let subtitle = subtitles[groceryList[indexPath.row]] {
            cell.detailTextLabel!.text = subtitle
        } else {
            subtitles[groceryList[indexPath.row]] = "Custom"
        }
        cell.imageView!.layer.borderWidth = 1
        cell.imageView!.layer.masksToBounds = false
        cell.imageView!.layer.borderColor = UIColor.blackColor().CGColor
        cell.imageView!.layer.cornerRadius = cell.imageView!.frame.height/2
        cell.imageView!.clipsToBounds = true
        
        if let image = allImages[subtitles[groceryList[indexPath.row]]!]{
            self.configureCell(cell, image: image, size: size)
        } else {
            
            let query = PFQuery(className: "Recipes")
            query.whereKey("title", equalTo: subtitles[groceryList[indexPath.row]]!)
            query.findObjectsInBackgroundWithBlock({ (objects, error) -> Void in
                if error == nil {
                    dispatch_async(dispatch_get_main_queue(), { () -> Void in
                        if let picture = objects?.first {
                            if let imageData = (picture as PFObject)["image"] {
                                imageData.getDataInBackgroundWithBlock({ (data, error) -> Void in
                                    if (error == nil) {
                                        dispatch_async(dispatch_get_main_queue(), { () -> Void in
                                            var img = UIImage(data: data!)!
                                            img = ImageUtil.RBSquareImage(img)!
                                            self.configureCell(cell, image: img, size: size)
                                            self.allImages[self.subtitles[self.groceryList[indexPath.row]]!] = img
                                            tableView.reloadData()
                                        })
                                    }
                                })
                            }
                        }
                    })
                }
            })
            
            
        }
        
        // Configure the cell...

        return cell
    }
    
    func configureCell(cell: UITableViewCell, image: UIImage, size: CGFloat) {
        cell.imageView!.image = image
        let widthScale = size / image.size.width;
        let heightScale = size / image.size.height;
        //this line will do it!
        cell.imageView!.transform = CGAffineTransformMakeScale(widthScale, heightScale)


    }

    
    @IBAction func addGroceryButton(sender: AnyObject) {
        if let text = addGroceryField.text {
            if text != "" {
                var list = user!["grocery"] as! [String]
                list.append(text)
                user!["grocery"] = list
                user?.saveInBackgroundWithBlock({ (bool, error) -> Void in
                    if error == nil {
                        self.subtitles[text] = "Custom"
                        self.tableView.reloadData()
                    }
                })
            }
        }
    }
    
    func textFieldShouldReturn(textField: UITextField) -> Bool {
        addGroceryField.resignFirstResponder()
        return true
    }
    
    func handleTap() {
        self.view.endEditing(true)
    }
    
    override func tableView(tableView: UITableView, heightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat {
        if groceryList[indexPath.row].characters.count > 780 {
            return 80
        }
        return 60
    }


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

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */
    @IBAction func doneShopping(sender: UIButton) {
        subtitles.removeAll()
        user!["grocery"] = [String]()
        user?.saveInBackgroundWithBlock({ (bool, error) -> Void in
            if error == nil {
                self.tableView.reloadData()
            }
        })
    }
    
    func refresh() {
        user?.fetchInBackgroundWithBlock({ (object, error) -> Void in
            self.tableView.reloadData()
        })
    }

}


//
//  RecipeTableViewController.swift
//  Entree
//
//  Created by Sagar Punhani on 1/6/16.
//  Copyright © 2016 Sagar Punhani. All rights reserved.
//

import UIKit
import Parse

class RecipeTableViewController: UITableViewController {
    
    //modal for all recipes
    var recipesSet = false
    
    var allRecipes: [PFObject]?
    var allImages = [String:UIImage]()
    
    var myRecipes: Set<PFObject> = Set()
    
    var recipeNames : Set<String> = Set()
    
    
    //recipe to send to next mvc
    var currentRecipe = PFObject(className: "Recipes")
    
    //current user
    var user = PFUser.currentUser()
    
    @IBOutlet weak var foodBar: UISegmentedControl!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.tableView.estimatedRowHeight = 120.0
        self.tableView.rowHeight = UITableViewAutomaticDimension
        updateFeed()
        


        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem()
    }
    
    override func viewDidAppear(animated: Bool) {
        updateSet()
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
        return ((allRecipes?.count) != nil) ? allRecipes!.count : 0
    }

    
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier("recipe", forIndexPath: indexPath) as! RecipeTableViewCell
        
        cell.recipe = allRecipes![indexPath.row]
        cell.selectedIndex = (foodBar == nil ? 2 : foodBar.selectedSegmentIndex)
        if cell.selectedIndex == 2 {
            cell.addHeatButton.setTitle("Add to Grocery", forState: .Normal)
            cell.addHeatButton.backgroundColor = UIColor.greenColor()
        } else {
            cell.addHeatButton.setTitle("Add Heat", forState: .Normal)
            cell.addHeatButton.backgroundColor = UIColor.redColor()

        }
        if myRecipes.contains(cell.recipe!) || cell.selectedIndex == 2 {
            cell.heatImage.image = UIImage(named: "fire2")
        } else {
            cell.heatImage.image = UIImage(named: "fire")
        }
        cell.addHeatButton.tag = indexPath.row
        cell.moreInfoButton.tag = indexPath.row
        cell.moreInfoButton.addTarget(self, action: #selector(RecipeTableViewController.selectRecipe(_:)), forControlEvents: UIControlEvents.TouchUpInside)
        
        if let image = allImages[cell.recipe!["title"] as! String]{
            self.configureCell(cell, image: image)
        } else {
            if let imageData = cell.recipe!["image"] {
                imageData.getDataInBackgroundWithBlock({ (data, error) -> Void in
                    if (error == nil) {
                        dispatch_async(dispatch_get_main_queue(), { () -> Void in
                            var img = UIImage(data: data!)!
                            let aspectRatio = img.size.height/img.size.width
                            let newwidth = self.view.bounds.width - 16.0
                            let newheight = newwidth * aspectRatio
                            img = ImageUtil.RBResizeImage(img, targetSize: CGSize(width: newwidth, height: newheight))!
                            self.configureCell(cell, image: img)
                            self.allImages[cell.recipe!["title"] as! String] = img
                            tableView.reloadData()
                        })
                    }
                })
            }
        }
        
        // Configure the cell...

        return cell
    }
    
    func configureCell(cell: RecipeTableViewCell, image: UIImage) {
        cell.foodImage.image = image
        cell.foodImage.contentMode = UIViewContentMode.ScaleAspectFill;
        cell.foodImage.clipsToBounds = true;

    }
    
    override func tableView(tableView: UITableView, heightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat {
        return UITableViewAutomaticDimension
        
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

    
    // MARK: - Navigation
    
    
     //In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
        if segue.identifier == "show recipe" {
            if let twc = segue.destinationViewController as? SpecificRecipeTableViewController {
                twc.recipe = currentRecipe
            }
        }
    }
    
    func selectRecipe(sender: UIButton) {
        currentRecipe = allRecipes![sender.tag]
        performSegueWithIdentifier("show recipe", sender: self)
    }
    
    
    @IBAction func indexChanged(sender: UISegmentedControl) {
        self.tableView.contentOffset = CGPointMake(0, 0 - self.tableView.contentInset.top);
        updateFeed()
    }
    
    func updateSet() {
        let myrecipes = user!["recipes"] as! [PFObject]
        PFObject.fetchAllIfNeededInBackground(myrecipes) { (objects, error) -> Void in
            if error == nil {
                dispatch_async(dispatch_get_main_queue(), { () -> Void in
                    self.myRecipes = Set(objects as! [PFObject])
                    self.tableView.reloadData()
                })
            }
        }
    }
    
    func updateFeed() {
        
        if foodBar != nil {
            switch foodBar.selectedSegmentIndex {
            case 0 :
                fallthrough
            case 1 :
                findObjects()
            case 2 :
                findObjectsForMe(user!)
            default:
                break
            }
        } else {
            findObjectsForMe(user!)
        }
    }

    
    func findObjects() {
        let friends = user!["following"] as! [String]
        var recipes = [PFObject]()
        self.recipeNames.removeAll()

        
        let myrecipes = user!["recipes"] as! [PFObject]
        PFObject.fetchAllIfNeededInBackground(myrecipes) { (objects, error) -> Void in
            if error == nil {
                dispatch_async(dispatch_get_main_queue(), { () -> Void in
                    let allObjects = objects as! [PFObject]
                    
                    for object in allObjects {
                        if !self.recipeNames.contains(object["title"] as! String) {
                            recipes.append(object)
                            self.recipeNames.insert(object["title"] as! String)
                        }
                    }
                    self.allRecipes = recipes
                    self.allRecipes!.sortInPlace({$0.createdAt!.compare($1.createdAt!) == .OrderedDescending  })
                    self.tableView.reloadData()
                })
            }
        }
        
        for friend in friends {
            let friendquery = PFUser.query()
            friendquery!.whereKey("username", equalTo: friend)
            friendquery!.findObjectsInBackgroundWithBlock({ (objects, error) -> Void in
                if error == nil {
                    dispatch_async(dispatch_get_main_queue(), { () -> Void in
                        if let friend = objects!.first as? PFUser {
                            let newrecipes = friend["recipes"] as? [PFObject]
                            PFObject.fetchAllIfNeededInBackground(newrecipes, block: { (objects, error) -> Void in
                                if error == nil {
                                    dispatch_async(dispatch_get_main_queue(), { () -> Void in
                                        let allObjects = objects as! [PFObject]
                                        for object in allObjects {
                                            if !self.recipeNames.contains(object["title"] as! String) {
                                                recipes.append(object)
                                                self.recipeNames.insert(object["title"] as! String)
                                            }
                                        }
                                        self.allRecipes = recipes
                                        self.allRecipes!.sortInPlace({$0.createdAt!.compare($1.createdAt!) == .OrderedDescending  })
                                        self.tableView.reloadData()
                                    })
                                    
                                }
                            })
                        }
                        
                    })
                }
            })

        }
    }
    
    func findObjectsForMe(user: PFUser) {
        var recipes = [PFObject]()
        
        let myrecipes = user["recipes"] as! [PFObject]
        PFObject.fetchAllIfNeededInBackground(myrecipes) { (objects, error) -> Void in
            if error == nil {
                dispatch_async(dispatch_get_main_queue(), { () -> Void in
                    recipes.appendContentsOf(objects as! [PFObject])
                    self.allRecipes = recipes
                    self.allRecipes!.sortInPlace({$0.createdAt!.compare($1.createdAt!) == .OrderedDescending  })
                    self.tableView.reloadData()
                })
            }
        }
        
    }
    


}

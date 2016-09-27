//
//  SpecificRecipeTableViewController.swift
//  Food For Thought
//
//  Created by Sagar Punhani on 6/15/15.
//  Copyright © 2015 Sagar Punhani. All rights reserved.
//

import UIKit
import Parse

class SpecificRecipeTableViewController: UITableViewController {
    
    //current recipe
    var recipe: PFObject? {
        didSet {
            updateUI()
        }
    }


    override func viewDidLoad() {
        super.viewDidLoad()
        updateUI()
        

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem()
    }
    
    override func viewDidAppear(animated: Bool) {
        updateUI()
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
        switch section {
        case 0:
            let ingredients = recipe!["ingredients"] as! [String]
            return ingredients.count
        default:
            let instructions = recipe!["instructions"] as! [String]
            return instructions.count
        }

    }

    //load cells
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        var bgcolor: UIColor {
            if indexPath.row % 2 == 0 {
                return UIColor(red: 240/255, green: 240/255, blue: 250/255, alpha: 1.0)
            }
            return UIColor.whiteColor()
        }
        switch indexPath.section {
        case 0:
            let cell = tableView.dequeueReusableCellWithIdentifier("ingredient", forIndexPath: indexPath) as! IngredientTableViewCell
            let cellIngredients = recipe!["ingredients"] as! [String]
            cell.ingredient = cellIngredients[indexPath.row]
            cell.selectionStyle = UITableViewCellSelectionStyle.None
            cell.backgroundColor = bgcolor
            return cell
        default:
            let cell = tableView.dequeueReusableCellWithIdentifier("instruction", forIndexPath: indexPath) as! InstructionsTableViewCell
            let cellInstructions = recipe!["instructions"] as! [String]
            cell.setOfInstructions = cellInstructions[indexPath.row]
            cell.row = indexPath.row + 1
            cell.selectionStyle = UITableViewCellSelectionStyle.None
            cell.backgroundColor = bgcolor
            return cell

        }
        

        // Configure the cell...

    }
    
    //check marks for ingredients and instructions
    override func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        if let cell = tableView.cellForRowAtIndexPath(indexPath) as? IngredientTableViewCell {
            if cell.plusView.image == UIImage(named: "blue") {
                cell.plusView.image = UIImage(named: "black")
                cell.accessoryType = UITableViewCellAccessoryType.None
            }
            else {
                cell.accessoryType = UITableViewCellAccessoryType.Checkmark
                cell.plusView.image = UIImage(named: "blue")
            }
        }
        /*if let cell = tableView.cellForRowAtIndexPath(indexPath) as? InstructionsTableViewCell {
            if indexPath.row == 0 {
                if cell.accessoryType == UITableViewCellAccessoryType.None {
                    cell.accessoryType = UITableViewCellAccessoryType.Checkmark
                }
                else {
                    cell.accessoryType = UITableViewCellAccessoryType.None
                }
            }
            else {
                if let previousCell = tableView.cellForRowAtIndexPath(NSIndexPath(forRow: indexPath.row - 1, inSection: indexPath.section)) {
                    if previousCell.accessoryType == UITableViewCellAccessoryType.Checkmark && cell.accessoryType == UITableViewCellAccessoryType.None {
                        cell.accessoryType = UITableViewCellAccessoryType.Checkmark
                    }
                    else {
                        cell.accessoryType = UITableViewCellAccessoryType.None
                    }
                }
            }
            tableView.reloadData()
        }*/
    }
    //section view
    override func tableView(tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let sectionView = UIView(frame: CGRect(origin: CGPointZero, size: CGSize(width: self.view.bounds.width, height: 50)))
        sectionView.backgroundColor = UIColor.whiteColor()
        let sectionTitle = UILabel(frame: sectionView.bounds)
        
        switch section {
        case 0: sectionTitle.text = "Ingredients"
        default: sectionTitle.text = "Instructions"
        }
        sectionTitle.textAlignment = .Center
        sectionView.addSubview(sectionTitle)
        sectionView.layer.borderWidth = 1
        
        return sectionView
    }
    
    override func tableView(tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        return 50
    }
    
    override func tableView(tableView: UITableView, heightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat {
        if indexPath.section == 0 {
            return 60
        }
        else {
            return UITableViewAutomaticDimension
        }
    }
    
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    /*override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        
    }*/
    
    //update all the stuff
    func updateUI() {
        title = recipe!["title"] as? String
        if let imageData = recipe!["image"] as? PFFile {
            imageData.getDataInBackgroundWithBlock({ (data, error) -> Void in
                if (error == nil) {
                    dispatch_async(dispatch_get_main_queue(), { () -> Void in
                        self.updateUI(data!)
                    })
                }
            })
        }
    }

    
    func updateUI(data: NSData) {

        
        //image
        let recipeImage = UIImage(data: data)
        let aspectRatio = recipeImage!.size.height/recipeImage!.size.width
        let frame = CGRect(origin: CGPointZero, size: CGSize(width: self.view.bounds.size.width, height: self.view.bounds.size.width * aspectRatio))
        let imageView = UIImageView(frame: frame)
        imageView.contentMode = UIViewContentMode.ScaleToFill
        imageView.image = recipeImage
        
        
        //headerView
        let headerView = UIView(frame: CGRect(origin: CGPointZero, size: CGSize(width: imageView.bounds.size.width, height: imageView.bounds.size.height)))
        headerView.addSubview(imageView)
        self.tableView.tableHeaderView = headerView
        
        //cells
        self.tableView.estimatedRowHeight = 120
        tableView.reloadData()
    }
  
    

}

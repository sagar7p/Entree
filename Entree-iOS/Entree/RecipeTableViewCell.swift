//
//  RecipeTableViewCell.swift
//  Entree
//
//  Created by Sagar Punhani on 1/6/16.
//  Copyright © 2016 Sagar Punhani. All rights reserved.
//

import UIKit
import Parse
import BRYXBanner


class RecipeTableViewCell: UITableViewCell {
    
    var recipe: PFObject? {
        didSet {
            updateUI()
        }
    }
    
    var user = PFUser.currentUser()
    
    @IBOutlet weak var title: UILabel!
    
    @IBOutlet weak var username: UILabel!

    @IBOutlet weak var foodImage: UIImageView!
    
    @IBOutlet weak var date: UILabel!
    
    @IBOutlet weak var reheats: UILabel!
    
    @IBOutlet weak var addHeatButton: UIButton!
    
    @IBOutlet weak var moreInfoButton: UIButton!
    
    @IBOutlet weak var heatImage: UIImageView!
    
    
    //subtitles
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
    
    var selectedIndex = 0
        
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    func updateUI() {
        title.text = recipe!["title"] as? String
        username.text = "@\(recipe!["username"] as! String)"
        let nsdate = recipe?.createdAt
        let formatter = NSDateFormatter()
        formatter.dateStyle = NSDateFormatterStyle.LongStyle
        formatter.timeStyle = NSDateFormatterStyle.ShortStyle
        date.text = formatter.stringFromDate(nsdate!)
        let num = recipe!["reheats"] as? NSNumber
        reheats.text =  num?.stringValue
                
    }

    @IBAction func addHeat(sender: UIButton) {
        
        if selectedIndex < 2 {
            heatImage.image = UIImage(named: "fire2")
            let allRecipes = Set(user!["recipes"] as! [PFObject])
            if !allRecipes.contains(recipe!) {
                let num = recipe!["reheats"] as? NSNumber
                recipe!["reheats"] = num!.integerValue + 1
                recipe?.saveInBackgroundWithBlock({ (success, error) -> Void in
                    if success {
                        dispatch_async(dispatch_get_main_queue(), { () -> Void in
                            self.reheats.text =  (self.recipe!["reheats"] as? NSNumber)?.stringValue
                            
                        })
                    }
                })
                var recipes = user!["recipes"] as! [PFObject]
                recipes.append(recipe!)
                recipes = Array(Set(recipes))
                user!["recipes"] = recipes
                user?.saveInBackground()
            }
        } else {
            if var grocery = user!["grocery"] as? [String] {
                grocery.appendContentsOf(recipe!["ingredients"] as! [String])
                user!["grocery"] = grocery
                user?.saveInBackground()
            } else {
                user!["grocery"] = recipe!["ingredients"] as! [String]
                user?.saveInBackground()
            }
            for item in recipe!["ingredients"] as! [String] {
                subtitles[item] = recipe!["title"] as? String
            }
            
            let banner = Banner(title: "Added to Grocery", subtitle: "Added \(title.text!) to Grocery List.", image: nil
                , backgroundColor: UIColor(red:48.00/255.0, green:174.0/255.0, blue:51.5/255.0, alpha:1.000))
            banner.dismissesOnTap = true
            banner.show(duration: 2.0)
        }
        
       

        
    }
    
    @IBAction func moreInfo(sender: UIButton) {
        
    }
    
}

//
//  MyRecipeTableViewCell.swift
//  Entree
//
//  Created by Sagar Punhani on 1/10/16.
//  Copyright © 2016 Sagar Punhani. All rights reserved.
//

import UIKit
import Parse
import BRYXBanner

class MyRecipeTableViewCell: UITableViewCell {

    var recipe: PFObject? {
        didSet {
            updateUI()
        }
    }
    
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
    
    @IBOutlet weak var title: UILabel!
    
    @IBOutlet weak var username: UILabel!
    
    @IBOutlet weak var foodImage: UIImageView!
    
    @IBOutlet weak var date: UILabel!
    
    @IBOutlet weak var reheats: UILabel!
    
    @IBOutlet weak var addToGrocery: UIButton!
    
    @IBOutlet weak var moreInfoButton: UIButton!
    
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
    
    @IBAction func addToGrocery(sender: UIButton) {
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

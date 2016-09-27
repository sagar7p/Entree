//
//  IngredientTableViewCell.swift
//  Food For Thought
//
//  Created by Sagar Punhani on 6/15/15.
//  Copyright © 2015 Sagar Punhani. All rights reserved.
//

import UIKit
import Parse

class IngredientTableViewCell: UITableViewCell {
    
    var ingredient: String? {
        didSet {
            updateUI()
        }
    }

    @IBOutlet weak var plusView: UIImageView!
    @IBOutlet weak var ingredientDescriptionLabel: UILabel!
    
    func updateUI() {
        
        ingredientDescriptionLabel.text = ingredient
    }
}

//
//  InstructionsTableViewCell.swift
//  Food For Thought
//
//  Created by Sagar Punhani on 6/16/15.
//  Copyright © 2015 Sagar Punhani. All rights reserved.
//

import UIKit

class InstructionsTableViewCell: UITableViewCell {

    var setOfInstructions: String? {
        didSet {
            updateUI()
        }
    }
    
    var row: Int? {
        didSet {
            updateUI()
        }
    }
    
    @IBOutlet weak var stepNumber: UILabel!
    

    @IBOutlet weak var instructions: UILabel!
    
    func updateUI() {
       if let currentRow = row {
            let attributedString = NSMutableAttributedString(string: "\(currentRow).", attributes: [NSFontAttributeName : UIFont.boldSystemFontOfSize(20)])
            stepNumber.attributedText = attributedString
        }
        instructions.text = setOfInstructions!
    }
    
}

//
//  SearchTableViewController.swift
//  Entree
//
//  Created by Sagar Punhani on 1/28/16.
//  Copyright © 2016 Sagar Punhani. All rights reserved.
//

import UIKit
import Parse

class SearchTableViewController: UITableViewController {
    
    var filteredSearchResults = [PFUser]()
    
    var allUsers = [PFUser]()
    
    let searchController = UISearchController(searchResultsController: nil)

    override func viewDidLoad() {
        super.viewDidLoad()
        title = "Search"
        searchController.searchResultsUpdater = self
        searchController.dimsBackgroundDuringPresentation = false
        definesPresentationContext = true
        tableView.tableHeaderView = searchController.searchBar
        searchController.searchBar.scopeButtonTitles = ["Users", "Recipes"]
        searchController.searchBar.delegate = self
        PFUser.query()?.findObjectsInBackgroundWithBlock({ (objects, error) -> Void in
            if error == nil {
                self.allUsers = objects as! [PFUser]
            }
        })
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
        return 1
    }

    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        if searchController.active && searchController.searchBar.text != "" {
            return filteredSearchResults.count
        }
        return 0
    }
    
    func filterContentForSearchText(searchText: String, scope: String = "All") {
        filteredSearchResults = allUsers.filter({ (user) -> Bool in
            let fullName = "\(user["first"] as! String) \(user["last"] as! String)\(user["username"] as! String)"
            //check category here
            let categoryMatch = (scope == "Users")
            return categoryMatch && fullName.lowercaseString.containsString(searchText.lowercaseString)
            
        })
        
        tableView.reloadData()
    }
    
    

    
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier("search", forIndexPath: indexPath)
        if searchController.active && searchController.searchBar.text != "" {
            let user = self.filteredSearchResults[indexPath.row]
            cell.textLabel?.text = "\(user["first"] as! String) \(user["last"] as! String)"
            cell.detailTextLabel?.text = "@\(user["username"] as! String)"
        }
                return cell
    }
    
    override func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        switch searchController.searchBar.selectedScopeButtonIndex {
        case 0 :
            performSegueWithIdentifier("show friend", sender: filteredSearchResults[indexPath.row])
        default :
            break
        }
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

}

extension SearchTableViewController: UISearchResultsUpdating {
    func updateSearchResultsForSearchController(searchController: UISearchController) {
        let searchBar = searchController.searchBar
        let scope = searchBar.scopeButtonTitles![searchBar.selectedScopeButtonIndex]
        filterContentForSearchText(searchController.searchBar.text!, scope: scope)    }
}

extension SearchTableViewController : UISearchBarDelegate {
    func searchBar(searchBar: UISearchBar, selectedScopeButtonIndexDidChange selectedScope: Int) {
        filterContentForSearchText(searchBar.text!, scope: searchBar.scopeButtonTitles![selectedScope])
    }
}

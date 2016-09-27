//
//  MBObject.h
//
//  Copyright (c) 2014 moBack, Inc. All rights reserved.
//

#import <moBack/MBConstants.h>

@class MBQuery;
@class MBRelation;
@class MBAccessControlList;

/*!
 *  The MBObject represents one row of data on the currently referenced app, from specific table.
 */
@interface MBObject : NSObject {
    @public
    BOOL unSaved;
    NSDictionary *oldObjectData;
    NSMutableDictionary *currentObjectData;
}

/*!
 *  @brief The object identifier
 *
 *  @discussion This is the only indicator whether or not the object is saved in the backend or not. If objectId is contained, object is saved. Otherwise, no.
 */
@property (strong, nonatomic, readonly, nullable) NSString *objectId;

/*!
 *  @brief The created date of the object
 */
@property (strong, nonatomic, readonly, nullable) NSDate *createdAt;

/*!
 *  @brief The modified date of the object
 */
@property (strong, nonatomic, readonly, nullable) NSDate *updatedAt;

/*!
 *  @brief The name of the table that
 */
@property (strong, nonatomic, nonnull) NSString *tableName;

/*!
 *  @brief The name of the table that
 */
@property (strong, nonatomic, nullable) MBAccessControlList *accessControlList;

/*!
 *  @brief Creates a MBObject on a table of a specific name.
 *
 *  @discussion After saving, a new table will be created on moBack with the table name if no table exists.
 *
 *  @param tableName    The name of the table on which the Object will be created
 *
 *  @return Newly initialized object with table name
 */
- (nonnull instancetype)initWithTableName:(nonnull NSString *)tableName;

/*!
 *  @brief Creates a MBObject on a table of a specific name with objectId. This can be treated as pointer type.
 *
 *  @discussion After saving, a new table will be created on moBack with the table name if no table exists.
 *
 *  @param tableName    The name of the table on which the Object will be created
 *  @param objectId     The object identifier
 *
 *  @return Newly initialized object with table name and objectId
 */
- (nonnull instancetype)initWithTableName:(nonnull NSString *)tableName objectId:(nonnull NSString *)objectId;

/*!
 *  @brief Find out whether the current object is saved/deleted successfully
 *
 *  @discussion It's NO by defualt, and will be set to YES if the object is saved/deleted successfully
 *
 *  @return Whetehr the object is saved/deleted successfully
 */
- (BOOL)isUnSaved;

/*!
 *  @brief Find out all the available columns the object has
 *
 *  @return An array of available column strings
 */
- (nonnull NSArray *)getColumns;

/*!
 *  @brief Saves the changes of one row of data from specific table asynchronously
 *
 *  @discussion If object is not created, a new object will be created in the moBack. Otherwise, object will be updated. Note that new column will be added if new column is stored in the object. moBack will make sure that the data is saved properly eventually
 */
- (void)saveEventually;

/*!
 *  @brief Saves the changes of one row of data from specific table asynchronously with completion block
 *
 *  @discussion Result will be returned with the completion block without blocking the current thread
 *
 *  @param completion   The completion call-back block for the results
 */
- (void)saveWithCompletionHandler:(nonnull MBCompletionBlockError)completionHandler;

/*!
 *  @brief Saves the changes of one row of data from specific table synchronously
 *
 *  @discussion It will block the current thread until it is finished
 *
 *  @return     An NSError object for indicating the error message
 */
- (nullable NSError *)save;

/*!
 *  @brief Saves the changes of multiple rows of data from specific table synchronously
 *
 *  @discussion It will block the current thread until it is finished
 *
 *  @param objects    An array of MBObjects from the same table that will be saved
 *
 *  @return     An NSError object for indicating the error message
 */
+ (BOOL)saveAll:(nullable NSArray *)objects;

/*!
 *  @brief Removes one row of data from specific table asynchronously
 *
 *  @discussion If object is removed successfully, objectId will be nil-out and you can not access this record anymore. SDK will make sure that data is removed properly.
 */
- (void)removeEventually;

/*!
 *  @brief Removes one row of data from specific table asynchronously
 *
 *  @discussion Result will be returned with the completion block without blocking the current thread
 *
 *  @param completion   The completion call-back block for the results
 */
- (void)removeWithCompletionHandler:(nonnull MBCompletionBlockError)completionHandler;

/*!
 *  @brief Removes one row of data from specific table synchronously
 *
 *  @discussion It will block the current thread until the task is finished
 *
 *  @return     An NSError object for indicating the error message
 */
- (nullable NSError *)remove;

/*!
 *  @brief Removes multiple rows of data from specific table synchronously
 *
 *  @discussion It will block the current thread until it is finished
 *
 *  @param objects    An array of MBObjects from the same table that will be deleted
 *
 *  @return     An NSError object for indicating the error message
 */
+ (BOOL)removeAll:(nullable NSArray *)objects;

/*!
 *  @brief Reload one row of data asynchronously
 *
 *  @discussion It will make sure that the object is up-to-date due to update calls might be performed in other places
 *
 *  @param completion   The completion call-back block for the results
 */
- (void)reloadWithCompletionHandler:(nonnull MBCompletionBlockError)completionHandler;

/*!
 *  @brief Reload one row of data synchronously
 *
 *  @discussion It will make sure that the object is up-to-date due to update calls might be performed in other places
 *
 *  @return     An NSError object for indicating the error message
 */
- (nullable NSError *)reload;

/*!
 *  @brief Return the MBQuery object with specific table name
 */
- (nullable MBQuery *)query;

/*!
 *  @brief Return the MBRelation object with specific table name
 *
 *  @discussion Before doing any opertions on relation, you have to save the current object first. Otherwise, it won't relate
 *
 *  @param column   The column to describe the relation
 */
- (nullable MBRelation *)relationWithColumn:(nonnull NSString *)column;

/*!
 *  @brief Returns the associated value from a given column
 *
 *  @discussion     Implement this method for enabling the literal syntax of dictioanry without subclassing from NSMutableDictionary
 *
 *  @param column   The column for which to return the corresponding value
 *
 *  @return         correspoding value if valid
 */
- (nullable id)objectForKeyedSubscript:(nonnull NSString *)column;

/*!
 *  @brief Sets the value to a given column
 *
 *  @discussion     Implement this method for enabling the literal syntax of dictioanry without subclassing from NSMutableDictionary
 *
 *  @param obj      The corresponding value
 *  @param column   The column for which to return the corresponding value
 */
- (void)setObject:(nullable id)obj forKeyedSubscript:(nonnull NSString *)column;

#pragma mark - Array Manipulation
/*!
 *  @brief Add new values to an existing array
 *
 *  @param value    The new values that are to be added
 *  @param column   The column for which to return the corresponding value which is an array
 */
- (void)addObjects:(nonnull NSArray *)values toArrayWithColumn:(nonnull NSString *)column;

/*!
 *  @brief Add unique new values to an existing array
 *
 *  @param value    The new unique values that are to be added
 *  @param column   The column for which to return the corresponding value which is an array
 */
- (void)addUniqueObjects:(nonnull NSArray *)values toArrayWithColumn:(nonnull NSString *)column;

/*!
 *  @brief Remove all occurences of values from an existing array
 *
 *  @param value    The values that are to be removed
 *  @param column   The column for which to return the corresponding value which is an array
 */
- (void)removeObjects:(nonnull NSArray *)valuess fromArrayWithColumn:(nonnull NSString *)column;

@end
//
//  MBRelation.h
//
//  Copyright (c) 2014 moBack, Inc. All rights reserved.
//

#import <moBack/MBObject.h>

@class MBQuery;

NS_ASSUME_NONNULL_BEGIN

/*!
 *  @brief The MBRelation represents a relation added on a particular object
 */
@interface MBRelation : NSObject

/*!
 *  @brief The objects that associate with the relation
 */
@property (strong, nonatomic, readonly) NSArray *objects;

/*!
 *  @brief The table name of the targeted objects
 */
@property (strong, nonatomic) NSString *tableName;

/*!
 *  @brief The name of the relation
 */
@property (strong, nonatomic) NSString *columnName;

/*!
 *  @brief  Initializes a new instance of MBQuery from the relation
 *
 *  @return     Newly initialized MBQuery object from the relation
 */
- (MBQuery *)query;

/*!
 *  @brief Add an object to the relation
 */
- (void)addObject:(MBObject *)object;

/*!
 *  @brief Remove an object to the relation
 */
- (void)removeObject:(MBObject *)object;

/*!
 *  @brief  Gets whether the object is present in the relation
 *
 *  @param object A MBObject object
 *
 *  @return `YES` if object is present in the relation
 */
- (BOOL)containsObject:(MBObject *)object;

NS_ASSUME_NONNULL_END

@end

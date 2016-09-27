//
//  MBACL.h
//
//  Copyright (c) 2015 ReliableCoders. All rights reserved.
//

#import <moBack/MBConstants.h>

@class MBUser;
@class MBRole;

@interface MBAccessControlList : NSObject

NS_ASSUME_NONNULL_BEGIN

/*!
 *  @brief  Initializes a MBAccessControlList object
 *
 *  @return Newly initialized acl
 */
- (instancetype)initAccessControlList;

/*!
 *  @brief Initializes a MBAccessControlList object with a user object
 *
 *  @param      user    The user object to assign access
 *
 *  @return Newly initialized acl with granted user
 */
- (instancetype)initWithUser:(MBUser *)user;

#pragma mark - Global access control

/*!
 *  @brief  Gets whether it's allowed to read this object by all users
 *
 *  @return `YES` if all users' read access is enabled
 */
- (BOOL)getGlobalRead;

/*!
 *  @brief  Set whether it's allowed to read this object by all users
 *
 *  @param allowed Whether all users can read this object
 */
- (void)setGlobalRead:(BOOL)allowed;

/*!
 *  @brief  Gets whether it's allowed to write this object by all users
 *
 *  @return `YES` if all users' write access is enabled
 */
- (BOOL)getGlobalWrite;

/*!
 *  @brief  Set whether it's allowed to write this object by all users
 *
 *  @param allowed Whether all users can write this object
 */
- (void)setGlobalWrite:(BOOL)allowed;

#pragma mark - User access control list

/*!
 *  @brief  Gets whether it's allowed to read this object by a user object
 *
 *  @return `YES` if the user's read access is enabled
 */
- (BOOL)getReadAccessForUser:(MBUser *)user;

/*!
 *  @brief  Set whether it's allowed to read this object by a user object
 *
 *  @param allowed Whether the user can read this object
 */
- (void)setReadAccess:(BOOL)allowed
              forUser:(MBUser *)user;

/*!
 *  @brief  Gets whether it's allowed to write this object by a user object
 *
 *  @return `YES` if the user's write access is enabled
 */
- (BOOL)getWriteAccessForUser:(MBUser *)user;

/*!
 *  @brief  Set whether it's allowed to write this object by a user object
 *
 *  @param allowed Whether the user can write this object
 */
- (void)setWriteAccess:(BOOL)allowed
               forUser:(MBUser *)user;

#pragma mark - Group access control list

/*!
 *  @brief  Gets whether it's allowed to read this object by a role object
 *
 *  @return `YES` if that role's read access is enabled
 */
- (BOOL)getReadAccessForRole:(MBRole *)role;

/*!
 *  @brief  Set whether it's allowed to read this object by a role object
 *
 *  @param allowed Whether users within the role object can read this object
 */
- (void)setReadAccess:(BOOL)allowed
              forRole:(MBRole *)role;

/*!
 *  @brief  Gets whether it's allowed to write this object by a role object
 *
 *  @return `YES` if that role's write access is enabled
 */
- (BOOL)getWriteAccessForRole:(MBRole *)role;

/*!
 *  @brief  Set whether it's allowed to write this object by a role object
 *
 *  @param allowed Whether users within the role object can write this object
 */
- (void)setWriteAccess:(BOOL)allowed
               forRole:(MBRole *)role;

NS_ASSUME_NONNULL_END

@end

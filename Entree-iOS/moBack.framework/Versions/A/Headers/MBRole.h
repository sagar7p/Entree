//
//  MBRole.h
//
//  Copyright (c) 2015 ReliableCoders. All rights reserved.
//

#import <moBack/MBConstants.h>
#import <moBack/MBObject.h>
#import <moBack/MBUser.h>

@class MBRelation;
@class MBAccessControlList;

@interface MBRole : MBObject

/*!
 *  @brief  The name of the role
 */
@property (strong, nonatomic, readonly) NSString *roleName;

/*!
 *  @brief  Members of the role
 */
@property (strong, nonatomic, readonly) MBRelation *memebers;

/*!
 *  @brief  Initializes a MBRole object with specified name
 *
 *  @param roleName The name of the role, it has to be unique
 *
 *  @return Newly initialized role
 */
- (instancetype)initWithRoleName:(NSString *)roleName;

/*!
 *  @brief  Initializes a MBRole object with specified name and pre-defined acl
 *
 *  @param name              The name of the role, it has to be unique
 *  @param accessControlList The acl object to grant the role
 *
 *  @return Newly initialized role with name and pre-defined acl
 */
- (instancetype)initWithRoleName:(NSString *)name
               accessControlList:(MBAccessControlList *)accessControlList;

/*!
 *  @brief  Add member to the role
 *
 *  @param member A user object to be added to the role
 */
- (void)addMemeber:(MBUser *)member;

/*!
 *  @brief  Remove member from the role
 *
 *  @param member A user object to be removed from the role
 */
- (void)removeMember:(MBUser *)member;

@end
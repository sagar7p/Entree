//
//  MBUser.h
//
//  Copyright (c) 2014 moBack, Inc. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <moBack/MBObject.h>

/*!
 *  The MBUser represents a single app user from User table which is the default table
 */
@interface MBUser : MBObject

/*!
 *  @brief The MBUser object's name
 */
@property (strong, nonatomic, nullable) NSString *userId;

/*!
 *  @brief The MBUser object's email
 */
@property (strong, nonatomic, nullable) NSString *email;

/*!
 *  @brief The MBUser object's password
 */
@property (strong, nonatomic, nullable) NSString *password;

/*!
 *  @brief Get the current instance of MBUser
 *
 *  @discussion It will return the same instance of MBUser as long as you log in. moBack will regain the session token for you if necessary. You will get a nil object if you sign out.
 *
 *  @return returns an instance of MBUser
 */
+ (nullable instancetype)currentUser;

/*!
 *  @brief  Get the current session token for the current user
 *
 *  @return returns a string of session token
 */
+ (nullable NSString *)sessionToken;

/*!
 *  @brief Initializes an instance of MBUser
 *
 *  @discussion userId will be used as userId for login. You can set up your email here but you can't use email as userId for logging in.
 *
 *  @param userId    The user identifier
 *  @param password    The password
 *
 *  @return Newly initialized user with userId and password
 */
- (nonnull instancetype)initWithUserId:(nonnull NSString *)userId
                              password:(nonnull NSString *)password;

/*!
 *  @brief Initializes an instance of MBUser
 *
 *  @discussion userId and email will be used as userId for login. You can use email as userId for logging in.
 *
 *  @param email        The email
 *  @param password     The password
 *
 *  @return Newly initialized user with email and password
 */
- (nonnull instancetype)initWithEmail:(nonnull NSString *)email
                             password:(nonnull NSString *)password;

/*!
 *  @brief  Initializes an instance of MBUser with objectId. This can be treated pointer type.
 *
 *  @param objectId The objectId from User table
 *
 *  @return Newly initialized user with objectId
 */
- (nullable instancetype)initWithUserWithObjectId:(nonnull NSString *)objectId;

/*!
 *  @brief Creates an instance of MBUser from the User table asynchronously
 *
 *  @discussion Before calling this method, you have to pass in the username or email fields and the password field are required. Once the call finishes, password field will be cleared.
 *
 *  @param completion   The completion call-back block for the results
 */
- (void)signUpUserWithCompletionBlock:(nonnull MBCompletionBlockObject)completion;

/*!
 *  @brief  Creates an instance of MBUser from the User table synchronously
 *
 *  @param  error The pointer of an address of NSError to indicate error information, nil if no error
 *
 *  @return The logged user
 */
- (nullable MBUser *)signUpUser:(NSError * __nullable * __nullable)error;

/*!
 *  @brief Log in user asynchronously
 *
 *  @discussion Before calling this method, you have to pass in the username or email fields and the password field are required. Once the call finishes, password field will be cleared. After login, this user object should be discarded and + (instancetype)currentUser should always be the choice to get the logged-in user
 *
 *  @param completion   The completion call-back block for the results
 */
- (void)loginUserWithCompletionBlock:(nonnull MBCompletionBlockObject)completion;

/*!
 *  @brief Log in user synchronously
 *
 *  @discussion Before calling this method, you have to pass in the username or email fields and the password field are required. Once the call finishes, password field will be cleared. After login, this user object should be discarded and + (instancetype)currentUser should always be the choice to get the logged-in user
 *
 *  @param  error The pointer of an address of NSError to indicate error information, nil if no error
 *
 *  @return The logged user
 */
- (nullable MBUser *)loginUser:(NSError * __nullable * __nullable)error;

/*!
 *  @brief  Log out user synchronously
 */
+ (void)signOut;

/*!
 *  @brief Reset password by given userId asynchronously
 *
 *  @discussion moBack backend will automatically send an email associated with given userId. Users can reset their password in that email
 *
 *  @param userId       The userId to receive the reset password email
 *  @param completion   The completion call-back block for the results
 */
+ (void)resetPasswordWithUserId:(nonnull NSString *)userId
                     completion:(nonnull MBCompletionBlockError)completion;

/*!
 *  @brief Reset password by given userId synchronously
 *
 *  @discussion moBack backend will automatically send an email associated with given userId. Users can reset their password in that email
 *
 *  @param userId       The userId to receive the reset password email
 *  @param completion   The completion call-back block for the results
 */
+ (nullable NSError *)resetPasswordWithUserId:(nonnull NSString *)userId;

/*!
 *  @brief Reset password by given email asynchronously
 *
 *  @discussion moBack backend will automatically send an email to the given email address. Users can reset their password in that email
 *
 *  @param email        The email address to receive the reset password email
 *  @param completion   The completion call-back block for the results
 */
+ (void)resetPasswordWithEmail:(nonnull NSString *)email
                    completion:(nonnull MBCompletionBlockError)completion;

/*!
 *  @brief Reset password by given email synchronously
 *
 *  @discussion moBack backend will automatically send an email to the given email address. Users can reset their password in that email
 *
 *  @param email        The email address to receive the reset password email
 *  @param completion   The completion call-back block for the results
 */
+ (nullable NSError *)resetPasswordWithEmail:(nonnull NSString *)email;


@end

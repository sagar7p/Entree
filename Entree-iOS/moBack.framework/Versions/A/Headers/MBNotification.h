//
//  MBNotification.h
//
//  Copyright (c) 2014 moBack, Inc. All rights reserved.
//

#import <moBack/MBConstants.h>

/*!
 *  The MBNotification handles application-level operation and user-level operation.
 */
@interface MBNotification : NSObject

/*!
 *  @brief The device token of current device
 */
@property (strong, nonatomic, readonly, nonnull) NSString *deviceToken;

/*!
 *  @brief The channels that current device token associates with
 */
@property (strong, nonatomic, readonly, nullable) NSArray *channels;

/*!
 *  @brief Set up device token for current device
 *
 *  @discussion You will need to call this method in - application:didRegisterForRemoteNotificationsWithDeviceToken: 
    in order to use notification service
 *
 *  @param deviceToken  The device token you get from - application:didRegisterForRemoteNotificationsWithDeviceToken: 
 */
+ (void)setDeviceToken:(nonnull NSData *)deviceToken;

/*!
 *  @brief Initializes the shared Push Notification object
 *
 *  @return The shared MBNotification object
 */
+ (nonnull instancetype)sharedManager;

/*!
 *  @brief Registers for Notifications with a device token to all channels synchronously.
 *
 *  @return error   error information
 */
- (nullable NSError *)registerNotification;

/*!
 *  @brief Registers for Notifications with a device token to custom channels synchronously.
 *
 *  @return error   error information
 */
- (nullable NSError *)registerNotificationWithChannels:(nullable NSArray *)channels;

/*!
 *  @brief Registers for Notifications with a device token to custom channels asynchronously.
 *
 *  @discussion When being called in - application:didRegisterForRemoteNotificationsWithDeviceToken:, it will subscribe default channel only. You can subscribe custom channels after logging in only
 *
 *  @param channels     The array of String that contains your custom channels
 *  @param completion   The completion call-back block for the results
 */
- (void)registerNotificationWithChannels:(nullable NSArray *)channels
                       completionHandler:(nullable MBCompletionBlockError)completion;

/*!
 *  @brief Deregisters user for Notifications with a device token from all channels synchronously.
 *
 *  @return An NSError object for indicating the error message
 */
- (nullable NSError *)deregisterNotification;

/*!
 *  @brief Deregisters user for Notifications with a device token from custom channels synchronously.
 *
 *  @param channels     The array of String that contains your custom channels
 *
 *  @return An NSError object for indicating the error message
 */
- (nullable NSError *)deregisterNotificationWithChannels:(nullable NSArray *)channels;

/*!
 *  @brief Deregisters user for Notifications with a device token from custom channels asynchronously.
 *
 *  @discussion It will deregister the current device token from all channels if you don't specify channels
 *
 *  @param channels     The array of String that contains your custom channels
 *  @param completion   The completion call-back block for the results
 */
- (void)deregisterNotificationWithChannels:(nullable NSArray *)channels
                         completionHandler:(nullable MBCompletionBlockError)completionHandler;

/*!
 *  @brief Send a Push message to a specified user by given objectId asynchronously
 *
 *  @discussion message and userId are required fields that are essential information to a push notification.
 *
 *  @param message      The push message
 *  @param userId       The user's object id
 *  @param badge        The number displayed on the top right corner of app icon
 *  @param sound        The name of the sound file 
 *  @param userInfo     The additional user info
 *  @param completion   The completion call-back block for the results
 */
+ (void)sendNotificationWithMessage:(nonnull NSString *)message
                             userId:(nonnull NSString *)userId
                              badge:(nullable NSNumber *)badge
                              sound:(nullable NSString *)sound
                           userInfo:(nullable NSDictionary *)userInfo
                  completionHandler:(nullable MBCompletionBlockError)completionHandler;

/*!
 *  @brief Send a Push message to a specified channel asynchronously
 *
 *  @discussion message is required field that is essential information to a push notification.
 *
 *  @param message      The push message
 *  @param channels     The array of String that contains your custom channels
 *  @param badge        The number displayed on the top right corner of app icon
 *  @param sound        The name of the sound file
 *  @param userInfo     The additional user info
 *  @param completion   The completion call-back block for the results
 */
+ (void)sendNotificationWithMessage:(nonnull NSString *)message
                           channels:(nullable NSArray *)channels
                              badge:(nullable NSNumber *)badge
                              sound:(nullable NSString *)sound
                           userInfo:(nullable NSDictionary *)userInfo
                  completionHandler:(nullable MBCompletionBlockError)completionHandler;
@end

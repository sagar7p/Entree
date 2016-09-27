//
//  MoBack.h
//  
//  Copyright (c) 2014 moBack, Inc. All rights reserved.
//

#import <moBack/MBObject.h>
#import <moBack/MBQuery.h>
#import <moBack/MBFile.h>
#import <moBack/MBNotification.h>
#import <moBack/MBUser.h>
#import <moBack/MBGeoPoint.h>
#import <moBack/MBRelation.h>
#import <moBack/MBRole.h>
#import <moBack/MBAccessControlList.h>
#import <moBack/MBImageView.h>

/*!
 *  The MoBack handles application-level's data and configuration.
 */
@interface moBack : NSObject

NS_ASSUME_NONNULL_BEGIN

#pragma mark Moback Setup
/*!
 *  @brief Sets An ApplicationID and Enviornment Key for the application, which are retrieved from the main moBack dashboard. The dashboard is accessible after app-creation on moback.com
 *
 *  @param applicationId  The Unique Application Key
 *  @param enviornmentKey The Development or Production Enviornment Key
 */
+ (void)setApplicationId:(NSString *)applicationId
          environmentKey:(NSString *)environmentKey;

/*!
 *  @brief Sets An ApplicationID and Enviornment Key for the application, which are retrieved from the main moBack dashboard for enterprise users. The dashboard is accessible after app-creation on enterprise user's website
 *
 *  @param applicationId  The Unique Application Key
 *  @param enviornmentKey The Development or Production Enviornment Key
 *  @param baseUrl        The base URL for the enterprise users
 */
+ (void)setApplicationId:(NSString *)applicationId
          environmentKey:(NSString *)environmentKey
                 baseUrl:(NSString *)baseUrl;

/*!
 *  @brief Sets the type of logging, which will print out related information in the console when interactiving with moBack
 *
 *  @param type  The type of logging
 */
+ (void)setLoggingType:(MBLoggingType)type;

/*!
 *  @brief Return the version string of the moback SDK
 * 
 *  moback SDK version: 1.2.1
 */
+ (NSString *)mobackSDKVersion;

NS_ASSUME_NONNULL_END

@end

//
//  MBConstants.h
//
//  Copyright (c) 2014 moBack, Inc. All rights reserved.
//

@class MBObject;
#import <Foundation/Foundation.h>

#ifndef MBConstants_h
#define MBConstants_h

typedef void (^MBCompletionBlockError)              ( NSError * __nullable error);
typedef void (^MBCompletionBlockObject)             (__nullable id result,  NSError * __nullable error);
typedef void (^MBCompletionBlockArray)              ( NSArray * __nullable array,  NSError * __nullable error);
typedef void (^MBCompletionProgressBlockFile)       (__nullable id result, double progress, NSError * __nullable error);
//typedef void (^MBCompletionBlockArray)              (NSArray<MBObject *> *array, NSError *error);

NS_ASSUME_NONNULL_BEGIN

#pragma mark - moback error domain
extern NSString *const MBNetworkingErrorDomain;
extern NSString *const MBObjectErrorDomain;
extern NSString *const MBRelationErrorDomain;
extern NSString *const MBUserErrorDomain;
extern NSString *const MBFileErrorDomain;
extern NSString *const MBQueryErrorDomain;
extern NSString *const MBNotificationErrorDomain;

extern NSString *const MBInternalErrorDomain;

NS_ASSUME_NONNULL_END

#pragma mark - moback error code for networking
static NSInteger const MBEnvironmentSetupErrorDomain = 2000;   //  Error to indicate the environment or application identifier is not set up properly
static NSInteger const MBJsonErrorDomain   = 2001;     // Error to indicate json serialization or deserialization problem
static NSInteger const MBSessionInvalidErrorDomain = 2002;     // Error to indicate the session token is not set or invalid
static NSInteger const MBBackendError = 2003;
static NSInteger const MBRequestBodyExceedSizeLimit = 2004;

#pragma mark - moback error code for object manager
static NSInteger const kMBErrorSetterMissingColumnOrValue = 1080;
static NSInteger const kMBErrorSetterDataTypeMismatch = 1081;
static NSInteger const kMBErrorNoNeedToUpdate = 1082;
static NSInteger const kMBErrorMissingObjectId = 1083;
static NSInteger const kMBErrorArrayManipulation = 1090;
static NSInteger const kMBErrorRelationManipulation = 1091;


#pragma mark - moback error code for query manager
static NSInteger const kMBErrorInvalidColumnType = 1030;
static NSInteger const kMBErrorUnsupportedDataTypeForQuery = 1032;
static NSInteger const kMBErrorInvalidConstraints = 1034;
static NSInteger const kMBErrorInvalidGeoUnitType = 1035;

#pragma mark - moback error code for user manager
static NSInteger const kMBErrorInvalidUserIdForResetPassword = 3002;       // Error to indicate userId is invalid when resetting password
static NSInteger const kMBErrorPasswordMissing = 3008;     // Error to indicate userId is missing when register user
static NSInteger const kMBErrorUserIdMissing = 3009;       // Error to indicuate password is missing when register user
static NSInteger const kMBErrorAccountExisting = 3011;     // Error to indicate duplicate account when register user
static NSInteger const kMBErrorInvalidCredential = 3013;       // Error to indicate credential is not correct when login user
static NSInteger const kMBErrorInvalidCallbackForSocialLogin = 3030;
static NSInteger const kMBErrorLoginCancel = 3031;

static NSInteger const kMBErrorObjectDataTypeMismatch = 111;

#pragma mark - moback error code for file manager
static NSInteger const kMBErrorFileExtensionMissing = 7000;        // Error to indicate file extension is missing
static NSInteger const kMBErrorFileEmptyContent = 7001;        // Error to indicate file content is empty
static NSInteger const kMBErrorFileDoesNotExist = 7002;        // Error to indicate file does not exist, it means that url is missing
static NSInteger const kMBErrorFileExceedSizeLimit = 7003;

#pragma mark - moback error code for notification manager
static NSInteger const kMBErrorNotificationEmptyToken = 9000;      // Error to indicate token is empty

#endif

typedef enum : NSUInteger {
    MBLoggingErrorOnly = 0,
    MBLoggingNone
} MBLoggingType;



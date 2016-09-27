//
//  MBFile.h
//
//  Copyright (c) 2014 moBack, Inc. All rights reserved.
//

#import <moBack/MBConstants.h>

/*!
 *  The MBFile represents the data object stored as a file.
 */
@interface MBFile : NSObject

/*!
 *  The name of the file.
 */
@property (strong, nonatomic, nonnull) NSString *name;

/*!
 *  The URL the file is reading or writing.
 */
@property (strong, nonatomic, nullable) NSString *url;

/*!
 *  The data object containing the content associated with the file.
 */
@property (strong, nonatomic, nullable) NSData *data;

/*!
 *  Initializes a MBFile object with specified data object and name of the file.
 *
 *  @discussion The maxiumn data size is 10 MB.
 *
 *  @param      data     The data object containing the content associated with the file.
 *  @param      name     The name of the file.
 *
 *  @return     Newly initialized item with the specified data object and file.
 */
- (nonnull instancetype)initWithData:(nonnull NSData *)data fileName:(nullable NSString *)name;

/*!
 *  Saves the file object to the backend asynchronously.
 *
 *  @discussion If file is saved to backend successfully, an url will be appended. The file name will be modified by adding a sequence of number. 
 *
 *  @param completionHandler   The handler to call when the save is done.
 */
- (void)saveWithCompletionHandler:(nonnull MBCompletionBlockError)completionHandler;

/*!
 *  Saves the file object to the backend synchronously.
 *
 *  @return     An NSError object for indicating the error message.
 */
- (nullable NSError *)save;

/*!
 *  Removes the file object from the backend asynchronously.
 *
 *  @discussion If file is removed successfully, you can not access this file using the url anymore. 
 *
 *  @param completionHandler   The handler to call when the deletion is done.
 */
- (void)removeWithCompletionHandler:(nonnull MBCompletionBlockError)completionHandler;

/*!
 *  Removes the file object from the backend asynchronously without getting notifying to the caller.
 *
 *  @discussion SDK will make sure that data is removed properly.
 */
- (void)removeEventually;

/*!
 *  Removes the file object from the backend synchronously
 *
 *  @discussion If file is removed successfully, you can not access this file using the url anymore.
 *
 *  @return     An NSError object for indicating the error message.
 */
- (nullable NSError *)remove;

@end

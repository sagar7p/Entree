//
//  MBImageView.h
//  moBack
//
//  Created by Lucus Huang on 7/17/15.
//  Copyright (c) 2015 ReliableCoders. All rights reserved.
//

#import <UIKit/UIKit.h>

@class MBFile;

@interface MBImageView : UIImageView

/**
 *  @abstract The moBack file of the image
 */
@property (strong, nonatomic) MBFile *file;

/**
 *  Returns an image view initialized with the specified MBFile instance.
 *
 *  @param      frame   The frame rectangle for the view, measured in points
 *  @param      file    The moBack file of the image
 *
 *  @return     An initialized view object or nil if the object couldn't be created.
 */
- (instancetype)initWithFrame:(CGRect)frame file:(MBFile *)file;

/**
 *  Fetches the image from moBack backend
 *
 *  @param completion   The completion call-back block for the results
 */
- (void)loadImageWithCompletion:(MBCompletionBlockError)completion;

@end

//
//  MBGeoPoint.h
//
//  Copyright (c) 2014 moBack, Inc. All rights reserved.
//

#import <CoreLocation/CoreLocation.h>

/**
 *  The MBGeoPoint represents a geo-location type.
 */
@interface MBGeoPoint : NSObject

NS_ASSUME_NONNULL_BEGIN
/*!
 *  @brief  The latitude of the location
 */
@property (assign, nonatomic) double latitude;

/*!
 *  @brief  longitude of the location
 */
@property (assign, nonatomic) double longitude;

/*!
 *  @brief  Initializes a MBGeoPoint object with latitude and longitude data
 *
 *  @param latitude  latitude
 *  @param longitude longitude
 *
 *  @return Newly initialized geo-point object
 */
- (instancetype)initWithLatitude:(double)latitude
                       longitude:(double)longitude;

/*!
 *  @brief  Convert CLLocation object to MBGeoPoint object
 *
 *  @param location The CLLocation object to be converted
 *
 *  @return Newly converted MBGeoPoint object
 */
+ (MBGeoPoint * __nullable)convertMBGeoPointFromCLLocation:(CLLocation *)location;

/*!
 *  @brief  Convert MBGeoPoint object to CLLocation object
 *
 *  @param geoPoint The MBGeoPoint object to be converted
 *
 *  @return Newly converted CLLocation object
 */
+ (CLLocation * __nullable)convertCLLocationFromMBGeoPoint:(MBGeoPoint *)geoPoint;

NS_ASSUME_NONNULL_END

@end

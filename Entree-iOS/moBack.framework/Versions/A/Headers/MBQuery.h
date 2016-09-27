//
//  MBQuery.h
//
//  Copyright (c) 2014 moBack, Inc. All rights reserved.
//

#import <moBack/MBConstants.h>

@class MBObject;
@class MBGeoPoint;

NS_ASSUME_NONNULL_BEGIN

/*!
 *  @brief The MBQuery represents a query with constraints that is used to fetch desired data from MBObject
 */
@interface MBQuery : NSObject

/*!
 *  @brief The number of objects to skip
 */
@property (assign, nonatomic) int skip;

/*!
 *  @brief  A limit on the number of objects to return
 */
@property (assign, nonatomic) int limit;

/*!
 *  @brief  The mode that each of the constraints on this query should have, either 'AND' or 'OR'
 */
@property (assign, getter = isOrMode, nonatomic) BOOL orMode;

/*!
 *  @brief  The name of the table the query should run on
 */
@property (strong, nonatomic) NSString *tableName;

/*!
 *  @brief  Initializes a new instance of MBQuery from table by given table name
 *
 *  @param tableName The table name
 *
 *  @return Newly initialized item
 */
- (instancetype)initWithTableName:(NSString *)tableName;

/*!
 *  @brief  Fetches a row based on objectId from specific table asynchronously
 *
 *  @discussion The result will be MBObject or MBUser only
 *
 *  @param objectId     The desired object identifier
 *  @param completion   The completion call-back block for the results
 */
- (void)fetchRowWithId:(NSString *)objectId
     completionHandler:(MBCompletionBlockObject)completionHandler;

/*!
 *  @brief Fetches a row based on objectId from specific table synchronously
 *
 *  @discussion The result will be MBObject or MBUser only
 *
 *  @param objectId     The desired object identifier
 *  @param error        The pointer of an address of NSError to indicate error information, nil if no error
 */
- (MBObject *)fetchRowWithId:(NSString *)objectId
                       error:(NSError * __nullable *)error;

/*!
 *  @brief Fetches rows based on the current query setting from specific table asynchronously
 *
 *  @discussion The results will be an array of MBObject or MBUser only
 *
 *  @param completion   The completion call-back block for the results
 */
- (void)fetchRowsWithCompletionHandler:(MBCompletionBlockArray)completionHandler;

/*!
 *  @brief Fetches rows based on the current query setting from specific table synchronously
 *
 *  @discussion It will block the current thread until it is finished
 *
 *  @param error    The pointer of an address of NSError to indicate error information, nil if no error
 */
- (NSArray *)fetchRows:(NSError * __nullable *)error;

/*!
 *  @brief Fetches the total number of the rows of data from specific table synchronously
 *
 *  @param error    The pointer of an address of NSError to indicate error information, nil if no error
 *
 *  @return     Total number of rows
 */
- (int)getCount:(NSError * __nullable *)error;

/*!
 *  @brief Fetches the total number of the rows of data from specific table asynchronously
 *
 *  @param completion   The completion call-back block for the results
 */
- (void)getCountWithCompletionHandler:(MBCompletionBlockObject)completionHandler;

#pragma mark - Basic constraints
/*!
 *  @brief Adds a query constraint that requires each of single returned object to include the given columns only
 *
 *  @param columns  The columns are used to be constrained
 */
- (void)addSelectedConstraintOnColumns:(NSArray *)columns;

/*!
 *  @brief Adds a query constraint that limits a particular column's object to be equal to the provided value
 *
 *  @param column   The column is used to be constrained
 *  @param value    The value that must be equalled
 */
- (void)addConstraintOnColumn:(NSString *)column equalTo:(id)value;

/*!
 *  @brief Adds a query constraint that limits a particular column's object not to be equal to the provided value
 *
 *  @param column   The column is used to be constrained
 *  @param value    The value that must not be equalled
 */
- (void)addConstraintOnColumn:(NSString *)column notEqualTo:(id)value;

/*!
 *  @brief Adds a query constraint that limits a particular column's object to be less than the provided value
 *
 *  @param column   The column is used to be constrained
 *  @param value    The value is used to provide the maximum value
 */
- (void)addConstraintOnColumn:(NSString *)column lessThan:(id)value;

/*!
 *  @brief Adds a query constraint that limits a particular column's object to be less than or equal to the provided value
 *
 *  @param column   The column is used to be constrained
 *  @param value    The value is used to provide the maximum value
 */
- (void)addConstraintOnColumn:(NSString *)column lessThanOrEqualTo:(id)value;

/*!
 *  @brief Adds a query constraint that limits a particular column's object to be greater than the provided value
 *
 *  @param column   The column is used to be constrained
 *  @param value    The value is used to provide the minimum value
 */
- (void)addConstraintOnColumn:(NSString *)column greaterThan:(id)value;

/*!
 *  @brief Adds a query constraint that limits a particular column's object to be greater than or equal to the provided value
 *
 *  @param column   The column is used to be constrained
 *  @param value    The value is used to provide the minimum value
 */
- (void)addConstraintOnColumn:(NSString *)column greaterThanOrEqualTo:(id)value;

/*!
 *  @brief Adds a query constraint that limits a particular column's object to be contained in the given array
 *
 *  @param column   The column is used to be constrained
 *  @param value    The array of values is used to search
 */
- (void)addConstraintOnColumn:(NSString *)column containedIn:(NSArray *)array;

/*!
 *  @brief Adds a query constraint that limits a particular column's object to be contained all in the given array
 *
 *  @param column   The column is used to be constrained
 *  @param value    The array of values is used to search
 */
- (void)addConstraintOnColumn:(NSString *)column containedAll:(NSArray *)array;

/*!
 *  @brief Adds a query constraint that limits a particular column's object not to be contained in the given array
 *
 *  @param column   The column is used to be constrained
 *  @param value    The array of values is used to search
 */
- (void)addConstraintOnColumn:(NSString *)column notContainedIn:(NSArray *)array;

/*!
 *  @brief Adds a ascending constraint on particular column
 *
 *  @param column   The column is used to be constrained
 */
- (void)addAscendingOrderConstraintOnColumn:(NSString *)column;

/*!
 *  @brief Adds a decending constraint on particular column
 *
 *  @param column   The column is used to be constrained
 */
- (void)addDescendingOrderConstraintOnColumn:(NSString *)column;

/*!
 *  @brief Adds a query constraint that requires an existing value based on the given column
 *
 *  @param column   The column is used to search for the existing value
 */
- (void)addHasValueConstraintOnColumn:(NSString *)column;

/*!
 *  @brief Adds a query constraint that requires a non-existing value based on the given column
 *
 *  @param column   The column is used to search for the non-existing value
 */
- (void)addHasNoValueConstraintOnColumn:(NSString *)column;

/*!
 *  @brief Adds a query constraint that return all the relational data(array, pointer or relation) by a given column other than just the meta data
 *
 *  @param column   The column of a relation data
 */
- (void)addIncludeRelationalDataConstraintOnColumn:(NSString *)column;

#pragma mark - String constraints

/*!
 *  @brief Adds a query constraint that limits a particular column's object to be substring of the provided value
 *
 *  @param column   The column is used to be constrained
 *  @param value    The value is used to search
 */
- (void)addConstraintOnColumn:(NSString *)column
            containsString:(NSString *)value;

/*!
 *  @brief Adds a query constraint that limits a particular column's object to be prefix of the provided value
 *
 *  @param column   The column is used to be constrained
 *  @param value    The value is used to search
 */
- (void)addConstraintOnColumn:(NSString *)column
                    hasPrefix:(NSString *)value;

/*!
 *  @brief Adds a query constraint that limits a particular column's object to be suffix of the provided value
 *
 *  @param column   The column is used to be constrained
 *  @param value    The value is used to search
 */
- (void)addConstraintOnColumn:(NSString *)column
                    hasSuffix:(NSString *)value;

#pragma mark - GeoLocation cosntraints
typedef enum : NSUInteger {
    MBGeoPointQueryUnitMiles,
    MBGeoPointQueryUnitKilometers,
    MBGeoPointQueryUnitDegrees,
} MBGeoPointQueryUnit;
/*!
 *  @brief Adds a query constraint that limits a particular column's coordinates to be nearest to the given coordinate
 *
 *  @param column   The column is used to be constrained
 *  @param geoPoint The coordinate is used to search
 */
- (void)addConstraintOnColumn:(NSString *)column
         nearestToGeoPoint:(MBGeoPoint *)geoPoint;

/*!
 *  @brief Adds a query constraint that limits a particular column's coordinates to be near to the certain distances from given coordinate with given unit
 *
 *  @param column       The column is used to be constrained
 *  @param geoPoint     The coordinate is used to search
 *  @param unit         The unit is used to search
 *  @param distance     The distance is used to search
 */
- (void)addConstraintOnColumn:(NSString *)column
                onGeoPoint:(MBGeoPoint *)geoPoint
                  withUnit:(MBGeoPointQueryUnit)unit
            withinDistance:(double)distance;

/*!
 *  @brief Adds a query constraint that limits a particular column's coordinates to be contained within a given rectangular bounding box that is composed by the top-right point and bottom-left point
 *
 *  @param column       The column is used to be constrained
 *  @param topright     The top right inclusive point of the box
 *  @param bottomleft   The bottom left inclusive point of the box
 */
- (void)addConstraintOnColumn:(NSString *)column
    withinRectBoxFromTopright:(MBGeoPoint *)topright
                 toBottomleft:(MBGeoPoint *)bottomleft;

#pragma mark - Combine constraints
/*!
 *  @brief Combine a subquery into one main query with OR relation
 *
 *  @param subqueries      An array of MBQuery object to be combined
 *
 *  @return     Newly combined MBQuery object
 */
+ (MBQuery *)orWithSubqueries:(NSArray *)subqueries;

#pragma mark - Convenient query getter
/*!
 *  @brief  Initializes a new instance of MBQuery from User table
 *
 *  @return     Newly initialized MBQuery object with User table
 */
+ (MBQuery *)userQuery;

/*!
 *  @brief  Initializes a new instance of MBQuery from Role table
 *
 *  @return     Newly initialized MBQuery object with Role table
 */
+ (MBQuery *)roleQuery;

NS_ASSUME_NONNULL_END

@end

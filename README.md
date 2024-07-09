# Data Store Documentation
## Overview
The application uses an H2 in-memory database and a caching system implemented with maps.

At application startup, the data store is created using all data from the database where the timestamp is in the configured timeframe.

When new data is received by the application, it is inserted into the database. After a successful insert, the maps are updated with the new price if the timestamp is valid. This entire insertion and update process is treated as one transaction.

This application is a RESTful service that receives and serves JSON payloads. The system can handle a few hundred transactions per day.

The prices in the maps are those with a timestamp within the last 30 days (2592000 seconds). This 30-day period is configurable in the <b>application.properties</b> file under <b>priceExpirationTime</b> and is expressed in seconds.

```
TESTING: For demonstration purposes, the value is set to 120 seconds to showcase the cache cleaning process.
``` 
The cache is checked every <b>cleanInterval</b> seconds for expired prices that need to be removed. <b>cleanInterval</b> is configurable in the <b>application.properties</b> file and is expressed in milliseconds.

## Endpoints
There are three endpoints:
```
1. Publish new price data:

   - Method: POST
   - URL: /instrumentprice
   
2. Get all prices submitted by a vendor:

   - Method: GET
   - URL: /instrumentprice/vendors/{vendorName}
   - Parameter: {vendorName} is the name of the vendor.
   
3. Get all prices for an instrument from different vendors:

    - Method: GET
    - URL: /instrumentprice/instruments/{instrumentName}
   - Parameter: {instrumentName} is the name of the instrument.
```
## Problems Encountered
The application requires a many-to-many relationship between Vendors and Instruments. To address this, a new entity was created, resulting in two one-to-many relationships.
## Running the application
1. ```mvn clean install```

2. ```java -jar .\target\data-store-0.0.1-SNAPSHOT.jar```

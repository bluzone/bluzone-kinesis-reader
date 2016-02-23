# bluzone-kinesis-reader
Java example for consuming data from a Kinesis Stream
==========================

## You will need the following
1.  AWS Account credentials (AWS Access Key and AWS Secret Key)
1.  A Kinesis stream setup in AWS

## How to build
1.  Clone this repository
1.  `cd` into the bluzone-kinesis-reader directory
1.  Edit the `pom.xml` to set the appropriate properties:  awsAccessKey, awsSecretKey, kinesisEndpoint and streamName
1.  Run `mvn spring-boot:run`



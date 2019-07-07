# Money Transfer Application

How to start the MoneyTransfer application
---

1. Navigate to MoneyTransfer folder
1. Run `mvn clean install` to build your application
1. Run `java -jar target/MoneyTransfer-1.0.0.jar db migrate config.yml` to create schema and sample data in H2 database using liquibase
1. Start application with `java -jar target/MoneyTransfer-1.0.0.jar server config.yml`

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`

Sample Request/Response
---

### Money Transfer Request

Below is the request to transfer funds/amount from `fromAccount` to `toAccount`.
```
curl -X POST \
  http://localhost:8080/transfer/ \
  -H 'Accept: */*' \
  -H 'Cache-Control: no-cache' \
  -H 'Connection: keep-alive' \
  -H 'Content-Type: application/json' \
  -H 'Host: localhost:8080' \
  -H 'Postman-Token: 7a61d164-09fe-4873-b1c5-d25ded6d79f1,fd63a840-22e5-4e31-aadb-f7528ccea36c' \
  -H 'User-Agent: PostmanRuntime/7.15.0' \
  -H 'accept-encoding: gzip, deflate' \
  -H 'cache-control: no-cache' \
  -H 'content-length: 62' \
  -H 'cookie: JSESSIONID=C74BCA110D3E8F965DC402C8B18FDA1D' \
  -b JSESSIONID=C74BCA110D3E8F965DC402C8B18FDA1D \
  -d '{
    "fromAccount": 1,
    "toAccount": 2,
    "amount": 11
}'
```

### Money Transfer Response
```
{
    "referenceId": "f4b066d9-4fb8-497f-be8f-ba40f7597984"
}
```

### List transactions using referenceId Request

Below is the request to list down transactions by referenceId.
```
curl -X GET \
  http://localhost:8080/transaction/referenceId/f4b066d9-4fb8-497f-be8f-ba40f7597984 \
  -H 'Accept: */*' \
  -H 'Cache-Control: no-cache' \
  -H 'Connection: keep-alive' \
  -H 'Host: localhost:8080' \
  -H 'Postman-Token: dd422f4e-fd05-4cea-a92e-15022cade341,21839b28-047e-4a26-8efa-995b48eb0cb3' \
  -H 'User-Agent: PostmanRuntime/7.15.0' \
  -H 'accept-encoding: gzip, deflate' \
  -H 'cache-control: no-cache' \
  -H 'cookie: JSESSIONID=C74BCA110D3E8F965DC402C8B18FDA1D' \
  -b JSESSIONID=C74BCA110D3E8F965DC402C8B18FDA1D
```

### List transactions using referenceId Response
```
[
    {
        "referenceId": "f4b066d9-4fb8-497f-be8f-ba40f7597984",
        "accountId": 1,
        "accountType": "SAVINGS",
        "accountState": "ACTIVE",
        "transactionType": "DEBIT",
        "amount": 11
    },
    {
        "referenceId": "f4b066d9-4fb8-497f-be8f-ba40f7597984",
        "accountId": 2,
        "accountType": "SAVINGS",
        "accountState": "ACTIVE",
        "transactionType": "CREDIT",
        "amount": 11
    }
]
```

Technology Stack
---

This application is built using dropwizard framework and JAVA. Dropwizard is a java framework to speed up the development of high-performance restful web services. It will use some of the stable libraries to create simple, light-weight package so that you can actually focus on business logic rather than worrying about configuration. 

Dropwizard provides out of the box support for configuration, application metrics, logging, health check and many other operational tools.
It will help you to create production ready web services in the shortest time possible.

Below libraries are used in this application
* Jetty for HTTP
* Jersey for rest
* Jackson for JSON
* Logback and sl4j for logging
* Hibernate validator  
* Liquibase
* Hibernate 

Datastore
---

This sample application leverages h2 as the DB. Liquibase is used to create schema and setup test data.

[migrations.xml](./src/main/resources/migrations.xml) is used to create tables and setup sample data.

Testing
---
There are separate unit test cases to test resources(rest), dao and web layers.
JaCoCo is used for measuring and reporting code coverage. [index.html](./target/jacoco-report/index.html) is the JaCoCo report.


Managing Transactions
---

Dropwizard-hibernate's `@UnitOfWork` is leveraged to manage transactions. This will automatically open a session, begin a transaction, call DAO layer's methods, commit the transaction, and finally close the session. If an exception is thrown, the transaction is rolled back.
                                                                        
Optimistic Locking
---

`version_id` column in `Accounts` table is used to manage versioning. Hibernate's '@Version' is leveraged for automatic versioning.
With automatic numeric versioning, Hibernate will use the version number to check that the row/account has not been updated since the last time it was retrieved when updating the persistent Account.                                                                         

In case account balance or state of account being debited is updated by some other thread or state of account being credited is updated by some other thread then exception would be thrown and transaction would be rolled back. 

Dropwizard project creation
---

Below maven command is used to create dropwizard project

`mvn archetype:generate -DarchetypeGroupId=io.dropwizard.archetypes -DarchetypeArtifactId=java-simple -DarchetypeVersion=1.3.12`

Below are the values given for requested properties

property 'groupId': `com.assignment`

property 'artifactId': `MoneyTransfer`

property 'version' 1.0-SNAPSHOT: : `1.0.0`

property 'package' com.assignment: : `com.assignment.moneytransfer`

property 'name': `MoneyTransfer`
# Project 1 - Employee Reimbursment System (ERS)

## Executive Summary
The Expense Reimbursement System (ERS)  manages the process of reimbursing employees for expenses incurred while on company time. All employees in the company can login and submit requests for reimbursement and view their past tickets and pending requests. Finance managers can log in and view all reimbursement requests and past history for all employees in the company. Finance managers are authorized to approve and deny requests for expense reimbursement. The reimbursement types should have the following options: LODGING, FOOD, TRAVEL


# Tech Stack
 - Java 8
 - Apache Maven
 - PostgreSQL
 - AWS RDS
 - Java Servlets
 - JDBC
 - HTML
 - CSS
 - JavaScript
 - AJAX / Fetch API



### Features:
#### Guest:
 - As a guest, I can register for a new account
 - As a guest, I can log into my account

#### User:
 - As a user, I can submit a request for reimbursement
 - As a user, I can cancel a pending request for reimbursement
 - As a user, I can view my pending and completed past requests for reimbursement
 - As a user, I can edit my pending requests for reimbursement

#### Admin/Finance Manager:
 - As an admin, I can approve expense reimbursements
 - As an admin, I can deny expense reimbursements
 - As an admin, I can filter requests by status

### How to start:
 - Clone this repository using to the desired directory using git clone
``` git clone <repositoryurl>```
 - Create a resource folder in main, then create an application.properties file and add the following to it
   - All other code should be in this repository
```
hostname=<Database connection url>
port=<port to connect to>
username=<Database username>
password=<Database password>
dbname=<Database Name>
schemaName=<Schema Name>
driver=org.postgresql.Driver
```
### Contributors:
 - Terrell Crawford

**State-chart Diagram (Reimbursement Statuses)** 

![](./imgs/state-chart.jpg)


**Logical Model**

![](./imgs/logical.jpg)

**Physical Model**

![](./imgs/physical.jpg)

**Use Case Diagram**

![](./imgs/use-case.jpg)

**Activity Diagram**

![](./imgs/activity.jpg)




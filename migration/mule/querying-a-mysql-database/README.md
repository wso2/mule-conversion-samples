# Querying a MySQL Database

This example illustrates how to use the database connector to connect to a MySQL database. After reading this document 
and creating and running the example in Micro Integrator, you should be able to leverage what you have learned to create 
an application that connects to a MySQL database.


### Assumptions

This document describes the details of the example within the context of WSO2 Integration Studio, WSO2 EI’s graphical 
user interface (GUI). This document assumes that you are familiar with WSO2 EI and the 
[Integration Studio interface](https://ei.docs.wso2.com/en/latest/micro-integrator/overview/quick-start-guide/). To 
increase your familiarity with Integration Studio, consider completing one or more 
[WSO2 EI Tutorials](https://ei.docs.wso2.com/en/latest/micro-integrator/use-cases/integration-use-cases/).

### Example Use Case

This application listens for HTTP GET requests with the form: http://<host>:8290/services/RDBMSDataService/Employee/{parameter}. 
The datasource is configured to use the <parameter> to use it for the SQL query listed below.

	select first_name from employees where last_name = :lastName 

So if the HTTP connector receives http://localhost:8081/?lastname=Smith, the SQL query will be select first_name from 
employees where last_name = Smith.

The datasource service instructs the database server to run the SQL query, retrieves the result of the query, and 
converts the result to JSON. 

### Set Up and Run the Example
1. Install the MySQL server.
2. Download the JDBC driver for MySQL from [here](https://dev.mysql.com/downloads/connector/j/) and copy it to your 
<MI_HOME>/lib directory.
3. Start the MySQL server.
4. Create the database
```sql
create database employees;
```
5. Create the table employee
```sql
create table employees (id int NOT NULL AUTO_INCREMENT, first_name varchar(100), last_name varchar(100), age int, PRIMARY KEY (id));
```
6. Enter the following data into the table:
```sql
insert into employees(first_name,last_name,age) values("Chava", "Puckett", 28);
insert into employees(first_name,last_name,age) values("Quentin", "Puckett", 28);
```
7. Start WSO2 Integration Studio ([Installing WSO2 Integration Studio](https://ei.docs.wso2.com/en/latest/micro-integrator/develop/installing-WSO2-Integration-Studio/)).
8. In your menu in Studio, click the **File** menu. In the File menu select the **Import...** item.
9. In the Import window select the **Existing WSO2 Projects into workspace** under **WSO2** folder.
10. Browse and select the file path to the downloaded sample of this github project ("querying-a-mysql-database" folder 
of the downloaded github repository).
11. Open the **RDBMSDataService.dbs** under **querying-a-mysql-database/QueryingAMysqlDatabase/dataservice** directory. 
12. The **RDBMSDataService.dbs** is the graphical view of the datasource. Configure connection attributes : url, username,
password.
13. Run the sample by right click on the **QueryingAMysqlDatabaseProjectCompositeApplication** under the main 
**querying-a-mysql-database** project and selecting **Export Project Artifacts and Run**.
14. Use REST Console or `curl` to make your request.<br/>
        Request URI: http://localhost:8290/services/RDBMSDataService/Employee/Puckett<br/>
        Request method: GET<br/>
        Headers : Accept=application/json<br/>
15. You should get the following JSON response
```json
{"employees":{"employee":[{"first_name":"Chava"},{"first_name":"Quentin"}]}}
```

### Go Further

* Learn more on [WSO2 datasources](https://ei.docs.wso2.com/en/latest/micro-integrator/develop/creating-artifacts/data-services/creating-datasources/)
* Learn more on [Creating A Data Service](https://ei.docs.wso2.com/en/latest/micro-integrator/develop/creating-artifacts/data-services/creating-data-services/)
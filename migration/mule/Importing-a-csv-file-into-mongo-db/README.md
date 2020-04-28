# Importing-a-csv-file-into-mongo-db

This example application illustrates the concept of using dataservices to add records to mongoDB. It also involves reading a csv file using [File connector](https://store.wso2.com/store/assets/esbconnector/details/5d6de1a4-1fa7-434e-863f-95c8533d3df2), map the data to json and conditionally route/iterate a payload.

### Assumptions ###

This document assumes that you are familiar with WSO2 EI and the 
[Integration Studio interface](https://ei.docs.wso2.com/en/latest/micro-integrator/develop/WSO2-Integration-Studio/), 
WSO2 EI’s graphical developer tool. To increase your familiarity with Integration Studio, consider completing one or more 
[WSO2 EI Tutorials](https://ei.docs.wso2.com/en/latest/micro-integrator/use-cases/integration-use-cases/).
And this assumes that you have mongoDB installed already in the machine or in an accessible remote instance.

### Example Use Case
In this example we use a scheduled task which fetches a csv file periodically, and adds the data rows to a mongo DB collection. And it will check whether mongo DB colletion exists and if not it will create a collection before adding the record.
![Importing-a-csv-file-into-mongo-db]()

### Set Up and Run the Example

1. Start WSO2 Integration Studio ([Installing WSO2 Integration Studio](https://ei.docs.wso2.com/en/latest/micro-integrator/develop/installing-WSO2-Integration-Studio/)).
2. In your menu in Studio, click the **File** menu. In the File menu select the **Import...** item.
3. In the Import window select the **Existing WSO2 Projects into workspace** under **WSO2** folder.
4. Browse and select the file path to the downloaded sample of this github project 
("Importing-a-csv-file-into-mongo-db" folder of the downloaded github repository).
5. Lets add the file connector into the workspace. Right click on the **ImportACSVFileIntoMongoDB** and select 
**Add or Remove Connector**. Keep the **Add connector** option selected and click **Next>**. Search for 'file' using the 
search bar and click the download button located at the bottom right corner of the file connector. Click **Finish**.
6. Open the **WriteCSVToMongoDB.xml** under 
**Importing-a-csv-file-into-mongo-db/ImportACSVFileIntoMongoDB/src/main/synapse-config/sequences/** directory. 
Copy the input.csv file located in **Importing-a-csv-file-into-mongo-db/ImportACSVFileIntoMongoDB/src/main/resources** directory to a location of your choice. 
Replace the csvFilePath property value with the copied path to input.csv.ex:- /Users/user/Desktop/sample/input.csv.
![Alt text]( "ImportACSVFileIntoMongoDB")
7. Open the **WriteCSVToMongoDB.xml** under 
**Importing-a-csv-file-into-mongo-db/ImportACSVFileIntoMongoDB/src/main/synapse-config/sequences/** directory. 
Copy the input.csv file located in **Importing-a-csv-file-into-mongo-db/ImportACSVFileIntoMongoDB/src/main/resources** directory to a location of your choice. 
Replace the csvFilePath property value with the copied path to input.csv.ex:- /Users/user/Desktop/sample/input.csv.
![Alt text]( "ImportACSVFileIntoMongoDB")
8. Open the **ImportingACSVFileIntoMongoDBDataService.dbs** in **Importing-a-csv-file-into-mongo-db/ImportACSVFileINtoMongoDBDataService/dataservice/** directory.
Go to source tab and change the value of mongoDB_servers property to IP address if you are using a remote mongoDB instance.
9. Run the sample by right click on the **ImportACSVFileIntoMongoDBCompositeApplication** under the main 
**Importing-a-csv-file-into-mongo-db** project and selecting **Export Project Artifacts and Run**.
10. Check the mongoDB instance for customers Collection and the inserted data inside the collection. You can use the below query in the MongoDB Console.
    db.customers.find()

### Go Further

* Learn more about [file connector](https://docs.wso2.com/display/ESBCONNECTORS/Working+with+the+File+Connector#WorkingwiththeFileConnector-append).
* Read more on [WSO2 connectors](https://docs.wso2.com/display/ESBCONNECTORS/WSO2+ESB+Connectors+Documentation)
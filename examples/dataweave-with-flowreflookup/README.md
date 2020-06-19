# Dataweave-with-flowreflookup

This example shows you how to utilize the [Salesforce REST Connector](https://store.wso2.com/store/assets/esbconnector/details/43e44763-0d73-4ab3-8ae9-d6f73532d164) to create sObjects from data stored in csv format and how to use sequence references to call another sequence.

### Assumptions ###

This document assumes that you are familiar with WSO2 EI and the 
[Integration Studio interface](https://ei.docs.wso2.com/en/latest/micro-integrator/overview/quick-start-guide/). To 
increase your familiarity with Integration Studio, consider completing one or more 
[WSO2 EI Tutorials](https://ei.docs.wso2.com/en/latest/micro-integrator/use-cases/integration-use-cases/).

### Example Use Case

In this example, We read multiple details of an account which are stored in a csv file and transform them within a seperate sequence((Which invokes a script mediator to change specific region field). We will lookup this seperate sequence(Call it using sequence reference) and set transformed output to a property and use that property to build sObject in salesforce using salesforcerest connector.

### Set Up and Run the Example

1. Start WSO2 Integration Studio ([Installing WSO2 Integration Studio](https://ei.docs.wso2.com/en/latest/micro-integrator/develop/installing-WSO2-Integration-Studio/)).
2. In your menu in Studio, click the **File** menu. In the File menu select the **Import...** item.
3. In the Import window select the **Existing WSO2 Projects into workspace** under **WSO2** folder.
4. Browse and select the file path to the downloaded sample of this Github project 
(`integration-studio-examples/migration/mule/dataweave-with-flowreflookup`) and click **finish**.
5. Let's add the Salesforce REST connector into the workspace. Right click on the **dataweavewithflowref** and select 
**Add or Remove Connector**. Keep the **Add connector** option selected and click **Next>**. Search for 'file' and 'salesforcerest' using the 
search bar and click the download button located at the bottom right corner of the File and Salesforce REST connectors. Click **Finish**.
6. Follow these [steps](https://ei.docs.wso2.com/en/latest/micro-integrator/references/connectors/salesforce-rest-connector/sf-access-token-generation/) to generate the Access Tokens for Salesforce and obtain the Access Token, and Refresh Token.
7. Copy the companies.csv file in **dataweave-with-flowreflookup/dataweavewithflowref/src/main/resources/** directory to location of your choice.
8. Open the **FlowRefSequence.xml** under 
**dataweave-with-flowreflookup/dataweavewithflowref/src/main/synapse-config/sequences/** directory. 
![dataweavewithflowref](../../resources/images/ "dataweavewithflowref")
Configure the following properties with the previously obtained values.
    - Access Token
    - Refresh Token
    - API URL (e.g.: https://<INSTANCE>.salesforce.com)
And configure csvFilePath property with the location path to companies.csv file.
9. Run the sample by right click on the **dataweavewithflowrefCompositeApplication** under the main 
**dataweave-with-flowreflookup** project and selecting **Export Project Artifacts and Run**.
10. Once the sample is run, from your browser, go to the Salesforce account used in the configs, then go to Accounts tab.
11. Display all Accounts, and search the accounts with the following two entries and you can see those are created.
```
WSO2
WSO2 LK
```
12. Following logs can be observed in the console log.
```
State to lookup is: FL
INFO {org.apache.synapse.mediators.builtin.LogMediator} - Region is  = SouthEast
State to lookup is: CA
INFO {org.apache.synapse.mediators.builtin.LogMediator} - Region is  = WestCoast
```

### Go Further

* Learn more about [File Connector](https://docs.wso2.com/display/ESBCONNECTORS/Working+with+the+File+Connector#WorkingwiththeFileConnector-append).
* Learn more about [Salesforce REST connector](https://docs.wso2.com/display/ESBCONNECTORS/Salesforce+REST+Connector).
* Read more on [WSO2 connectors](https://docs.wso2.com/display/ESBCONNECTORS/WSO2+ESB+Connectors+Documentation)

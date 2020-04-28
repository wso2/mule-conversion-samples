# Import Contacts Asynchronously

This application uses pre-packaged tools to connect with Salesforce. Based on a simple use case, the application takes 
a CSV file of contacts and uploads the contact information to an active Salesforce user account. The processing is 
done asynchronously and is triggered by an HTTP POST request. It uses Datamapper to transform data, thereby 
facilitating quick integration with this Software-as-a-Service (SaaS) provider.

At times, you may find that you need to connect one or more of your organization's on-premises systems with a SaaS 
such as Salesforce. Ideally, these independent systems would talk to each other and share data to enable automation of 
end-to-end business processes. Use integration applications to facilitate communication between your on-prem system(s) 
and Salesforce. (Though this use case does not extend as far, you can also use such application to facilitate 
communication between SaaS providers).

This example shows you how to utilize the 
[File Connector](https://store.wso2.com/store/assets/esbconnector/details/5d6de1a4-1fa7-434e-863f-95c8533d3df2) and 
[Salesforce REST Connector](https://store.wso2.com/store/assets/esbconnector/details/43e44763-0d73-4ab3-8ae9-d6f73532d164) 
to import data in a file to Salesforce in an asynchronous manner.

### Assumptions ###

This document assumes that you are familiar with WSO2 EI and the 
[Integration Studio interface](https://ei.docs.wso2.com/en/latest/micro-integrator/overview/quick-start-guide/). To 
increase your familiarity with Integration Studio, consider completing one or more 
[WSO2 EI Tutorials](https://ei.docs.wso2.com/en/latest/micro-integrator/use-cases/integration-use-cases/).

### Example Use Case
Though a simple example, this application nonetheless employs complex functionality to demonstrate a basic use case. 
The application accepts CSV files that contain contact information – name, phone number, email – and uploads them into 
a Salesforce account, automatically inserting the correct data into each Salesforce field.  

### Set Up and Run the Example

1. Start WSO2 Integration Studio 
([Installing WSO2 Integration Studio](https://ei.docs.wso2.com/en/latest/micro-integrator/develop/installing-WSO2-Integration-Studio/)).
2. In your menu in Studio, click the **File** menu. In the File menu select the **Import...** item.
3. In the Import window select the **Existing WSO2 Projects into workspace** under **WSO2** folder.
4. Browse and select the file path to the downloaded sample of this github project 
("import-contacts-asynchronously" folder of the downloaded github repository).
5. Lets add the file connector into the workspace. Right click on the **ImportContactsIntoSalesforce** and select 
**Add or Remove Connector**. Keep the **Add connector** option selected and click **Next>**. Search for 'file' using the 
search bar and click the download button located at the bottom right corner of the file connector. Click **Finish**.
6. Similarly, add the Salesforce REST Connector to the workspace.
7. Follow these 
[steps](https://ei.docs.wso2.com/en/latest/micro-integrator/references/connectors/salesforce-rest-connector/sf-access-token-generation/) 
to generate the Access Tokens for Salesforce and obtain the Access Token, and Refresh Token.
8. Copy the **contacts.csv** file in 
**import-contacts-asynchronously/SalesforceImportContactsAsynchronously/src/main/resources/contacts.csv** to a 
location on your computer.
9. Run the sample by right clicking on the **import-contacts-into-salesforce** project and selecting **Run as -> 
Run On Micro Integrator**. (Make sure to select ImportContactsIntoSalesforceRegistry, 
SalesforceImportContactsAsynchronously, SalesforceRestConnector while creating the car app during this step prompt 
wizard)
10. You can use the HTTP Client in Integration Studio to send the HTTP request to trigger the  import of contacts
    1. Set the request type tp POST
    2. Set the request url to 
        `http://localhost:8290/salesforce/importContactAsynchronously`
    3. Paste the following into the Headers text box
        `Content-Type=application/json`
    4. Paste the following into the Body text box. Make sure to replace the <YOUR_ACCESS_TOKEN>, <YOUR_REFRESH_TOKEN>,
    <LOCATION_OF_contact.csv_IN_YOUR_COMPUTER> with your custom values
        ```
        {
          "accessToken":"<YOUR_ACCESS_TOKEN>",
          "apiUrl":"https://ap17.salesforce.com",
          "refreshToken": "<YOUR_REFRESH_TOKEN>",
          "hostName": "https://login.salesforce.com",
          "intervalTime" : "100000",
          "apiVersion": "v44.0",
          "blocking": "false",
          "sObjectName":"Contact",
          "registryPath": "connectors/SalesforceRest",
          "source": "file:///<LOCATION_OF_contact.csv_IN_YOUR_COMPUTER>",
          "contentType": "text/plain",
          "filePattern": ".*.csv"
        }
        ```
    5. Click on the green play icon next to the url box

11. Following logs can be observed in the HTTP response box after 10 seconds
```
HTTP/1.1 201 Created
Strict-Transport-Security: max-age=31536000; includeSubDomains
X-Robots-Tag: none
Cache-Control: no-cache,must-revalidate,max-age=0,no-store,private
X-Content-Type-Options: nosniff
Public-Key-Pins-Report-Only: ...
Set-Cookie: BrowserId=...; domain=.salesforce.com; path=/; expires=Wed, 28-Apr-2021 08:43:39 GMT; Max-Age=31536000
Vary: Accept-Encoding
X-XSS-Protection: 1; mode=block
Content-Type: application/json;charset=UTF-8
Expect-CT: max-age=86400, report-uri="https://a.forcesslreports.com/Expect-CT-report/nullm"
Sforce-Limit-Info: api-usage=18/15000
Date: Tue, 28 Apr 2020 08:43:40 GMT
Transfer-Encoding: chunked

{"hasErrors":false,"results":[{"referenceId":"John","id":"0032x000002yF6cAAE"},{"referenceId":"Jane","id":"0032x000002yF6dAAE"}]}
```

12. We have added 10 seconds delay to this POST request so that we can send a GET request to the /importContactStatus 
resource and check the status of the import.

### How it works

The application consists of two separate flows that are triggered by HTTP POST and GET requests.The POST request will 
use the file connector to read the contents of a csv file and use datamapper to transform it to required json. Then,
Salesforce rest connector will be used to create the contacts. Meanwhile you can check the progress of the import 
using GET request.

### Go Further

* Learn more about [File Connector](https://docs.wso2.com/display/ESBCONNECTORS/Working+with+the+File+Connector#WorkingwiththeFileConnector-append).
* Learn more about [Salesforce REST connector](https://docs.wso2.com/display/ESBCONNECTORS/Salesforce+REST+Connector).
* Read more on [WSO2 connectors](https://docs.wso2.com/display/ESBCONNECTORS/WSO2+ESB+Connectors+Documentation)
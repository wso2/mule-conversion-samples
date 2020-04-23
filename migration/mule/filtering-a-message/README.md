# Filtering a Message #

This example shows how to use validation components within Anypoint Studio to validate an incoming message.  

### Assumptions ###

This document describes the details of the example within the context of **WSO2 Integration Studio**, WSO2 EI’s graphical 
user interface (GUI). This document assumes that you are familiar with WSO2 EI and the 
[Integration Studio interface](https://ei.docs.wso2.com/en/latest/micro-integrator/overview/quick-start-guide/). To 
increase your familiarity with Integration Studio, consider completing one or more 
[WSO2 EI Tutorials](https://ei.docs.wso2.com/en/latest/micro-integrator/use-cases/integration-use-cases/).

### Example Use Case ###

This example application receives the list of users in JSON format. Every record in the list should contains id, email 
in correct format, object with information about user connection. Connection info should contains IP address. If all of 
the mentioned fields are presented and validation process is successful as well, we will see: *User records are valid!* 
in response of the HTTP call.

### Set Up and Run the Example ###

Follow the steps in this procedure to create and run this example in your own instance of Integration Studio. You can 
create template applications straight out of the box in Integration Studio and tweak the configurations of the 
use case-based templates to create your own customized applications in WSO2 Integrator.

1. Start WSO2 Integration Studio ([Installing WSO2 Integration Studio](https://ei.docs.wso2.com/en/latest/micro-integrator/develop/installing-WSO2-Integration-Studio/)).
2. In your menu in Studio, click the **File** menu. In the File menu select the **Import...** item.
3. In the Import window select the **Existing WSO2 Projects into workspace** under **WSO2** folder.
4. Browse and select the file path to the downloaded sample of this github project ("filtering-a-message" folder of the 
downloaded github repository).
5. Open the **FilteringAMessage.xml** under **filtering-a-message/FilteringAMessage/src/main/synapse-config/api** 
directory. 
6. The **FilteringAMessage.xml** is the graphical view of the simple hello world service.
7. Run the sample by right click on the **FilteringAMessageCompositeApplication** under the main **filtering-a-message** 
project and selecting **Export Project Artifacts and Run**.
8. Send a POST request with the following JSON in the body to *http://localhost:8290/filter*.

		[
        	{
        		"id": "8fa8435c-fca4-4b42-9b5a-e81f9bd9aa16",
        		"username": "bob",
        		"email": "bob@mulesoft",
        		"connectionInfo": {
        			"IPAddress": "212.141.190.171",
        			"MACAddress": "2A-7A-6F-D3-64-54"
        		}
        	},{
        		"id": "f125a585-df12-404a-8c80-711d117dd353",
        		"username": "greg",
        		"email": "greg@mulesoft.com",
        		"connectionInfo": {
        			"IPAddress": "128.211.42.181",
        			"MACAddress": "8D-BD-C3-C4-D8-A4"
        		}
        	},{
        		"id": "6d4747ee-eb00-4e81-b7dc-2b01585e6d99",
        		"username": "anna",
        		"email": "anna@mulesoft.com",
        		"connectionInfo": {
        			"IPAddress": "40.125.118.175",
        			"MACAddress": "9E-05-9B-68-8E-80"
        		}
        	}
        ]

9. Examine the response body to see that validation has been not successful:

        Email in record: {
          "id": "8fa8435c-fca4-4b42-9b5a-e81f9bd9aa16",
          "username": "bob",
          "email": "bob@mulesoft",
          "connectionInfo": {
            "IPAddress": "212.141.190.171",
            "MACAddress": "2A-7A-6F-D3-64-54"
          }
        } is not valid!
        
10. As you can see email is not valid. Try to fix it with *.com* suffix.

11. Examine the response body to see that validation has been not successful once again: 
        
        IP address of record: {
          "id": "6d4747ee-eb00-4e81-b7dc-2b01585e6d99",
          "username": "anna",
          "email": "anna@mulesoft.com",
          "connectionInfo": {
            "IPAddress": "40.125.118.175",
            "MACAddress": "9E-05-9B-68-8E-80"
          }
        } is on blacklist!
        
12. Looks like IPAddress of that user is blacklisted.

13. In case that you will send valid message, you will see: *User records are valid!* in the response of HTTP call.

### How it Works ###

The following steps outline the process to build an application for validation of messages.

1. Create a new Integration Project by going to **File > New > Integration Project** and name it **Filtering a Message**.
2. Use ForEach Mediator validate each record.
3. Add required validation criteria.
4. Export capp and run the sample.

### Go Further ###

* Learn more about configuring an [REST API](https://ei.docs.wso2.com/en/latest/micro-integrator/references/synapse-properties/rest-api-properties/) in Studio.
* Learn more on [PayloadFactory Mediator](https://ei.docs.wso2.com/en/next/micro-integrator/references/mediators/payloadFactory-Mediator/).
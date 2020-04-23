# Data Handling Basics

This example application contains few simple examples that introduce most of the basic data handling. We are using the
[File connector](https://store.wso2.com/store/assets/esbconnector/details/5d6de1a4-1fa7-434e-863f-95c8533d3df2) for this particular sample.

### Assumptions

This document describes the details of the example within the context of WSO2 Integration Studio, WSO2 EI’s graphical 
user interface (GUI). This document assumes that you are familiar with WSO2 EI and the 
[Integration Studio interface](https://ei.docs.wso2.com/en/latest/micro-integrator/overview/quick-start-guide/). To 
increase your familiarity with Integration Studio, consider completing one or more 
[WSO2 EI Tutorials](https://ei.docs.wso2.com/en/latest/micro-integrator/use-cases/integration-use-cases/).

### Set up and run the example

Follow the steps in this procedure to create and run this example in your own instance of Integration Studio. You can 
create template applications straight out of the box in Integration Studio and tweak the configurations of the use 
case-based templates to create your own customized applications in WSO2 Integrator.

1. Start WSO2 Integration Studio ([Installing WSO2 Integration Studio](https://ei.docs.wso2.com/en/latest/micro-integrator/develop/installing-WSO2-Integration-Studio/)).
2. In your menu in Studio, click the **File** menu. In the File menu select the **Import...** item.
3. In the Import window select the **Existing WSO2 Projects into workspace** under **WSO2** folder.
4. Browse and select the file path to the downloaded sample of this github project ("data-handling-basics" folder of the 
downloaded github repository).
5. Open the **HelloWorld.xml** under **data-handling-basics/DataHandlingBasics/src/main/synapse-config/api** directory. 
6. The **DataHandlingBasics.xml** is the graphical view of the simple hello world service.
7. Right click on the **DataHandlingBasics** project and select **Add or Remove Connector**. Keep the **Add connector**
option selected and click **Next**. Search for 'file' using the search bar and click the download button located at the
bottom right corner of the file connector. Click **Finish**.
8. Run the sample by right click on the **DataHandlingBasicsCompositeApplication** under the main **data-handling-basics** 
project and selecting **Export Project Artifacts and Run**. In WSO2 Platform Distribution window select both 
DataHandlingBasics and DataHandlingBasicsConnectorExporter and click finish.
9. ****Sample 1**** : Through a web browser, access the URL **http://localhost:8290/basic/greet1?username=yourName** 
   The response prints the words **Hello (yourName)** in your browser.
10. ****Sample 2**** : Through a web browser, access the URL **http://localhost:8290/basic/greet2?username=yourName**. 
This prints the words **Hello (yourName)** in your browser. Then, access the URL again, but this time do not include any 
parameters. Verify that the expected output is received.
11. ****Sample 3**** : Through a web browser, access the URL **http://localhost:8290/basic/greet3?username=yourName&age=22**. 
This will print the words **Hello (yourName)** in your browser and also save a txt file that contains this data.
12. ****Sample 4**** : In a browser, access the URL **http://localhost:8290/basic/greet4?username=yourName&age=22**. 
This will print the words **Hello (yourName)** in your browser and also save a csv file that contains this data.
13. ****Sample 5**** : You must now send the HTTP endpoint an HTTP request that includes a body with an attached XML 
file. Send a POST request to **http://localhost:8290/basic/greet5 ** attaching an XML to the body of the message. A 
sample XML is provided below.

The easiest way to do this is to send a POST via a browser extension such as Postman (for Google Chrome) or the c
url command line utility.
    
    		<user>
    		    <username> test </username>
    		    <age> 21 </age>
    		</user>
     
This will print the words Hello yourName in your browser and also save a txt file that contains this data.
13. ****Sample 6**** : You must now send the HTTP endpoint an HTTP request that includes a body with an attached JSON 
file. Send a POST request to **http://localhost:8290/basic/greet6**, attaching a JSON object the body of the message. A 
sample JSON is provided below.
    
The easiest way to do this is by sending a POST via a browser extension such as Postman (for Google Chrome) or the curl 
command line utility.
    
    		{ "username": "test", "age" : 21 }
     
This will print the words Hello yourName in your browser and also save a csv file that contains this data.

##### Sample 1 – Accessing Properties

This sample creates a simple web service that takes an HTTP request that includes a username parameter and returns a 
greeting using that username.

In this example you:

* access query parameter
* dynamically set the payload
 
##### Sample 2 – Dynamic Routing by Evaluating a Condition

In the previous sample, if your call to the service doesn't include a username parameter, it results in an error. This 
example use filter mediator that verifies if the required parameter is being passed.

In this example you:

* evaluate conditions in a choice component
* access query parameter
* dynamically set the payload
 
##### Sample 3 – Variable Assignment and Evaluating Conditions

In this sample, the service saves a text file with user data besides just returning a greeting. The call to the service 
will now include two parameters, username and age. The service stores these two parameters.

In this example you:

* access query parameters
* create a file including the user input
* dynamically set the payload


##### Sample 4 – Variable Assignment and Evaluating Conditions

In this sample, like in the previous one, the Integration application saves a CSV file with user data and returns a greeting. 

* access query parameters
* create a file including the user input
* dynamically set the payload

##### Sample 5 – XML request

In all the previous sample, calls to the service were made via GET requests that included query parameters. In this 
example, the service you create is an API that accepts POST requests with XML bodies. The required XML includes two 
parameters, username and age. The service stores these two parameters and saves a text file 

In this example you:

* generate an output based on evaluating the input
* parse an XML
* dynamically set the payload
* create a text file

##### Sample 6 – JSON request

This Sample is just like Sample 5, except that the service now receives JSON inputs rather than of XML.

The JSON input includes two parameters, username and age. The service stores these two parameters.

In this example, you will use DataWeave to:

* generate an output based on evaluating the input
* dynamically set the payload
* create a csv file
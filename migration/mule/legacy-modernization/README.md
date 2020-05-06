# Legacy Modernization

This application illustrates how to automate communication between a legacy system and a Web service which accepts 
HTTP requests. EI "front-ends" the legacy system – which only accepts input via a file – so as to prepare data from 
an HTTP Web service to a format that the legacy system accepts. To demonstrate these capabilities, this example adopts 
the use case of a legacy fulfillment system which must adapt to accept orders via HTTP request. You can think of this 
example as an application that acts as a Web service proxy for a legacy, file-based system.

This example shows you how to utilize the 
[File Connector](https://store.wso2.com/store/assets/esbconnector/details/5d6de1a4-1fa7-434e-863f-95c8533d3df2)
to append data in a file.

### Legacy Modernization ###
An older, legacy system may be limited in the form of data that it accepts. For example, an older system may only 
accept input as a file or via FTP. To update such a system so that it accepts more modern input formats, such as Web 
service calls, WSO2 EI can sit in front of a legacy system, converting HTTP requests, for example, to strings. This 
conversion activity effectively "modernizes" the legacy system so that it accepts HTTP requests.

### Assumptions ###

This document assumes that you are familiar with WSO2 EI and the 
[Integration Studio interface](https://ei.docs.wso2.com/en/latest/micro-integrator/overview/quick-start-guide/). To 
increase your familiarity with Integration Studio, consider completing one or more 
[WSO2 EI Tutorials](https://ei.docs.wso2.com/en/latest/micro-integrator/use-cases/integration-use-cases/).

### Example Use Case
This example demonstrates legacy system modernization within the context of a simple use case.

An organization uses an old fulfillment system which only accepts orders in flat file format. However, the company 
wants to begin accepting orders via a SOAP Web service and automatically submitting the orders to the legacy system 
for fulfillment. In order to process orders, the company uses WSO2 EI to convert HTTP requests into a file format that 
the legacy system accepts. 

### Set Up and Run the Example

#### Setting up the soap request ####
1. To simulate a request submission to the EI application, use the soapUI interface available for free download at 
www.soapui.org. This tool enables you to submit a request to simulate the submission of a new order in this example's 
use case. If you haven't already done so, download and launch soapUI.
2. In soapUI, select File > Import Project. Browse to the downloaded sample directory of this github project on your 
local drive to locate the sample soapUI project file: 
legacy-modernization/legacyModernization/src/main/resources/LegacyModernizationExample-soapui-project.xml. 
Click Open.
3. In the new LegacyModernizationExample project in soapUI, expand the folders to reveal Request 1. Double-click 
Request 1 to open the request-response window.

#### Setting up Integration Studio ####
1. Start WSO2 Integration Studio 
([Installing WSO2 Integration Studio](https://ei.docs.wso2.com/en/latest/micro-integrator/develop/installing-WSO2-Integration-Studio/)).
2. In your menu in Studio, click the **File** menu. In the File menu select the **Import...** item.
3. In the Import window select the **Existing WSO2 Projects into workspace** under **WSO2** folder.
4. Browse and select the file path to the downloaded sample of this github project 
("legacy-modernization" folder of the downloaded github repository).
5. Lets add the file connector into the workspace. Right click on the **legacyModernization** and select 
**Add or Remove Connector**. Keep the **Add connector** option selected and click **Next>**. Search for 'file' using the 
search bar and click the download button located at the bottom right corner of the file connector. Click **Finish**.
6. Copy the **CSVOutput.csv** file in 
**legacy-modernization/legacyModernization/src/main/resources/CSVOutput.csv** to a location on your computer.
7. Update the fileConnector.append/destination parameter in 
**legacy-modernization/legacyModernization/src/main/synapse-config/api/WriteToFile.xml** to the new location of the 
copied file in your computer. (By default it is set as **/Users/WSO2User/CSVOutput.csv**)
8. Save the modifications by pressing ctrl+s
9. Run the sample by right clicking on the **legacyModernizationCompositeApplication** project and selecting **Run as -> 
Run On Micro Integrator**. (Make sure to select legacyModernization, LegacyModernizationFileConnectorExporter, 
legacyModernizationRegistry while creating the car app during this step prompt wizard)
10. Now go back to the SoapUI to send the soap request
12. Click the submit request icon (green "play" button at upper left) to submit the request to the EI application 
(see below, left). soapUI displays the response from the EI application in the response pane (see below, right).
12. Review the contents of the SOAP response, to examine the details of your processed request. Note, in particular, 
the orderReceivedStatus with the value "true". The entire response in raw format will be as follows.

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ord="http://ordermgmt.org/">
   <soapenv:Body>
      <shippingOrderResponse xmlns="http://ws.apache.org/ns/synapse">
         <shippingOrderID>1234</shippingOrderID>
         <orderReceivedStatus>true</orderReceivedStatus>
      </shippingOrderResponse>
   </soapenv:Body>
</soapenv:Envelope>
```
13. Navigate to the **CSVOutput.csv** file in your computer and open it. You can see the following content added in it.

```
1234.0,500.0,1.99,WSO2,100 Geary St Level 4,San Francisco,CA,USA,94108.0,WSO2,100 Geary St Level 4,San Francisco,CA,USA,94108.0,1234.0
6789.0,1500.0,9.59,WSO2,100 Geary St Level 4,San Francisco,CA,USA,94108.0,WSO2,100 Geary St Level 4,San Francisco,CA,USA,94108.0,1234.0
9998.0,5000.0,3.00,WSO2,100 Geary St Level 4,San Francisco,CA,USA,94108.0,WSO2,100 Geary St Level 4,San Francisco,CA,USA,94108.0,1234.0

```

### How it works

Using a single flow, this application exposes a SOAP Web service which accepts new order requests from customers. The 
it tranforms the data received into the desired csv format which the legacy system accepts. Asynchronously (relative 
to the HTTP request-response activity), this application writes the data to a csv file, which it sends to the legacy 
system for fulfillment. In this example, since there is no actual legacy system to perform order fulfillment, the File 
connector at the end of the flow simply outputs the CSV file we configure.

### Go Further

* Learn more about [File Connector](https://docs.wso2.com/display/ESBCONNECTORS/Working+with+the+File+Connector#WorkingwiththeFileConnector-append).
* Learn more about [Salesforce REST connector](https://docs.wso2.com/display/ESBCONNECTORS/Salesforce+REST+Connector).
* Read more on [WSO2 connectors](https://docs.wso2.com/display/ESBCONNECTORS/WSO2+ESB+Connectors+Documentation)
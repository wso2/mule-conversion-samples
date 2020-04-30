# Web Service Consumer Example #

This application illustrates how to consume a Web service.
![WebServiceConsumerUseCase](../resources/images/web-service-consumer/web-service-consumer-use-case.png?raw=true "Web Service Consumer Use Case")

### Assumptions

This document describes the details of the example within the context of WSO2 Integration Studio, WSO2 EI’s graphical 
developer tool. This document assumes that you are familiar with WSO2 EI and the 
[Integration Studio interface](https://ei.docs.wso2.com/en/latest/micro-integrator/develop/WSO2-Integration-Studio/). To 
increase your familiarity with Integration Studio, consider completing one or more 
[WSO2 EI Tutorials](https://ei.docs.wso2.com/en/latest/micro-integrator/use-cases/integration-use-cases/).

### Example Use Case ###

This example application simulates consuming a Web service that belongs to a T-Shirt retailer. Through HTTP requests, customers can check the availability of products and place purchase orders. When the application receives an order, it turns the JSON input into XML, adds an APIKey header, and then sends the request to the Web service. The application then transforms the response from the Web service into JSON, builds a final response, and then sends the response to the requester.

When the application receives a list-inventory request, it forwards the request to the Web service, turns the response into JSON, and builds a final response for the requester.

### Set Up and Run the Example ###

1. Start WSO2 Integration Studio ([Installing WSO2 Integration Studio](https://ei.docs.wso2.com/en/latest/micro-integrator/develop/installing-WSO2-Integration-Studio/)).
2. In your menu in Studio, click the **File** menu. In the File menu select the **Import...** item.
3. In the Import window select the **Existing WSO2 Projects into workspace** under **WSO2** folder.
4. Browse and select the file path to the downloaded sample of this github project ("content-based-routing" folder of the downloaded github repository).
5. Open the **ContentBasedRoutingAPI.xml** under **web-service-consumer/WebServicesConsumer/src/main/synapse-config/api/WebServiceConsumerAPI.xml** directory. 
6. The **WebServiceConsumerAPI.xml** is the graphical view of the content based routing sample.
![WebServiceConsumerScreenshot](../resources/images/web-service-consumer/web-service-consumer.png?raw=true "Web Service Consumer screenshot")
7. Run the sample by right click on the **WebServicesConsumerCompositeApplication** under the main **web-service-consumer** project and selecting **Export Project Artifacts and Run**.  
**Result:** You can now send posts to your application via a browser extension such as Postman (for Google Chrome), or the `curl` command-line utility.
8. To get an inventory list for the retailer, send a GET request to http://localhost:8290/tshirt/inventory.
9. To send an order, send a PUT request to the address http://localhost:8290/tshirt/orders , setting the `Content-Type` header to `application/json` and appending the following JSON:

```
{
  "email":"info@wso2.com",
  "address1":"20 Palm Grove",
  "address2":"Colombo 03",
  "city":"Colombo",
  "country":"Sri Lanka",
  "name":"WSO2 SriLanka",
  "postalCode":"C1043AAQ",
  "size":"L",
  "stateOrProvince":"Western Province"
}
```

### How It Works ###

The Client-Side T-Shirt API example consumes a SOAP-based Web service, which accepts two different kinds of requests, each handled in a different flow, accessed through a different HTTP request path.

### orderTshirt Flow

The orderTshirt flow accepts HTTP requests that are directed at its address, then turns the JSON payload into XML by using the Datamapper transformer. Because the consumed Web service requires an APIKey to be passed with every request, the flow creates the XML SOAP header hardcoding the APIKey value using Datamapper transformer. With the XML envelope built, the flow then contacts the Web service via the Web Service Consumer. This flow is also responsible for returning a response to the caller to confirm that the order was processed, so it transforms the resulting response to JSON format.

### listInventory Flow

When issued a "list inventory" request, the flow directs it to the Web service via the Web service consumer, its response is then transformed into a JSON by the Datamapper transformer. Finally, the HTTP connector returns the response to the requester.

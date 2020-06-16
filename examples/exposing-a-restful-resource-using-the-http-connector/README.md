# Exposing a RESTful resource using the HTTP connector Example

This example application illustrates how to use the WSO2 EI to expose a RESTful resource using the REST APIs. After reading this document, creating and running the example, you should be able to leverage what you have learned to create a simple HTTP request-response application that can expose a RESTful resource by providing different verbs (HTTP methods) using JSON data.

### Assumptions

This document describes the details of the example within the context of Integration Studio, WSO2 EI’s graphical user interface (GUI). This document assumes that you are familiar with WSO2 EI and the [Integration Studio interface](https://ei.docs.wso2.com/en/latest/micro-integrator/develop/WSO2-Integration-Studio/). To increase your familiarity with Integration Studio, consider completing one or more [Integration Studio Tutorials](https://ei.docs.wso2.com/en/latest/micro-integrator/use-cases/integration-use-cases/).

### Example Use Case

In this example, a user calls the WSO2 application by submitting a request via a REST client. The application receives the request and processes it based on the URL. The application is capable of retrieving and inserting person data. <br> 
<img width="60%" src="../../../docs/assets/images/migration-mule/exposing-a-restful-resource-using-the-http-connector-use-case.png">

### Set Up and Run the Example

You can tweak the configurations of these use case-based examples to create your customized applications in WSO2.

1. Start WSO2 Integration Studio ([Installing WSO2 Integration Studio](https://ei.docs.wso2.com/en/latest/micro-integrator/develop/installing-WSO2-Integration-Studio/)).

2. In your menu in Studio, click the **File** menu. In the File menu select the **Import...** item.

3. In the Import window select the **Existing WSO2 Projects into workspace** under **WSO2** folder.

4. Browse and select the file path to the downloaded sample of this Github project (``integration-studio-examples/migration/mule/exposing-a-restful-resource-using-the-http-connector``) and click **Finish**.

5. Open the **RESTfulService.xml** under **exposing-a-restful-resource-using-the-http-connector/ExposeRESTfulService/src/main/synapse-config/api** directory.<br>
    <img width="40%" src="../../../docs/assets/images/migration-mule/exposing-a-restful-resource-using-the-http-connector.png">
    
6. The **RESTfulService.xml** is the graphical view of the service.

7. Run the sample by right click on the **ExposeRESTfulServiceCompositeApplication** under the main **exposing-a-restful-resource-using-the-http-connector** project and selecting **Export Project Artifacts and Run**.

8. Open HTTP Client in Integration Studio. Follow [HTTP Client Guidelines](../../../docs/common/adding-http-client-to-integration-studio.md) to open HTTP Client if the window is not visible in the interface.

9. Send a POST request to *http://localhost:8290/person* with following JSON payload
```json
{
   "firstname":"John",
   "lastname":"Doe",
   "age":"12",
   "address":{
      "streetAddress":"Lincoln St.",
      "city":"San Francisco",
      "state":"CA",
      "zipCode":"90401"
   }
}
```

10. You should receive a response saying a person was created successfully. For example: 
```json
{
   "personId":"1",
   "message":"Person was created"
}
```

11. Send a GET request to *http://localhost:8290/person*.
 You should receive a response with all created persons.
 
12. Take the value of `personId` from the previous response and send a GET request to *http://localhost:8290/person/{personId}*
 You should receive a response with the person data:
```json
{
   "personId":"1",
   "firstname":"John",
   "lastname":"Doe",
   "address":{
      "streetAddress":"Lincoln St.",
      "city":"San Francisco",
      "state":"CA",
      "zipCode":"90401"
   },
   "age":12
}
```

### How it Works

The **Exposing a RESTful resource using the HTTP connector** example application consists of three, simple [API Resources](https://ei.docs.wso2.com/en/latest/micro-integrator/develop/creating-artifacts/creating-an-api/) which receive end-user HTTP requests, set payloads based on the request paths, and send data and return the payloads to end-users as HTTP responses.

The sections below elaborate further on the configurations of the application and how it works to respond to end-user requests.

### POST Request Handling (1st Resource)

When an end user's POST request reaches the application, first it will come to REST API's first resource, which listens to POST requests at *http://localhost:8290/person*. 

Next, the PayloadFactory create the following message to send back to the client: 
```json
{ "personId": "1", "message": "Person was created"}
```

Finally, WSO2 EI sends the message back to the client using [Respond mediator](https://ei.docs.wso2.com/en/latest/micro-integrator/references/mediators/respond-Mediator/).

### GET /person/{personId} Resource (2nd Resource)

This resource is responsible for retrieving a specific person's data, based on the provided person ID. It starts with a WSO2 API Resource, which listens at http://localhost:8290/person/{personId}.

Next, the flow will create a mock person detail using the PayloadFactory mediator and send the response using Respond mediator.<br>
**Note** this sample is only sending a mock response with the peronId path parameter as the ID
    

### GET Resource for /person (3rd Resource)

When an end-user request reaches the application for http://localhost:8290/person the 3rd Resource of the API will get invoked.

Next, the resource creates a dummy payload with a set of two-person details using [PayloadFactory mediator](https://ei.docs.wso2.com/en/latest/micro-integrator/references/mediators/payloadFactory-Mediator/).

Finally, the response is sent back to the client using the Respond Mediator.<br>
**Note** this sample is only sending a mock response with pre-defined person details

  
### Go Further

- Learn more about the [HTTP REST API](https://ei.docs.wso2.com/en/latest/micro-integrator/references/synapse-properties/rest-api-properties/).

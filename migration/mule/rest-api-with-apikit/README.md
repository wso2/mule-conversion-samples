# REST API with APIkit

This example illustrates an API implementation with Integration Studio which was created using a OAS2.0 swagger file. A mock backend is used in the implementation, whereas the user is provided the capability of using a backend of choice instead of the one provided.

### Assumptions ###

This document assumes that you are familiar with the [Integration Studio](https://ei.docs.wso2.com/en/latest/micro-integrator/develop/WSO2-Integration-Studio/). To increase your familiarity with Studio, consider completing one or more [Integration Studio Tutorials](https://ei.docs.wso2.com/en/latest/micro-integrator/use-cases/integration-use-cases/). Further, this example assumes that you have a basic understanding of [EI flows](https://ei.docs.wso2.com/en/latest/micro-integrator/overview/key-concepts/).

### Example Use Case ###

This API mocks endpoints for customer entity.

### Set Up and Run the Example ###

Follow the procedure below to run and test the functionality in Integration Studio.

1. Start WSO2 Integration Studio ([Installing WSO2 Integration Studio](https://ei.docs.wso2.com/en/latest/micro-integrator/develop/installing-WSO2-Integration-Studio/)).
2. In your menu in Studio, click the **File** menu. In the File menu select the **Import...** item.
3. In the Import window select the **Existing WSO2 Projects into workspace** under **WSO2** folder.
4. Browse and select the file path to the downloaded sample of this Github project
(``integration-studio-examples/migration/mule/rest-api-with-apikit``) and click **Finish**.
5. Open the **RestApiWithApikit.xml** under **rest-api-with-apikit/RestApiWithApikit/src/main/synapse-config/api** 
directory.
<p align="center">
  <img width="70%" src="../../../docs/assets/images/migration-mule/rest-api-with-apikit.png">
</p>

6. The **RestApiWithApikit.xml** is the graphical view of the resources of the used API. This design is based on a OAS2.0 swagger file. It is provided in **rest-api-with-apikit/RestApiWithApikit/src/main/resources**. You can refer [Creating a REST API](https://ei.docs.wso2.com/en/latest/micro-integrator/develop/creating-artifacts/creating-an-api/) to create the project from scratch using the swagger file.
7. Run the sample by right click on the **RestApiWithApikitCompositeApplication** under the main **rest-api-with-apikit** project and selecting **Export Project Artifacts and Run**.
8. Open HTTP Client in Integration Studio. Follow [HTTP Client Guidelines](../../../docs/common/adding-http-client-to-integration-studio.md)
to open HTTP Client if the window is not visible in the interface.
9. Make a GET request to *http://localhost:8290/api/customers*.

You can use a browser extension such as Postman or the curl command line utility. As a response to this request, you should receive a list of customers. You can try out the other resources in the **RestApiWithApikit.xml** to test the behaviour.

### How it Works ###

You can use the [Payload Factory](https://ei.docs.wso2.com/en/latest/micro-integrator/references/mediators/payloadFactory-Mediator/) to alter the mock response payload you receive. Integration Studio supports multiple REST methods for a single resource. Therefore, as **RestApiWithApikit.xml** suggests you can use a switch mediator with multiple cases (HTTP method property of the request can be taken using `get-property('axis2', 'HTTP_METHOD')`) to handle the logic for different REST methods.

>**NOTE**
If you have a RAML file for the API specification, you can use a tool such as [OAS RAML Converter](https://mulesoft.github.io/oas-raml-converter/) to convert it to OAS2.0 in order to be used in Integration Studio.

### Go Further 

* Learn more about configuring a [REST API](https://ei.docs.wso2.com/en/latest/micro-integrator/references/synapse-properties/rest-api-properties/) in Studio.
* Learn more about configuring [Endpoints](https://ei.docs.wso2.com/en/latest/micro-integrator/references/synapse-properties/endpoint-properties/) in Studio.

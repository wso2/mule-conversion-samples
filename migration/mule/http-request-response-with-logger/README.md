# HTTP Request-Response with Logger Example

This example application demonstrat how to use WSO2 EI to build a simple HTTP request-response application. This example was designed to demonstrate the ability of a WSO2 Integration applications to interact with HTTP request and read parameters from the URL. The goal is to introduce users to Integration Studio by illustrating very simple functionality.

### Assumption

This document describes the details of the example within the context of WSO2 Integration Studio, WSO2 EI’s graphical user interface (GUI). This document assumes that you are familiar with WSO2 EI and the [Integration Studio interface](https://ei.docs.wso2.com/en/latest/micro-integrator/overview/quick-start-guide/). To increase your familiarity with Integration Studio, consider completing one or more [WSO2 EI Tutorials](https://ei.docs.wso2.com/en/latest/micro-integrator/use-cases/integration-use-cases/).

### Set Up and Run the Example

Follow the steps in this procedure to create and run this example in your own instance of Integration Studio. You can create template applications straight out of the box in Integration Studio and tweak the configurations of the use case-based templates to create your own customized applications in WSO2 Integrator.

1. Start WSO2 Integration Studio ([Installing WSO2 Integration Studio](https://ei.docs.wso2.com/en/latest/micro-integrator/develop/installing-WSO2-Integration-Studio/)).
2. In your menu in Studio, click the **File** menu. In the File menu select the **Import...** item.
3. In the Import window select the **Existing WSO2 Projects into workspace** under **WSO2** folder.
4. Browse and select the file path to the downloaded sample of this github project ("http-request-response-with-logger" folder of the downloaded github repository).
5. Open the **HttpRequestResponseWithLogger.xml** under **http-request-response-with-logger/HttpRequestResponseWithLogger/src/main/synapse-config/api/HttpRequestResponseWithLoggerAPI.xml** directory. 
6. The **HttpRequestResponseWithLoggerAPI.xml** is the graphical view of the simple http-request-response-with-logger service.
7. Run the sample by right click on the **HttpRequestResponseWithLoggerCompositeApplication** under the main **http-request-response-with-logger** project and selecting **Export Project Artifacts and Run**.
8. In the address bar, type the following URL: `http://localhost:8290/api/moon
9. Press Enter to send the request and server sends the reponse as `/moon`.

### How it Works

This example consists a simple [Synapse API](https://ei.docs.wso2.com/en/latest/micro-integrator/develop/creating-artifacts/creating-an-api/) that accept a HTTP request and read the postfix of the URL. Log mediator used to log the URL prefix and response send back to the client by suing a [Payload factory](https://ei.docs.wso2.com/en/latest/micro-integrator/references/mediators/payloadFactory-Mediator/) mediator.

After opening the Integration project, there are two projects under the http-request-response-with-logger main project. First project (named as "HttpRequestResponseWithLogger") is the WSO2 Integration Project where all the integration use-case related file are stored. Second one is the "HttpRequestResponseWithLoggerCompositeApplication", which is used as the Packaging and exporting artifact to the server runtime.

You can run **Composite Application** using WSO2 Enterprise Integrator server runtime.
### Go Further

* Learn more about configuring an [REST API](https://ei.docs.wso2.com/en/latest/micro-integrator/references/synapse-properties/rest-api-properties/) in Studio.
* Read about the concept of [WSO2 Message Transforming Mediators](https://ei.docs.wso2.com/en/latest/micro-integrator/references/mediators/about-mediators/) in WSO2 Enterprise Integrator.
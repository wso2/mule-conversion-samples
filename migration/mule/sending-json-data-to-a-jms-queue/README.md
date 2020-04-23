# Sending JSON Data to a JMS Queue

WSO2 offers components that are easy to use for connecting to JMS queues and topics. This example shows how to use Apache 
ActiveMQ, a leading open-source JMS implementation from Apache that supports the JMS 1.1 specification.

### Assumptions
This document describes the details of the example within the context of WSO2 Integration Studio, WSO2 EI’s graphical 
user interface (GUI). This document assumes that you are familiar with WSO2 EI and the 
[Integration Studio interface](https://ei.docs.wso2.com/en/latest/micro-integrator/overview/quick-start-guide/). 
To increase your familiarity with Integration Studio, consider completing one or more 
[WSO2 EI Tutorials](https://ei.docs.wso2.com/en/latest/micro-integrator/use-cases/integration-use-cases/).

### Sample Use Case
In this example, an HTTP request holding JSON sales data reaches an HTTP endpoint. A success message is logged and the 
data is added to a JMS queue, where you can view it in the ActiveMQ admin console.

### Set Up and Run the Example
Follow the steps in this procedure to create and run this example in your own instance of Integration Studio. You can 
create template applications straight out of the box in Integration Studio and tweak the configurations of the use 
case-based templates to create your own customized applications in WSO2 Integrator.

1. Start WSO2 Integration Studio ([Installing WSO2 Integration Studio](https://ei.docs.wso2.com/en/latest/micro-integrator/develop/installing-WSO2-Integration-Studio/)).
2. In your menu in Studio, click the **File** menu. In the File menu select the **Import...** item.
3. In the Import window select the **Existing WSO2 Projects into workspace** under **WSO2** folder.
4. Browse and select the file path to the downloaded sample of this github project (`sending-json-data-to-a-jms-queue` 
folder of the downloaded github repository).
5. Open the **SendingJsonDataToAJmsQueue.xml** under **sending-json-data-to-a-jms-queue/SendingJsonDataToAJmsQueue/src/main/synapse-config/api/SendingJsonDataToAJmsQueue.xml** directory. 
6. The **SendingJsonDataToAJmsQueue.xml** is the graphical view of the sample.
7. Configure WSO2 Micro Integrator to connect with [ActiveMQ](https://ei.docs.wso2.com/en/latest/micro-integrator/setup/brokers/configure-with-ActiveMQ/)
8. Run the sample by right click on the **SendingJsonDataToAJmsQueueCompositeApplication** under the main 
**sending-json-data-to-a-jms-queue** project and selecting **Export Project Artifacts and Run**.
9. Send JSON data to the URL by using REST Console or `curl`. Use the following information to make your request.

        Request URI: http://localhost:8290/sales
        Request method: POST
        Body : {"ITEM_ID" : 001, "ITEM_NAME" : "Shirt", "QTY" : 1, "PRICE" : 20}
10. Log in to the ActiveMQ admin page at [http://localhost:8161/admin/queues.jsp](http://localhost:8161/admin/queues.jsp)
 with the default username and password “admin”. Check whether the message was added to the queue
In the ActiveMQ queue, click the Sales link and then the link under the Message ID column. You'll see the details of the 
message that was added to the JMS queue.


### Go Further

* Learn more about configuring an [REST API](https://ei.docs.wso2.com/en/latest/micro-integrator/references/synapse-properties/rest-api-properties/) in Studio.
* Read about the concept of [WSO2 Message Transforming Mediators](https://ei.docs.wso2.com/en/latest/micro-integrator/references/mediators/about-mediators/) in WSO2 Enterprise Integrator.
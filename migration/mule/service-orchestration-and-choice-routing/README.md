# Service Orchestration and Choice Routing

This example illustrates how the orchestration of Web service calls and asynchronous message processing are utilized to realize an order processing application. With efficient routing and iterative processing, the integration service coordinates with web services hosted by respective vendors to complete the ordering process.

#### Iterative Processing and Aggregating 

**Iterative mediator** allows us to split a message into a collection of elements and process them iteratively. The iterative mediator has the capability to process the elements parallelly resulting in an efficient mediation flow. Hence, it is recommended when iterative processing involves back-end calls. **Aggregator mediator** then collects and combines the results of each iteration. In this example, a single order contains multiple items and in order to gather information regarding these individual items, they are processed iteratively. Finally, they are aggregated to calculate the total.

#### Content-Based Routing 

Using the **Switch mediator**, WSO2 provides the capability to route different messages into different flows of meditation. We can simply provide an expression to evaluate a property of the message or part of the payload then route the message to a specific sequence (a series of mediators). For instance, suppose our expression evaluates the name of the manufacturer of a product, then products from manufacturer A can be routed to sequence X and others to sequence Y.

#### Service Orchestration 
When it is required to make several web service calls to complete the processing of a single message finally constructing a single response back to the caller, the process is commonly referred to as service orchestration. In this example, calls are made to get prices from different vendors and to provide order details to an auditing service. But in the end, the customer is notified with a single response.

#### Asynchronous Message Processing
In order to drive the latency to a minimum, it is desirable to execute parts of the process, which are not necessary to complete before the response is delivered, asynchronously. With WSO2, asynchronous message processing can be realized in various ways. Message cloning is one of the simplest such mechanisms. With a **Clone mediator**, a message is duplicated with its context and sent through multiple paths. All the processing that is mandatory to complete prior to responding can be executed with one clone and other processes can be executed with one or more other clones.

### Assumption

This document assumes that you are familiar with WSO2 EI, the 
[Integration Studio interface](https://ei.docs.wso2.com/en/latest/micro-integrator/overview/quick-start-guide/), SOAP calls, and Web service development with WSDL. To 
increase your familiarity with Integration Studio, consider completing one or more 
[WSO2 EI Tutorials](https://ei.docs.wso2.com/en/latest/micro-integrator/use-cases/integration-use-cases/).

### Example Use Case

This application demonstrates the processing of orders in a store. Since an order can contain multiple items, that are processed iteratively. Price and other details of each item are obtained by making web service calls from respective vendors and using them final order details are calculated and a summary is sent back to the customer. Order details are also audited by a separate service and stored for future needs.

### Set Up and Run the Example 

1. Install the MySQL server.
2. Download the JDBC driver for MySQL from [here](https://dev.mysql.com/downloads/connector/j/) and copy it to your 
<MI_HOME>/lib directory.
3. Start the MySQL server.
4. Create the database
    ```sql
    create database OrderProcess;
    ```
5. Create the tables `orders` and `order_audits`. 
    ```sql
   CREATE TABLE `orders` (  `id` varchar(256) NOT NULL,  `product_id` varchar(256) DEFAULT NULL,  `name` varchar(256) DEFAULT NULL,  `manufacturer` varchar(256) DEFAULT NULL,  `quantity` int(11) DEFAULT NULL,  `price` int(11) DEFAULT NULL,  PRIMARY KEY (`id`));
   CREATE TABLE `order_audits` ( `id` varchar(256) NOT NULL, `order_id` varchar(256) DEFAULT NULL,  `total_value` int(11) DEFAULT NULL,  PRIMARY KEY (`id`));
    ```
6. Create the user `process_manager` and grant access to the created tables. If you want to use an existing user, synapse configurations need to be updated with details of the relevant user:
    ```sql
    CREATE USER 'process_manager'@'localhost' IDENTIFIED BY 'process123';
    GRANT ALL PRIVILEGES ON `OrderProcess`.`orders` TO 'process_manager'@'localhost';
    GRANT ALL PRIVILEGES ON `OrderProcess`.`order_audits` TO 'process_manager'@'localhost';
    ```
7. Start WSO2 Integration Studio ([Installing WSO2 Integration Studio](https://ei.docs.wso2.com/en/latest/micro-integrator/develop/installing-WSO2-Integration-Studio/)).
8. In your menu in Studio, click the **File** menu. In the File menu select the **Import...** item.
9. In the Import window select the **Existing WSO2 Projects into workspace** under **WSO2** folder.
10. Browse and select the file path to the downloaded sample of this Github project 
(`integration-studio-examples/migration/mule/service-orchestration-and-choice-routing`) and click **finish**.
11. Run the sample by right click on the **ProcessOrderCompositeApplication** under the main 
**service-orchestration-and-choice-routing** project and selecting **Export Project Artifacts and Run**.
12. Open HTTP Client in Integration Studio. Follow [HTTP Client Guidelines](../../../docs/common/adding-http-client-to-integration-studio.md)
to open HTTP Client if the window is not visible in the interface.
13. Make a POST request to `http://localhost:8290/services/Orders`.
14. Add following `SOAPAction` and `Content-Type` headers.
    - SOAPAction: processOrder
    - Content-Type: text/xml
15. Add the following input payload.
    ```xml
    <soapenv:Envelope 
     	xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
     	xmlns:ord="http://orders.wso2.org/">
        <soapenv:Header/>
        <soapenv:Body>
           <ord:processOrder>
                 <order>
                 <orderId>1223</orderId>
                 <customer>
                    <address>Main Street 123</address>
                    <firstName>John</firstName>
                    <lastName>Doe</lastName>
                 </customer>
                <item>
                   <manufacturer>Samsung</manufacturer>
                   <name>s-10</name>
                   <productId>SM350</productId>
                   <quantity>1</quantity>
                </item>
                <item>
                   <manufacturer>SoftSell</manufacturer>
                   <name>B12</name>
                   <productId>LK12</productId>
                   <quantity>2</quantity>
                </item>              
              </order>
           </ord:processOrder>
        </soapenv:Body>
     </soapenv:Envelope>
    ```
16. Following response can be observed in the HTTP Response pane.
    ```xml
    <?xml version='1.0' encoding='UTF-8'?>
    <soap:Response xmlns:soap="http://schemas.xmlsoap.org/soap/envelope">
        <message>"Order 1223 was processed successfully" </message>
    </soap:Response>
    ```
	
### How It Works
When an order is received (by the Orders proxy), first the payload is converted to JSON using the **DataMapper mediator** which is then passed on to the **Iterative mediator** to split the message into individual items of the order. According to the manufacturer of the item, items are then passed into different sequences. If the manufacturer is `Samsung`, then the item is routed to the **SamsungOrder sequence**. Otherwise, to the **InHouseOrder sequnece**. These sequences obtain prices for each item by calling the respective web services and return them. **Aggregator mediator** aggregate all the responses and calculate the total value of the order. Next, the **Clone mediator** creates two copies of the message. One message is used to send the response while the other is used to invoke the **Audit_service sequence**. The followings are the components of the application.

#### Orders Proxy Service
Order service is the point of entry of an order. The service, exposed as a SOAP-based Web service, accepts the order, process items iteratively, and sends an order summary to the customer.
![Orders Proxy](../resources/images/service-orchestration-and-choice-routing/order-proxy.png?raw=true "Orders Service")

#### SamsungOrderService Proxy Service
The external Web service representing the vendor `Samsung`.

#### SamsungOrder sequence
When the manufacturer of the item is `Samsung`, the item is passed to this sequence which calls the vendor `SamsungOrderService` to obtain price and other information from the vendor.
![SamsungOrder Sequence](../resources/images/service-orchestration-and-choice-routing/samsung-order-sequence.png?raw=true "Samsung Order Sequence")
	
#### InHouseOrder sequnece 
When the manufacturer of the item is not `Samsung`, the item is passed to this sequence which calls the in house RESTful PriceService API to obtain price and log order data.
![InHouse Order](../resources/images/service-orchestration-and-choice-routing/in-order-sequence.png?raw=true "In-Order Sequence")

#### Audit_service sequence
Audits the order and logs the results.

#### PriceService API
Provides prices for non-Samsung products.

### Go Further 

* Learn more about [Message Routing](https://ei.docs.wso2.com/en/latest/micro-integrator/use-cases/tutorials/routing-requests-based-on-message-content/).
* Learn more about [Service Orchestration](https://ei.docs.wso2.com/en/latest/micro-integrator/use-cases/tutorials/exposing-several-services-as-a-single-service/). 
* Learn more about [Asynchronous Messaging](https://ei.docs.wso2.com/en/latest/micro-integrator/use-cases/tutorials/storing-and-forwarding-messages/).
* Read more on [WSO2 Proxy Services](https://ei.docs.wso2.com/en/latest/micro-integrator/use-cases/examples/proxy_service_examples/Introduction-to-Proxy-Services/)
* Read more on [Iterate Mediator](https://ei.docs.wso2.com/en/latest/micro-integrator/references/mediators/iterate-Mediator/)
* Read more on [DB Report Mediator](https://ei.docs.wso2.com/en/latest/micro-integrator/references/mediators/dB-Report-Mediator/)
* Read more on [Clone Mediator](https://ei.docs.wso2.com/en/latest/micro-integrator/references/mediators/clone-Mediator/)
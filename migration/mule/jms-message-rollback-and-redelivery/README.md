# JMS message rollback and redelivery

This example shows how to implement jms rollback and redelivery within the WSO2 Integration Studio. While when we passing the jms messages inside the WSO2 EI, following failures could be happening.
* Failures during message mediation process.
* Message sending failure,due to endpoint is unavailability.

The JMS message passing flow in this example is configured with the ActiveMQ MOM. The requirement is if any of the above failures happen, the message should not process further and send to end point, rather it should roll back. After a specific number of unsuccessful failures happen the message should send to ActiveMQ topic.
## Assumptions

This document describes the details of the example within the context of WSO2 Integration Studio, editor to develop WSO2 EI artifacts. This document assumes that you are familiar with WSO2 EI and the [Integration Studio interface](https://ei.docs.wso2.com/en/latest/micro-integrator/develop/WSO2-Integration-Studio/). To increase your familiarity with Integration Studio, consider completing one or more [WSO2 EI Tutorials](https://ei.docs.wso2.com/en/latest/micro-integrator/use-cases/integration-use-cases/).

## Example Use Case

In this example, there is a JMS message consuming inbound endpoint implemented inside WSO2 EI. It listens to the ActiveMQ Queue. After consuming the JMS message by the Inbound endpoint, which rollbacks and retries to deliver the same message to the topic reside in the ActiveMQ. This rollback process is handled by `OnErrorPropagate` sequence.  

The number of JMS message delivery count and filtering process is handled by sequence called `choice`. After a specific number of unsuccessful attempts to commit (4 in this case), it sends the message successfully.

## Set Up and Run the Example

Follow the steps in this procedure to create and run this example in your own instance of Integration Studio. You can create template applications straight out of the box in Integration Studio and tweak the configurations of the use case-based templates to create your own customized applications in WSO2 Integrator.

1. Start WSO2 Integration Studio. See [Installing WSO2 Integration Studio](https://ei.docs.wso2.com/en/latest/micro-integrator/develop/installing-WSO2-Integration-Studio/) 
2. In your menu in Studio, click the File menu. In the File menu select the **Import...** item.
3. In the Import window select the **Existing WSO2 Projects into workspace** under **WSO2** folder.
4. Browse and select the file path to the downloaded sample of this github project (`jms-message-rollback-and-redelivery` folder of the downloaded github repository)
5. For setup the ActiveMQ with WSO2 EI please refer [Setting up the Micro Integrator with ActiveMQ](https://ei.docs.wso2.com/en/latest/micro-integrator/setup/brokers/configure-with-ActiveMQ/#setting-up-the-micro-integrator-with-activemq).
6. Now login to the activeMQ admin console at http://localhost:8161/admin/send.jsp with the default username and password admin. Create a Queue name as "in". Type json message (Ex: `{"name":"John"}`) and then click on Send.
7. The transaction will fail with not matched with 'jms.message.delivery.count' condition in `choice` sequence. The first four attempts (to simulate an error condition), and then the message will be delivered correctly to the `topic1`.   
8. Search for "topic1" in http://localhost:8161/admin/topics.jsp and notice that the number under the Messages Enqueued column has increased by 1. This verifies that the message has been delivered correctly and the transaction was successful.   
9. You can see a log entry in WSO2 server console similar to the following.
    
   ```
   [2020-05-01 12:00:31,148]  INFO {org.apache.synapse.mediators.builtin.LogMediator} - DeliveryCount = 1
   [2020-05-01 12:00:31,149]  INFO {org.apache.synapse.mediators.builtin.LogMediator} - This is the reason why the processing has failed = Execute the failing script
   [2020-05-01 12:00:31,155] ERROR {org.apache.synapse.core.axis2.Axis2Sender} - Inbound Response Sender not found - Inbound Endpoint may not support sending a response back
   [2020-05-01 12:00:31,179]  WARN {org.apache.synapse.core.axis2.Axis2SynapseEnvironment} - Executing fault handler due to exception encountered
   [2020-05-01 12:00:31,181]  INFO {org.apache.synapse.mediators.builtin.LogMediator} - Error Message = An Unexpeted erroe occured
   [2020-05-01 12:00:33,091]  INFO {org.apache.synapse.mediators.builtin.LogMediator} - DeliveryCount = 2
   [2020-05-01 12:00:33,092]  INFO {org.apache.synapse.mediators.builtin.LogMediator} - This is the reason why the processing has failed = Execute the failing script
   [2020-05-01 12:00:33,094] ERROR {org.apache.synapse.core.axis2.Axis2Sender} - Inbound Response Sender not found - Inbound Endpoint may not support sending a response back
   [2020-05-01 12:00:33,137]  WARN {org.apache.synapse.core.axis2.Axis2SynapseEnvironment} - Executing fault handler due to exception encountered
   [2020-05-01 12:00:33,138]  INFO {org.apache.synapse.mediators.builtin.LogMediator} - Error Message = An Unexpeted erroe occured
   [2020-05-01 12:00:35,090]  INFO {org.apache.synapse.mediators.builtin.LogMediator} - DeliveryCount = 3
   [2020-05-01 12:00:35,091]  INFO {org.apache.synapse.mediators.builtin.LogMediator} - This is the reason why the processing has failed = Execute the failing script
   [2020-05-01 12:00:35,093] ERROR {org.apache.synapse.core.axis2.Axis2Sender} - Inbound Response Sender not found - Inbound Endpoint may not support sending a response back
   [2020-05-01 12:00:35,139]  WARN {org.apache.synapse.core.axis2.Axis2SynapseEnvironment} - Executing fault handler due to exception encountered
   [2020-05-01 12:00:35,140]  INFO {org.apache.synapse.mediators.builtin.LogMediator} - Error Message = An Unexpeted erroe occured
   [2020-05-01 12:00:37,089]  INFO {org.apache.synapse.mediators.builtin.LogMediator} - DeliveryCount = 4
   [2020-05-01 12:00:37,089]  INFO {org.apache.synapse.mediators.builtin.LogMediator} - This is the reason why the processing has failed = Execute the failing script
   [2020-05-01 12:00:37,090] ERROR {org.apache.synapse.core.axis2.Axis2Sender} - Inbound Response Sender not found - Inbound Endpoint may not support sending a response back
   [2020-05-01 12:00:37,107]  WARN {org.apache.synapse.core.axis2.Axis2SynapseEnvironment} - Executing fault handler due to exception encountered
   [2020-05-01 12:00:37,108]  INFO {org.apache.synapse.mediators.builtin.LogMediator} - Error Message = An Unexpeted erroe occured
   [2020-05-01 12:00:40,091]  INFO {org.apache.synapse.mediators.builtin.LogMediator} - DeliveryCount = 5
   [2020-05-01 12:00:40,093]  INFO {org.apache.synapse.mediators.builtin.LogMediator} - No exception thrown = Transaction without error
   [2020-05-01 12:00:40,139]  INFO {org.apache.synapse.core.axis2.TimeoutHandler} - This engine will expire all callbacks after GLOBAL_TIMEOUT: 120 seconds, irrespective of the timeout action, after the specified or optional timeout
   [2020-05-01 12:00:40,146]  INFO {org.apache.axis2.transport.jms.JMSConnectionFactory} - JMS ConnectionFactory : jms:/topic1?transport.jms.ConnectionFactoryJNDIName=TopicConnectionFactory&java.naming.factory.initial=org.apache.activemq.jndi.ActiveMQInitialContextFactory&java.naming.provider.url=tcp://localhost:61616&transport.jms.DestinationType=topic initialized
   [2020-05-01 12:00:40,186]  INFO {org.apache.synapse.mediators.builtin.LogMediator} - Rollback transaction = The message rolls back to its original state for reprocessing.
   ```

### Go Further 

Please refer following topics to learn more about JMS 

* [JMS Inbound](https://ei.docs.wso2.com/en/latest/micro-integrator/references/synapse-properties/inbound-endpoints/polling-inbound-endpoints/jms-inbound-endpoint-properties/).
* [Publish and Subscribe with JMS](https://ei.docs.wso2.com/en/latest/micro-integrator/use-cases/examples/jms_examples/publish-subscribe-with-jms/)
* [Connecting to ActiveMQ](https://ei.docs.wso2.com/en/latest/micro-integrator/setup/brokers/configure-with-ActiveMQ/)
* [JMS Transport Parameters](https://ei.docs.wso2.com/en/latest/micro-integrator/references/synapse-properties/transport-parameters/jms-transport-parameters/)
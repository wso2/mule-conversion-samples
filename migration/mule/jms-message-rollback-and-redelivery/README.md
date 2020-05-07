# JMS message rollback and redelivery

This example shows how to implement jms rollback and redelivery within the WSO2 Integration Studio. The JMS message passing flow in this example is configured with the ActiveMQ MOM.
If one of the following failures occurred, the message should be rollbacked.
* Failures during message mediation process.
* Message sending failure, due to endpoint is unavailability.

## Assumptions

This document describes the details of the example within the context of WSO2 Integration Studio, WSO2 EI’s graphical developer tool. This document assumes that you are familiar with WSO2 EI and the [Integration Studio interface](https://ei.docs.wso2.com/en/latest/micro-integrator/develop/WSO2-Integration-Studio/). To increase your familiarity with Integration Studio, consider completing one or more [WSO2 EI Tutorials](https://ei.docs.wso2.com/en/latest/micro-integrator/use-cases/integration-use-cases/).
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               
## Example Use Case

In this example there is a JMS message in transaction inside WSO2 EI mediation flow which throws an exception when processed. This message is then handled by the `OnErrorPropagate` sequence, which rollbacks and retries to deliver the same message. The number of JMS message delivery count and filtering process is handled by `choice` sequence. 
After a specific number of unsuccessful attempts to commit (4 in this case), it sends the message successfully.
<p align="center">
    <img width="70%" src="../../../docs/assets/images/migration-mule/jms-message-rollback-and-redelivery-use-case.png">
</p>

## Set Up and Run the Example

1. Start WSO2 Integration Studio. See [Installing WSO2 Integration Studio](https://ei.docs.wso2.com/en/latest/micro-integrator/develop/installing-WSO2-Integration-Studio/) 
2. In your menu in Studio, click the File menu. In the File menu select the **Import...** item.
3. In the Import window select the **Existing WSO2 Projects into workspace** under **WSO2** folder.
4. Browse and select the file path to the downloaded sample of this Github project (``integration-studio-examples/migration/mule/jms-message-rollback-and-redelivery``) and click **Finish**.
5. Open **jmsMessageRollbackAndRedelivery.xml** under **jms-message-rollback-and-redelivery/jms-message-rollback-redelivery/src/main/synapse-config/inbound-endpoints** directory.<br>
   Here we have defined jms configuration parameters, sequence that the message should be injected, fault sequence that should be invoked in case of failure.
   
   <p align="center">
     <img width="20%" src="../../../docs/assets/images/migration-mule/jms-message-rollback-and-redelivery-inboundEP.png">
   </p>
   
   Open **choice.xml** under **jms-message-rollback-and-redelivery/jms-message-rollback-redelivery/src/main/synapse-config/sequences** directory.<br>
   In this sequence we throw an exception for first 4 messages and sends the message in 5th attempt.
    
   <p align="center">
     <img width="60%" src="../../../docs/assets/images/migration-mule/jms-message-rollback-and-redelivery-choice-seq.png">
   </p>   
   
   **OnErrorPropagate.xml**
   This sequence will be invoked on an error and will roll back the transaction.
   
   <p align="center">
     <img width="30%" src="../../../docs/assets/images/migration-mule/jms-message-rollback-and-redelivery-OnErrorPropagate-seq.png">
   </p>

6. Download [WSO2 MI](https://wso2.com/integration/micro-integrator/) and Apache [ActiveMQ](http://activemq.apache.org/getting-started.html). 
7. Copy the following client libraries from `<ACTIVEMQ_HOME>/lib` directory to `<MI_HOME>/lib` directory where 
`MI_HOME` and `ACTIVEMQ_HOME` is the home directories of MI and ActiveMQ distributions respectively.
   **ActiveMQ 5.8.0 and above**

   * activemq-broker-5.8.0.jar
   * activemq-client-5.8.0.jar
   * activemq-kahadb-store-5.8.0.jar
   * geronimo-jms_1.1_spec-1.1.1.jar
   * geronimo-j2ee-management_1.1_spec-1.0.1.jar
   * geronimo-jta_1.0.1B_spec-1.0.1.jar
   * hawtbuf-1.9.jar
   * Slf4j-api-1.6.6.jar
   * activeio-core-3.1.4.jar (available in the ACTIVEMQ_HOME/lib/optional directory)

   **Earlier version of ActiveMQ**

   * activemq-core-5.5.1.jar
   * geronimo-j2ee-management_1.0_spec-1.0.jar
   * geronimo-jms_1.1_spec-1.1.1.jar
   
9. Update `deployment.toml` file located at `<MI_HOME>/conf` with the relevant connection parameters. Add the following configurations to enable the JMS sender with ActiveMQ connection parameters.
    
   ```
   [[transport.jms.sender]]
   name = "myTopicSender"
   parameter.initial_naming_factory = "org.apache.activemq.jndi.ActiveMQInitialContextFactory"
   parameter.provider_url = "tcp://localhost:61616"
   parameter.connection_factory_name = "TopicConnectionFactory"
   parameter.connection_factory_type = "topic"
   parameter.cache_level = "producer"
  
   ``` 

10. Lets [export composite application](https://ei.docs.wso2.com/en/latest/micro-integrator/develop/exporting-artifacts/) in Micro Integrator. Right click on the **jms-message-rollback-redeliveryCompositeApplication** under the main **jms-message-rollback-and-redelivery** project and select **Export Composite Application Project**. Browse and provide carbonapps location of Micro Integrator(`[MI_HOME]/repository/deployment/server/carbonapps` where `MI_HOME` is the home directory of the distribution you downloaded). 

11. Open a terminal and navigate to the `MI_HOME/bin/` directory and execute the relevant command:
    * On MacOS/Linux/CentOS:<br/>
    ``sh micro-integrator.sh``
    * On Windows:<br/>
    ``micro-integrator.bat``

12. Now login to activeMQ admin console at *http://localhost:8161/admin/send.jsp* with the default username and password admin. Create a Queue named `in`. Enter a json message (Ex: `{"name":"John"}`) and click **Send**.
13. The transaction will fail with "Exception" the first four attempts, and then the message will be delivered correctly to the `topic1`.
14. Search for "topic1" in http://localhost:8161/admin/topics.jsp and notice that the number under the Messages Enqueued column has increased by 1. This verifies that the message has been delivered correctly and the transaction was successful.   
15. You can see a log entry in WSO2 server console similar to the following.

   **The number of JMS message delivery count and generated exception**
    
   ```
   [2020-05-04 16:11:23,921]  INFO {LogMediator} - DeliveryCount = 1
   [2020-05-04 16:11:23,974] ERROR {ScriptMediator} - The script engine returned an error executing the inlined nashornJs script function mediate javax.script.ScriptException: java.lang.Exception: This is the reason why the processing has failed in <eval> at line number 1 at column number 0
   at jdk.nashorn.api.scripting.NashornScriptEngine.throwAsScriptException(NashornScriptEngine.java:470)
   at jdk.nashorn.api.scripting.NashornScriptEngine.evalImpl(NashornScriptEngine.java:426)
   at jdk.nashorn.api.scripting.NashornScriptEngine.access$300(NashornScriptEngine.java:73)
   at jdk.nashorn.api.scripting.NashornScriptEngine$3.eval(NashornScriptEngine.java:514)
   at javax.script.CompiledScript.eval(CompiledScript.java:92)
   at org.apache.synapse.mediators.bsf.ScriptMediator.mediateForInlineScript(ScriptMediator.java:395)
   at org.apache.synapse.mediators.bsf.ScriptMediator.invokeScript(ScriptMediator.java:290)
   at org.apache.synapse.mediators.bsf.ScriptMediator.mediate(ScriptMediator.java:262)
   at org.apache.synapse.mediators.AbstractListMediator.mediate(AbstractListMediator.java:109)
   at org.apache.synapse.mediators.AbstractListMediator.mediate(AbstractListMediator.java:71)
   at org.apache.synapse.config.xml.AnonymousListMediator.mediate(AnonymousListMediator.java:37)
   at org.apache.synapse.mediators.filters.FilterMediator.mediate(FilterMediator.java:205)
   at org.apache.synapse.mediators.AbstractListMediator.mediate(AbstractListMediator.java:109)
   at org.apache.synapse.mediators.AbstractListMediator.mediate(AbstractListMediator.java:71)
   at org.apache.synapse.mediators.base.SequenceMediator.mediate(SequenceMediator.java:158)
   at org.apache.synapse.core.axis2.Axis2SynapseEnvironment.injectInbound(Axis2SynapseEnvironment.java:469)
   at org.wso2.carbon.inbound.endpoint.protocol.jms.JMSInjectHandler.invoke(JMSInjectHandler.java:239)
   at org.wso2.carbon.inbound.endpoint.protocol.jms.JMSPollingConsumer.poll(JMSPollingConsumer.java:293)
   at org.wso2.carbon.inbound.endpoint.protocol.jms.JMSPollingConsumer.execute(JMSPollingConsumer.java:203)
   at org.wso2.carbon.inbound.endpoint.protocol.jms.JMSTask.taskExecute(JMSTask.java:46)
   at org.wso2.carbon.inbound.endpoint.common.InboundTask.execute(InboundTask.java:43)
   at org.wso2.micro.integrator.mediation.ntask.NTaskAdapter.execute(NTaskAdapter.java:105)
   at org.wso2.micro.integrator.ntask.core.impl.TaskQuartzJobAdapter.execute(TaskQuartzJobAdapter.java:63)
   at org.quartz.core.JobRunShell.run(JobRunShell.java:213)
   at java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:511)
   at java.util.concurrent.FutureTask.run(FutureTask.java:266)
   at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
   at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
   at java.lang.Thread.run(Thread.java:748)
   Caused by: <eval>:1:0 java.lang.Exception: This is the reason why the processing has failed
   at jdk.nashorn.internal.runtime.ECMAException.create(ECMAException.java:113)
   at jdk.nashorn.internal.scripts.Script$\^eval\_.:program(<eval>:1)
   at jdk.nashorn.internal.runtime.ScriptFunctionData.invoke(ScriptFunctionData.java:637)
   at jdk.nashorn.internal.runtime.ScriptFunction.invoke(ScriptFunction.java:494)
   at jdk.nashorn.internal.runtime.ScriptRuntime.apply(ScriptRuntime.java:393)
   at jdk.nashorn.api.scripting.NashornScriptEngine.evalImpl(NashornScriptEngine.java:421)
   ... 27 more
   Caused by: java.lang.Exception: This is the reason why the processing has failed
   ... 32 more
   ```
   
   **Sends the message successfully to the topic**
   
   ```
   [2020-05-04 16:11:30,726]  WARN {Axis2SynapseEnvironment} - Executing fault handler due to exception encountered
   [2020-05-04 16:11:30,727]  INFO {LogMediator} - Rollback transaction = The message rolls back to its original state for reprocessing.
   [2020-05-04 16:11:32,701]  INFO {LogMediator} - DeliveryCount = 5
   [2020-05-04 16:11:32,702]  INFO {LogMediator} - No exception thrown = Transaction without error
   [2020-05-04 16:11:32,726]  INFO {TimeoutHandler} - This engine will expire all callbacks after GLOBAL_TIMEOUT: 120 seconds, irrespective of the timeout action, after the specified or optional timeout
   [2020-05-04 16:11:32,735]  INFO {JMSConnectionFactory} - JMS ConnectionFactory : jms:/topic1?transport.jms.ConnectionFactoryJNDIName=TopicConnectionFactory&java.naming.factory.initial=org.apache.activemq.jndi.ActiveMQInitialContextFactory&java.naming.provider.url=tcp://localhost:61616&transport.jms.DestinationType=topic initialized
   ```
   
<!-- INCLUDE_MD: ../../../docs/common/get-the-code.md -->

### Go Further 

Please refer following topics to learn more about JMS 

* [JMS Inbound](https://ei.docs.wso2.com/en/latest/micro-integrator/references/synapse-properties/inbound-endpoints/polling-inbound-endpoints/jms-inbound-endpoint-properties/).
* [Publish and Subscribe with JMS](https://ei.docs.wso2.com/en/latest/micro-integrator/use-cases/examples/jms_examples/publish-subscribe-with-jms/).
* [Connecting to ActiveMQ](https://ei.docs.wso2.com/en/latest/micro-integrator/setup/brokers/configure-with-ActiveMQ/).
* [JMS Transport Parameters](https://ei.docs.wso2.com/en/latest/micro-integrator/references/synapse-properties/transport-parameters/jms-transport-parameters/)
* [Setting up the Micro Integrator with ActiveMQ](https://ei.docs.wso2.com/en/latest/micro-integrator/setup/brokers/configure-with-ActiveMQ/#setting-up-the-micro-integrator-with-activemq).

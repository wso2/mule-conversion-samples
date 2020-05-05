# Login form using the HTTP connector Example

This example shows you how to use WSO2 EI server to build simple HTTP application with a login form. Here, we are using three HTML pages (login.html, loginSuccessful.html, and loginFailure.html) to illustrate the HTTP login application and those are stored in the Registry project.

### Assumptions ###

This document assumes that you are familiar with WSO2 EI and the [Integration Studio interface](https://ei.docs.wso2.com/en/latest/micro-integrator/overview/quick-start-guide/). To increase your familiarity with Integration Studio, consider completing one or more [WSO2 EI Tutorials](https://ei.docs.wso2.com/en/latest/micro-integrator/use-cases/integration-use-cases/).

### Example Use Case

In this example, a user submits a username and a password using the HTML login form provided by the application. 
Once user submit the username and password values, WSO2 EI server receives the credentials and respond the relevant HTML 
page with the authentication result.  

<p align="center">
  <img width="70%" src="../../../docs/assets/images/migration-mule/login-form-using-the-http-connector-use-case.png">
</p>

### Set Up and Run the Example

1. Start WSO2 Integration Studio ([Installing WSO2 Integration Studio](https://ei.docs.wso2.com/en/latest/micro-integrator/develop/installing-WSO2-Integration-Studio/)).
2. In your menu in Studio, click the **File** menu. In the File menu select the **Import...** item.
3. In the Import window select the **Existing WSO2 Projects into workspace** under **WSO2** folder.
4. Browse and select the file path to the downloaded sample of this Github project 
(`integration-studio-examples/migration/mule/login-form-using-the-http-connector`) and click **finish**.
5. Open **login-form-using-the-http-connector.xml** under **login-form-using-the-http-connector/LoginFormUsingHttpConnector/src/main/synapse-config/api** directory.
* **Get Login Page** (GET `/api/login`). This resource gets the index.html page from the Registry project and respond back.

    <img width="60%" src="../../../docs/assets/images/migration-mule/login-form-using-the-http-connector-get-login-page-flow.png">

* **Do Login** (POST `/api/login`). This resource checks whether the username and password are equal to `wso2` and 
respond the relevant HTML page based on the authentication status.

    <img width="70%" src="../../../docs/assets/images/migration-mule/login-form-using-the-http-connector-do-login-flow.png">

* **Requester Login** (GET `/api/requesterLogin`). This resource is responsible to fill in the correct credentials and 
call `Do Login` resource in order to make a successful login.

    <img width="70%" src="../../../docs/assets/images/migration-mule/login-form-using-the-http-connector-call-login-flow-using-requester.png">

6. Run the sample by right click on the **LoginFormUsingHttpConnectorCompositeApplication** under the main 
**login-form-using-the-http-connector** project and select **Export Project Artifacts and Run**.

**NOTE**<br/>
If you are using micro-integrator version below 1.2.0, add following configuration to `deployment.toml` located in 
`<MI_HOME>/conf`  where `MI_HOME` is the home directory of the distribution.

```
[[custom_message_formatters]]
content_type = "text/html"
class = "org.apache.axis2.transport.http.ApplicationXMLFormatter"
```

7. Open your browser and hit [http://localhost:8290/api/login](http://localhost:8290/api/login).
    <p align="center">
        <img width="40%" src="../../../docs/assets/images/migration-mule/login-form-using-the-http-connector-login-page.png">
    </p>
    
8. Enter `wso2` for username and `wso2` for password. Hit submit button.

9. You should receive this response: 
    <p align="center">
        <img width="40%" src="../../../docs/assets/images/migration-mule/login-form-using-the-http-connector-login-success.png">
    </p>

   Alternately you will receive the following:
   <p align="center">
        <img width="40%" src="../../../docs/assets/images/migration-mule/login-form-using-the-http-connector-login-error.png">
   </p>
    
10. Open HTTP Client in Integration Studio. Follow [HTTP Client Guidelines](../../../docs/common/adding-http-client-to-integration-studio.md)
to open HTTP Client if the window is not visible in the interface.
11. Make a GET request to *http://localhost:8290/api/requesterLogin*. You should see the successful message because 
correct credentials are set by the flow and the login is always successful.
<!-- INCLUDE_MD: ../../../docs/common/get-the-code.md -->

### Go Further

* Learn more about configuring an [REST API](https://ei.docs.wso2.com/en/latest/micro-integrator/references/synapse-properties/rest-api-properties/) in Studio.

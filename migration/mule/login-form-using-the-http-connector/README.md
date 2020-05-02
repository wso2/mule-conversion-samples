# Login form using the HTTP connector Example

This example shows you how to use WSO2 EI server to build simple HTTP application with a login form. Here, we are using three HTML pages (login.html, loginSuccessful.html and loginFailure.html) to illustrate the HTTP login application and those are stored in the Registry project.

### Assumptions ###

This document assumes that you are familiar with WSO2 EI and the 
[Integration Studio interface](https://ei.docs.wso2.com/en/latest/micro-integrator/overview/quick-start-guide/). To 
increase your familiarity with Integration Studio, consider completing one or more 
[WSO2 EI Tutorials](https://ei.docs.wso2.com/en/latest/micro-integrator/use-cases/integration-use-cases/).

### Example Use Case

In this example, a user submits a username and a password using the HTML login form provided by the application. Once user submit the username and password values, WSO2 EI server receives the credentials and respond the relevant HTML page with the authentication result.  
* **Get Login Page** (GET `/api/login`). This resource get the index.html page from the Registry project and respond back.

    ![Login page](../resources/images/login-form-using-the-http-connector/GetLoginPageFlow.png?raw=true "Login page")

* **Do Login** (POST `/api/login`). This resource checks the username and password is equals to the `wso2` and respond the relevant HTML page based on the authentication status.

    ![Login submit](../resources/images/login-form-using-the-http-connector/DoLoginFlow.png?raw=true "Login submit")

* **Requester Login** (GET `/api/requesterLogin`). TThis resource is responsible for filling in the correct credentials and calling Do Login resource in order to make a successful login.

    ![Login requester](../resources/images/login-form-using-the-http-connector/CallLoginFlowUsingRequester.png?raw=true "Login requester")

### Set Up and Run the Example

1. Start WSO2 Integration Studio ([Installing WSO2 Integration Studio](https://ei.docs.wso2.com/en/latest/micro-integrator/develop/installing-WSO2-Integration-Studio/)).
2. In your menu in Studio, click the **File** menu. In the File menu select the **Import...** item.
3. In the Import window select the **Existing WSO2 Projects into workspace** under **WSO2** folder.
4. Browse and select the file path to the downloaded sample of this Github project 
(`integration-studio-examples/migration/mule/login-form-using-the-http-connector`) and click **finish**.

5. Run the sample by right click on the **LoginFormUsingHttpConnectorCompositeApplication** under the main 
**login-form-using-the-http-connector** project and selecting **Export Project Artifacts and Run**.
6. Open your browser and hit [http://localhost:8290/api/login](http://localhost:8290/api/login).

    ![Login Page](../resources/images/login-form-using-the-http-connector/login_page.png?raw=true "Login Page")

7. Enter `wso2` for username and `wso2` for password. Hit submit button.
8. You should receive this response: 

    ![Login Success](../resources/images/login-form-using-the-http-connector/login_success.png?raw=true "Login Success")
    
    Otherwise:
    
    ![Login Failure](../resources/images/login-form-using-the-http-connector/login_error.png?raw=true "Login Failure")

9. Open HTTP Client in Integration Studio. Follow [HTTP Client Guidelines](../../../docs/common/adding-http-client-to-integration-studio.md)
to open HTTP Client if the window is not visible in the interface.
10. Make a GET request to `http://localhost:8290/api/requesterLogin`, you should see the successful message as the correct credentials are set by the flow and the login is always successful.


### Go Further

* Learn more about configuring an [REST API](https://ei.docs.wso2.com/en/latest/micro-integrator/references/synapse-properties/rest-api-properties/) in Studio.

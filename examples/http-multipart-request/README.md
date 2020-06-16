# http-multipart-request

This example application illustrates the concept of handling multipart request. It also involves reading and writing a file using [File connector](https://store.wso2.com/store/assets/esbconnector/details/5d6de1a4-1fa7-434e-863f-95c8533d3df2) and use it in a text/html resource.

### Assumptions ###

This document assumes that you are familiar with WSO2 EI and the [Integration Studio interface](https://ei.docs.wso2.com/en/latest/micro-integrator/develop/WSO2-Integration-Studio/), WSO2 EI’s graphical developer tool. To increase your familiarity with Integration Studio, consider completing one or more [WSO2 EI Tutorials](https://ei.docs.wso2.com/en/latest/micro-integrator/use-cases/integration-use-cases/).

### Example Use Case
In this example we use two resources : <br>
 * to output a html page which provides a form to upload any file
 * to handle the uploaded file(Multipart request) and write the content of the file to a preferred location by the same file name.

<p align="center">
  <img width="70%" src="../../../docs/assets/images/migration-mule/http-multipart-request-use-case.png"/>
</p>

### Set Up and Run the Example

1. Start WSO2 Integration Studio ([Installing WSO2 Integration Studio](https://ei.docs.wso2.com/en/latest/micro-integrator/develop/installing-WSO2-Integration-Studio/)).

2. In your menu in Studio, click the **File** menu. In the File menu select the **Import...** item.

3. In the Import window select the **Existing WSO2 Projects into workspace** under **WSO2** folder.

4. Browse and select the file path to the downloaded sample of this Github project
(``integration-studio-examples/migration/mule/http-multipart-request``) and click **Finish**.

5. Lets add the file connector into the workspace. Right click on the **HTTPMultipartRequest** and select 
**Add or Remove Connector**. Keep the **Add connector** option selected and click **Next>**. Search for 'file' using the 
search bar and click the download button located at the bottom right corner of the file connector. Click **Finish**.

6. Open the **HTTPMultiPartRequestAPI.xml** under 
**http-multipart-request/HTTPMultiPartRequest/src/main/synapse-config/api/** directory. 
<p align="center">
  <img width="60%" src="../../../docs/assets/images/migration-mule/http-multipart-request.png"/>
</p>

7. Copy the uploadFile.html file located in **http-multipart-request/HTTPMultiPartRequest/src/main/resources** directory 
to a location of your choice.

8. Replace the {htmllocation} placeholder with the copied path to uploadFile.html and {destinationDir} property value 
with the destination directory.
ex:- /Users/user/Desktop/destination/. `The destinationDir directory path should followed by a trailing / character`

10. Run the sample by right click on the **HTTPMultipartRequestCompositeApplication** under the main 
**HTTPMultiPartRequest** project and selecting **Export Project Artifacts and Run**.

11. Open any web browser, paste the following URL in the address bar, and press Enter:<br>
*http://localhost:8290/uploadFile*

12. In the html page click on Choose File button and upload any file. Then click submit.

13. Verify that the file that was uploaded to the html form is in the defined destination(destinationDir) Directory. 
And in the console log you can see the below log.

```
INFO {org.apache.synapse.mediators.builtin.LogMediator} - File Content-Type = text/xml
```
<!-- INCLUDE_MD: ../../../docs/common/get-the-code.md -->

### Go Further

* Learn more about [file connector](https://docs.wso2.com/display/ESBCONNECTORS/Working+with+the+File+Connector#WorkingwiththeFileConnector-append).
* Read more on [WSO2 connectors](https://docs.wso2.com/display/ESBCONNECTORS/WSO2+ESB+Connectors+Documentation)
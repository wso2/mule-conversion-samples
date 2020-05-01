# Foreach Processing and Choice Routing #

This example explains how to perform content based routing along with processing per each item in the payload. 

### Content Based Routing ###
The Content-Based Router reads the content of a message and routes it to a specific recipient based on its content. It can evaluate request url, request payload or a part of it and route it to a specific path. 

### Foreach Processing ###
This concept can be achived using the WSO2 Foreach Mediator. It requires an XPath expression and a sequence (inline or referred). It splits the message into a number of different messages derived from the original message by finding matching elements for the XPath expression specified. Based on the matching elements, new messages are created for each iteration and processed sequentially. The processing is carried out based on a specified sequence. The behaviour of ForEach mediator is similar to a generic loop. After mediation, the sub-messages are merged back to their original parent element in the original message sequentially.

### Assumptions ###

This document describes the details of the example within the context of WSO2 Integration Studio, WSO2 EI’s graphical developer tool. This document assumes that you are familiar with WSO2 EI and the [Integration Studio interface](https://ei.docs.wso2.com/en/latest/micro-integrator/develop/WSO2-Integration-Studio/). To increase your familiarity with Integration Studio, consider completing one or more [WSO2 EI Tutorials](https://ei.docs.wso2.com/en/latest/micro-integrator/use-cases/integration-use-cases/).

### Example Use Case ###

In this example, application exposes a service that processes end user requests and provide information about possibility of loan from different banks. Once the user submits the request with the customer name, amount of the loan, term of the loan, and customer's social security number (SSN) as query parameters, the application processes the request and responds the end user. The response identifies the banks which can provide a loan for customer with approved amount and term with calculated payment per month.

As an example, assume the user provides the following details. 
  customer name: John
  SSN: 1234
  Loan amount: 2500000
  term: 30

It provides the user a set of banks which can fullfill the request along with the monthly installement amount calculated that user needs to pay to the bank. 

### Set Up and Run the Example ###

Follow the steps in this procedure to create and run this example in your own instance of Integration Studio. 

1. Start WSO2 Integration Studio ([Installing WSO2 Integration Studio](https://ei.docs.wso2.com/en/latest/micro-integrator/develop/installing-WSO2-Integration-Studio/)).
2. In your menu in Studio, click the **File** menu. In the File menu select the **Import...** item.
3. In the Import window select the **Existing WSO2 Projects into workspace** under **WSO2** folder.
4. Browse and select the file path to the downloaded sample of this github project ("foreach-processing-and-choice-routing" folder of the 
downloaded github repository).
5. Open the **Loan.xml** under **foreach-processing-and-choice-routing/foreachProcessingandchoicerouting/src/main/synapse-config/api** 
directory. 
6. The **Loan.xml** is the graphical view of the foreach-processing-and-choice-routing example.
7. Run the sample by right click on the **foreachProcessingandchoiceroutingCompositeApplication** under the main **foreach-processing-and-choice-routing** 
project and selecting **Export Project Artifacts and Run**.
8. Open any web browser, paste the following URL in the address bar, and press Enter. 
```
	http://localhost:8290/loan/calculate?term=30&ssn=1234&name=John&amount=2500000
```
**Result:** You will be provided a message as below. 
```
	Bank info for customer John about loan 2500000 EUR in term 30 months: Bank 1: Payment 97500.0 EUR per month , Bank 3: Payment 93333.33 EUR per month
```

### How It Works
1. First, the application checks if user has sent all the details through query parameters. If not, user is prompted that the request is incomplete. 
2. Then it has an API published through swagger which will get published when the applicatio is deployed in the Micro Integrator. It provides the possible banks that the request can be fullfilled. 
3. Then, per each bank the monthly installement amount is calculated and provided to the user. 

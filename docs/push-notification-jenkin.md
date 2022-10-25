Push Notification Jenkins Guide
======================

### Background  
This guide will give you step by step instructions on how to gain access to the Jenkin's Push Notification job. Access  
is required if you wish to run any script that uses the `JenksinMessagingRequest` class.

### A. Raising a Service Now request  
1. Request Link: https://hsbcitid.service-now.com/servicenow?id=sc_cat_item&sys_id=7bb841061b59c414dce3fc49cd4bcb0e
2. Select `New access` from `What would you like to request`
3. Ensure `DigitalCiCdEng` is selected for `System Categories`
4. Enter `InfoDir-jenkins-vadm-users` into `Entitlements`

After the request has been approved, you will have access to:
[Send Push Notification Jenkins Job](https://jenkins-custom-vadm01.digital-tools.euw1.prod.aws.cloud.hsbc/job/VAM/job/UK/job/microint/job/ExternalTools/job/jobs/job/SendPushNotification/)  
  
  
### B. Creating a Jenkins User Token
To be able to use the `JenksinMessagingRequest` class, you must provide your Jenkins account's username and token.  
Refer to this Jenkin blog post for details on account tokens: [blog post](https://www.jenkins.io/blog/2018/07/02/new-api-token-system/)  

### C. Setting Your Environment Variables
`JenksinMessagingRequest` will look for the following two env. vars.: `automationJenkinsUsername` and `automationJenkinsToken`
Add them to your IntelliJ run scripts by adding the following cli arguments
```shell
-DautomationJenkinsUsername=your.hsbc.email
-DautomationJenkinsToken=your.jenkins.token
```


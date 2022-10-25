Sample App Implementation
======================

*   [Background](#Background)
*   [Login Methods for Sample App](#Login-Methods-for-Sample-App)
*   [Sample App Feature Files and Scenarios](#Sample-App-Feature-Files-and-Scenarios)
*   [Sample App Entities and Tagging](#Sample-App-Entities-and-Tagging)

### Background
Sample app integration is a necessary step for developers to create features and ensure their functionality before 
code is merged into the mobilex-global app.  As such, sample app automation has become a staple step of automation.
However, automation of the sample app can become fragmented.  This document serves to assist in maintaining
some consistency and consolidation in sample app automation.

# Login Methods for Sample App
As with all page objects, each page object should represent only a single screen on the app.  This means that methods 
which assist in sample app login should be implemented in a single  "sampleAppLogin" style page object, not in the
page object(s) for the feature being inmplemented via the sample app.   This reduces the amount of fragmentation in our 
codebase and also allows for easier deprecation and removal of sample app code, once the feature has been implemented in
the mobilex-global app.

# Sample App Feature Files and Scenarios
Scenarios and Feature files which require sample app usage should be segregated from other Feature files in their own 
subfolder under src/resources/sampleApp, and tagged with @sampleapp.
  
e.g src/resources/sampleApp/AboutMe/AboutMe.feature

After the feature has been fully implemented into mobilex-global, these tags can be removed, and the features can be 
moved out of the sampleApp subfolder and into appropriate subfolders within src/resources

e.g src/resources/AboutMe/AboutMe.feature

# Sample App Entities and Tagging
As Sample App functionality appears to differ per entity/platform, it is ideal to tag Features and Scenarios which rely 
on Sample App usage with their relevant entity.  

Sample app entity tags shall be separated by a single underscore

e.g `@sampleApp_Canada`, `@sampleApp_UK`, `@sampleApp_HK`, and so on


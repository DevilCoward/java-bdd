Page Object Guide
======================

*   [Background](#Background)
*   [Folder Structure](#Folder-Structure)
    *   [Contract Interface](#Contract-Interface)
    *   [Common](#Common)
    *   [Platform](#Platform)
    *   [Entity Specifics](#Entity-Specifics)
*   [Binding](#Binding)
    

### Background
For readability and maintenance best practices for page object creation are provided here.
The abstraction approach outlined here allows for less redundant code while enabling platform or
entity differences to be handled.

### Folder Structure
- For the example below, the following file structure is used:
```
com/hsbc/digital/testautomation/global/mobilextestautomation/
/ contracts 
    / example
        - Example.java
/ screens
   / android
        / example
            - ExampleScreenAndroid.java
            - ExampleScreenAndroid_entity.java
   / common
        / example
            - ExampleScreen.java
   / ios
        / example
            - ExampleScreenIos.java
```

#### Contract Interface
- Create an Interface class in the contracts folder that indicates which methods are available in the page object
- Interfaces must extend the `VisibleInterface`, which provides the default `waitVisible()` and `isVisible()` methods
- Each new screen, pop-up, modal, or half-modal should be represented by its' own Interface and corresponding page object.  For example if the user clicks on a button on a screen, and doing so creates a single pop-up, even with only one button element on it -- then the pop-up should have it's own page object to handle it
- All Interface classes should have a comment in code with a link to a Confluence page or JIRA ticket with attached screenshots of the new screen
- e.g. Example.java above would look like:
```java
public interface Example extends VisibleInterface {
    // Example Screen Screenshots : http://jira.com/JIRA-123
    void clickContinue();

    void clickBack();
}
```

#### Common
- Create a page object in the 'common' screens folder
    - This PO implements the contract Interface and extends `BaseScreen` from `com.hsbc.digital.testautomation.global.mobilextestautomation.screens`
    - This PO shall be declared abstract, as it should not be used directly
- Declare the element selectors here as protected
    - Using a `By` as the variable type is preferred, however it is not required
    - `By`s allow for the locator type to differ between Platform while maintaining the same code in the common class
    - Values shall not be assigned to the selectors in the common class unless they are the same betwen platforms
- Implement methods from the interface here that are functionally the same between platforms
- If there are methods that differ between platforms 
    - Add the method as an abstract method to the common class
    - Implement the methods that differ in the Platform class (e.g. here `clickBack()` from the example interface is defined in the Platform class below)
- If methods are the same between platforms
    - Implement all methods in the common PO
- Example below
 ```java
public abstract class ExampleScreen extends BaseScreen implements Example {

    protected AppiumDriver appiumDriver;
    protected AppiumHelpers appiumHelpers;

    // Element Selectors Example
    protected By continueButtonSelector;
    protected By uniquePageLocator;

    public ExampleScreen(AppiumDriver appiumDriver, AppiumHelpers appiumHelpers) {
        super(appiumDriver);
        this.appiumDriver = appiumDriver;
        this.appiumHelpers = appiumHelpers;
    }

    @Override
    public void clickContinue() {
        appiumHelpers.waitForElement(continueButtonSelector).click();
    }
    
    @Override
    public abstract void clickBack();

}
```
#### Platform
- In the platform specific folder (Android, iOS) create a Platform Specific PO
- The Platform PO extends the common page object and implements Provider<T>
    - Provider type `T` shall be the Platform screen class (see below)
- Set platform specific identifiers in the constructor
- If a method has behaviour that is different between platforms (e.g. Android needs to scroll, iOS does not) implement it here
- Implement the provider method `get`
    - For the most simple version, simply `return this`
    - For instances where the page may differ between entity, the provider shall be used to return the entity specific page
    - A helper method in the base screen `getEntityVersion` can be used to dynamically retrieve entity pages if they follow a naming scheme
- Example Below
```java
public class ExampleScreenAndroid extends ExampleScreen implements Provider<ExampleScreenAndroid> {
    
    @Inject
    public ExampleScreenAndroid(AppiumDriver appiumDriver, AppiumHelpers appiumHelpers) {
        super(appiumDriver, appiumHelpers);
        // Set locators here
        uniquePageLocator = By.id("androidIDForPage");  // This locator is used by waitVisible and isVisible
        continueButtonSelector = By.id("androidIDForButton");
    }

    @Override
    public void clickBack() {
        // Platform specific behaviour here
    }

    @Override
    public ExampleScreenAndroid get() {
        return this;
    }
}
```

#### Entity Specifics
- In the event that entity behaviour for a page is different, an Entity specific PO can be created
- In the same folder as the Platform Page Object create an Entity specific page object
- In the Entity specific Page Object you shall:
    - Set any entity specific locators in the constructor   
    - Override any methods that have entity specific behaviour
- Example Below
```java
public class ExampleScreenAndroid_entity extends ExampleScreenAndroid {
    
    @Inject
    public ExampleScreenAndroid_canada(AppiumDriver appiumDriver, AppiumHelpers appiumHelpers) {
        super(appiumDriver, appiumHelpers);
        uniquePageLocator = By.linkText("Entity Specific Locator Here");
    }
}
```
- If an entity specific PO exists then the platform PO acts as the Provider for this and the platform example above becomes:
```java
public class ExampleScreenAndroid extends ExampleScreen implements Provider<ExampleScreenAndroid> {
    
    @Inject
    public ExampleScreenAndroid(AppiumDriver appiumDriver, AppiumHelpers appiumHelpers) {
        super(appiumDriver, appiumHelpers);
        // Set locators here
        uniquePageLocator = By.id("androidIDForPage");
        continueButtonSelector = By.id("androidIDForButton");
    }

    @Override
    public void clickBack() {
        // Platform specific behaviour here
    }

    @Override
    public ExampleScreenAndroid get() {
        if (System.getProperty("country").equalsIgnoreCase("<entity>")) {
            return new ExampleScreenAndroid_entity(appiumDriver, appiumHelper);
        }
        return this;
    
       // OR
       return (ExampleScreenAndroid)getEntityVersion(this);  // if using naming convention to retrieve entity pages

    }
}
```

### Binding
- To bind the page object to the interface use a `.toProvider` call in the Abstract modules
- e.g. For android
```java
public class AbstractAndroidModule extends AbstractModule {
...
    bind(Example.class).toProvider(ExampleScreenAndroid.class);
...
}
```
- `toProvider` instead of `to` allows the concrete class to be provided by the `get` method of the page object

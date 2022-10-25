Config and User data
======================

*   [Background](#Background)
*   [Difference between Configuration and User data](#Difference-between-Configuration-and-User-data)
*   [Config Details and Usage](#Config-Details-and-Usage)
    *   [Where is configuration data stored](#Where-is-configuration-data-stored)
    *   [How to reference configuration data in tests](#How-to-reference-configuration-data-in-tests)
*   [User Details and Usage](#User-Details-and-Usage)
    *   [Where is user data stored?](#Where-is-user-data-stored)
    *   [How to reference user data in the test](#How-to-reference-user-data-in-the-test)

### Background
User type was initially created as a way to separate configuration data from user data to provide clarity on how the data is used.

Original spike on updating the configuration files that lead to the separation of config and user data.  
[Config Refactor Spike](https://alm-github.systems.uk.hsbc/mobile/mobilex-global-tests/pull/1138)


Difference between Configuration and User data
---------------------------------------------------
### Configuration
Configuration data refers to data used for test set up. These include:
  
| Config data type | Example  |
| :---   | :- |
| Environment | profile = fp8 |
| Application | ios.app.path = `%s/bundle-to-test/ios/Australia-shielded.ipa` |
| Test | invalid-username = notARealusername, dsk-password = 112233 |
| API | ecare-url = `hkl20049360.p2g.netd2.hk.hsbc/1/2` |


### User
User data refers to the data specific to a user. Data may include:
* username
* password
* current-account-number

Basically, any data that belongs to that specific user (that is available to us). 


### Should this data be User data or Config data?
Consider the following when deciding between User data and Config data.  

If the data can be successfully used by any user, that's most likely Config data.  
If the data is only valid for a specific user, that's most likely User data.  
Example:
  
| Data | Type  | Reason  |
| :---   | :-: | :-: |
| payee = "BC Hydro" | Config | Payees is a list of companies any users can select |
| saving-account-number = "123-456-789" | User | Account number is specific to 1 user |

Another way of looking at it, eventually we may want to pull user data from an API to support parallel testing. Would you expect to be able to pull that data for a specific user from a user API.

  
Config Details and Usage 
------------------------  
### Where is configuration data stored  
Configuration data files are located in the `src/resources/config` directory.  
The data files are separated by country then further by environment: `<country>/<env>_env.conf`.  
Example: `src/resources/config/canada/cert_env.conf`. 

### How to reference configuration data in tests
Configuration data can be used in any class in the framework using these steps:

#### 1. Config type injections
```java
import com.google.inject.Inject;

public class ClassA {
    Config config;
    
    // Inject Config type to ClassA to pull configuration data
    @Inject
    public ClassA (Config config) {
        this.config = config;
    }
}
```
Config type is injected into the class constructor via [Guice](https://github.com/google/guice) dependency library.  
From there, simply assign the class member property value.


#### 2. Retrieving the data through config
Depending on the type of data being pulled from the configuration files, you would exercise a different call method.
Details can be found here: [Config GitHub](https://github.com/lightbend/config)

The following example illustrates pulling the `String` value for currency.
```java
public class ClassA {
    Config config;

    @Inject
    public ClassA (Config config) {
        this.config = config;
    }
    
    // Get currency value from configuration file
    public String getCurrencyFromConfig() {
        return config.getString("currency");
    }
}
```

User Details and Usage
-----------------------
### Where is user data stored
With the help of Guice Provider and the `UsersProvider` class, we have the ability to pull user data from a local  
`.conf` files or directly from an API (such as Ladle).  


#### Local User Data files
User data is stored as objects in `*users.conf` files. `*users.conf` are located in the `src/resources/users` directory and are separated by country then further by environment: `<country>/<env>_users.conf` 
Example: `src/resources/users/canada/cert_users.conf` . 

```
default {                                   // user type is the user's object key
    username = CA123455678                  // key name = value
    password = 12345
}

psk {
    username = CA3494095
    password = 39586
    memorable-answer = testtesttest
}
```
The above is an example of how user data objects are stored in the .conf files.
For more information on `.conf` files, refer to [Config GitHub](https://github.com/lightbend/config)

#### User data via API
With the `UsersProvider` class, entities are free to add their own implementation to pull data from a service (such as Ladle). An example of this would be: `TempUsersFactory` and `ComposedUsers`

**TempUsersFactory** - Provides the methods to pull a user by type from Ladle service.  
**ComposedUsers** - Provides a solution to enable entities to set up a primary and an alternative user data source. For example, via Config and Canada Ladle.  

### How to reference user data in the test
Regardless of where the user data is stored, you will be able to pull the user's data using the same `Users` API with the following steps:

#### 1. Users type injections
Set up is similar to `Config` where `Users` is injected into the class constructor via [Guice](https://github.com/google/guice) dependency library. 
From there, simply assign the class member property value.
```Java
import com.google.inject.Inject;

public class ClassBSteps {
    Users users;
    
    // Inject Users type to ClassBSteps to pull users data
    @Inject
    public ClassBSteps (Users users) {
        this.users = users;
    }
}

``` 

#### 2. Retrieving the user's data through Users API
The Users interface is designed to return a User object that has various helper methods to extract user data.  
Depending on the data required, you're approach to data retrieval may vary.

Examples below:  
##### A. Chaining methods to pull a single piece of data
```Java
import com.google.inject.Inject;

public class ClassBSteps {
    Users users;
    
    // Inject Users type to ClassB to pull configuration data
    @Inject
    public ClassBSteps (Users users) {
        this.users = users;
    
        // Get username by chaining getUser() and getUsername() methods
        When("^The ([a-zA-Z0-9]+) user enters username$", (String userType) -> {
            String username = users.getUser(userType).getUsername();              
            // send keys code here
        });
    }
}
``` 

##### B. Creating a User object to extract data from
```
    ... same set up code as above

        // Get user's object then extract the necessary data from it
        Given("^The ([a-zA-Z0-9]+) user details are available$", (String userType) -> {
            User theUser = users.getUser(userType);

            String username = theUser.getUsername();  // helper methods are available for username and password
            String password = theUser.getPassword();
            String currentAccount = theUser.getData("current-account-number"); // other data access via getData()
        });
}
``` 

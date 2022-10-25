# Global Mobile-X Test Automation Framework Setup

This document is currently a work in progress. 

If you find that any aspects are inaccurate or you have found a way to fix a problem that is not highlighted please feel free to either tell us these issues so that this document can be amended by yourself or the UK-based Automation team.

## Background
After evaluating multiple open-source mobile UI testing tools, Appium has been selected to be the tool to perform automated testing as it has the most community support and simplest API to work with. 

It is worth noting that initially HSBC began creating a Calabash framework to work with. However, due to the large differences in the API between iOS/Android and the announcement that development of the Calabash framework would end after iOS 11, Appium became the best option.

To learn more about Appium visit: https://appium.io

## Table of Contents  
* [Managed Mac Users Installation](#managed-mac-users-installation) 
* [How to build the App?](#build-an-app)
  * [Android App](#android-app)
  * [iOS App](#ios-app)
* [How to Run Tests?](#running-tests)
  * [Cloning The Repo](#clone-repo)
  * [Setup Test Runner](#setup-test-runner)
  * [Android](#android-runs)
    * [On Physical Device](#physical-device-setup)
    * [On Simulator](#simulator-device-setup)
  * [iOS](#ios-runs)
      * [Simulator and Real Device](#simulator-and-real-device)
* [Interacting With Mobile Elements](#interacting-with-mobile-elements)
    * [Arc](#arc)
    * [Inspector](#inspector)
* [Localization](#localization)
* [UnManaged Mac Users Installation](#unmanaged-mac-users-installation) 
* [Coding Style Guide](#coding-style-guide)

## Managed Mac Users Installation

Note: These steps will detail you of the steps to be performed on the New Managed Mac

The Mobile cli tools provide the easy and fast installation of the following
*cntlm
*gradle
*npm
*HSBC Certificates

### Requirement
1. Java

For new Managed Mac, raise a Service now for Java RunTime Environment 8. Once the request is approved, open Self Service and run Force Update Inventory.

2. Xcode

- Raise a Service Now to request Xcode 12.4. Once the request is approved, download the Xcode 12.4 from Self-Service.
- After the Xcode installation, download the CLI tools from Self-Service "Xcode CLI Tools 12.4"
- Once the CLI tools installed, run the "XCode 12.4 Post Install"from the Self service. This will select the Command Line Tools in XCode->Preferences-Locations
- Install simulators by adding in Xcode -> Preferences -> Components

3. Python3

For Managed Mac Python 3.7 is installed by Xcode.
check with ```python3 --version```

## Way 1: Setup test environment automatically by below scripts
Please refer to this confluence page for details: [How to setup test environment](https://wpb-confluence.systems.uk.hsbc/display/AMT/How+to+setup+development+and+test+environment+on+Managed+Mac)
or run the below scripts directly if you finished first step of this confluence page.

### Run below script to set up test environment:
```
cd scripts/setup-env && ./auto-setup-environment.sh
```

### Restart IDEA and open your project to wait all the dependencies until they download is complete.


### (Optional) Refresh your test environment by below script if your HSBC AD password is changed:
```
cd scripts/setup-env && ./please-run-me-after-changed-password.sh
```

## Way 2: Setup test environment manually
### Install the Mobile CLI tools
Please refer to this for installation process as well https://alm-github.systems.uk.hsbc/mobile/mobile-cli

DECEMBER 2021 UPDATE : You must follow the steps in the above link (https://alm-github.systems.uk.hsbc/mobile/mobile-cli) as the manual installation step below to install the mobile-cli tools WILL NO LONGER WORK

```
bash -c 'read -p "Username: " USERNAME ; read -srp "Password: " PASSWORD ; echo ; export USERNAME PASSWORD ; exec sh <(curl -L -u "$USERNAME:$PASSWORD" \ -H "Accept: application/vnd.github.v3.raw" \ https://alm-github.systems.uk.hsbc/api/v3/repos/mobile/mobile-cli/contents/runme.sh)'
```

check
```
mobile -h
```

Run the below commands one by one.

!DO NOT RUN "mobile bootstrap all".This might not work.

```
mobile bootstrap cntlm
```
```
mobile bootstrap maven
```
```
mobile bootstrap pip
```
```
mobile bootstrap ca
```
```
mobile bootstrap gradle
```
```
mobile bootstrap appium
```


### Mobile CLI CI tools
```
mobile ci-script --install
```

After installing all of the above your bash_profile (```cat ~/.bash_profile```) should look something like this

```
# The original version is saved in .bash_profile.pysave
#PATH="/Library/Frameworks/Python.framework/Versions/3.6/bin:${PATH}"
#export PATH

for file in ~/.mobile_bash_profile_scripts/*; do source $file; done

. ~/.bash_profile_ci_script
```
#### References:
* https://digital-confluence.systems.uk.hsbc/confluence/display/INFRA/Mobile+CLI+tools
* https://alm-github.systems.uk.hsbc/mobile/mobile-cli
* https://alm-github.systems.uk.hsbc/mobile/mobile-cli/tree/master/mobile-bootstrap 
 
## Install npm, nvm, and ios-deploy (You must run this even if you use the automatic setup)
Run this to add Jenkins Credentials
```
mobile bootstrap npm
```
then run this
```
git clone https://github.com/creationix/nvm.git ~/.nvm
( cd ~/.nvm && ./install.sh )
```

### ios-deploy is required to run the tests on devices
Close and re-open your terminal window, then
```
export NVM_NODEJS_ORG_MIRROR=https://fileserver-mobile.systems.uk.hsbc/node-js/dist/
nvm install 6.10.3
npm install -g ios-deploy
```

## Program Setup
### IntelliJ IDEA
* Ensure your JAVA_HOME environment variable is exported in ~/.bash_profile
```
export JAVA_HOME=$(/usr/libexec/java_home -v 1.8)
```
* Set proxy to autoconfigure in IntelliJ preferences
* Confirm the correct proxy for your location is being used
* Set project JDK to 1.8 in IntelliJ > Preferences > Build > Compiler > Java Compiler

### Android Studio
* If sdk tools are not installed along with Android Studio, download the Command Line Tools from https://developer.android.com/studio
  * In order to download the SDK Tools for Android Studio you must be part of the Digital Developer Proxy Group
  * Copy To: /Users/$USER/Library/Android/sdk
  * Edit ~/.bash_profile to export paths for sdk
  ```
  export ANDROID_HOME=/Users/$USER/Library/Android/sdk
  export PATH=${PATH}:$ANDROID_HOME/tools/bin:$ANDROID_HOME/platform-tools 
  ```
* Ensure proxy is set per IntelliJ Instructions


### Appium

**Via ServiceNow:** [https://hsbcitid.service-now.com/servicenow?id=search&q=MAC00144](https://hsbcitid.service-now.com/servicenow?id=search&q=MAC00144)  

**Manual Install:** Download the appium mac installer [here.](https://github.com/appium/appium-desktop/releases/latest) (If the dmg download is blocked, try the *mac.zip file.)
* Run the installer, but move it into /User/YOUR USERNAME/Applications rather than the system applications folder. This way you don't need root permissions.
**NB:** Necessary gems (particularly nokogiri as this tends to have c-lang/gcc issues. Typically 'xcode-select-install' resolves the issue).

### Run appium from command line
```
$(ci-script appium/appium) -p 4726
```
Android - 4725
iOS - 4726

## Build an App
### Android App
* Pull or clone the latest Android repo/version:
```
git clone https://alm-github.systems.uk.hsbc/mobile/mobilebanking-global-android
```
* In Android Studio open the 'Android' subfolder of the cloned repo.
* If not already syncing when opened, select File>Sync Project With Gradle Files
* Go to the ‘Gradle’ tab on the right and find and double click the build job you want to initiate in under jobs - e.g. assembleCertAllCountryDebug.
* If this build fails, look into the stack trace to see the build tasks that did not build properly. You must then build them individually. This can be done by going into your mobilex-global-android/Android/ folder and entering the following command:
    ```
    ./gradlew [TASK NAME HERE]
    ```
* If step failing is something along the lines of "app:transformClassesWithInstantRun...", disable Instant Run
    ```
    Android Studio > Preferences > Build, ... > Instant Run
    ```
* If the migration wrapper is failing, do:
    ```
    ./gradlew wrapper
    ```
* If the adb exec installation fails - enter the following commands:
    ```
    adb uninstall io.appium.settings
    adb uninstall io.appium.unlock
    ```
* You can find the apk build path for the Android App in mobilex-global-android/app/build/outputs/apk.
* Put this build path in the ‘android.app.path’ in your application.conf file.

### iOS App
* Pull or clone the latest iOS repo/version:
    ```
    git clone https://alm-github.systems.uk.hsbc/mobile/mobilebanking-global-ios
    cd mobilebanking-global-ios/
    ```
* Please make sure you have the ci-script installed on your machine by executing the following from the root of repo 
    ```
    mobile ci-script --install
    ``` 
* Close and reopen terminal window
* Install Dependencies from the root of repo
    ```
    ./iOS/BuildScripts/fetch-deps.sh
    ```
* If any dependencies failed, they can be installed manually
    ```
    ~/infra/ci-script-library/dependencies/0.6.0/install
    ```
* Open Xcode and open the project from the iOS folder ```MobileXGlobal.xcworkspace``` in the project folder.
* Make sure you have the debug and production signing provisions and import them.
* Press the play button when you have selected MobileXGlobal and the device or simulator you wish to build on.
* You can find the .app (simulator) or .ipa (device) build path for the iOS App on the right hand side in Xcode after it has been built.
* Put this build path in the ‘ios.app.path’ in your application.conf file.

### How to modify iOS all country build into entity-specific build
The process for creating an entity-specific build on iOS currently is to build the all country build and strip away all of the other the entities. We are able to do this ourselves providing we have the all country build locally and know which entity we want. The following example is for Canada:
1. Run ```./ci/prepare-for-entity.bash -e <entity_code> <file_path_to_app> <env>``` within the mobilex-global tests repository. ex: entity codes - CA,BH
An array of environments can be passed into the command
2. The all country build will now be modified to an entity-specific build

## Running Tests
### Clone Repo
* Pull or clone the latest mobilex-global-tests repo/version:
```
git clone https://alm-github.systems.uk.hsbc/mobile/mobilex-global-tests
```

#### Git Submodule
##### How to retrieve helper classes from submodule
The Global & UK Team use helper classes from the helpers repo ( https://alm-github.systems.uk.hsbc/mobile/mobilex-helpers-test ). This requires the engineer to retrieve the helpers as a submodule. When the repo has just been cloned, the following command must be ran to initialise the submodule within the project.
```
git submodule update --init --recursive
```
To retrieve the latest changes from the submodule the following command can be ran
```
git submodule update --recursive --remote
```
Alternatively the engineer can navigate to the folder where the repo is located and run `git status` to view any changes.

##### How to retrieve helper classes along with master
Please ensure you have checked out the correct branch first otherwise it won't work and currently doesn't accept parameters. To add the command, run this in your terminal.This is one time setup.
```
git config --global alias.pullall '!git fetch && git pull && git submodule update --recursive --remote'
```

After you run the above command, not you can use the below to pull everything all at once when using the submodule. The command can be used,instead of just ``` git pull ```
```
git pullall
```

### Setup Test Runner
* Using the CukeWipTest.java runner file, you can run the specific features using the tags option for example @androidcanada or @ioschina. 
* Environments, platforms and entities you wish to test must be specified in the command line or configuration in the IDE.
* In the IDE you can create and edit test-running configurations for the test-runner file. Make sure you specify with the following criteria:
1. Where environment variable 'env' is set, this should be, mock, sct, cert or prod.
2. Environment variable 'platform' refers to the device platforms that the tests can run in. At the time of writing this we have two platforms, android and ios.
3. Entity variable 'country' is specified when testing a certain country's features only. To select every country, leave this value out of the configuration.
* Your configuration should look like this:
```
-ea -Denv=[ENVIRONMENT HERE] -Dplatform=[PLATFORM HERE] -Dcountry=[COUNTRY HERE] -Dlocale=[LANGUAGE HERE]
```
* You are also required to use Java 8 as a minimum.
* Project dependencies are located in nexus so ensure that you distribution setting are correct (check your .m2 /setting.xml).

**NB:** cucumber.properties specifies the location of the injector source (this is required for cucumber steps).

### Android Runs
* Start Appium Desktop app on port 4725 or enter the following command:
```
$(ci-script appium/appium) -p 4725
```
* Before you follow the step below - make sure to set up a physical or simulated device.Steps are shown below
* To run a test edit your Run/Debug configurations to have the VM options such as “-ea -Denv=cert -Dplatform=android -Dcountry=canada -Dlocale=en” with the ‘Use class path of module:’ set to “mobilextestautomation_test” or run the following type of command in unix:
```
./gradlew -Denv=cert -Dplatform=android -Dcountry=canada -Dlocale=en test
```

#### Physical Device Setup
* Connect the device to your computer with a usb cable.
* On the Device, go to the Developer Options or the device equivalent and enable Debugging Mode.
* Find out if your device is connected by typing 
  ```
  adb devices
  ```
* If device is not detected
  1. Revoke the usb debugging authentication and allow it again in device settings
  2. Restart the adb server:
  ```
  adb kill-server
  adb start-server
  ```

#### Simulator Setup
* Install HAXM using Android Studio.
* When in Android Studio, click Tools, then Android and select ‘AVD Manager’.
* Set up a device of your choosing with Tools > AVD Manager in Android Studio.
* Start the Emulator you have created with the green ‘Play’ button on the right hand side of the AVD list.
* Go to wifi settings and modify the default network with a manually set proxy with the IP of **10.0.2.2** and Port of **3128**. Or use auto configuration with the .pac address for your region
* If WiFi does not connect automatically after entering proxy, toggle it off/on
* Open Chrome on the device and enter proxy login info if prompted
* Unlike the iOS simulator, the emulator must be open when you initiate the tests which can be done in the command line with the above command or the play button in your IDE.

### iOS Runs
#### Simulator and Real Device

Ensure you have Xcode 9.2 installed on your machine

To use the latest iOS symbols in Xcode 9.2 Follow instructions on this page on how to add them
https://digital-confluence.systems.uk.hsbc/confluence/display/INFRA/Mobile+Dev+-+Use+latest+iOS+symbols+in+Xcode

In the project root run the command:

```
./scripts/iOSDeviceSetup.sh

```
Prompt ``password to unlock login.keychain-db:`` will appear. Please enter your AD password.
A popup will appear asking you to enter a password for 'MobileX.p12', please ask a team member for this password. 

To see the logs for Xcode build please navigate to ```~/mobile/WeDriverAgent/build.log ```

To see the logs for Xcode build please navigate to ```~/mobile/WeDriverAgent/iproxy.log```

## Interacting With Mobile Elements
There are two methods of interacting with elements on the mobile pages. They both have their own advantages and disadvantages so it is advised that you learn to work with both.

### Arc
Arc is a command-line tool that helps you interact with customised commands to see whether or not the elements you have specified in your tests are correct.

* Prerequisite commands:
```
gem install nokogiri
```
```
bundle install
```
* To get Arc you must run:
```
bundle install appium_console
```
* Next, you need to install the dependencies. You can do this by running bundle install.
* Create a capabilities file in the root directory named either android.txt or ios.txt that looks like this:
```
[caps]
platformName = "iOS"
platformVersion = "11.1"
deviceName = "iPhone"
app = "/Users/YOUR USERNAME/Library/Developer/Xcode/DerivedData/MobileXGlobal-fblbdacfkwxizudvhukhnasplpia/Build/Products/Debug-iphoneos/MobileXGlobalDev.app"
noReset = false
```
* Followed by (everything but the port can be left empty):
```
[appium_lib]
port = 4726
sauce_username = ""
sauce_access_key = ""
```
* Launch arc using one of the following commands:
```
arc toml ios.txt
```
```
arc toml android.txt
```
* This will begin an interactive ruby pry environment.
* The most useful commands here are:
```
puts page
```
* This prints a list of elements on the page.
* And also:
```
find_elements(:id,’element_id’).click()
```
* This activates the element specified by clicking it.

### Inspector
Inspector is a tool that comes with the appium desktop application. It allows you to hover over an interactive version of the app’s pages and interact with it in a variety of ways. This is helpful when identifying which elements you want to place in your tests but is limited by the fact that it cannot receive custom commands.

* To configure this, once you have started an appium server, click the magnifying glass when an appium server is running and create a desired capability template with the inspector. The JSON template format is as follows:
```
{
  "platformName": "Android",
  "deviceName": "Nexus 5X",
  "app": "/Users/YOUR USERNAME/mobilex-global-android/Android/app/build/outputs/apk/certAllCountry/debug/app-cert-allCountry-debug.apk",
  "platformVersion": "7.1.1"
}
```
**NB:** This works with both simulators/emulators as well as physical devices.


### Localization
From now, we can use the localized text to identify error message text, screen names text, button names text

#### How do we do this?
* A folder must be created for each country in 
```
/src/main/resources/locales
```
* Each Country folder will have csv files for the locales
```
For example Canada will have en.csv and fr.csv
```
*The csv folders will have Key and Value. The Key and Value must be the same as in the localized string files for ios or Android.For iOS it can be found here.
https://alm-github.systems.uk.hsbc/mobile/mobilebanking-global-ios/tree/develop/iOS/ProjectResources/CountryFiles
Note: Please follow the correct format while editing in the csv file.

#### How do we run?
Add a new command line options -Dlocale=<locale name>
```
-ea -Denv=cert -Dplatform=ios -Dcountry=canada -Dlocale=en
```

# UnManaged Mac Users Installation

## CNTLM Proxy Installation
* If you haven’t yet; install CNTLM following [these instructions](https://digital-confluence.systems.uk.hsbc/confluence/display/KF/cntlm).
* CNTLM should be running at all times when you are testing to allow both the device and your tests to utilise the proxy.

## Ruby Installation
* We use [rbenv](https://github.com/rbenv/rbenv) to easily manage the *ruby* version, and to keep it consistent across machines.
* A Ruby port of appium lib is used to communicate with the appium server, so having Ruby installed is a pre-requisite to running and writing tests.
* Install via brew using:
```
brew install rbenv
```
* *Don't forget* to call rbenv init at the end of your .bash_profile by adding:
```
if which rbenv > /dev/null;
  then eval "$(rbenv init -)"
fi
```
* And also:
```
export PATH="$HOME/.rbenv/bin:$PATH" 
```
* We currently use ruby **v2.3.1**. To install, go to the terminal and run:
```
rbenv install 2.3.1
```
* Should you see the error ‘./miniruby permission denied’, change to the directory it is talking about and to make it work do:
```
make install
```
* If the HSBC certificates are untrusted while on the proxy do the following:
```
RUBY_BUILD_CURL_OPTS=--insecure rbenv install 2.3.1 --verbose
rbenv rehash
```
* This ruby version will be compiled in the machine during install and it will most possibly fail due to an issue with MacOS, follow the solution [here](https://github.com/rbenv/ruby-build/issues/992).
* Call `rbenv global 2.3.1` after the installation so that this becomes your default ruby version.
* We use *bundler* to manage what ruby gems are needed, and to keep them consistent across machines. There is a *Gemfile* file on the project root to define the gems are required.
```
gem install bundler
```
**NB:** In order to run the tests you will need to have all the application dependencies installed and ready for use.

# Coding Style Guide

Style guide documentation can be found [here](https://alm-github.systems.uk.hsbc/mobile/mobilex-global-tests/blob/master/docs/code-style-guide.md)

# Running a Regression

Documentation explaining our standard operating procedure (SOP) for regressions can be found [here](https://digital-confluence.systems.uk.hsbc/confluence/display/MXGL/Regression+SOP)

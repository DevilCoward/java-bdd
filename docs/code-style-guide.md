Mobile X Test Automation : Coding Style Guide
=============================================

*   [Introduction](#Introduction)
    *   [Terminology](#Terminology)
*   [Source file basics](#Sourcefilebasics)
    *   [Whitespace](#Whitespace)
        *   [Indenting](#Indenting)
        *   [Import statements](#Importstatements)
        *   [Wildcard imports](#Wildcardimports)
        *   [Line-wrapping](#Line-wrapping)
        *   [Ordering and spacing](#Orderingandspacing)
    *   [Class declarations](#Classdeclarations)
        *   [Exactly one top-level class declaration](#Exactlyonetop-levelclassdeclaration)
        *   [Ordering of class contents](#Orderingofclasscontents)
            *   [Contracts](#Contracts)
            *   [Page Objects](#PageObjects)
            *   [Steps](#Steps)
*   [Formatting](#Formatting)
    *   [Braces](#Braces)
        *   [Braces used where optional](#Bracesusedwhereoptional)
        *   [Non-empty blocks: K & R style](#Non-emptyblocks:K&Rstyle)
        *   [Empty blocks](#Emptyblocks)
    *   [Block indentation](#Blockindentation)
    *   [Column Limit](#ColumnLimit)
        *   [Exceptions:](#Exceptions:)
    *   [Line-wrapping](#Line-wrapping.1)
        *   [Where to break](#Wheretobreak)
    *   [Whitespace](#Whitespace.1)
        *   [Vertical Whitespace](#VerticalWhitespace)
        *   [Horizontal Whitespace](#HorizontalWhitespace)
        *   [In one-line methods](#Inone-linemethods)
        *   [Horizontal Alignment](#HorizontalAlignment)
    *   [Variable Declarations](#VariableDeclarations)
        *   [One variable per declaration](#Onevariableperdeclaration)
        *   [In Page Object files](#InPageObjectfiles)
    *   [Arrays](#Arrays)
    *   [Annotations](#Annotations)
    *   [Comments](#Comments)
    *   [Modifiers](#Modifiers)
*   [Naming Conventions](#NamingConventions)
    *   [Common Rules](#CommonRules)
    *   [Rules by Type](#RulesbyType)
        *   [Package Names](#PackageNames)
        *   [Class Names](#ClassNames)
        *   [Method Names](#MethodNames)
        *   [Constant Names](#ConstantNames)
        *   [Non-Constant Names](#Non-ConstantNames)
        *   [Local Variable Names](#LocalVariableNames)
        *   [Locale CSV Names](#LocaleCSVNames)
*   [Programming Practices](#ProgrammingPractices)
    *   [Common Practices](#CommonPractices)
        *   [Assertions](#Assertions)
        *   [Contracts](#Contracts.1)
        *   [Logging](#Logging)
    *   [Page Objects](#PageObjects.1)
        *   [Methods](#Methods)
        *   [waitVisible() vs. isVisible()](#waitVisible()vs.isVisible())
        *   [Locator Strategies](#LocatorStrategies)
        *   [Non-constant Locators](#Non-constantLocators)
        *   [Element Declaration](#ElementDeclaration)
        *   [Non-constant elements](#Non-constantelements)
    *   [Handling elements base on Text to click or return a boolean value](#HandlingelementsbaseonTexttoclickorreturnabooleanvalue)
    *   [Contracts](#Contracts.2)
    *   [Steps](#Steps.1)
        *   [Adverbs](#Adverbs)
        *   [Use of Lambda Expressions in Single-Method Steps](#UseofLambdaExpressionsforSingle-Method Steps)
	*   [Optional Args Syntax](#OptionalArgsSyntax)
        *   [Parameters](#Parameters)
        *   [Assertions](#Assertions.1)
        *   [Naming](#Naming)
*   [Project Structure](#ProjectStructure)
    *   [Contracts & Page Objects](#Contracts&PageObjects)
        *   [Exactly One Contract per Page Object(s)](#ExactlyOneContractperPageObject(s))
        *   [Package Structure](#PackageStructure)
            *   [Common Packages](#CommonPackages)

Introduction
============

This document serves as a **living document** defining coding standards for the Mobile X Test Automation framework, which is written primarily in Java and Cucumber/Gherkin.

This style guide covers conventions, best practices, and coding standards for that project.  

Terminology
-----------

For the purposes of this document:

1.  The term **_class_** is used inclusively to mean any ordinary "class", with several exceptions:
2.  The **page object classes** we use to define methods and instructions on screens for each platform shall be referred to as **page objects** or **poo's**
3.  The **interface classes** we use to abstract out to Android and iOS page objects shall be referred to as **contracts** (or **_interfaces_**)
4.  The **Cucumber feature files** we use to define test scenarios using steps shall be referred to as **_features_**
5.  The **Cucumber step classes** we use to glue step definitions to their corresponding contracts (or other code) shall be referred to as **steps**

Source file basics
==================

Whitespace
----------

### Indenting

*   All sub statements or declarations shall be indented.  For example, the declarations in a class, the body of a method, and the **`if`**\- and **`then`**\- parts of a conditional statement shall be indented.
*   Indent size for all **Java** code is **4 columns**.  Nested continuations shall also add **4 columns**.
*   Indent size and nested continuations in Cucumber **features** is **2 columns**.
*   Tabs shall **not** be used for indentation.  (IntelliJ takes care of this automatically)

### Import statements

### Wildcard imports

With the **exception** of contract imports, **wildcard imports shall not be used**.

### Line-wrapping

Import statements shall **not** be line-wrapped.  The column limit does not apply to import statements.

### Ordering and spacing

Imports are ordered as follows:

1.  All static imports in a single block.
2.  All non-static imports in a single block.

If there are both static and non-static imports, a single blank line separates the two blocks.  No other blank lines shall be used between import statements.  Within each import block, imports shall appear in ASCII sort order.

Class declarations
------------------

### Exactly one top-level class declaration

Each top-level class resides in a source file of its own.

### Ordering of class contents

#### Contracts

Contract classes shall contain an ASCII-ordered declaration of all methods, each separated by a single blank line.

#### Page Objects

Page object classes shall begin with an ASCII-ordered declaration of all helpers, drivers, and element locator variable declarations, followed by ASCII-ordered declarations of all `public` methods from their corresponding contracts, then any additional `private` helper methods.

**Page Object Example** 

    public class PageObjectExample extends Base implements CorrespondingContract {
	
	    private final AppiumHelpers appiumHelpers;

	    private final String fooElementId = "foo";
	    private final String barElementId = "bar";
	
	    private final String localizedStringElementLabel = LocaleCSVParser.getLocaleValue("localeCSVKey");

#### Steps

Steps shall begin with an ASCII-ordered declaration of all local variables, then a _logically-ordered_ declaration of steps.

Formatting
==========

Braces
------

### Braces used where optional

Braces shall always be used with **`if`**, **`else`**, **`for`**, **`do`**, and **`while`** statements, even when the body is empty, or contains only a single statement.

### Non-empty blocks: K & R style

Braces follow the Kernighan and Ritchie style ("[Egyptian brackets](http://www.codinghorror.com/blog/2012/07/new-programming-jargon.html)") for non-empty blocks:

*   No line break before the opening brace
*   Line break after the opening brace
*   Line break before the closing brace
*   Line break after the closing brace, _only_ if that brace terminates a statement or terminates the body of a method, constructor, or named class.  For example, there is **no** line break after the brace if it is followed by **`catch`, `else`**, or a comma.

  

**K & R style braces** 

    return () -> {									// No line break before opening brace, but line break after
        while (condition()) {
            method();
        }											// Line break before the closing brace
    };												// Line break after this closing brace, as it terminates the statement
    
    return new MyClass() {							
        @Override public void method() {
            if (condition()) {
                try {
                    something();
                } catch (ProblemException e) {		// No line break after closing brace, because of catch statement
                    recover();
                }
            } else if (otherCondition()) {			// No line break after, because of else statement
                somethingElse();
            } else {								// As above
                lastThing();
            }
        }
    };

### Empty blocks

An empty block may be in K & R style, or it may be closed immediately after it is opened, with no characters or line break in between, **unless** it is part of a multi-block statement (one that contains multiple blocks: ( **`if`** / **`else`** or **`try`** / **`catch`** / **`finally`** ).

**Empty block examples** 

    // Good empty block
    void doNothing() {}
    
    // Also good
    void doNothingElse() {
    }
    
    // Not good : no concise empty blocks in a multi-block statement
    try { 
        doSomething();
    } catch (Exception e) {} 

Block indentation
-----------------

Each time a new block or block-like construct is opened, increase indent by 4 spaces, per [indentation](https://digital-confluence.systems.uk.hsbc/confluence/display/MXTA/Coding+Style+Guide#CodingStyleGuide-Orderingandspacing) guidelines.

Column Limit
------------

Code shall comply with a 100-character column limit.  Except as noted below, any line that would exceed this limit shall be line-wrapped.

### Exceptions:

1.  Lines where obeying the column limit is not possible
2.  **package** and **import** statements
3.  Command lines in a comment that may be cut-and-pasted into a shell.

**Class Declaration Line Wrapping** 

    public LoginSteps(Username username, Config config, MemorableAnswer memorableAnswer, Password password, 
                      Accounts accounts, LogonSelection logonSelection, DSKLogon dskLogon, MauthLogin mauthLogin) {

Line-wrapping
-------------

Line-wrapping shall be used whenever a line of code would exceed the column limit.  However, even code that might not exceed the limit may be line-wrapped at the author's discretion.  Line-wrapping shall be done for **readability**.  The primary goal for line-wrapping is to have **clear, readable** code, not necessarily code that fits in the smallest number of lines.

### Where to break

1.  When a line is broken at a _non-assignment_ operator, the break shall come _before_ the symbol.
    1.  This also applies to the following "operator-like" symbols:
        *   the dot separator ( **`.`** )
        *   the two colons of a method reference ( **`::`** )
        *   an ampersand in a type bound ( **`<T extends Foo & Bar>`** )
        *   a pipe in a catch block ( **`catch (FooException | BarException e)`** )
2.  When a line is broken at an _assignment_ operator, the break shall come _after_ the symbol, but either way is acceptable.
3.  A method or constructor name stays attached to the open parenthesis ( **`(`** ) that follows it
4.  A comma ( **`,`** ) stays attached to the token that precedes it
5.  A line is never broken adjacent to the arrow in a lambda, except that a break may come immediately after the arrow if the body of the lambda consists of a single unbraced expression.

  

**Line-wrapping Examples** 

    MyLamdbda<String, Long, Object> lambda = 
        (String label, Long value, Object obj) -> {
            ...
        };
    
    
    Predicate<String> predicate = str ->
        longExpressionInvolving(str);

Whitespace
----------

### Vertical Whitespace

A single blank line shall always appear:

1.  Between consecutive members or initializers of a class or interface: fields, constructors, methods, nested classes, static initializers, and instance initializers. 
    *   **Exception**: a blank line between two consecutive fields (having no other code between them) is optional.  Such blank lines are used as needed to create **logical groupings** of fields.
    *   **Exception**: Blank lines between enum constants.
2.  As required by other sections of the style guide (Source file structure, Import statements)

A single blank line may also appear anywhere it **improves readability**, for example between statements to organize the code into logical subsections. 

Multiple consecutive blank lines are permitted, but discouraged.

### Horizontal Whitespace

Beyond what is required by the language or other style rules, and aside from string literals and comments, a single character of whitespace shall also appear in the following places:

1.  Separating any reserved word, such as **`if`**, **`for`**, or **`catch`**, from an open parenthesis ( **`(`** ) that follows it on that line
2.  Separating any reserved word, such as **`else`** or **`catch`**, from a closing curly brace ( **`}`** ) that precedes it on that line
3.  Before any open curly brace ( **`{`** ), with two exceptions:
    *   **`@SomeAnnotation({a, b})`** (no space is used)
    *   **```String[][] x = `{{``"foo"}};` ```**
4.  On both sides of any binary or ternary operator.  This also applies to the following "operator-like" symbols:
    
    *   the ampersand in a conjunctive type bound: **`<T extends Foo & Bar>`**
    *   the pipe for a catch block that handles multiple exceptions: **`catch (FooException | BarException e)`**
    *   the colon (**`:`** ) in an enhanced `for` ("foreach") statement
    *   the arrow in a lambda expression: **`(String str) → str.length()`**
    
    but not
    
    *   the two colons ( **`::`** ) of a method reference, which is written like **`Object::toString`**
    *   the dot separator ( **`.`** ), which is written like `` `**object.toString()**` ``
        
          
        
   **Horizontal Whitespace**
        
        // Bad
        int foo=a+b+1;
        
        // Good
        int foo = a + b + 1;
        
          
        
5.  After** **`,:;`** or the closing parenthesis ( **`)`** ) of a cast
6.  On both sides of the double slash ( **`//`** ) that begins an end-of-line comment.  Here, multiple spaces are allowed, but not required.
7.  Between the type and variable of a declaration: **`List<String> list`**
8.  _Optionally_ just inside both braces of an array initializer.  Both of the following examples are valid:  
    *   **``new int[] `{`5, 6}``**
    *   **``new int[] `{` 5, 6 }``**
9.  Between a type annotation and **`[]`** or **`...`**

### In one-line methods

Developers shall use a single space of whitespace on either side of a one-line method definition for readability:

**Whitespace for Readability** 

    // Good
    public void foo() { bar(); }
    
    // Also good
    public void foo() {
        bar();
    }
    
    // Bad
    public void foo() {bar();}

### Horizontal Alignment

Horizontal alignment is the practice of adding a variable number of additional whitespaces to your code with the goal of making certain tokens appear directly below other tokens on previous lines.

This practice is **required only** in **features**:

**Horizontal Alignment in Feature Files** 

    Feature: Example feature
      Feature documentation
    
      Scenario Outline: Outline of scenario goes here
      Given some condition
      When the user enters <usernameType> username
      And the user chooses to move money
      And the user creates <transferType> transfer <numberOfTimes> repeating
      Then the user transfers money between accounts <transferCheck>
    
    
      Examples:  														// Horizontal alignment shall be used to format the following table
      | usernameType | transferType | numberOfTimes | transferCheck |
      | default      | immediate    | 0             | now           |
      | default      | future       | 0             | later         |
      | fx           | fx           | 0             | now           | 

This practice is **not required** elsewhere in code, but runs the risk of maintenance, so it is discouraged:

**Horizontal Alignment in Java** 

    private int x; // this is fine
    private Color color; // this too
    
    
    private int		x;		// permitted, but discouraged :
    private Color 	color;	// think of future maintenance costs!

Alignment can aid readability, but it creates problems for future maintenance. Consider a future change that needs to touch just one line. This change may leave the formerly-pleasing formatting mangled, and that is **allowed**. More often it prompts the coder (perhaps you) to adjust whitespace on nearby lines as well, possibly triggering a cascading series of reformattings. That one-line change now has a "blast radius." This can at worst result in pointless busywork, but at best it still corrupts version history information, slows down reviewers and exacerbates merge conflicts.

Variable Declarations
---------------------

### One variable per declaration

Every variable declaration declares **exactly one** variable: declarations such as **`int a, b;`** are not used.

### In Page Object files

Local variables shall be declared at the top of every page object, per [Class Declarations : Page Objects](https://digital-confluence.systems.uk.hsbc/confluence/display/MXTA/Coding+Style+Guide#CodingStyleGuide-PageObjects).  

Arrays
------

Any array initializer may optionally be formatted as if it were a "block-like construct."  For example, the following are all valid:

**Array Formatting** 

    new int\[\] {           new int\[\] {
      0, 1, 2, 3            0,
    }                       1,
                            2,
    new int\[\] {             3,
      0, 1,               }
      2, 3
    }                     new int\[\]
                              {0, 1, 2, 3}

Annotations
-----------

Annotations applying to a class, method, or constructor shall appear immediately before the class, method, or constructor, and each annotation shall be listed on a line of its own.  These line breaks do **not** constitute line-wrapping, so the indentation level is **not** increased:

**Annotations** 

    @Override
    @Nullable
    public String getNameIfPresent() { ... }

Comments
--------

Comments in code should be avoided.  Instead, make every effort to write code that is self-documenting and readable.  However, if a method or algorithm seems unclear, you (or your PR reviewers) may request that a comment be added to clarify your code.   

**Exception:** If stubbing out a method, constructor, or class for later work, add a single-line TODO comment showing the date of the comment and a JIRA ticket URL for the task, epic, or defect.  This may be optionally line-wrapped.  TODOs **must** have dates and corresponding JIRA tickets.

**Comments** 

    // This is fine:
    public void selectAccount() { return false; }    // TODO 31.10.2018 (https://jira-digital.systems.uk.hsbc/jira/browse/MXGL-16678)
    
    
    // So is this:
    public void selectAccount() { return false; }    	// TODO 31.10.2018 : a brief explanation of work to be done
                                                        // (https://jira-digital.systems.uk.hsbc/jira/browse/MXGL-16678)

Modifiers
---------

Class and member modifiers, when present, shall appear in the order recommended by Java:

**`public protected private abstract default static final transient volatile synchronized native strictfp`**

Naming Conventions
==================

Common Rules
------------

Identifiers shall use only ASCII letters and digits, and, rarely, underscores.

All names -- packages, classes, interfaces, methods, and variables -- shall be declared with meaningful names, without shorthand or acronyms, with exceptions only for those acronyms defined in docs/glossary.md, typically for bank-specific acronyms.

Rules by Type
-------------

### Package Names

Package names shall be **all lowercase**, with consecutive words simply concatenated together, with no underscores.

**`com.hsbc.digital.testautomation`**

### Class Names

Class names shall be written in **`UpperCamelCase`**.

Class names are typically nouns or noun phrases.  For example, **`AccountDetails`** or **`AccountDetailsScreen`**. 

### Method Names

Method names shall be written in **`lowerCamelCase`**.

Method names are typically verbs or verb phrases.  For example, **`getAccountDetails`** or **`waitVisible`**.

Method names shall clearly describe the output or purpose of the method.  

### Constant Names

Constant names shall use **`CONSTANT_CASE`** : all uppercase letters, with each word separated by a single underscore.

### Non-Constant Names

Non-constant names (static or otherwise) shall be written in **`lowerCamelCase`**.  Acronyms shall be overridden by camelCasing (for example: **`iosButtonId`**, not **`iOSButtonID`**)

### Local Variable Names

Local variable names shall be written in **`lowerCamelCase`**.

Even when final and immutable, local variables shall not be considered constants, and shall **not** be styled in **`CONSTANT_CASE`**.

Extremely short variable names shall be reserved for instances like loop indices.  Where possible, include units in variable names.

**Variable Names** 

    // Bad
    class User {
        private final int a;
        private final String m;
    }
    
    // Good
    class User {
        private final int age;			
        private final String maidenName;
    }
    
    
    // Better
    class User {
        private final int ageInYears;			// including units in variable names for clarity
        private final String maidenName;
    }

### Locale CSV Names

Locale CSV key values shall match their corresponding localized string keys in the iOS / Android projects and/or "copykitten".  This is typically **`snake_case`**.

### Config Names (Users and Config)

.conf file keys shall follow the recommended [HOCON](https://github.com/lightbend/config/blob/master/HOCON.md#hyphen-separated-vs-camelcase) variable naming convention: **`hypen-separated-keys`** 

Programming Practices
=====================

Common Practices
----------------

### Assertions

Assertions shall be used **exclusively in "**Then**"-style Step Definitions, only.**   This means that many page object methods may fail by throwing **`Exception`**s.  Additionally, developers shall use **`boolean ``isVisible()`** methods in page objects, which call **`appiumHelpers.checkForElementBy...(elementLocator)`** wherever possible to Catch those Exceptions and return **`boolean`**s for our **`assertThat`**s for now.  As of writing we are investigating a way to filter Exceptions and callstacks from the Cucumber Extent report, which may alter this practice in the future.

**Page Object Exceptions** 

    // In a Page Object...
    
    
    public class PageObject () { 
        private final Integer someTimeoutInSeconds = 5;
    
        public void waitVisible() {									// This method will wait for 20 seconds for a given element, and Throw if it fails.
            appiumHelpers.waitForElementById(someElementId);		// use this method to wait for pages to completely load, etc., as \*part of\* a test. 
        }
    
    
        public void enterUsername(String username) {
            appiumHelpers.waitForElementById(usernameTextFieldId);	// Maybe it's necessary in some other methods to wait for a race condition
                                                                    // e.g there is some delay on the app and we need to wait before performing an action
                                                                    // in which case, use waitForElement...() -- but only if it's necessary
            usernameTextField.sendKeys(username)
        }
    
    
        public void clickContinue() {
            appiumHelpers.waitForElementToBeClickableById(continueButtonId);		// Example of a race condition here -- after entering username,
            continueButton.click();													// maybe there is a delay before the Continue button is clickable.
        }																			// Use explicit waits to avoid timing issues where necessary
    
    
        public boolean isVisible(Integer... timeoutInSeconds) {
        int timeout = timeoutInSeconds.length == 0 ? defaultTimeoutInSeconds : timeoutInSeconds\[0\];
        return appiumHelpers.checkForElementById(accountNameId,
            timeout);
        }															// This method will wait for timeoutInSeconds for a given element, and return a boolean
                                                                    // true if the element appears, false if it does not.  Use this when you are \*explicitly\*
                                                                    // testing whether or not an element appears for a test

All assertions shall be written using Hamcrest libraries – **do not** use JUnit libraries.

Assertions shall be written explicitly, across multiple lines, using **`actual`**, **`expected`**, and **`reasonForFailure`** variables.

**Assertions** 

    // Good 
    Then("^user shall see some screen$", () -> {	
        boolean expected = true;
        boolean actual = someScreen.isVisible();
        String reasonForFailure = "Some Screen did not appear as expected";
        assertThat(reasonForFailure, actual, equalTo(expected));
    });
    
    
    // Bad -- this is not very readable, and it is in a page object method, not a Step definition
    public void navigateToSomeScreen() {
        assertThat("Some screen did not appear", true, equalTo(someScreen.waitVisible));  
    }
    
**Handling multiple assertions**

Each assertion should be written within its own curly braced code block

    // Good – What we shall do
    Then("some step which has multiple assertions") -> {
        { 
            String reasonForFailure = "foo";
            boolean expected = true;
            boolean actual = pageObject.validateSomething();
            assertThat(reasonForFailure, actual, equalTo(expected);
        }
        { // like so
            String reasonForFailure = "foo1";
            boolean expected = true;
            boolean actual = pageObject.validateSomething();
            assertThat(reasonForFailure, actual, equalTo(expected);
        }
        { // and so on 
            ...
        }
    });
     
    // Bad
     Then("some step which has multiple assertions") -> { 
          String reasonForFailure = "foo";
          boolean expected = true;
          boolean actual = pageObject.validateSomething();
          assertThat(reasonForFailure, actual, equalTo(expected);
          
          reasonForFailure = "foo1";
          expected = true;
          actual = pageObject.validateSomething();
          assertThat(reasonForFailure, actual, equalTo(expected);
     });

### Contracts

All contract interfaces need to extend the **VisibleInterface** and all contracts will inherit the **`void waitVisible()`** and **`boolean isVisible(Integer...  timeoutInSeconds)`** methods, as well as the **`defaultTimeoutInSeconds`** constant, which can be overrided at the page object level if needed.

This means all screen classes will need to provide an implementation for **`void waitVisible()`** and **`boolean isVisible(Integer...  timeoutInSeconds)`**.  These methods should share the same element locator and strategy.  For information on implementation of these methods, please look at the section [below](https://digital-confluence.systems.uk.hsbc/confluence/display/MXTA/Coding+Style+Guide#CodingStyleGuide-waitVisible()vs.isVisible()).

**Contracts** 

    // Good – What we shall do
    public interface NewInterface extends WaitVisibleInterface {
        // waitVisible() does not need to be added here
        // add additional methods
    }
    
    // Bad – What we shall avoid
    public interface NewInterface {
        // waitVisible() would need to be added here
        // add additional methods
}

### Logging

Please use the **`org.slf4j.LoggerFactory`** package for all logging.

Page Objects
------------

### Methods

Page objects must implement **all** methods defined in their corresponding contracts.  These methods shall be **`public`**.  

If additional methods are implemented in a page object (e.g to assist another "in-contract" method, to avoid code duplication, etc.), they shall be implemented as **`private`** methods, to ensure they are not called from outside the class. 

### waitVisible() vs. isVisible()

waitVisible() methods shall be used to explicitly wait for one or more elements that indicate the page is visible (using **`waitForElement`**style methods) we expect to be present on a screen, but they shall Throw if any such element is not found.   It shall **not** be used to check for **all** expected elements of a given page.

isVisible() methods shall use **`checkForElement`** style methods to mask Thrown exceptions and instead return **`boolean`**s.

If a page object has an **`isVisible()`** method, also declare a **`private final Integer timeoutInSeconds`** alongside the element locator strategies, which can be overridden as needed.

Developers may wish to have more than one **`isVisible()`** method in a page object, e.g to check if multiple given elements are present during a test.  All methods shall use the same default **`timeoutInSeconds`** declared in the **contract** for the screen, which can be optionally overridden in the **page object**.  **However**, each page object should represent **exactly one screen** -- developers should avoid page object implementations that incorporate multiple screens into a single page object using multiple **`boolean isVisible()`** methods.

**isVisible usage** 

    // In the contract :
    public interface ThisPageContract extends WaitVisible {
        private final Integer defaultTimeoutInSeconds = 5;  // here we set the timeout for the isVisible() methods for both platforms/page objects
    }
    
    
    // in the page object :
    public class thisPageObject {
        private final String someElementLocatorId = "foo";
        private final String someOtherElementLocatorId = "bar";
        private final Integer defaultTimeoutInSeconds = 10;	// here we override the default for this page object specifically
    
        public boolean isVisible(Integer... timeoutInSeconds) {
            int timeout = timeoutInSeconds.length == 0 ? defaultTimeoutInSeconds : timeoutInSeconds\[0\];
            // finally here we give the user the option to override the defaultTimeout with their own value, else we use ours
            return appiumHelpers.checkForElementById(accountNameId, timeout);
        }
    }

### Locator Strategies

Wherever possible, page objects shall use unique **`WebElement`** IDs for all references.  Using Names, Labels, Class Names, Links, and XPaths are discouraged.

Variable names shall explicitly identify which element locator type they are using.  e.g **`progressBarId`**, **`progressBarClass`**, **`progressBarXpath`**.

Locator variable declarations shall be **`private`**.

### Non-constant Locators

In some cases, the values of a locator may be too generic, or, more frequently, may be dynamically populated with the localized copy of whatever language the app is running in.  In these cases, JIRA tickets shall be logged in the MOFO project to refactor these elements to have unique locator values. 

In the event that a locator value cannot be refactored, but it contains localized copy, the **`LocaleCSVParser`** library can be used to fetch these strings, based on the **`Dlocale`** provided at runtime. 

More information on this strategy can be found in the [README.MD](https://alm-github.systems.uk.hsbc/mobile/mobilex-global-tests#localization).

### Element Declaration

Elements shall be declared using **`@FindBy`** injection immediately after locator declarations.  Element declarations shall be **`public`**.

**Page Object Declaration Order** 

    public class ExampleScreen extends Base implements Example {
        private final AppiumDriver appiumDriver;
        private final AppiumHelpers appiumHelpers;
    
    
        private final String titleId = "title";
        private final String yesButtonId = "yesBtn";
        private final String noButtonId = "noBtn";
    
    
        // More on this later
        private final String nonConstantButtonName = LocaleCSVParser.getLocaleValue("example\_value");
    
    
        @FindBy(id = titleId)
        public WebElement title;
    
    
        @FindBy(id = yesButtonId)
        public WebElement yesButton;
    
    
        @FindBy(id = noButtonId)
        public WebElement noButton;

### Non-constant elements

When using the **`LocaleCSVParser`** strategy for elements, the **`@FindBy`** injection method will fail – the attribute values for **`@FindBy`** injection must be constants.  In these cases, it is acceptable to declare these elements inside the page object methods which use them.  Continuing from the previous code block:

**Non-Constant Element Locators** 

	// First, we declare a stubbed-out WebElement during the rest of our element declarations.
	public WebElement nonConstantButton;
	
	@Inject
	public ExampleScreen(AppiumDriver appiumDriver, AppiumHelpers appiumHelpers) {
		super(appiumDriver);
		this.appiumHelpers = appiumHelpers;
		this.appiumDriver = appiumDriver;
	}


	public void clickNonConstantElement(){
		// now, we use appiumDriver to populate the stubbed element
		nonConstantButton = appiumDriver.findElementByName(nonConstantButtonName);
		nonConstantButton.click();
	}

**Handling elements base on Text to click or return a boolean value**
---------------------------------------------------------------------

When we have list of web elements which we will like to click on it base on the text contains with the elements or return a boolean if it contains the expected text. Then the best way to handle it is to use stream api as stated on the example below.

**Non-Constant Element Locators** 

        // First, we declare a WebElements .
        @FindAll({@FindBy(id = moreTabsId)})
        public List<WebElement> moreTab;
    
    
        
        @Inject
        public ExampleScreen(AppiumDriver appiumDriver, AppiumHelpers appiumHelpers) {
            super(appiumDriver);
            this.appiumHelpers = appiumHelpers;
            this.appiumDriver = appiumDriver;
        }
    
    
        public void navigateToTab(String textToCheck) throws InterruptedException {
        moreTab.stream().filter(webElement -> webElement.getText().equalsIgnoreCase(textToCheck)).findFirst().get().click();
    }
    
    
    public boolean isDisplayed(String textToCheck) throws InterruptedException {
        return moreTab.stream().filter(webElement -> webElement.getText().equalsIgnoreCase(textToCheck)).findFirst().isPresent();
    }

Contracts
---------

Contracts shall contain **only** empty declarations of the methods which their corresponding page objects shall implement.

Steps
-----

### Adverbs

Gherkin allows us to pair one of a set of adverbs
(`And`, `Given`, `When`, `Then`, `But`) with a step.
There is no functional difference between the adverbs;
In fact, there is no guarantee the same adverb is used
in the step definition and feature files for any given step.
Despite this, we choose to give each adverb semantic meaning
to make it easier to understand our code.

`And` : Can be read as "whatever adverb was used for the previous
step". `And` is used **only** in the feature files, as it is
effectively meaningless in the step definition
(where ordering is not important).

`Given` : Used to denote test setup.

`When` : Used to denote a testable action, subsequently verified by
at least one `Then` step.

`Then` : Used to denote a test assertion. `Then` steps should **not**
take any actions other than asserting and, if necessary,
storing test data (e.g storing the value of an element, such
as account balance, for use in other Steps).
See [Assertions](assertions-1) section below for more details.

`But` : Not used widely in our framework, but can be understood
as equivelent to `And`

### Parameters
Steps definitions can be written parametrically using regex
matchers. As complex step definitions reduce readability,
steps shall have at most 2 parameters.

### Use of Lambda Expressions for Single-Method Steps
Steps which call only a single method shall be written as lambda
expressions.

**Example**

    // rather than writing this
    When("^the Step name goes here$", () -> {
        pageObject.methodName();
        });
        
    // try this
    When(^"the Step name goes here$", pageObject::methodName);
       
### Optional Args Syntax
Steps which perform an action based only on an optional condition (e.g "the user clicks a button only if the button is present on the screen") shall be written in the follow syntax/format:

**Example**

        When("^the user skips DSK activation( if present)?$",
                (String optionalStep) -> {
                    boolean isPresent = activateDSK.isVisible(5);
                    if (isPresent || optionalStep == null) {
                        activateDSK.selectSkip();
                    }
                });
		
		
### Assertions

As stated in [Common Practices](https://digital-confluence.systems.uk.hsbc/confluence/display/MXTA/Coding+Style+Guide#CodingStyleGuide-Assertions), Step definitions that assert shall do so with explicit **`actual`**, **`expected`**, and **`reasonForFailure`** variables.

1.  Only **`Then`** style step definitions shall Assert, although they may Assert multiple times on different conditions (e.g a different assert for each element on the page)
2.  **`And`** and **`Given`** style step definitions shall not assert at all.  
3.  Remove assertions in **`And`** and **`Given`** style step definitions, or split them into multiple Steps (one **`When`** step which performs actions, and another **`Then`** which validates the outcome of those actions)
4.  **`Then`** style step definitions shall call **`isVisible()`** instead of **`waitVisible()`** to get the bool result needed to Assert on

### Naming

If not immediately obvious, the step name shall specify where the action and/or check is being performed.

Examples:
* `the user enters a valid activation code AC1` - it is immediately obvious that the Activation Code will be entered on the Activation Code screen
* `the user clicks on the close button on the DSK Activation screen` - it is impossible to tell on which screen the close button is, as many screens have a close button so it must be specified

Steps shall follow the standard naming convention to ensure readability of the scenarios.
1. Where the step name begins with a singular noun, it shall be prefixed with a determiner in lower case
2. Where the noun is singular, a subsequent verb shall be in singular form
3. Where the noun is plural, a subsequent verb shall be in plural form
4. Features or screens shall be in upper case

Examples:
* the<sub>1</sub> user clicks<sub>2</sub> the Global Money<sub>4</sub> button
* Push Notifications<sub>4</sub> are<sub>3</sub> allowed
* the<sub>1</sub> Track Your Money On The Go<sub>4</sub> screen is<sub>2</sub> displayed

Project Structure
=================

Contracts & Page Objects
------------------------

### Exactly One Contract per Page Object(s)

Each contract shall map to a corresponding Page Object on both iOS and Android.  Even if the screen does not exist on one platform, a stubbed-out Page Object must be implemented (otherwise there will be compile errors at runtime). 

### Package Structure

Contracts and their corresponding Page Objects shall be organized into logical grouped subfolders, to aide scaleability and reduce [wildcard imports](https://digital-confluence.systems.uk.hsbc/confluence/display/MXTA/Coding+Style+Guide#CodingStyleGuide-Wildcardimports).  There is no hard-and-fast rule for groupings.  Logical groupings can be by feature or journey, for example.  

Page objects shall implement **exactly one contract**; the contract that corresponds to that page object.  Page objects shall contain validation methods, which can be called by our test Steps to validate any test case that spans multiple screens (e.g click "Log On" button and validate that the "Username" screen appears).  

#### Common Packages

Some Contracts & Page Objects are shared or commonly available throughout the app (e.g the TabBar Contract, which is visible in nearly every screen of the app).  Common Contracts & Page Objects shall be stored in the **common** package subfolder.


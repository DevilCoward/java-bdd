Pull Request Guide
======================

*   [Background](#Background)
*   [Compile](#Compile)
*   [Spotless Apply](#Spotless-Apply)
*   [Commit Messages](#Commit-Messages)
*   [Splitting Up PRs](#Splitting-Up-PRs)
*   [Filling Out The Template](#Filling-out-the-template)
*   [PR Checks](#PR-Checks)
*   [Addressing Review Comments](#Addressing-Review-Comments)
*   [Stale PRs](#Stale-PRs)
*   [Completing a PR](#Completing-a-PR)
*   [Expectations From Reviewers](#Expectations-From-Reviewers)


### Background
To respect the time of reviewers and reviewees, and to ensure that reviews are 
likely to catch as many issues present as possible, PRs that do not meet the following guidelines 
shall be declined and a review may not occur until all issues are addressed.


### Compile
- PR branch must be compiled locally before opening a PR
- Local compilation shall be done before pushing any further changes to a PR


### Spotless Apply
- Ensure Spotless is run against your branch before each push
```shell script
./gradlew spotlessApply
```
- Commit any changes that are made by spotless


### Commit Messages
- Commit messages will include details on what was affected by the change
- Bad Example:
    > Addressing PR Comments
- Good Example:
    > Refactored Move Money Step File  
                  - Updated regex for move money steps  
                  - Removed unused step


### Splitting Up PRs
- PRs that exceed the line limit shall be split up into multiple PRs
- For adding new journeys, the recommended PR distribution is in this order:
1. Any libraries or supporting code changes
2. Page Object Interfaces and Page Objects  
    - If multiple Page objects are implemented that will exceed the line limit, 
     break up the page objects into logical groups
3. Page Object Bindings, Step and Feature Files
- Git branching strategy enables doing this with ease while still continuing to work
- The Journey implementation does not have to be fully completed to create a PR
- e.g. Implementing a new journey "x"
    1. Determine any new libraries or changes to existing libraries required
    2. Create a Branch e.g. "x/library-change" 
        - Implement the library change, and create a PR for the initial library code, including any
stubbed out methods to be implemented
    3. While waiting for the PR review to complete, create a new branch "x/page-objects" that 
    branches from "x/library-change"
        - Implement page object code
        - If library code requires changes from PR review, switch to branch "x/library-change"
        - Address PR changes, commit, and push
        - Switch back to "x/page-objects" merge the changes from "x/library-change"
         into page objects branch and continue working
    4. Repeat for steps and feature files as needed
    
    
### Filling out the template
- Fill out all all sections of the PR template **accurately**
- Include a detailed description of features added or changed
- For new page objects, include a link to the confluence page showing the expected layout


### PR Checks
- Before requesting reviews, ensure the PR checks have completed successfully
- If PR Checks have failed, resolve all errors shown in the "details" log
    - For assistance in resolving issues, post in the `mobile-appium-automation` slack channel


### Addressing Review Comments
- If clarity is required for a review comment, ask for clarification
- In the event of disagreement on implementation, start a slack channel to discuss the proper approach
- If it is agreed that the comment is not in scope for the current PR, but does require resolution, 
create a Jira ticket, and reference it in a code comment for the code in question
- If a PR review has been completely addressed, but the reviewer has not re-reviewed within 24 hours 
the Review can be dismissed
- All PR comments shall be responded to, in order to indicate they have been acknowledged and addressed
- Unless you have agreed to the initial comment completely, do not resolve the conversation yourself, 
the person that started the conversation shall be the one that confirms that their comments have 
been resolved.


### Stale PRs
- If a PR will remain without update for over a week, close the PR and reopen once all issues are 
addressed
- In the new PR, reference the initial PR by linking to it in the PR Description


### Completing a PR
- After all PR comments are addressed and code owner approval has occurred, the PR can be closed
- Apply the "mergebot" label to the PR or merge manually
- If merging manually your branch must also be deleted manually


### Expectations From Reviewers
- Reviews shall be done thoroughly, regardless of PR size
    - Take your time, and aim to review 500-1000 lines of code an hour max
- If you feel someone else may be able to provide insightful comments on a PR, request them 
specifically as an additional reviewer
- If a review can not be completed in one sitting, indicate in review comments that more 
may follow
- As a code owner, make an effort to review PRs daily
- If you have left PR comments, ensure you are checking for responses daily
- If you have a question, leave it as a comment instead of a request for change
- Approval of a PR shall not occur if it will decrease the overall stability of the framework
- Approval of a PR shall not occur if it involves inherently bad practices that result in 
unreliable, or inaccurate tests
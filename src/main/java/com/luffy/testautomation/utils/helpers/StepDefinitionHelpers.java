package com.luffy.testautomation.utils.helpers;

import com.google.inject.Inject;

/**
 * Helper class to be used by Step Defintion classes.
 *
 * @return An instantiated {@link StepDefinitionHelpers} class
 */
public class StepDefinitionHelpers {

    /** Creates a new instance */
    @Inject
    public StepDefinitionHelpers() {}

    /**
     * Get the username key from the usertype
     *
     * @param typeOfUser is the usertype to be found in config. An example is typeOfUser ==
     *     "interac" then "interac-username" is created and returned
     * @return username to be used as a key to retrieve username from config file
     */
    public static String getUserTypeForGlobalFromConfigByUserType(String typeOfUser) {
        String usernameToFind;
        if (typeOfUser != null && !typeOfUser.equalsIgnoreCase("default")) {
            usernameToFind = typeOfUser + "-username";
        } else {
            usernameToFind = "username";
        }
        return usernameToFind;
    }

    public static String getUserTypeForZAPPFromConfigByUserType(String typeOfUser) {
        String usernameToFind;
        switch (typeOfUser) {
            case "general":
                usernameToFind = "zapp-username";
                break;
            case "overdraft":
                usernameToFind = "zapp-overdraft-username ";
                break;
            case "inhibit":
                usernameToFind = "zapp-inhibit-username";
                break;
            case "ineligible":
                usernameToFind = "zapp-noaccounts-username";
                break;
            case "suspended":
                usernameToFind = "zapp-suspended-username";
                break;
            default:
                usernameToFind = "username";
        }
        return usernameToFind;
    }
}

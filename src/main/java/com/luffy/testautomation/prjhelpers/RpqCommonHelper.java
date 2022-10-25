package com.luffy.testautomation.prjhelpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpqCommonHelper {

    private final Logger log = LoggerFactory.getLogger(RpqCommonHelper.class);

    /**
     * This method will return the number associated with the risk
     *
     * @param risk
     * @return value of the risk
     * @throws IllegalArgumentException if the risk is not in options
     */
    public int getRiskNumber(String risk) {
        int value = 0;
        switch (risk) {
            case "Secure":
                value = 0;
                break;
            case "Very Cautious":
                value = 1;
                break;
            case "Cautious":
                value = 2;
                break;
            case "Balanced":
                value = 3;
                break;
            case "Adventurous":
                value = 4;
                break;
            case "Speculative":
                value = 5;
                break;
            default:
                throw new IllegalArgumentException("Risk is not correct");
        }
        return value;
    }

    public boolean shouldPopupDisplay(
            String riskBeforeRetakeQuestionnaire, String riskAfterRetakeQuestionnaire) {
        log.info(" RPQ status BEFORE retake was --> {}", riskBeforeRetakeQuestionnaire);
        log.info(" RPQ status AFTER retake is --> {}", riskAfterRetakeQuestionnaire);
        int riskValueBeforeRetakeQuestionnaire = getRiskNumber(riskBeforeRetakeQuestionnaire);
        int riskValueAfterRetakeQuestionnaire = getRiskNumber(riskAfterRetakeQuestionnaire);
        log.info("riskValueBeforeRetakeQuestionnaire: {}", riskValueBeforeRetakeQuestionnaire);
        log.info("riskValueAfterRetakeQuestionnaire: {}", riskValueAfterRetakeQuestionnaire);

        return riskValueBeforeRetakeQuestionnaire < riskValueAfterRetakeQuestionnaire;
    }

    /**
     * This method will return the user and product risk level comparison number to help user
     * understand the risk of buying the fund
     *
     * @param productLevelRisk,userRiskLevel
     * @return result
     * @throws IllegalArgumentException if the risk levels are not correct
     */
    public int compareProductAndUserRiskLevel(int productLevelRisk, int userRiskLevel) {
        if (productLevelRisk <= userRiskLevel
                && (userRiskLevel != 0 && userRiskLevel != 6 && userRiskLevel != 7)) {
            log.info("**** Product Risk level is Lower than or Equal user risk Level ");
            return 0;
        } else if (productLevelRisk > userRiskLevel && (userRiskLevel != 6 && userRiskLevel != 7)) {
            log.info(
                    "**** Product Risk level is Higher than User risk level , But You can still Buy the product");
            return 1;
        } else if (userRiskLevel == 6) {
            log.info("User risk level is Unknown - New Profile");
            return userRiskLevel;
        } else if (userRiskLevel == 7) {
            log.info("User risk level is Expired - Retake RPQ");
            return userRiskLevel;
        } else {
            throw new IllegalArgumentException("The risk level passed are not correct");
        }
    }
}

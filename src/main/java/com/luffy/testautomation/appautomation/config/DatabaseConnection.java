package com.luffy.testautomation.appautomation.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseConnection {

    private Connection conn = null;

    public void connectToDatabase(String url, String username, String password) {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver").getDeclaredConstructor().newInstance();
            conn = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public String getResetPasswordCode(String user) {
        String query =
                "SELECT * from USER_CODE_STATUS where SERVICEID_PK =(select SERVICEID from USER_SERVICEID where USERID=(SELECT GUID FROM USER_CRED_HISTORY WHERE USERID='"
                        + user
                        + "'AND ROWNUM <= 1))";
        String prcCode = null;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                prcCode = rs.getString(2);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return prcCode;
    }

    public String getPTActivationCode(String user, String codeType) {
        String query =
                "SELECT * from HK_HBAP.USER_CODE_STATUS where SERVICEID_PK=(select SERVICEID from HK_HBAP.USER_SERVICEID where USERID=(SELECT GUID FROM HK_HBAP.USER_CRED_HISTORY WHERE USERID='"
                        + user
                        + "' AND ROWNUM <= 1))";
        String activationCode = null;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                activationCode = rs.getString(2);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return activationCode;
    }

    public String getActivationCode(String user, String codeType) {
        String query =
                "SELECT * from AP638_IC.USER_CODE_STATUS where SERVICEID_PK=(select SERVICEID from AP638_IC.USER_SERVICEID where USERID=(SELECT GUID FROM AP638_IC.USER_CRED_HISTORY WHERE USERID='"
                        + user
                        + "'AND ROWNUM <= 1)) AND CODE_TYPE='"
                        + codeType
                        + "'";
        String activationCode = null;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                activationCode = rs.getString(2);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return activationCode;
    }

    public void closeDatabaseConnection() {
        if (conn != null)
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
    }
}

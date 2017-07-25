package org.neriko.bankapp;

/**
 * Nikita Kartomin 2k17
 */

public class AppShared {

    private static String authToken;
    private static String url = "https://bank-2-0-nexiko.c9users.io/bank_api/";

    public static void setAuthToken(String newToken) {
        authToken = newToken;
    }
    public static String getAuthToken() {
        return authToken;
    }

    public static String getUrl() {
        return url;
    }

}

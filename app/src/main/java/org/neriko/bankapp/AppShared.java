package org.neriko.bankapp;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Nikita Kartomin 2k17
 */

public class AppShared {

    private static String authToken = null;
    //private static String url = "https://bank-2-0-nexiko.c9users.io/bank_api/";
    private static String url = "https://lfmsh.ru/bank_api";

    private static String name = null;
    private static String nameBackwards = null;
    private static String balance = null;
    private static String certificate = null;

    private static List<Counter> counters;
    private static List<Transaction> transactions;

    private static String seminarInfo;
    private static String labInfo;
    private static String facInfo;
    private static int facRequired = 0;

    private static String lecturesAttended;
    private static String lecturesMissed;

    private static String nextFine;

    private static String login;

    private static Date lastTransaction = null;

    public static void setAuthToken(String newToken) {
        authToken = newToken;
    }
    public static String getAuthToken() {
        return authToken;
    }

    public static String getUrl() {
        return url;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        AppShared.name = name;
    }

    public static String getBalance() {
        return balance;
    }

    public static void setBalance(String balance) {
        AppShared.balance = balance;
    }

    public static List<Counter> getCounters() {
        return counters;
    }

    public static void setCounters(List<Counter> counters) {
        AppShared.counters = counters;
    }

    public static List<Transaction> getTransactions() {
        return transactions;
    }

    public static void setTransactions(List<Transaction> transactions) {
        AppShared.transactions = transactions;
    }

    public static String getSeminarInfo() {
        return seminarInfo;
    }

    public static void setSeminarInfo(int seminar_attend, int fac_attend, int study_needed) {
        AppShared.seminarInfo = (seminar_attend + fac_attend) +
                "(" + seminar_attend + "/" + fac_attend + ") из " + study_needed;
    }

    public static String getCertificate() {
        return certificate;
    }

    public static void setCertificate(String certificate) {
        AppShared.certificate = certificate;
    }

    public static String getLabInfo() {
        return labInfo;
    }

    public static void setLabInfo(String labInfo) {
        AppShared.labInfo = labInfo;
    }

    public static String getLecturesAttended() {
        return lecturesAttended;
    }

    public static void setLecturesAttended(String lecturesAttended) {
        AppShared.lecturesAttended = lecturesAttended;
    }

    public static String getLecturesMissed() {
        return lecturesMissed;
    }

    public static void setLecturesMissed(String lecturesMissed) {
        AppShared.lecturesMissed = lecturesMissed;
    }

    public static String getNextFine() {
        return nextFine;
    }

    public static void setNextFine(String nextFine) {
        AppShared.nextFine = nextFine;
    }

    public static String getFacInfo() {
        return facInfo;
    }

    public static void setFacInfo(String facInfo) {
        AppShared.facInfo = facInfo;
    }

    public static String getLogin() {
        return login;
    }

    public static void setLogin(String login) {
        AppShared.login = login;
    }

    public static int getFacRequired() {
        return facRequired;
    }

    public static void setFacRequired(int facRequired) {
        AppShared.facRequired = facRequired;
    }

    public static String getNameBackwards() {
        return nameBackwards;
    }

    public static void setNameBackwards(String nameBackwards) {
        AppShared.nameBackwards = nameBackwards;
    }
}

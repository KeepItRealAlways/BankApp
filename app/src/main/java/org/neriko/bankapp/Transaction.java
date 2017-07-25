package org.neriko.bankapp;

/**
 * Created by Neriko on 25.07.2017.
 */

public class Transaction {

    public String title;
    public String description;
    public String cost;
    public String date;
    public String sender;

    public Transaction(String title, String description, String cost, String date, String sender) {
        this.title = title;
        this.description = description;
        this.cost = cost;
        this.date = date;
        this.sender = sender;
    }
}

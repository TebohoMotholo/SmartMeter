package com.example.smartmeterapp;

import java.util.ArrayList;

public class MeterData {
    // Singleton instance
    private static MeterData instance;

    // The current meter ID (default value for demo)
    private String meterId = "123456";

    // Current balance in units
    private int balance;

    // The last token applied
    private String lastToken;

    // History of token applications
    private ArrayList<String> history;

    // Private constructor to enforce singleton
    private MeterData() {
        balance = 0;
        lastToken = "N/A";
        history = new ArrayList<>();
    }

    /**
     * Get the singleton instance of MeterData.
     */
    public static synchronized MeterData getInstance() {
        if (instance == null) {
            instance = new MeterData();
        }
        return instance;
    }

    /**
     * Get the current meter ID.
     */
    public String getMeterId() {
        return meterId;
    }

    /**
     * Set the meter ID. Call this when user enters or changes meter ID.
     */
    public void setMeterId(String meterId) {
        this.meterId = meterId;
    }

    /**
     * Get the current balance in units.
     */
    public int getBalance() {
        return balance;
    }

    /**
     * Set the current balance. Should be called with the server-returned balance.
     */
    public void setBalance(int balance) {
        this.balance = balance;
    }

    /**
     * Get the last token that was applied.
     */
    public String getLastToken() {
        return lastToken;
    }

    /**
     * Set the last token that was applied.
     */
    public void setLastToken(String lastToken) {
        this.lastToken = lastToken;
    }

    /**
     * Get the history of token applications.
     */
    public ArrayList<String> getHistory() {
        return history;
    }

    /**
     * Add a new entry to the history list (newest at top).
     */
    public void addToHistory(String entry) {
        history.add(0, entry);
    }
}

package com.ahmad.util;

/**
 * 
 * @author Ahmad Alrefai
 */
public enum GhostnetStatus {
    GEMELDET("Gemeldet"),
    BERGUNG_BEVORSTEHEND("Bergung Bevorstehend"),
    GEBORGEN("Geborgen"),
    VERSCHOLLEN("Verschollen");

    private final String displayName;

    GhostnetStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
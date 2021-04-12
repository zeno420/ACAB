package de.ndfnb.acab.data.model;

import java.util.Date;

public class Message {
    private final String message;
    private String displayedText;
    private final int secVisible;

    public Message(String message, int secVisible) {
        this.displayedText = secVisible + " seconds available";
        this.message = message;
        this.secVisible = secVisible;
    }

    public String getMessage() {
        return message;
    }

    public int getSecVisible() {
        return secVisible;
    }

    public String getDisplayedText() {
        return displayedText;
    }

    public void open() {
        this.displayedText = this.message;
    }
}
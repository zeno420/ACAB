package de.ndfnb.acab.data.model;

import java.util.Date;

public class Message {
    private String message;
    private Date dateOpened;

    public String getMessage() {
        return message;
    }

    public Date getDateOpened() {
        return dateOpened;
    }

    public Message(String message) {
        this.message = message;
    }

    public void open() {

    }

    public void destroy() {

    }
}

package de.urkallinger.kallingapp.model;

import java.io.Serializable;

public class NewsListElement extends DataObject implements Serializable {
    private String title;

    public NewsListElement() {}

    public NewsListElement(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public NewsListElement setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public NewsListElement setMessage(String message) {
        this.message = message;
        return this;
    }

    private String message;
}

package com.seniorglez.polyglotfx.model;

import com.seniorglez.polyglotfx.model.PFXMessage;

public class PFXMessageBuilder {
    private String author, body;

    public PFXMessageBuilder setAuthor(String author) {
        this.author = author;
        return this;
    }

    public PFXMessageBuilder setBody(String body) {
        this.body = body;
        return this;
    }

    public PFXMessage build(){
        return new PFXMessage(this.author, this.body);
    }
    
}

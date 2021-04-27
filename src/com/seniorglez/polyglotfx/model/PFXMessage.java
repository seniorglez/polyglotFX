package com.seniorglez.polyglotfx.model;

import java.text.MessageFormat;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

public class PFXMessage {

    private final String author, body;

    public static PFXMessage formJson(String json) {
        try (Context polyglot = Context.create("js")) {
            Value jsObj = polyglot.eval("js", "JSON.parse('" + json + "')");
            if (jsObj.hasMember("author") && jsObj.hasMember("body")) {
                PFXMessage message = new PFXMessageBuilder()
                    .setAuthor(jsObj.getMember("author").asString())
                    .setBody(jsObj.getMember("body").asString())
                    .build();
                return message;
            }
        }
        return null;
    }

    public PFXMessage(String author, String body) {
        this.author = author;
        this.body = body;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getBody() {
        return this.body;
    }


    @Override
    public String toString() {
        return MessageFormat.format("{0} : {1}\n", this.getAuthor(), this.getBody());
    }
}

package com.seniorglez.polyglotfx.view;

import java.io.File;
import java.io.IOException;

import com.seniorglez.polyglotfx.model.PFXMessage;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;

import javafx.beans.property.SimpleStringProperty;

public class PolyglotFXModel {

    SimpleStringProperty messages;

    public PolyglotFXModel() {
        messages = new SimpleStringProperty("");
    }

    public void notifyMessage(PFXMessage pfxMessage) {
        try (Context polyglot = Context.newBuilder().allowAllAccess(true).build()) {
            File file = new File("notifier");
            Source source = Source.newBuilder("llvm", file).build();
            Value cpart = polyglot.eval(source);
            if (cpart.canInvokeMember("notify_message"))
                cpart.invokeMember("notify_message", pfxMessage.getAuthor(), pfxMessage.getBody());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printMessage(PFXMessage pfxMessage) {
        messages.set(messages.get() + pfxMessage.toString());
    }

    public SimpleStringProperty getMessages() {
        return messages;
    }

}

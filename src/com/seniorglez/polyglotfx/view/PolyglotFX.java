package com.seniorglez.polyglotfx.view;

import com.seniorglez.polyglotfx.conections.MessageListener;
import com.seniorglez.polyglotfx.model.PFXMessage;
import com.seniorglez.polyglotfx.model.PFXMessageBuilder;


import java.io.*;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.stage.Stage;

import org.graalvm.polyglot.*;

public class PolyglotFX extends Application {

    @Override
    public void start(Stage stage) {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        TextField textField = new TextField();
        TextArea textArea = getTextArea();
        VBox vbox = new VBox(textArea,textField);
        vbox.setMargin(textArea, new Insets(10,10,10,10));
        vbox.setMargin(textField, new Insets(0,10,10,10));
        Scene scene = new Scene(vbox, 640, 480);
        stage.setScene(scene);
        ScheduledService<String> service = new MessageListener();
        service.setOnSucceeded((event) -> {
                PFXMessage pfxMessage = PFXMessage.formJson((String) event.getSource().getValue());
                printMessage(pfxMessage, textArea);
                if (stage.isIconified())
                    notifyMessage(pfxMessage);
        });
        service.start();
        stage.show();
    }

    public TextArea getTextArea() {
        TextArea textArea = new TextArea();
        textArea.setPrefHeight(500.0);
        textArea.setEditable(false);
        return textArea;
    }
    
    public static void main(String[] args) {
        launch();
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

    public void printMessage(PFXMessage pfxMessage,TextArea textArea) {
        textArea.setText(textArea.getText() + pfxMessage.toString());
    }
}

package com.seniorglez.polyglotfx;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.text.MessageFormat;

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
        service.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                PFXMessage pfxMessage = parseMessage((String) workerStateEvent.getSource().getValue());
                printMessage(pfxMessage, textArea);
                if (stage.isIconified())
                    notifyMessage(pfxMessage);
            }
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
        textArea.setText(textArea.getText() + formatMessage(pfxMessage));
    }

    public PFXMessage parseMessage(String json) {
        try (Context polyglot = Context.create("js")) {
            Value jsObj = polyglot.eval("js", "JSON.parse('" + json + "')");
            if (jsObj.hasMember("author") && jsObj.hasMember("body")) {
                PFXMessage message = new PFXMessage();
                message.setAuthor(jsObj.getMember("author").asString());
                message.setBody(jsObj.getMember("body").asString());
                return message;
            }
        }
        return null;
    }

    public String formatMessage(PFXMessage pfxMessage) {
        return MessageFormat.format("{0} : {1}\n", pfxMessage.getAuthor(), pfxMessage.getBody());
    }
}

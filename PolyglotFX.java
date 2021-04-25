import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.text.MessageFormat;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
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
                MessagePOJO messagePOJO = parseMessage((String) workerStateEvent.getSource().getValue());
                printMessage(messagePOJO, textArea);
                if (stage.isIconified())
                    notifyMessage(messagePOJO);
            }
        });
        service.start();
        stage.show();
    }

    public TextArea getTextArea(){
        TextArea textArea = new TextArea();
        textArea.setPrefHeight(500.0);
        textArea.setEditable(false);
        return textArea;
    }
    
    public static void main(String[] args) {
        launch();
    }

    public void notifyMessage(MessagePOJO messagePOJO) {
        try (Context polyglot = Context.newBuilder().allowAllAccess(true).build()) {
            File file = new File("notifier");
            Source source = Source.newBuilder("llvm", file).build();
            Value cpart = polyglot.eval(source);
            if (cpart.canInvokeMember("notify_message"))
                cpart.invokeMember("notify_message", messagePOJO.getAuthor(), messagePOJO.getBody());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printMessage(MessagePOJO messagePOJO,TextArea textArea) {
        textArea.setText(textArea.getText() + formatMessage(messagePOJO));
    }

    public MessagePOJO parseMessage(String json) {
        try (Context polyglot = Context.create("js")) {
            Value jsObj = polyglot.eval("js", "JSON.parse('" + json + "')");
            if (jsObj.hasMember("author") && jsObj.hasMember("body")) {
                MessagePOJO message = new MessagePOJO();
                message.setAuthor(jsObj.getMember("author").asString());
                message.setBody(jsObj.getMember("body").asString());
                return message;
            }
        }
        return null;
    }

    public String formatMessage(MessagePOJO messagePOJO) {
        return MessageFormat.format("{0} : {1}\n", messagePOJO.getAuthor(), messagePOJO.getBody());
    }

    class MessageListener extends ScheduledService<String> {

        @Override
        protected Task<String> createTask() {

            return new Task<String>() {
                @Override
                protected String call() throws Exception {
                    /*
                     * You can make any type of connection in this method without having to handle
                     * any type of exception, if this method throws an exception the service will
                     * call it again. I'm just going to pretend that messages are arriving.
                     */
                    Thread.sleep(10000);
                    return "{\"author\": \"Diego\",\"body\": \"Hi m8s\"}";
                }
            };
        }
    }

    class MessagePOJO {
        private String author, body;

        public String getAuthor() {
            return this.author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getBody() {
            return this.body;
        }

        public void setBody(String body) {
            this.body = body;
        }

    }
}
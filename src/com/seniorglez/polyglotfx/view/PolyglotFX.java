package com.seniorglez.polyglotfx.view;

import com.seniorglez.polyglotfx.connections.MessageListener;
import com.seniorglez.polyglotfx.model.PFXMessage;

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

public class PolyglotFX extends Application {

    private TextField textField;
    private TextArea textArea;
    private PolyglotFXModel viewModel;

    @Override
    public void start(Stage stage) {
        viewModel = new PolyglotFXModel();
        textField = new TextField();
        TextArea textArea = getTextArea(viewModel);
        VBox vbox = new VBox(textArea,textField);
        vbox.setMargin(textArea, new Insets(10,10,10,10));
        vbox.setMargin(textField, new Insets(0,10,10,10));
        Scene scene = new Scene(vbox, 640, 480);
        stage.setScene(scene);
        ScheduledService<String> service = new MessageListener();
        service.setOnSucceeded((event) -> {
                PFXMessage pfxMessage = PFXMessage.formJson((String) event.getSource().getValue());
                viewModel.printMessage(pfxMessage);
                if (stage.isIconified()) viewModel.notifyMessage(pfxMessage);
        });
        service.start();
        stage.show();
    }

    public TextArea getTextArea(PolyglotFXModel viewModel) {
        TextArea textArea = new TextArea();
        textArea.setPrefHeight(500.0);
        textArea.setEditable(false);
        textArea.textProperty().bind(viewModel.getMessages());
        return textArea;
    }
    
    public static void main(String[] args) {
        launch();
    }
}

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.graalvm.polyglot.*;

public class HelloFX extends Application {

    @Override
    public void start(Stage stage) {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        Label l = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        Scene scene = new Scene(new StackPane(l), 640, 480);
        stage.setScene(scene);
        stage.show();
        ScheduledService<String> service = new MessageListener();
        service.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                if(stage.isIconified())notifyMessage();
                System.out.println((String) workerStateEvent.getSource().getValue());
            }
        });
        service.start();
    }

    public static void main(String[] args) {
        launch();
    }

    public void notifyMessage() {
        try(Context polyglot = Context.newBuilder()
                               .allowAllAccess(true)
                               .build())
        {
            File file = new File("notifier");
            Source source = Source.newBuilder("llvm", file).build();
            Value cpart = polyglot.eval(source);
            String name = "Diego";
            String message = "Hi m8s";
                                
            if(cpart.canInvokeMember("notify_message")) cpart.invokeMember("notify_message",name, message);
        } catch(IOException e) {
            e.printStackTrace();
        }
        
    }

    class MessageListener extends ScheduledService<String> {

        @Override
        protected Task<String> createTask() {
            
            return new Task<String>() {
                @Override
                protected String call() throws Exception {
                    /*
                    You can make any type of connection in this method without having to handle any type of exception,
                    if this method throws an exception the service will call it again. I'm just going to pretend that messages are arriving.
                    */
                    Thread.sleep(10000);
                    return "hola";
                }
            };
        }
    }
}
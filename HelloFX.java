import java.io.*;

import org.graalvm.polyglot.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class HelloFX extends Application {

    @Override
    public void start(Stage stage) {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        Label l = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        Scene scene = new Scene(new StackPane(l), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws IOException {
	    Context polyglot = Context.newBuilder()
                               .allowAllAccess(true)
                               .build();
        File file = new File("notifier");
        Source source = Source.newBuilder("llvm", file).build();
	    Value cpart = polyglot.eval(source);
        String name = "Diego";
        String message = "Hi m8s";
        
        if(cpart.canInvokeMember("notify_message")){
            cpart.invokeMember("notify_message",name, message);
        };
        launch();
    }
}

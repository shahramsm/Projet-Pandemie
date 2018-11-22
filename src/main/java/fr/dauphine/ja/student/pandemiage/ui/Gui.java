package fr.dauphine.ja.student.pandemiage.ui;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;


/**
 * Stupid empty scene in JavaFX
 * 
 *
 */
public class Gui extends Application{
    @Override
    public void start(Stage stage) {    
        stage.setTitle("Pandemiage");
    	
        Button b = new Button("OK");
        b.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.exit(0);
            }
        });
        
        StackPane p = new StackPane(b);        
        Scene scene = new Scene(p, 640, 480);        
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
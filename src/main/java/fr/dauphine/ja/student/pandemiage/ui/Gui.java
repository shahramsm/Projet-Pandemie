package fr.dauphine.ja.student.pandemiage.ui;
import java.util.ArrayList;

import javax.imageio.ImageIO;

//import com.sun.javafx.geom.Circle;
import com.sun.prism.paint.LinearGradient;
import com.sun.prism.paint.Stop;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.*;
import javafx.scene.Node;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import fr.dauphine.ja.student.pandemiage.gameengine.City;
import fr.dauphine.ja.student.pandemiage.gameengine.GameEngine;
import javafx.application.Application; 
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.QuadCurveTo;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;




/**
 * Stupid empty scene in JavaFX
 * 
 *
 */
public class Gui extends Application{
	// 48 cities !
    private static final String cityGraphFilename = "./pandemic.graphml";
	@Override
    public void start(Stage stage) {    
        // Title's stage
		stage.setTitle("Pandemiage");
		
        
		// New GameEngine
        GameEngine ge = new GameEngine(cityGraphFilename, "aiJar");
        ArrayList<City> cities = new ArrayList<City>();
        
        // Get all Cities of ge
        cities = ge.getAllCity();

        ArrayList<Circle> circles = new ArrayList();
        // Transform cities to circles
        circles = createCircles(cities);

        Group root = new Group();
        Scene s = new Scene(root, 1000,600);
        
        
        // Background pandemic
        final ImageView selectedImage = new ImageView();   
        Image image = new Image(Gui.class.getResourceAsStream("image3.jpg"));
        
        selectedImage.setImage(image);
        selectedImage.setFitWidth(1000);
        selectedImage.setFitHeight(600);
        root.getChildren().addAll(selectedImage);
        
        
      
        // Adding circles to the Group
        for(int i =0; i< circles.size();i++) {
    	   root.getChildren().add(circles.get(i));

        }
        
        stage.setScene(s);
        stage.show();
        
        
        }
        
 
	/*
	 *  This method transform a city to circle
	 *  One city by one
	 *  By getting the X and Y from GameEngine 
	 *  
	 */
    

    public static ArrayList<Circle> createCircles(ArrayList<City> cities) {
    	ArrayList<Circle> circles = new ArrayList<>();
    	
    
    	for(City c : cities) {
    		Circle circle = new Circle();
        	circle.setRadius(10);
    		String d = "RED";
    		switch (d) {
    		case "RED" : 
    			circle.setFill(Color.RED);
    			break;
    		case "BLUE":
    			circle.setFill(Color.BLUE);
    			break;
    		case "YELLOW": 
    			circle.setFill(Color.YELLOW);
    			break;
    		case "BLACK" : 
    			circle.setFill(Color.BLACK);
    			break;
    		}
    		if(Double.parseDouble(c.getX())>800) {
    			circle.setCenterX(Math.abs(Double.parseDouble(c.getX()))-200);
        		circle.setCenterY(Math.abs(Double.parseDouble(c.getY())/2));
        		circles.add(circle);
        		System.out.println(circle);
    			
    		}
    		else {
    			circle.setCenterX(Math.abs(Double.parseDouble(c.getX())));
        		circle.setCenterY(Math.abs(Double.parseDouble(c.getY())/2));
        		circles.add(circle);
        		System.out.println(circle);
    		}
    	}
   
    	return circles;
    	  	
    }
    public static void main(String[] args) {
       launch();
    	
    }
}
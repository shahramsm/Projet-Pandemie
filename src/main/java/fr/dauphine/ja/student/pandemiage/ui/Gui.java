//package save;

package fr.dauphine.ja.student.pandemiage.ui;
import java.util.ArrayList;
import java.awt.*; 

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
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import fr.dauphine.ja.student.pandemiage.gameengine.City;
import fr.dauphine.ja.student.pandemiage.gameengine.GameEngine;
import javafx.application.Application; 
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
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

        ArrayList<Circle> circles = new ArrayList<>();
        // Transform cities to circles
        circles = createCircles(cities);
        // get the name of the cities
        ArrayList<Text> texts = new ArrayList<>();
        texts = createTexts(cities);

       ArrayList<Line> lines = new ArrayList<>();
        lines = createLines(cities);
        
        Group root = new Group();
        Scene s = new Scene(root, 1300,650);
        
        
        // Background pandemic
        final ImageView imageView = new ImageView();   
        Image image = new Image(Gui.class.getResourceAsStream("image.jpg"));
        
        imageView.setImage(image);
        imageView.setFitWidth(1300);
        imageView.setFitHeight(650);
        root.getChildren().addAll(imageView);
       
      
        // Adding circles to the Group
       for(int i =0; i< circles.size();i++) {
    	   root.getChildren().add(circles.get(i));

        }
        
        for(int i = 0; i< texts.size(); i++) {
        	root.getChildren().add(texts.get(i));
        }
        
       for(int i =0; i<lines.size();i++) {
     	   root.getChildren().add(lines.get(i));

         }
         
       
        stage.setScene(s);
        stage.show();
        
        
        }
        
 
	/*
	 *  This method transform a city to circle
	 *  One city by one
	 *  By getting the X and Y from GameEngine 
	 *  
	 *  @param ArrayList<City> cities
	 *  @return ArrayList<Circle> circles
	 */
	
	
	public static ArrayList<Circle> createCircles(ArrayList<City> cities) {
		ArrayList<Circle> circles = new ArrayList<>();    
    	
		for(City c : cities) {
    			Circle circle = new Circle();
    			circle.setRadius(10);
    			double r = Double.parseDouble(c.getR());
    			//System.out.println(r);
    			double g = Double.parseDouble(c.getG());
    			double b = Double.parseDouble(c.getB());
    			
    			circle.setCenterX((1600+Double.parseDouble(c.getX()))/2.5);
        		circle.setCenterY((1500-Double.parseDouble(c.getY()))/2.5);
        		//Every city with its color
        		Color color = Color.rgb((int) r,(int)  g, (int) b, .99);
        		circle.setFill(color);
        		circles.add(circle);
        		//System.out.println(circle);
    	}
   
    	return circles;
    	  	
    }
	
	/*
	 *  This method get the names of cities
	 *  One city by one
	 *  using getName() method
	 *  
	 *  @param ArrayList<City> cities
	 *  @return ArrayList<Text> texts
	 *  
	 */	

	public static ArrayList<Text> createTexts(ArrayList<City> cities){
		ArrayList<Text> texts = new ArrayList<Text>();
		for(City c : cities) {
			Text t = new Text (c.getName());
			
				t.setX(((1600+Double.parseDouble(c.getX()))/2.5)+7);
				t.setY(((1500-Double.parseDouble(c.getY()))/2.5)+7);
				t.setFill(Color.WHITE);
				texts.add(t);
		}
		
		return texts;
		
	}
	
	public static ArrayList<Line> createLines(ArrayList<City> cities){
		ArrayList<Line> lines = new ArrayList<Line>();
		//Course the cities in param
		ArrayList<String> n = new ArrayList<String>();
		
		
		for(int i = 0; i< cities.size(); i++) {
			ArrayList<String> neighbours = new ArrayList<String>();
			// Getting the neighbours of city i
			neighbours = cities.get(i).getNeighbours();

			//Course the neighbours of city i
			for(int k=0; k< neighbours.size();k++) {
				//Course the city equals to the neighbours k to get the X and Y of the neighbour 
				for(int j=0; j<cities.size();j++) {
					if(neighbours.get(k).equals(cities.get(j).getName())) {
						Line line = new Line();
						line.setStartX((1600+Double.parseDouble(cities.get(i).getX()))/2.5);
						line.setStartY((1500-Double.parseDouble(cities.get(i).getY()))/2.5);
						line.setEndX((1600+Double.parseDouble(cities.get(j).getX()))/2.5);
						line.setEndY((1500-Double.parseDouble(cities.get(j).getY()))/2.5);
						//Adding the X and Y to form a line

						line.setFill(Color.WHITE);
						//System.out.println(line.getFill());
						lines.add(line);
					}
				}
			}
		}
			
		return lines;
		
	}
	
	
	
   //Start launch 

    public static void main(String[] args) {
       launch();
    	
    }
}
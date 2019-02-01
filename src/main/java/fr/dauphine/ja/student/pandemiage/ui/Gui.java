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
import javafx.scene.shape.Rectangle;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.*;
import javafx.scene.Node;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import fr.dauphine.ja.student.pandemiage.gameengine.City;
import fr.dauphine.ja.student.pandemiage.gameengine.GameEngine;
import javafx.application.Application; 
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;





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
        // Get the name of the cities
        ArrayList<Text> texts = new ArrayList<>();
        texts = createTexts(cities);
        // Add neighbours cities
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
        // Adding city's name
        for(int i = 0; i< texts.size(); i++) {
        	root.getChildren().add(texts.get(i));
        }
        // Adding city's neighbours
       for(int i =0; i<lines.size();i++) {
     	   root.getChildren().add(lines.get(i));

         }
       
       Label l = new Label("Pandemic");
       //root.getChildren().add(l1);
     /*  final ImageView imageView2 = new ImageView();   
       Image image2 = new Image(Gui.class.getResourceAsStream("pandemic.png"));
       
       imageView2.setImage(image2);
       imageView2.setFitWidth(300);
       imageView2.setFitHeight(200);
       root.getChildren().addAll(imageView2);
   */
       
      //Text Pandemic game
       Text t = new Text();
       t.setX(200);
       t.setY(-300);
       t.setCache(true);
       t.setText("PANDEMIC");
       t.setFill(Color.RED);
       t.setFont(Font.font("Verdana", FontWeight.BOLD,FontPosture.ITALIC, 70));
       Reflection r = new Reflection();
       r.setFraction(0.7f);
       t.setEffect(r);
       t.setTranslateY(400);
       root.getChildren().add(t);
       
       Label l2 = new Label("Text-Only Label");
       
       //root.getChildren().addAll(l2);
       // Left side design
       Image i = new Image(getClass().getResourceAsStream("infectionRate.png"));
       Label label3 = new Label("", new ImageView(i));
       label3.setTranslateX(20);//Positionner
       label3.setTranslateY(200);//Positionner
       label3.setScaleX(.75);//Reduire taille
       label3.setScaleY(.75);//Reduire taille
       //label3.resize(500,500);
       root.getChildren().add(label3);

       //Circle 1
       Circle c1 = new Circle(90,280,25,Color.TRANSPARENT);
       c1.setFill(Color.rgb(200, 200, 200, 0.2));
       Text t1 = new Text(82,287,"1");
       t1.setFill(Color.WHITE);
       t1.setStyle("-fx-font: 24 arial;");
       root.getChildren().add(c1);
       root.getChildren().add(t1);
       
       //Circle 2
       Circle c2 = new Circle(53,325,25,Color.TRANSPARENT);
       c2.setFill(Color.rgb(200, 200, 200, 0.2));
       Text t2 = new Text(47,333,"2");
       t2.setFill(Color.WHITE);
       t2.setStyle("-fx-font: 24 arial;");
       root.getChildren().add(c2);
       root.getChildren().add(t2);
       
       //Circle 3
       Circle c3 = new Circle(90,370,25,Color.TRANSPARENT);
       c3.setFill(Color.rgb(200, 200, 200, 0.2));
       Text t3 = new Text(82,379,"3");
       t3.setFill(Color.WHITE);
       t3.setStyle("-fx-font: 24 arial;");
       root.getChildren().add(c3);
       root.getChildren().add(t3);
       
       //Circle 4
       Circle c4 = new Circle(53,415,25,Color.TRANSPARENT);
       c4.setFill(Color.rgb(200, 200, 200, 0.2));
       Text t4 = new Text(47,425,"4");
       t4.setFill(Color.WHITE);
       t4.setStyle("-fx-font: 24 arial;");
       root.getChildren().add(c4);
       root.getChildren().add(t4);
       
       //Circle 5
       Circle c5 = new Circle(90,460,25,Color.TRANSPARENT);
       c5.setFill(Color.rgb(200, 200, 200, 0.2));
       Text t5 = new Text(82,471,"5");
       t5.setFill(Color.WHITE);
       t5.setStyle("-fx-font: 24 arial;");
       root.getChildren().add(c5);
       root.getChildren().add(t5);
       
       //Circle 6
       Circle c6 = new Circle(53,505,25,Color.TRANSPARENT);
       c6.setFill(Color.rgb(200, 200, 200, 0.2));
       Text t6 = new Text(47,517,"6");
       t6.setFill(Color.WHITE);
       t6.setStyle("-fx-font: 24 arial;");
       root.getChildren().add(c6);
       root.getChildren().add(t6);
       
       //Circle 7
       Circle c7 = new Circle(90,550,25,Color.TRANSPARENT);
       c7.setFill(Color.rgb(200, 200, 200, 0.2));
       Text t7 = new Text(82,563,"7");
       t7.setFill(Color.WHITE);
       t7.setStyle("-fx-font: 24 arial;");
       root.getChildren().add(c7);
       root.getChildren().add(t7);
       
     
       //Circle 8
       Circle c8 = new Circle(53,595,25,Color.TRANSPARENT);
       c8.setFill(Color.rgb(200, 200, 200, 0.2));
       Text t8 = new Text(47,609,"");
       t8.setFill(Color.WHITE);
       t8.setStyle("-fx-font: 24 arial;");
       root.getChildren().add(c8);
       root.getChildren().add(t8);
       
       // Final Image left side design teteMort
       // Dangerous
       final ImageView imageView2 = new ImageView();   
       Image image2 = new Image(Gui.class.getResourceAsStream("teteMort.png"));
       
       imageView2.setImage(image2);
       imageView2.setFitWidth(45);
       imageView2.setFitHeight(45);
       imageView2.setX(32);
       imageView2.setY(570);    
       root.getChildren().addAll(imageView2);
       
       // Rectangle
       Rectangle r1 = new Rectangle();
       r1.setX(15);
       r1.setY(13);
       r1.setWidth(1230);
       r1.setHeight(625);
       r1.setArcWidth(20);
       r1.setArcHeight(20);
       r1.setFill(Color.rgb(200, 200, 200, 0.0));
       r1.setStroke(Color.WHITE);
       root.getChildren().add(r1);
       
    // Rectangle down design
       Rectangle r2 = new Rectangle();
       r2.setX(500);
       r2.setY(573);
       r2.setWidth(250);
       r2.setHeight(65);
       r2.setArcWidth(20);
       r2.setArcHeight(20);
       r2.setFill(Color.rgb(200, 200, 200, 0.0));
       r2.setStroke(Color.WHITE);
       root.getChildren().add(r2);
       
       // YellowCure icon
       final ImageView imageView3 = new ImageView();   
       Image image3 = new Image(Gui.class.getResourceAsStream("yellowCure.png"));
       
       imageView3.setImage(image3);
       imageView3.setFitWidth(45);
       imageView3.setFitHeight(45);
       imageView3.setX(515);
       imageView3.setY(588);    
       root.getChildren().addAll(imageView3);
       
       // RedCure icon
       final ImageView imageView4 = new ImageView();   
       Image image4 = new Image(Gui.class.getResourceAsStream("redCure.png"));
       
       imageView4.setImage(image4);
       imageView4.setFitWidth(45);
       imageView4.setFitHeight(45);
       imageView4.setX(570);
       imageView4.setY(588);    
       root.getChildren().addAll(imageView4);
       
       // BlueCure icon
       final ImageView imageView5 = new ImageView();   
       Image image5 = new Image(Gui.class.getResourceAsStream("blueCure.png"));
       
       imageView5.setImage(image5);
       imageView5.setFitWidth(45);
       imageView5.setFitHeight(45);
       imageView5.setX(625);
       imageView5.setY(588);    
       root.getChildren().addAll(imageView5);
       
       // BlackCure icon
       final ImageView imageView6 = new ImageView();   
       Image image6 = new Image(Gui.class.getResourceAsStream("blackCure.png"));
       
       imageView6.setImage(image6);
       imageView6.setFitWidth(45);
       imageView6.setFitHeight(45);
       imageView6.setX(680);
       imageView6.setY(588);    
       root.getChildren().addAll(imageView6);
       
       
       
       
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
	
	
	/*
	 *  This method draws lines between cities neighbours
	 *  One city by one
	 *  using getNeighbours()
	 *  
	 *  @param ArrayList<City> cities
	 *  @return ArrayList<Line> lines
	 *  
	 */	
	public static ArrayList<Line> createLines(ArrayList<City> cities){
		ArrayList<Line> lines = new ArrayList<Line>();
		
		
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

						/*Color c = new Color(0,0,0,0);
						c = Color.web("#E1E8E7");
						line.setFill(Color.WHITE);
						System.out.println(line.getFill());*/
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
	package fr.dauphine.ja.vong_touahri_mahdavi.pandemiage.ui;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import fr.dauphine.ja.vong_touahri_mahdavi.pandemiage.gameengine.GameEngine;

public class Cli {	    
	public static final String DEFAULT_AIJAR = "./target/pandemiage-vong_touahri_mahdavi-1.0-SNAPSHOT-ai.jar"; 
	public static final String DEFAULT_CITYGRAPH_FILE = "./pandemic.graphml";
	public static final int DEFAULT_TURN_DURATION = 1;	//in seconds
	public static final int DEFAULT_DIFFICULTY = 0; // Normal
	public static final int DEFAULT_HAND_SIZE = 9;
	 
	
	public static void main(String[] args) {
		String aijar = DEFAULT_AIJAR; 
		String cityGraphFile = DEFAULT_CITYGRAPH_FILE; 
		int difficulty = DEFAULT_DIFFICULTY; 
		int turnDuration = DEFAULT_TURN_DURATION;
		int handSize = DEFAULT_HAND_SIZE;
		
		Options options = new Options();
		CommandLineParser parser = new DefaultParser();

		options.addOption("a", "aijar", true, "use <FILE> as player Ai.");
		options.addOption("d", "difficulty", true, "Difficulty level. 0 (Introduction), 1 (Normal) or 3 (Heroic).");
		options.addOption("c", "citygraph", true, "City graph filename.");
		options.addOption("t", "turnduration", true, "Number of seconds allowed to play a turn.");
		options.addOption("s", "handsize", true, "Maximum size of a player hand.");
		options.addOption("h", "help", false, "Display this help");
		options.addOption("g", "gui", false, "Display the GUI");
		
		try {
			CommandLine cmd = parser.parse( options, args);
			
			if(cmd.hasOption("a")) {
				aijar = cmd.getOptionValue("a");				
			}
			
			if(cmd.hasOption("c")) {
				cityGraphFile = cmd.getOptionValue("c");
			}

			if(cmd.hasOption("d")) {
				difficulty = Integer.parseInt(cmd.getOptionValue("d"));
			}
			
			if(cmd.hasOption("t")) {
				turnDuration = Integer.parseInt(cmd.getOptionValue("t"));
			}
			if(cmd.hasOption("s")) {
				handSize = Integer.parseInt(cmd.getOptionValue("s"));
			}

			if(cmd.hasOption("g")) {
				Gui.main(args);
			} 
			
			if(cmd.hasOption("h")) {
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp( "pandemiage", options );
				System.exit(0);
			}			
			
		} catch (ParseException e) {
			System.err.println("Error: invalid command line format.");
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp( "pandemiage", options );
			System.exit(1);
	    }
		
		GameEngine g = new GameEngine(cityGraphFile, aijar);
		g.setTurnDuration(turnDuration);
		g.loop();		
	}
}

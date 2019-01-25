package fr.dauphine.ja.student.pandemiage.gameengine;

import java.util.List;

import fr.dauphine.ja.pandemiage.common.Disease;
import fr.dauphine.ja.pandemiage.common.PlayerCardInterface;
import fr.dauphine.ja.pandemiage.common.PlayerInterface;
import fr.dauphine.ja.pandemiage.common.UnauthorizedActionException;

public class Player implements PlayerInterface {
	
	private String location="";
	private int cpt;
	private GameEngine g;
	
	public Player(GameEngine g){
		this.g = g;
		this.cpt = 4;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public void moveTo(String cityName) {
		if(g.neighbours(location).contains(cityName)){
			this.location = cityName;
			this.cpt-=1;
		}
	}

	@Override
	public void flyTo(String cityName) throws UnauthorizedActionException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void flyToCharter(String cityName) throws UnauthorizedActionException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void skipTurn() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void treatDisease(Disease d) throws UnauthorizedActionException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void discoverCure(List<PlayerCardInterface> cardNames) throws UnauthorizedActionException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String playerLocation() {
		// TODO Auto-generated method stub
		return this.location;
	}

	@Override
	public List<PlayerCardInterface> playerHand() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}

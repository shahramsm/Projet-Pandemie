package fr.dauphine.ja.pandemiage.common;

import java.util.List;

public class Player implements PlayerInterface {
	
	private String location="";
	
	
	

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public void moveTo(String cityName) throws UnauthorizedActionException {
		// TODO Auto-generated method stub
		
		
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

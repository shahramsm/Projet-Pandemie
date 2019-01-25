package fr.dauphine.ja.student.pandemiage.gameengine;

import fr.dauphine.ja.pandemiage.common.Disease;

public class ActionCard extends PlayerCard {
	private String cityName;
	private String action;

	public ActionCard(String cityName, String action) {
		super(cityName);
		this.action = action;
	}
	
	public ActionCard(String action) {
		super();
		this.action = action;
	}
	
	public String getCityName(){
		return this.cityName;
	}

	public String getAction() {
		return action;
	}
}

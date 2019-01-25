package fr.dauphine.ja.student.pandemiage.gameengine;

import fr.dauphine.ja.pandemiage.common.Disease;

public class InfectionCard extends PlayerCard{
	private String cityName;
	private Disease disease;
	

	public InfectionCard(String cityName, Disease disease) {
		super(cityName, disease);
	}

	public String getCityName() {
		return cityName;
	}

	public Disease getDisease() {
		return disease;
	}
	
}

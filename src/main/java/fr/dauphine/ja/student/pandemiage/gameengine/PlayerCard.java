package fr.dauphine.ja.student.pandemiage.gameengine;

import fr.dauphine.ja.pandemiage.common.Disease;
import fr.dauphine.ja.pandemiage.common.PlayerCardInterface;

public class PlayerCard implements PlayerCardInterface {
	private String cityName;
	private Disease disease;
	private String action;

	public PlayerCard(String cityName, String action) {
		this.cityName = cityName;
		this.disease = null;
		this.action = action;
	}

	public PlayerCard(String action) {
		this.cityName = null;
		this.disease = null;
		this.action = action;
	}

	public PlayerCard(Disease disease, String action) {
		this.cityName = null;
		this.disease = disease;
		this.action = action;
	}

	public PlayerCard(String cityName, Disease disease) {
		this.cityName = cityName;
		this.disease = disease;
	}

	@Override
	public String getCityName() {
		return this.cityName;
	}

	@Override
	public Disease getDisease() {
		return this.disease;
	}

	public String getAction() {
		return action;
	}
}

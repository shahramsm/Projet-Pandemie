package fr.dauphine.ja.vong_touahri_mahdavi.pandemiage.gameengine;

import fr.dauphine.ja.pandemiage.common.Disease;
import fr.dauphine.ja.pandemiage.common.PlayerCardInterface;

public class PlayerCard implements PlayerCardInterface {
	private String cityName;
	private Disease disease;

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
}
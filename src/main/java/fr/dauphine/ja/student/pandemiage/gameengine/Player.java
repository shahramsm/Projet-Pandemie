package fr.dauphine.ja.student.pandemiage.gameengine;

import java.util.ArrayList;
import java.util.List;

import fr.dauphine.ja.pandemiage.common.Disease;
import fr.dauphine.ja.pandemiage.common.PlayerCardInterface;
import fr.dauphine.ja.pandemiage.common.PlayerInterface;
import fr.dauphine.ja.pandemiage.common.UnauthorizedActionException;

public class Player implements PlayerInterface {
	private String location = "Atlanta";
	private int cpt;
	private GameEngine g;
	private List<ActionCard> l;

	public Player(GameEngine g, List<ActionCard> l) {
		this.g = g;
		this.cpt = 4;
		this.l = l;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public void moveTo(String cityName) throws UnauthorizedActionException {
		if (this.l.isEmpty() || this.cpt <= 0 || !g.neighbours(location).contains(cityName) || !g.allCityNames().contains(cityName)) {
			throw new UnauthorizedActionException("l'action n'est pas autorisé");
		} else {
			for (int i = 0; i < l.size(); i++) {
				if (!(this.l.get(i).getAction().equals("moveTo"))) {
					throw new UnauthorizedActionException("l'action n'est pas autorisé");
				}
			}
			this.location = cityName;
			this.cpt -= 1;
		}
	}

	@Override
	public void flyTo(String cityName) throws UnauthorizedActionException {
		if (this.l.isEmpty() || this.cpt <= 0 || !g.allCityNames().contains(cityName)) {
			throw new UnauthorizedActionException("l'action n'est pas autorisé");
		} else {
			for (int i = 0; i < l.size(); i++) {
				if (!(this.l.get(i).getAction().equals("flyTo")) && !(this.l.get(i).getCityName().equals(cityName))) {
					throw new UnauthorizedActionException("l'action n'est pas autorisé");
				}
			}
			this.location = cityName;
			this.cpt -= 1;
		}
	}

	@Override
	public void flyToCharter(String cityName) throws UnauthorizedActionException {
		if (this.l.isEmpty() || this.cpt <= 0 || !g.allCityNames().contains(cityName)) {
			throw new UnauthorizedActionException("l'action n'est pas autorisé");
		} else {
			for (int i = 0; i < l.size(); i++) {
				if (!(this.l.get(i).getAction().equals("flyToCharter"))) {
					throw new UnauthorizedActionException("l'action n'est pas autorisé");
				}
			}
			this.location = cityName;
			this.cpt -= 1;
		}
	}

	@Override
	public void skipTurn() {
		cpt -= 1;
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

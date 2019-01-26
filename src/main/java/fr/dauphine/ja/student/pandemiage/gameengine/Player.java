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
					throw new UnauthorizedActionException("vous n'avez pas la carte correspondante");
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
					throw new UnauthorizedActionException("vous n'avez pas la carte correspondante");
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
					throw new UnauthorizedActionException("vous n'avez pas la carte correspondante");
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
		if (this.cpt <= 0) {
			throw new UnauthorizedActionException("l'action n'est pas autorisé");
		} else {
			for (int i = 0; i < l.size(); i++) {
				if (!(this.l.get(i).getAction().equals("treatDisease"))) {
					throw new UnauthorizedActionException("vous n'avez pas la carte correspondante");
				}
			}
			for (int i = 0; i < g.getAllCity().size(); i++) {
				if (g.getAllCity().get(i).getName().equals(location)) {
					switch (d) {
					case BLUE:
						if (g.isBlueDicoverdCure()) {
							g.getAllCity().get(i).setBlue(0);
						} else {
							g.getAllCity().get(i).setBlue(g.getAllCity().get(i).getBlue() - 1);
						}
						break;
					case BLACK:
						if (g.isBlackDicoverdCure()) {
							g.getAllCity().get(i).setBlack(0);
						} else {
							g.getAllCity().get(i).setBlack(g.getAllCity().get(i).getBlack() - 1);
						}
						break;
					case YELLOW:
						if (g.isYellowDicoverdCure()) {
							g.getAllCity().get(i).setYellow(0);
						} else {
							g.getAllCity().get(i).setYellow(g.getAllCity().get(i).getYellow() - 1);
						}
						break;
					case RED:
						if (g.isRedDicoverdCure()) {
							g.getAllCity().get(i).setRed(0);
						} else {
							g.getAllCity().get(i).setRed(g.getAllCity().get(i).getRed() - 1);
						}
						break;
					}
					cpt--;
				}
			}
		}
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
		List<PlayerCardInterface> playerCardList = new ArrayList<PlayerCardInterface>();
		for(int i = 0; i < l.size(); i++) {
			playerCardList.add(l.get(i));
		}
		return playerCardList;
	}

}

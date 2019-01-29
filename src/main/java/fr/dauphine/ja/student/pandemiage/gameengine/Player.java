package fr.dauphine.ja.student.pandemiage.gameengine;

import java.util.ArrayList;
import java.util.List;

import fr.dauphine.ja.pandemiage.common.Disease;
import fr.dauphine.ja.pandemiage.common.GameInterface;
import fr.dauphine.ja.pandemiage.common.PlayerCardInterface;
import fr.dauphine.ja.pandemiage.common.PlayerInterface;
import fr.dauphine.ja.pandemiage.common.UnauthorizedActionException;

public class Player implements PlayerInterface {
	private String location = "Atlanta";
	private int cpt;
	private GameEngine g;
	private List<PlayerCardInterface> l;

	public Player(GameEngine g, List<PlayerCardInterface> l) {
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
		if (this.l.isEmpty() || this.cpt <= 0 || !g.neighbours(location).contains(cityName)
				|| !g.allCityNames().contains(cityName)) {
			throw new UnauthorizedActionException("l'action n'est pas autorisé");
		} else {
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
				if (!(this.l.get(i).getCityName().equals(cityName))) {
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
				if (!(this.l.get(i).getCityName().equals(this.location))) {
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
			for (int i = 0; i < g.getAllCity().size(); i++) {
				if (g.getAllCity().get(i).getName().equals(location)) {
					switch (d) {
					case BLUE:
						if (g.isBlueDicoverdCure()) {
							g.setNbCubeBlue(g.getAllCity().get(i).getBlue() + g.getNbCubeBlue());
							g.getAllCity().get(i).setBlue(0);

						} else {
							g.getAllCity().get(i).setBlue(g.getAllCity().get(i).getBlue() - 1);
							g.setNbCubeBlue(g.getNbCubeBlue() + 1);
						}
						break;
					case BLACK:
						if (g.isBlackDicoverdCure()) {
							g.setNbCubeBlack(g.getAllCity().get(i).getBlack() + g.getNbCubeBlack());
							g.getAllCity().get(i).setBlack(0);
						} else {
							g.getAllCity().get(i).setBlack(g.getAllCity().get(i).getBlack() - 0);
							g.setNbCubeBlack(g.getNbCubeBlack() + 1);
						}
						break;
					case YELLOW:
						if (g.isYellowDicoverdCure()) {
							g.setNbCubeYellow(g.getAllCity().get(i).getYellow() + g.getNbCubeYellow());
							g.getAllCity().get(i).setYellow(0);
						} else {
							g.getAllCity().get(i).setYellow(g.getAllCity().get(i).getYellow() - 1);
							g.setNbCubeYellow(g.getNbCubeYellow() + 1);
						}
						break;
					case RED:
						if (g.isRedDicoverdCure()) {
							g.setNbCubeRed(g.getAllCity().get(i).getRed() + g.getNbCubeRed());
							g.getAllCity().get(i).setRed(0);
						} else {
							g.getAllCity().get(i).setRed(g.getAllCity().get(i).getRed() - 1);
							g.setNbCubeRed(g.getNbCubeRed() + 1);
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
		int nbTreatCardBlue = 0;
		int nbTreatCardYellow = 0;
		int nbTreatCardBlack = 0;
		int nbTreatCardRed = 0;
		Disease d;
		// vérifie le nombre de cartes traitement pour chaque maladie que le
		// joueur
		// possède
		for (int i = 0; i < l.size(); i++) {
			d = cardNames.get(i).getDisease();
			switch (d) {
			case BLUE:
				nbTreatCardBlue++;
				break;
			case YELLOW:
				nbTreatCardYellow++;
				break;
			case BLACK:
				nbTreatCardBlack++;
				break;
			case RED:
				nbTreatCardRed++;
				break;
			}
		}
		// met à jour le status de la découverte d'un remède
		if (nbTreatCardBlue == 5) {
			g.setBlueDiscoveredCure(true);
		} else if (nbTreatCardYellow == 5) {
			g.setYellowDiscoveredCure(true);
		} else if (nbTreatCardBlack == 5) {
			g.setBlackDiscoveredCure(true);
		} else if (nbTreatCardRed == 5) {
			g.setRedDiscoveredCure(true);
		}
		cpt--;
	}
	
	@Override
	public String playerLocation() {
		return this.location;
	}
	

	public void setPlayerHand(List<PlayerCardInterface> l, PlayerCardInterface pc) {
		l.add(pc);
	}

	@Override
	public List<PlayerCardInterface> playerHand() {
		return this.l;
	}

	public void infect(String cityName, Disease d) throws UnauthorizedActionException {
		if (!g.allCityNames().contains(cityName)) {
			throw new UnauthorizedActionException("l'action n'est pas autorisé");
		} else {
			int nbCub;
			switch (d) {
			case BLUE:
				nbCub = 0;
				for (int i = 0; i < g.getAllCity().size(); i++) {
					if (g.getAllCity().get(i).getName().equals(cityName)) {
						nbCub = g.getAllCity().get(i).getBlue();
						if (nbCub == 2) {
							g.setNbEpidemi(g.getNbEpidemi() + 1);
							g.getAllCity().get(i).setBlue(nbCub + 1);
							g.setNbCubeBlue(g.getNbCubeBlue() - 1);
						} else if (nbCub == 3) {
							g.setNbOutbreaks(g.getNbOutbreaks() + 1);
							List<String> neighbours = g.neighbours(cityName);
							if (neighbours.size() > 0) {
								for (int j = 0; j < neighbours.size(); j++) {
									for (int k = 0; k < g.getAllCity().size(); k++) {
										if (g.getAllCity().get(k).getName().equals(neighbours.get(j))) {
											infect(g.getAllCity().get(k).getName(), Disease.BLUE);
										}
									}
								}
							}
						} else {
							g.getAllCity().get(i).setBlue(nbCub + 1);
							g.setNbCubeBlue(g.getNbCubeBlue() - 1);
						}
					}
				}
				break;
			case YELLOW:
				nbCub = 0;
				for (int i = 0; i < g.getAllCity().size(); i++) {
					if (g.getAllCity().get(i).getName().equals(cityName)) {
						nbCub = g.getAllCity().get(i).getYellow();
						if (nbCub == 2) {
							g.setNbEpidemi(g.getNbEpidemi() + 1);
							g.getAllCity().get(i).setYellow(nbCub + 1);
							g.setNbCubeYellow(g.getNbCubeYellow() - 1);
						} else if (nbCub == 3) {
							g.setNbOutbreaks(g.getNbOutbreaks() + 1);
							List<String> neighbours = g.neighbours(cityName);
							if (neighbours.size() > 0) {
								for (int j = 0; j < neighbours.size(); j++) {
									for (int k = 0; k < g.getAllCity().size(); k++) {
										if (g.getAllCity().get(k).getName().equals(neighbours.get(j))) {
											infect(g.getAllCity().get(k).getName(), Disease.YELLOW);
										}
									}
								}
							}
						} else {
							g.getAllCity().get(i).setYellow(nbCub + 1);
							g.setNbCubeYellow(g.getNbCubeYellow() - 1);
						}
					}
				}
				break;
			case BLACK:
				nbCub = 0;
				for (int i = 0; i < g.getAllCity().size(); i++) {
					if (g.getAllCity().get(i).getName().equals(cityName)) {
						nbCub = g.getAllCity().get(i).getBlack();
						if (nbCub == 2) {
							g.setNbEpidemi(g.getNbEpidemi() + 1);
							g.getAllCity().get(i).setBlack(nbCub + 1);
							g.setNbCubeBlack(g.getNbCubeBlack() - 1);
						} else if (nbCub == 3) {
							g.setNbOutbreaks(g.getNbOutbreaks() + 1);
							List<String> neighbours = g.neighbours(cityName);
							if (neighbours.size() > 0) {
								for (int j = 0; j < neighbours.size(); j++) {
									for (int k = 0; k < g.getAllCity().size(); k++) {
										if (g.getAllCity().get(k).getName().equals(neighbours.get(j))) {
											infect(g.getAllCity().get(k).getName(), Disease.BLACK);
										}
									}
								}
							}
						} else {
							g.getAllCity().get(i).setBlack(nbCub + 1);
							g.setNbCubeBlack(g.getNbCubeBlack() - 1);
						}
					}
				}
				break;
			case RED:
				nbCub = 0;
				for (int i = 0; i < g.getAllCity().size(); i++) {
					if (g.getAllCity().get(i).getName().equals(cityName)) {
						nbCub = g.getAllCity().get(i).getRed();
						if (nbCub == 2) {
							g.setNbEpidemi(g.getNbEpidemi() + 1);
							g.getAllCity().get(i).setRed(nbCub + 1);
							g.setNbCubeRed(g.getNbCubeRed() - 1);
						} else if (nbCub == 3) {
							g.setNbOutbreaks(g.getNbOutbreaks() + 1);
							List<String> neighbours = g.neighbours(cityName);
							if (neighbours.size() > 0) {
								for (int j = 0; j < neighbours.size(); j++) {
									for (int k = 0; k < g.getAllCity().size(); k++) {
										if (g.getAllCity().get(k).getName().equals(neighbours.get(j))) {
											infect(g.getAllCity().get(k).getName(), Disease.RED);
										}
									}
								}
							}
						} else {
							g.getAllCity().get(i).setRed(nbCub + 1);
							g.setNbCubeRed(g.getNbCubeRed() - 1);
						}
					}
				}
				break;
			}
		}
	}

	/*public List<InfectionCard> discard() {
		List<InfectionCard> discard = new ArrayList<>();
		for (int i = 0; i < g.infectionRate(); i++) {
			discard.add(g.getInfectionCard());
		}
		return discard;
	}*/
}
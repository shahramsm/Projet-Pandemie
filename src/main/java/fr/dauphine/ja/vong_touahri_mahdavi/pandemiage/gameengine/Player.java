package fr.dauphine.ja.vong_touahri_mahdavi.pandemiage.gameengine;

import java.util.ArrayList;
import java.util.List;

import fr.dauphine.ja.pandemiage.common.DefeatReason;
import fr.dauphine.ja.pandemiage.common.Disease;
import fr.dauphine.ja.pandemiage.common.GameInterface;
import fr.dauphine.ja.pandemiage.common.PlayerCardInterface;
import fr.dauphine.ja.pandemiage.common.PlayerInterface;
import fr.dauphine.ja.pandemiage.common.UnauthorizedActionException;

public class Player implements PlayerInterface {
	private String location = "Atlanta";
	private int cpt;
	private GameEngine g;
	private List<PlayerCardInterface> playerHand;

	public Player(GameEngine g, List<PlayerCardInterface> playerHand) {
		this.g = g;
		this.cpt = 4;
		this.playerHand = playerHand;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getCpt() {
		return cpt;
	}

	public void setCpt(int cpt) {
		this.cpt = cpt;
	}

	@Override
	public void moveTo(String cityName) throws UnauthorizedActionException {
		if (this.cpt <= 0) {
			throw new UnauthorizedActionException("Plus d'actions restantes.");
		} else if (!g.neighbours(location).contains(cityName)) {
			throw new UnauthorizedActionException("La ville destination n'est pas voisine.");
		} else if (!g.allCityNames().contains(cityName)) {
			throw new UnauthorizedActionException("La ville destination ne fait pas parti de la carte");
		} else {
			System.out.println("Move from " + this.location + " to " + cityName);
			this.location = cityName;
			this.cpt -= 1;
		}
	}

	@Override
	public void flyTo(String cityName) throws UnauthorizedActionException {
		if (this.playerHand.size() == 0 && this.cpt > 0) {
			throw new UnauthorizedActionException("Plus de carte dans la main du joueur.");
		} else if (this.cpt <= 0) {
			throw new UnauthorizedActionException("Plus d'actions restantes.");
		} else if (!g.allCityNames().contains(cityName)) {
			throw new UnauthorizedActionException("La ville destination ne fait pas parti de la carte");
		} else {
			boolean t = true;
			for (int i = 0; i < this.playerHand.size(); i++) {
				if (!this.playerHand.get(i).getCityName().equals(cityName)) {
					t = false;
				} else {
					t = true;
					break;
				}
			}
			if (t == false) {
				throw new UnauthorizedActionException("vous n'avez pas la carte correspondante" + cityName);
			}
			System.out.println("Fly from " + this.location + " to " + cityName);
			this.location = cityName;
			for (int i = 0; i < playerHand.size(); i++) {
				if (this.playerHand.get(i).getCityName().equals(cityName)) {
					this.playerHand().remove(i);
				}
			}
			this.cpt -= 1;
		}
	}

	@Override
	public void flyToCharter(String cityName) throws UnauthorizedActionException {
		if (this.playerHand.isEmpty()) {
			throw new UnauthorizedActionException("Plus de carte dans la main du joueur.");
		} else if (this.cpt <= 0) {
			throw new UnauthorizedActionException("Plus d'actions restantes.");
		} else if (!g.allCityNames().contains(cityName)) {
			throw new UnauthorizedActionException("La ville destination ne fait pas parti de la carte");
		} else {
			boolean t = true;
			for (int i = 0; i < this.playerHand.size(); i++) {
				if (!this.playerHand.get(i).getCityName().equals(this.location)) {
					t = false;
				} else {
					t = true;
					break;
				}
			}
			if (t == false) {
				throw new UnauthorizedActionException("vous n'avez pas la carte correspondante" + cityName);
			}
			System.out.println("Fly Charter from " + this.location + " to " + cityName);
			for (int i = 0; i < playerHand.size(); i++) {
				if (this.playerHand.get(i).getCityName().equals(this.location)) {
					this.playerHand().remove(i);
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
			throw new UnauthorizedActionException("plus d'actions restantes");
		} else {
			City city = getCityFromCityList(this.location, g.getAllCity());
			if (g.isBlueDicoverdCure()) {
				if (city.getBlue() == 3) {
					g.setNbEpidemi(g.getNbEpidemi() - 1);
				}
				g.setNbCubeColor(city.getNbColor(d) + city.getNbColor(d), d);
				city.setNbColor(0, d);
				g.setColorCured(d, true);
				g.isEradicated(d);
				System.out.println("Treat the disease " + d);
			}
			if (city.getBlue() == 3) {
				g.setNbEpidemi(g.getNbEpidemi() - 1);
				g.setNbCubeColor(g.getNbCubeColor(d) + 1, d);
				city.setNbColor(city.getNbColor(d) - 1, d);
			} else {
				city.setNbColor(city.getNbColor(d) - 1, d);
				g.setNbCubeColor(g.getNbCubeColor(d) + 1, d);
				System.out.println("Treat the disease " + d);
			}
			cpt--;
		}
	}

	@Override
	public void discoverCure(List<PlayerCardInterface> cardNames) throws UnauthorizedActionException {
		if (cardNames.size() < 5) {
			throw new UnauthorizedActionException("la main du joueur est incomplète.");
		} else if (this.cpt <= 0) {
			throw new UnauthorizedActionException("plus d'actions restantes");
		}
		int nbTreatCardBlue = 0;
		int nbTreatCardYellow = 0;
		int nbTreatCardBlack = 0;
		int nbTreatCardRed = 0;
		Disease d;
		// vérifie le nombre de cartes traitement pour chaque maladie que le
		// joueur
		// possède
		for (int i = 0; i < cardNames.size(); i++) {
			d = cardNames.get(i).getDisease();
			if (!(cardNames.get(i) instanceof PlayerCard)) {
				throw new UnauthorizedActionException("le joueur n'a pas la bonne carte");
			} else if (d != Disease.BLACK && d != Disease.YELLOW && d != Disease.RED && d != Disease.BLUE) {
				throw new UnauthorizedActionException("la carte n'est pas de bonne couleur");
			}
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

	public void setPlayerHand(List<PlayerCardInterface> playerHand, PlayerCardInterface pc) {
		playerHand.add(pc);
	}

	@Override
	public List<PlayerCardInterface> playerHand() {
		return this.playerHand;
	}

	// methode pour récupérer une ville avec ses informations
	private City getCityFromCityList(String location, List<City> cityList) {
		City city = null;
		for (int i = 0; i < cityList.size(); i++) {
			if (g.getAllCity().get(i).getName().equals(location)) {
				city = g.getAllCity().get(i);
				break;
			}
		}
		return city;
	}

	// methode pour récupérer une ville avec ses informations
	private void infectBis(Disease d, City city) throws UnauthorizedActionException {
		if (!g.allCityNames().contains(location)) {
			throw new UnauthorizedActionException("La ville n'est pas dans la carte.");
		} else {
			if (g.getNbOutbreaks() < 8) {
				if (g.getNbCubeColor(d) > 0) {
					int nbCube = 0;
					if (!g.isEradicated(Disease.BLUE) && !g.isEradicated(Disease.BLACK)
							&& !g.isEradicated(Disease.YELLOW) && !g.isEradicated(Disease.RED)) {
						nbCube = city.getNbColor(d);
						if (nbCube == 2) {
							g.setNbEpidemi(g.getNbEpidemi() + 1);
							city.setNbColor(nbCube + 1, d);
							g.setNbCubeColor(g.getNbCubeColor(d) - 1, d);
						} else if (nbCube < 2) {

							city.setNbColor(nbCube + 1, d);
							g.setNbCubeColor(g.getNbCubeColor(d) - 1, d);
						}
					}
				}
			}
		}
	}

	public void infect(String cityName, Disease d) throws UnauthorizedActionException {
		if (!g.allCityNames().contains(cityName)) {
			throw new UnauthorizedActionException("La ville n'est pas dans la carte.");
		} else {
			if (g.getNbOutbreaks() < 8) {
				City city = getCityFromCityList(cityName, g.getAllCity());
				int nbCube = city.getNbColor(d);
				if (nbCube == 3) {
					List<String> cityOutbreak = new ArrayList();
					List<String> neighbours = new ArrayList();
					cityOutbreak.add(cityName);
					if (!g.isEradicated(d)) {
						g.setNbOutbreaks(g.getNbOutbreaks() + 1);
						neighbours = g.neighbours(cityName);
						if (neighbours.size() > 0) {
							int compt = 0;
							for (int j = 0; j < neighbours.size(); j++) {
								City cityNeighbour = getCityFromCityList(neighbours.get(j), g.getAllCity());
								// ville j infecte ses voisins k dans la meseure ou nbcure ne dépasse pas 3
								if (!cityOutbreak.contains(neighbours.get(j))) {
									break;
								} else if (cityNeighbour.getNbColor(d) == 3 && compt == 0) {
									cityOutbreak.add(neighbours.get(j));
									// ville j fait foyer
									g.setNbOutbreaks(g.getNbOutbreaks() + 1);
									compt++;
									if (g.getNbCubeColor(d) > 0) {
										if (g.neighbours(neighbours.get(j)).size() > 0) {
											City cityNeighbourBis = getCityFromCityList(cityNeighbour.getName(),
													g.getAllCity());
											infectBis(d, cityNeighbourBis);
										}
									} else if (g.getNbOutbreaks() >= 8) {
										break;
									}
									continue;
								} else {
									infectBis(d, cityNeighbour);
								}
								break;
							}
						}
					}
				} else if (nbCube >= 0 && nbCube <= 2) {
					infectBis(d, city);
				}
			}
		}
	}
}

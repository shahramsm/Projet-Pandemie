package fr.dauphine.ja.student.pandemiage.gameengine;

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
			for (int i = 0; i < g.getAllCity().size(); i++) {
				if (g.getAllCity().get(i).getName().equals(location)) {
					switch (d) {
					case BLUE:
						if(g.getAllCity().get(i).getBlue() == 3) {
							g.setNbEpidemi(g.getNbEpidemi() - 1);
						}
						if (g.isBlueDicoverdCure()) {	
							g.setNbCubeBlue(g.getAllCity().get(i).getBlue() + g.getNbCubeBlue());
							g.getAllCity().get(i).setBlue(0);
							g.setBlueCured(true);
							g.isEradicated(Disease.BLUE);
							System.out.println("Treat the disease " + d);
						} else {
							g.getAllCity().get(i).setBlue(g.getAllCity().get(i).getBlue() - 1);
							g.setNbCubeBlue(g.getNbCubeBlue() + 1);
							System.out.println("Treat the disease " + d);
						}
						break;
					case BLACK:
						if(g.getAllCity().get(i).getBlack() == 3) {
							g.setNbEpidemi(g.getNbEpidemi() - 1);
						}
						if (g.isBlackDicoverdCure()) {
							g.setNbCubeBlack(g.getAllCity().get(i).getBlack() + g.getNbCubeBlack());
							g.getAllCity().get(i).setBlack(0);
							g.setBlackCured(true);
							g.isEradicated(Disease.BLACK);
							System.out.println("Treat the disease " + d);
						} else {
							g.getAllCity().get(i).setBlack(g.getAllCity().get(i).getBlack() - 0);
							g.setNbCubeBlack(g.getNbCubeBlack() + 1);
							System.out.println("Treat the disease " + d);
						}
						break;
					case YELLOW:
						if(g.getAllCity().get(i).getYellow() == 3) {
							g.setNbEpidemi(g.getNbEpidemi() - 1);
						}
						if (g.isYellowDicoverdCure()) {
							g.setNbCubeYellow(g.getAllCity().get(i).getYellow() + g.getNbCubeYellow());
							g.getAllCity().get(i).setYellow(0);
							g.setYellowCured(true);
							g.isEradicated(Disease.YELLOW);
							System.out.println("Treat the disease " + d);
						} else {
							g.getAllCity().get(i).setYellow(g.getAllCity().get(i).getYellow() - 1);
							g.setNbCubeYellow(g.getNbCubeYellow() + 1);
							System.out.println("Treat the disease " + d);
						}
						break;
					case RED:
						if(g.getAllCity().get(i).getRed() == 3) {
							g.setNbEpidemi(g.getNbEpidemi() - 1);
						}
						if (g.isRedDicoverdCure()) {
							g.setNbCubeRed(g.getAllCity().get(i).getRed() + g.getNbCubeRed());
							g.getAllCity().get(i).setRed(0);
							g.setRedCured(true);
							g.isEradicated(Disease.RED);
							System.out.println("Treat the disease " + d);
						} else {
							g.getAllCity().get(i).setRed(g.getAllCity().get(i).getRed() - 1);
							g.setNbCubeRed(g.getNbCubeRed() + 1);
							System.out.println("Treat the disease " + d);
						}
						break;
					}
					cpt--;
				}
			}
		}
	}

//ajout des tests pour l'exception
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
		for (int i = 0; i < cardNames.size(); i++) {
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

	public void setPlayerHand(List<PlayerCardInterface> playerHand, PlayerCardInterface pc) {
		playerHand.add(pc);
	}

	@Override
	public List<PlayerCardInterface> playerHand() {
		return this.playerHand;
	}

	public void infect(String cityName, Disease d) throws UnauthorizedActionException {
		if (!g.allCityNames().contains(cityName)) {
			throw new UnauthorizedActionException("La ville n'est pas dans la carte.");
		} else if (g.isEradicated(d)) {
			throw new UnauthorizedActionException("La maladie " + d + " a été éradiquée.");
		} else {
			int nbCub;
			switch (d) {
			case BLUE:
				if(g.isEradicated(Disease.BLUE)){
					break;
				}
				List<String> cityOutbreak=new ArrayList();
				if (g.getNbOutbreaks() == 8) {
					break;
				}
				nbCub = 0;
				for (int i = 0; i < g.getAllCity().size(); i++) {
					if (g.getAllCity().get(i).getName().equals(cityName)) {
						nbCub = g.getAllCity().get(i).getBlue();
						if (nbCub == 2) {
							g.setNbEpidemi(g.getNbEpidemi() + 1);
							g.getAllCity().get(i).setBlue(nbCub + 1);
							g.setNbCubeBlue(g.getNbCubeBlue() - 1);
							break;
						} else if (nbCub == 3) {
							cityOutbreak.add(cityName);
							//ville i fait foyer
							g.setNbOutbreaks(g.getNbOutbreaks() + 1);
							List<String> neighbours = g.neighbours(cityName);
							if (neighbours.size() > 0) {
								int compt = 0;
								for (int j = 0; j < neighbours.size(); j++) {
									for (int k = 0; k < g.getAllCity().size(); k++) {
										//ville j infecte ses voisins k dans la meseure ou nbcure ne dépasse pas 3
										if (g.getAllCity().get(k).getName().equals(neighbours.get(j)) && !cityOutbreak.contains(neighbours.get(j))) {
											if (g.getAllCity().get(k).getBlue() == 3 && compt == 0) {
												cityOutbreak.add(neighbours.get(j));
												//ville j fait foyer
												g.setNbOutbreaks(g.getNbOutbreaks() + 1);
												compt++;
												if(g.neighbours(neighbours.get(j)).size() > 0){
													for (int l = 0; l < g.neighbours(neighbours.get(j)).size(); l++) {
														for (int m = 0; m < g.getAllCity().size(); m++) {
															if (g.getAllCity().get(m).getName().equals(g.neighbours(neighbours.get(j)).get(l)) && !cityOutbreak.contains(g.neighbours(neighbours.get(j)).get(l))) {
																if (g.getAllCity().get(m).getBlue() == 2) {
																	g.setNbEpidemi(g.getNbEpidemi() + 1);
																	g.getAllCity().get(m).setBlue(nbCub + 1);
																	g.setNbCubeBlue(g.getNbCubeBlue() - 1);
																	continue;
																} else if (g.getAllCity().get(m).getBlue() < 2) {
																	g.getAllCity().get(m).setBlue(nbCub + 1);
																	g.setNbCubeBlue(g.getNbCubeBlue() - 1);
																	continue;
																}
															}
														}
													}
												}
												continue;
											} else if (g.getAllCity().get(k).getBlue() == 2) {
												g.setNbEpidemi(g.getNbEpidemi() + 1);
												g.getAllCity().get(k).setBlue(nbCub + 1);
												g.setNbCubeBlue(g.getNbCubeBlue() - 1);
												continue;
											} else if (g.getAllCity().get(k).getBlue() < 2) {
												g.getAllCity().get(k).setBlue(nbCub + 1);
												g.setNbCubeBlue(g.getNbCubeBlue() - 1);
												continue;
											}
											break;
										}
									}
								}
							} else {
								break;
							}
						} else if (nbCub < 2) {
							g.getAllCity().get(i).setBlue(nbCub + 1);
							g.setNbCubeBlue(g.getNbCubeBlue() - 1);
						}
					}
				}
				break;
			case YELLOW:
				if(g.isEradicated(Disease.YELLOW)){
					break;
				}
				List<String> cityOutbreak2=new ArrayList();
				if (g.getNbOutbreaks() == 8) {
					break;
				}
				nbCub = 0;
				for (int i = 0; i < g.getAllCity().size(); i++) {
					if (g.getAllCity().get(i).getName().equals(cityName)) {
						nbCub = g.getAllCity().get(i).getYellow();
						if (nbCub == 2) {
							g.setNbEpidemi(g.getNbEpidemi() + 1);
							g.getAllCity().get(i).setYellow(nbCub + 1);
							g.setNbCubeYellow(g.getNbCubeYellow() - 1);
							break;
						} else if (nbCub == 3) {
							cityOutbreak2.add(cityName);
							//ville i fait foyer
							g.setNbOutbreaks(g.getNbOutbreaks() + 1);
							List<String> neighbours = g.neighbours(cityName);
							if (neighbours.size() > 0) {
								int compt = 0;
								for (int j = 0; j < neighbours.size(); j++) {
									for (int k = 0; k < g.getAllCity().size(); k++) {
										//ville j infecte ses voisins k dans la meseure ou nbcure ne dépasse pas 3
										if (g.getAllCity().get(k).getName().equals(neighbours.get(j)) && !cityOutbreak2.contains(neighbours.get(j))) {
											if (g.getAllCity().get(k).getYellow() == 3 && compt == 0) {
												cityOutbreak2.add(neighbours.get(j));
												//ville j fait foyer
												g.setNbOutbreaks(g.getNbOutbreaks() + 1);
												compt++;
												if(g.neighbours(neighbours.get(j)).size() > 0){
													for (int l = 0; l < g.neighbours(neighbours.get(j)).size(); l++) {
														for (int m = 0; m < g.getAllCity().size(); m++) {
															if (g.getAllCity().get(m).getName().equals(g.neighbours(neighbours.get(j)).get(l)) && !cityOutbreak2.contains(g.neighbours(neighbours.get(j)).get(l))) {
																if (g.getAllCity().get(m).getYellow() == 2) {
																	g.setNbEpidemi(g.getNbEpidemi() + 1);
																	g.getAllCity().get(m).setYellow(nbCub + 1);
																	g.setNbCubeYellow(g.getNbCubeYellow() - 1);
																	continue;
																} else if (g.getAllCity().get(m).getYellow() < 2) {
																	g.getAllCity().get(m).setYellow(nbCub + 1);
																	g.setNbCubeYellow(g.getNbCubeYellow() - 1);
																	continue;
																}
															}
														}
													}
												}
												continue;
											} else if (g.getAllCity().get(k).getYellow() == 2) {
												g.setNbEpidemi(g.getNbEpidemi() + 1);
												g.getAllCity().get(k).setYellow(nbCub + 1);
												g.setNbCubeYellow(g.getNbCubeYellow() - 1);
												continue;
											} else if (g.getAllCity().get(k).getYellow() < 2) {
												g.getAllCity().get(k).setYellow(nbCub + 1);
												g.setNbCubeYellow(g.getNbCubeYellow() - 1);
												continue;
											}
											break;
										}
									}
								}
							} else {
								break;
							}
						} else if (nbCub < 2) {
							g.getAllCity().get(i).setYellow(nbCub + 1);
							g.setNbCubeYellow(g.getNbCubeYellow() - 1);
						}
					}
				}
				break;
			case BLACK:
				if(g.isEradicated(Disease.BLACK)){
					break;
				}
				List<String> cityOutbreak3=new ArrayList();
				if (g.getNbOutbreaks() == 8) {
					break;
				}
				nbCub = 0;
				for (int i = 0; i < g.getAllCity().size(); i++) {
					if (g.getAllCity().get(i).getName().equals(cityName)) {
						nbCub = g.getAllCity().get(i).getBlack();
						if (nbCub == 2) {
							g.setNbEpidemi(g.getNbEpidemi() + 1);
							g.getAllCity().get(i).setBlack(nbCub + 1);
							g.setNbCubeBlack(g.getNbCubeBlack() - 1);
							break;
						} else if (nbCub == 3) {
							cityOutbreak3.add(cityName);
							//ville i fait foyer
							g.setNbOutbreaks(g.getNbOutbreaks() + 1);
							List<String> neighbours = g.neighbours(cityName);
							if (neighbours.size() > 0) {
								int compt = 0;
								for (int j = 0; j < neighbours.size(); j++) {
									for (int k = 0; k < g.getAllCity().size(); k++) {
										//ville j infecte ses voisins k dans la meseure ou nbcure ne dépasse pas 3
										if (g.getAllCity().get(k).getName().equals(neighbours.get(j)) && !cityOutbreak3.contains(neighbours.get(j))) {
											if (g.getAllCity().get(k).getBlack() == 3 && compt == 0) {
												cityOutbreak3.add(neighbours.get(j));
												//ville j fait foyer
												g.setNbOutbreaks(g.getNbOutbreaks() + 1);
												compt++;
												if(g.neighbours(neighbours.get(j)).size() > 0){
													for (int l = 0; l < g.neighbours(neighbours.get(j)).size(); l++) {
														for (int m = 0; m < g.getAllCity().size(); m++) {
															if (g.getAllCity().get(m).getName().equals(g.neighbours(neighbours.get(j)).get(l)) && !cityOutbreak3.contains(g.neighbours(neighbours.get(j)).get(l))) {
																if (g.getAllCity().get(m).getBlack() == 2) {
																	g.setNbEpidemi(g.getNbEpidemi() + 1);
																	g.getAllCity().get(m).setBlack(nbCub + 1);
																	g.setNbCubeBlack(g.getNbCubeBlack() - 1);
																	continue;
																} else if (g.getAllCity().get(m).getBlack() < 2) {
																	g.getAllCity().get(m).setBlack(nbCub + 1);
																	g.setNbCubeBlack(g.getNbCubeBlack() - 1);
																	continue;
																}
															}
														}
													}
												}
												continue;
											} else if (g.getAllCity().get(k).getBlack() == 2) {
												g.setNbEpidemi(g.getNbEpidemi() + 1);
												g.getAllCity().get(k).setBlack(nbCub + 1);
												g.setNbCubeBlack(g.getNbCubeBlack() - 1);
												continue;
											} else if (g.getAllCity().get(k).getBlack() < 2) {
												g.getAllCity().get(k).setBlack(nbCub + 1);
												g.setNbCubeBlack(g.getNbCubeBlack() - 1);
												continue;
											}
											break;
										}
									}
								}
							} else {
								break;
							}
						} else if (nbCub < 2) {
							g.getAllCity().get(i).setBlack(nbCub + 1);
							g.setNbCubeBlack(g.getNbCubeBlack() - 1);
						}
					}
				}
				break;
			case RED:
				if(g.isEradicated(Disease.RED)){
					break;
				}
				List<String> cityOutbreak4=new ArrayList();
				if (g.getNbOutbreaks() == 8) {
					break;
				}
				nbCub = 0;
				for (int i = 0; i < g.getAllCity().size(); i++) {
					if (g.getAllCity().get(i).getName().equals(cityName)) {
						nbCub = g.getAllCity().get(i).getRed();
						if (nbCub == 2) {
							g.setNbEpidemi(g.getNbEpidemi() + 1);
							g.getAllCity().get(i).setRed(nbCub + 1);
							g.setNbCubeRed(g.getNbCubeRed() - 1);
							break;
						} else if (nbCub == 3) {
							cityOutbreak4.add(cityName);
							//ville i fait foyer
							g.setNbOutbreaks(g.getNbOutbreaks() + 1);
							List<String> neighbours = g.neighbours(cityName);
							if (neighbours.size() > 0) {
								int compt = 0;
								for (int j = 0; j < neighbours.size(); j++) {
									for (int k = 0; k < g.getAllCity().size(); k++) {
										//ville j infecte ses voisins k dans la meseure ou nbcure ne dépasse pas 3
										if (g.getAllCity().get(k).getName().equals(neighbours.get(j)) && !cityOutbreak4.contains(neighbours.get(j))) {
											if (g.getAllCity().get(k).getRed() == 3 && compt == 0) {
												cityOutbreak4.add(neighbours.get(j));
												//ville j fait foyer
												g.setNbOutbreaks(g.getNbOutbreaks() + 1);
												compt++;
												if(g.neighbours(neighbours.get(j)).size() > 0){
													for (int l = 0; l < g.neighbours(neighbours.get(j)).size(); l++) {
														for (int m = 0; m < g.getAllCity().size(); m++) {
															if (g.getAllCity().get(m).getName().equals(g.neighbours(neighbours.get(j)).get(l)) && !cityOutbreak4.contains(g.neighbours(neighbours.get(j)).get(l))) {
																if (g.getAllCity().get(m).getRed() == 2) {
																	g.setNbEpidemi(g.getNbEpidemi() + 1);
																	g.getAllCity().get(m).setRed(nbCub + 1);
																	g.setNbCubeRed(g.getNbCubeRed() - 1);
																	continue;
																} else if (g.getAllCity().get(m).getRed() < 2) {
																	g.getAllCity().get(m).setRed(nbCub + 1);
																	g.setNbCubeRed(g.getNbCubeRed() - 1);
																	continue;
																}
															}
														}
													}
												}
												continue;
											} else if (g.getAllCity().get(k).getRed() == 2) {
												g.setNbEpidemi(g.getNbEpidemi() + 1);
												g.getAllCity().get(k).setRed(nbCub + 1);
												g.setNbCubeRed(g.getNbCubeRed() - 1);
												continue;
											} else if (g.getAllCity().get(k).getRed() < 2) {
												g.getAllCity().get(k).setRed(nbCub + 1);
												g.setNbCubeRed(g.getNbCubeRed() - 1);
												continue;
											}
											break;
										}
									}
								}
							} else {
								break;
							}
						} else if (nbCub < 2) {
							g.getAllCity().get(i).setRed(nbCub + 1);
							g.setNbCubeRed(g.getNbCubeRed() - 1);
						}
					}
				}
				break;
			}
		}
	}
}
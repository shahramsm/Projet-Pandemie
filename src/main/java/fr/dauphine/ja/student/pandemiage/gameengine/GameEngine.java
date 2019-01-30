package fr.dauphine.ja.student.pandemiage.gameengine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.Color;
import java.io.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;

import fr.dauphine.ja.pandemiage.common.AiInterface;
import fr.dauphine.ja.pandemiage.common.AiLoader;
import fr.dauphine.ja.pandemiage.common.DefeatReason;
import fr.dauphine.ja.pandemiage.common.Disease;
import fr.dauphine.ja.pandemiage.common.GameInterface;
import fr.dauphine.ja.pandemiage.common.GameStatus;
import fr.dauphine.ja.pandemiage.common.PlayerCardInterface;
import fr.dauphine.ja.pandemiage.common.PlayerInterface;
import fr.dauphine.ja.pandemiage.common.UnauthorizedActionException;

/**
 * Empty GameEngine implementing GameInterface
 *
 */
public class GameEngine implements GameInterface {

	private final String aiJar;
	private final String cityGraphFilename;
	private GameStatus gameStatus;
	private final int maxHandSize = 9;
	private ArrayList<City> allCity;
	private int turnDuration = 1;
	private boolean blueCured = false;
	private boolean yellowCured = false;
	private boolean blackCured = false;
	private boolean redCured = false;
	private int nbCubeRed = 24;
	private int nbCubeBlack = 24;
	private int nbCubeBlue = 24;
	private int nbCubeYellow = 24;
	private boolean blueDiscoveredCure = false;
	private boolean yellowDiscoveredCure = false;
	private boolean blackDiscoveredCure = false;
	private boolean redDiscoveredCure = false;
	private int nbEpidemi = 0;
	private int nbOutbreaks = 0;
	private int nbPlayerCard = 54;
	private List<InfectionCard> infectionCardList;
	private List<PlayerCardInterface> playerCardList;
	private List<InfectionCard> infectionCardListDiscard;
	private List<PlayerCardInterface> playerCardListDiscard;
	private List<PlayerCardInterface> playerHand = new ArrayList();

	// Do not change!
	private void setDefeated(String msg, DefeatReason dr) {
		gameStatus = GameStatus.DEFEATED;
		System.err.println("Player(s) have been defeated: " + msg);
		System.err.println("Result: " + gameStatus);
		System.err.println("Reason: " + dr);
		printGameStats();
		System.exit(2);
	}

	// Do not change!
	private void setVictorious() {
		gameStatus = GameStatus.VICTORIOUS;
		System.err.println("Player(s) have won.");
		System.err.println("Result: " + gameStatus);
		printGameStats();
		System.exit(0);
	}

	// Do not change!
	private void printGameStats() {
		Map<Disease, Integer> blocks = new HashMap<>();
		for (String city : allCityNames()) {
			for (Disease d : Disease.values()) {
				blocks.put(d, blocks.getOrDefault(city, 0) + infectionLevel(city, d));
			}
		}
		System.err.println(blocks);
		System.err.println("Infection-rate:" + infectionRate());
		for (Disease d : Disease.values()) {
			System.err.println("Cured-" + d + ":" + isCured(d));
		}
		System.err.println("Nb-outbreaks:" + getNbOutbreaks());
		System.err.println("Nb-player-cards-left:" + getNbPlayerCardsLeft());
	}

	public GameEngine(String cityGraphFilename, String aiJar) {
		this.cityGraphFilename = cityGraphFilename;
		this.aiJar = aiJar;
		this.gameStatus = GameStatus.ONGOING;
		playerCardList = new ArrayList<PlayerCardInterface>();
		infectionCardList = new ArrayList<InfectionCard>();
		playerCardListDiscard = new ArrayList<PlayerCardInterface>();
		infectionCardListDiscard = new ArrayList<InfectionCard>();

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			Document document = builder.parse(new File(cityGraphFilename));

			document.getDocumentElement().normalize();

			Element root = document.getDocumentElement();

			NodeList nList = document.getElementsByTagName("node");
			ArrayList<City> listState = new ArrayList<City>();
			for (int temp = 0; temp < nList.getLength(); temp++) {
				ArrayList<String> listStateAttribut = new ArrayList<String>();
				Node node = nList.item(temp);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) node;
					listStateAttribut.add(eElement.getAttribute("id"));
					for (int i = 0; i < eElement.getElementsByTagName("data").getLength(); i++) {
						listStateAttribut.add(eElement.getElementsByTagName("data").item(i).getTextContent());
					}
				}
				Disease d = convertColor(listStateAttribut.get(5), listStateAttribut.get(6), listStateAttribut.get(7));
				City c = new City(listStateAttribut.get(0), listStateAttribut.get(1), listStateAttribut.get(2),
						listStateAttribut.get(3), listStateAttribut.get(4), listStateAttribut.get(5),
						listStateAttribut.get(6), listStateAttribut.get(7), listStateAttribut.get(8),
						listStateAttribut.get(9), d);
				listState.add(c);
				PlayerCard playerCard = new PlayerCard(listStateAttribut.get(1), d);
				playerCardList.add(playerCard);
				InfectionCard infectionCard = new InfectionCard(listStateAttribut.get(1), d);
				infectionCardList.add(infectionCard);
			}

			NodeList nList1 = document.getElementsByTagName("edge");
			ArrayList<ArrayList<String>> listStateEdge = new ArrayList<ArrayList<String>>();
			for (int temp = 0; temp < nList1.getLength(); temp++) {
				Node node = nList1.item(temp);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) node;
					String source = eElement.getAttribute("source");
					String target = eElement.getAttribute("target");
					for (int i = 0; i < listState.size(); i++) {
						if (listState.get(i).getId().equals(source)) {
							for (int j = 0; j < listState.size(); j++) {
								if (listState.get(j).getId().equals(target)) {
									listState.get(i).getNeighbours().add(listState.get(j).getName());
								}
							}
						}
					}
				}
			}
			this.allCity = listState;
		} catch (Exception e) {
			System.out.println(e);
		}
		Player p = new Player(this, playerHand);
		Collections.shuffle(playerCardList);
		for (int i = 0; i < 5; i++) {
			playerCardListDiscard.add(playerCardList.get(playerCardList.size() - 1));
			PlayerCardInterface pc = playerCardList.remove(playerCardList.size() - 1);
			p.setPlayerHand(p.playerHand(), pc);
		}
		for (int i = 0; i < 6; i++) {
			PlayerCardInterface pc = new PlayerCard(null, null);
			playerCardList.add(pc);
		}
		Collections.shuffle(playerCardList);
		Collections.shuffle(infectionCardList);
		for (int j = 0; j < 3; j++) {
			infectionCardListDiscard.add(infectionCardList.get(infectionCardList.size() - 1));
			InfectionCard i = infectionCardList.remove(infectionCardList.size() - 1);
			try {
				this.nbEpidemi++;
				for (int k = 0; k < 3; k++) {
					p.infect(i.getCityName(), i.getDisease());
				}
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		for (int j = 0; j < 3; j++) {
			infectionCardListDiscard.add(infectionCardList.get(infectionCardList.size() - 1));
			InfectionCard i = infectionCardList.remove(infectionCardList.size() - 1);
			try {
				for (int k = 0; k < 2; k++) {
					p.infect(i.getCityName(), i.getDisease());
				}
			} catch (Exception e) {
				System.out.println(e);
			}
		}

		for (int j = 0; j < 3; j++) {
			infectionCardListDiscard.add(infectionCardList.get(infectionCardList.size() - 1));
			InfectionCard i = infectionCardList.remove(infectionCardList.size() - 1);
			try {
				p.infect(i.getCityName(), i.getDisease());
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

	public Disease convertColor(String r, String g, String b) {
		Disease d = null;
		if (r.equals("107") && g.equals("112") && b.equals("184")) {
			d = Disease.BLUE;
		}
		if (r.equals("153") && g.equals("153") && b.equals("153")) {
			d = Disease.BLACK;
		}
		if (r.equals("242") && g.equals("255") && b.equals("0")) {
			d = Disease.YELLOW;
		}
		if (r.equals("153") && g.equals("18") && b.equals("21")) {
			d = Disease.RED;
		}
		return d;
	}

	public ArrayList<City> getAllCity() {
		return allCity;
	}

	public void setAllCity(ArrayList<City> allCity) {
		this.allCity = allCity;
	}

	public int getNbCubeRed() {
		return nbCubeRed;
	}

	public void setNbCubeRed(int nbCubeRed) {
		this.nbCubeRed = nbCubeRed;
	}

	public int getNbCubeBlack() {
		return nbCubeBlack;
	}

	public void setNbCubeBlack(int nbCubeBlack) {
		this.nbCubeBlack = nbCubeBlack;
	}

	public int getNbCubeBlue() {
		return nbCubeBlue;
	}

	public void setNbCubeBlue(int nbCubeBlue) {
		this.nbCubeBlue = nbCubeBlue;
	}

	public int getNbCubeYellow() {
		return nbCubeYellow;
	}

	public void setNbCubeYellow(int nbCubeYellow) {
		this.nbCubeYellow = nbCubeYellow;
	}

	public boolean isBlueCured() {
		return blueCured;
	}

	public boolean isBlueDiscoveredCure() {
		return blueDiscoveredCure;
	}

	public void setBlueDiscoveredCure(boolean blueDiscoveredCure) {
		this.blueDiscoveredCure = blueDiscoveredCure;
	}

	public boolean isYellowDiscoveredCure() {
		return yellowDiscoveredCure;
	}

	public void setYellowDiscoveredCure(boolean yellowDiscoveredCure) {
		this.yellowDiscoveredCure = yellowDiscoveredCure;
	}

	public boolean isBlackDiscoveredCure() {
		return blackDiscoveredCure;
	}

	public void setBlackDiscoveredCure(boolean blackDiscoveredCure) {
		this.blackDiscoveredCure = blackDiscoveredCure;
	}

	public boolean isRedDiscoveredCure() {
		return redDiscoveredCure;
	}

	public void setRedDiscoveredCure(boolean redDiscoveredCure) {
		this.redDiscoveredCure = redDiscoveredCure;
	}

	public void setBlueCured(boolean blueCured) {
		this.blueCured = blueCured;
	}

	public boolean isYellowCured() {
		return yellowCured;
	}

	public void setYellowCured(boolean yellowCured) {
		this.yellowCured = yellowCured;
	}

	public boolean isBlackCured() {
		return blackCured;
	}

	public void setBlackCured(boolean blackCured) {
		this.blackCured = blackCured;
	}

	public boolean isRedCured() {
		return redCured;
	}

	public void setRedCured(boolean redCured) {
		this.redCured = redCured;
	}

	public boolean isBlueDicoverdCure() {
		return blueDiscoveredCure;
	}

	public void setBlueDicoverdCure(boolean blueDicoverdCure) {
		this.blueDiscoveredCure = blueDicoverdCure;
	}

	public boolean isBlackDicoverdCure() {
		return blackDiscoveredCure;
	}

	public void setBlackDicoverdCure(boolean blackDicoverdCure) {
		this.blackDiscoveredCure = blackDicoverdCure;
	}

	public boolean isRedDicoverdCure() {
		return redDiscoveredCure;
	}

	public void setRedDicoverdCure(boolean redDicoverdCure) {
		this.redDiscoveredCure = redDicoverdCure;
	}

	public boolean isYellowDicoverdCure() {
		return yellowDiscoveredCure;
	}

	public void setYellowDicoverdCure(boolean yellowDicoverdCure) {
		this.yellowDiscoveredCure = yellowDicoverdCure;
	}

	public int getNbEpidemi() {
		return nbEpidemi;
	}

	public void setNbEpidemi(int nbEpidemi) {
		this.nbEpidemi = nbEpidemi;
	}

	public void setNbOutbreaks(int nbOutbreaks) {
		this.nbOutbreaks = nbOutbreaks;
	}

	public List<InfectionCard> getInfectionCardList() {
		return infectionCardList;
	}

	public void setInfectionCardList(List<InfectionCard> infectionCardList) {
		this.infectionCardList = infectionCardList;
	}

	public List<PlayerCardInterface> getPlayerCardList() {
		return playerCardList;
	}

	public void setPlayerCardList(List<PlayerCardInterface> playerCardList) {
		this.playerCardList = playerCardList;
	}

	public List<InfectionCard> getInfectionCardListDiscard() {
		return infectionCardListDiscard;
	}

	public void setInfectionCardListDiscard(List<InfectionCard> infectionCardListDiscard) {
		this.infectionCardListDiscard = infectionCardListDiscard;
	}

	public List<PlayerCardInterface> getPlayerCardListDiscard() {
		return playerCardListDiscard;
	}

	public void setPlayerCardListDiscard(List<PlayerCardInterface> playerCardListDiscard) {
		this.playerCardListDiscard = playerCardListDiscard;
	}

	public void loop() {
		// Load Ai from Jar file
		System.out.println("Loading AI Jar file " + aiJar);
		AiInterface ai = AiLoader.loadAi(aiJar);
		Player p = new Player(this, playerHand);
		p.setPlayerHand(p.playerHand(), new PlayerCard("Atlanta",Disease.BLUE));

		while (gameStatus == GameStatus.ONGOING) {
			// fait 4 actions
			ai.playTurn(this, p);
			p.setCpt(4);
			// pioche 2 cartes joueur
			for (int i = 0; i < 2; i++) {
				if (playerCardList.size() < 2) {
					setDefeated("Lost game. No more player cards.", DefeatReason.NO_MORE_PLAYER_CARDS);
				} else {
					playerCardListDiscard.add(playerCardList.get(playerCardList.size() - 1));
					PlayerCardInterface pc = playerCardList.remove(playerCardList.size() - 1);
					if (pc.getCityName() == null && pc.getDisease() == null) {
						infectionCardListDiscard.add(infectionCardList.get(infectionCardList.size() - 1));
						String cityInfect = infectionCardList.get(infectionCardList.size() - 1).getCityName();
						InfectionCard ic = infectionCardList.remove(infectionCardList.size() - 1);
						// infecte 3 fois la même ville
						for (int j = 0; j < 3; j++) {
							try {
								p.infect(cityInfect, ic.getDisease());
							} catch (Exception e) {
								System.out.println(e);
							}
						}
						Collections.shuffle(infectionCardListDiscard);
						infectionCardList.addAll(infectionCardListDiscard);
						infectionCardListDiscard.removeAll(infectionCardListDiscard);
					} else {
						p.setPlayerHand(p.playerHand(), pc);
					}
				}
			}
			ai.discard(this, p, this.maxHandSize, this.nbEpidemi);
			// joue le rôle de l'infecteur en fonction du taux d'infection
			for (int i = 0; i < infectionRate(); i++) {
				infectionCardListDiscard.add(infectionCardList.get(infectionCardList.size() - 1));
				String cityInfect = infectionCardList.get(infectionCardList.size() - 1).getCityName();
				InfectionCard ic = infectionCardList.remove(infectionCardList.size() - 1);
				try {
					p.infect(cityInfect, ic.getDisease());
				} catch (Exception e) {
					System.out.println(e);
				}
			}
			if (isBlueDiscoveredCure() == true && isBlackDiscoveredCure() == true && isYellowDiscoveredCure() == true
					&& isRedDiscoveredCure() == true) {
				setVictorious();
			} else if (getNbCubeBlue() == 0 || getNbCubeBlack() == 0 || getNbCubeYellow() == 0 || getNbCubeRed() == 0) {
				setDefeated("Lost game. No more blocks.", DefeatReason.NO_MORE_BLOCKS);
			} else if (getNbOutbreaks() == 8) {
				setDefeated("Lost game. Too many outbreaks.", DefeatReason.TOO_MANY_OUTBREAKS);
			}
		}
	}

	@Override
	public List<String> allCityNames() {
		ArrayList<String> allCityNames = new ArrayList<String>();
		for (int i = 0; i < allCity.size(); i++) {
			allCityNames.add(allCity.get(i).getName());
		}
		return allCityNames;
	}

	@Override
	public List<String> neighbours(String cityName) {
		ArrayList<String> neighbours = new ArrayList<String>();
		for (int i = 0; i < allCity.size(); i++) {
			if (cityName.equals(allCity.get(i).getName())) {
				neighbours = allCity.get(i).getNeighbours();
				for (int j = 0; j < allCity.size(); j++) {
					if (allCity.get(j).getNeighbours().contains(cityName)
							&& (!allCity.get(i).getNeighbours().contains(allCity.get(j).getName()))) {
						allCity.get(i).getNeighbours().add(allCity.get(j).getName());
					}
				}
			}
		}
		return neighbours;
	}

	@Override
	public int infectionLevel(String cityName, Disease d) {
		int infectionLevel = 0;
		for (int i = 0; i < allCity.size(); i++) {
			if (cityName.equals(allCity.get(i).getName())) {
				switch (d) {
				case BLUE:
					infectionLevel = allCity.get(i).getBlue();
					break;
				case YELLOW:
					infectionLevel = allCity.get(i).getYellow();
					break;
				case BLACK:
					infectionLevel = allCity.get(i).getBlack();
					break;
				case RED:
					infectionLevel = allCity.get(i).getRed();
					break;
				}
			}
		}
		return infectionLevel;
	}

	@Override
	public boolean isCured(Disease d) {
		switch (d) {
		case BLUE:
			return this.blueCured;
		case YELLOW:
			return this.yellowCured;
		case BLACK:
			return this.blackCured;
		case RED:
			return this.redCured;
		}
		return false;
	}

	@Override
	public int infectionRate() {
		int infectionRate = 0;
		if (nbEpidemi <= 3 && nbEpidemi > 0) {
			infectionRate = 2;
		} else if (nbEpidemi <= 5 && nbEpidemi > 3) {
			infectionRate = 3;
		} else if (nbEpidemi <= 7 && nbEpidemi > 5) {
			infectionRate = 4;
		} else {
			return 0;
		}
		return infectionRate;
	}

	@Override
	public GameStatus gameStatus() {
		return this.gameStatus;
	}

	public void setTurnDuration(int turnDuration) {
		this.turnDuration = turnDuration;
	}

	@Override
	public int turnDuration() {
		return this.turnDuration;
	}

	@Override
	public boolean isEradicated(Disease d) {
		int il = -1;
		A: for (int i = 0; i < allCity.size(); i++) {
			switch (d) {
			case BLUE:
				// = allCity.get(i).getBlue();
				il = infectionLevel(allCity.get(i).getName(), d);
				break;
			case YELLOW:
				il = infectionLevel(allCity.get(i).getName(), d);
				break;
			case BLACK:
				il = infectionLevel(allCity.get(i).getName(), d);
				break;
			case RED:
				il = infectionLevel(allCity.get(i).getName(), d);
				break;
			}

			if (il == 0 && isCured(d)) // Si le niveau d'infection est nul et
										// qu'il existe un remede dans cette
										// ville
			{
				continue A;
			} else
				return false;
		}

		return true;
	}

	@Override
	public int getNbOutbreaks() {
		return nbOutbreaks;
	}

	@Override
	public int getNbPlayerCardsLeft() {
		return nbPlayerCard;
	}

	public InfectionCard getInfectionCard() {
		InfectionCard infectionCard = this.infectionCardList.get(infectionCardList.size() - 1);
		this.infectionCardList.remove(infectionCardList.size() - 1);
		return infectionCard;
	}

}

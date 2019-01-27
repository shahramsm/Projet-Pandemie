package fr.dauphine.ja.student.pandemiage.gameengine;

import java.util.ArrayList;
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

/**
 * Empty GameEngine implementing GameInterface
 *
 */
public class GameEngine implements GameInterface {

	private final String aiJar;
	private final String cityGraphFilename;
	private GameStatus gameStatus;
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
				City c = new City(listStateAttribut.get(0), listStateAttribut.get(1), listStateAttribut.get(2),
						listStateAttribut.get(3), listStateAttribut.get(4), listStateAttribut.get(5),
						listStateAttribut.get(6), listStateAttribut.get(7), listStateAttribut.get(8),
						listStateAttribut.get(9));
				listState.add(c);
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

	public void loop() {
		// Load Ai from Jar file
		System.out.println("Loading AI Jar file " + aiJar);
		AiInterface ai = AiLoader.loadAi(aiJar);

		// Very basic game loop
		while (gameStatus == GameStatus.ONGOING) {

			if (Math.random() < 0.5)
				setDefeated("Game not implemented.", DefeatReason.UNKN);
			else
				setVictorious();
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

			if (il == 0 && isCured(d)) // Si le niveau d'infection est nul et qu'il existe un remede dans cette ville
			{
				continue A;
			} else
				return false;
		}

		return true;
	}

	@Override
	public int getNbOutbreaks() {
		// TODO
		throw new UnsupportedOperationException();
	}

	@Override
	public int getNbPlayerCardsLeft() {
		// TODO
		throw new UnsupportedOperationException();
	}

}

package fr.dauphine.ja.student.pandemiage.gameengine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private ArrayList<ArrayList<String>> allCity;
	private ArrayList<ArrayList<String>> cityEdge;

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
			ArrayList<ArrayList<String>> listState = new ArrayList<ArrayList<String>>();
			for (int temp = 0; temp < nList.getLength(); temp++) {
				ArrayList<String> listStateAttribut = new ArrayList<String>();
				Node node = nList.item(temp);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) node;
					listStateAttribut.add(eElement.getAttribute("id"));
					for(int i = 0; i < eElement.getElementsByTagName("data").getLength(); i++) {
						listStateAttribut.add(eElement.getElementsByTagName("data").item(i).getTextContent());
					}
				}
				listState.add(listStateAttribut);
			}
			this.allCity = listState;

			NodeList nList1 = document.getElementsByTagName("edge");
			ArrayList<ArrayList<String>> listStateEdge = new ArrayList<ArrayList<String>>();
			for (int temp = 0; temp < nList1.getLength(); temp++) {
				ArrayList<String> listStateEdgeNeightbour = new ArrayList<String>();
				Node node = nList1.item(temp);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) node;
					String source = eElement.getAttribute("source");
					String target = eElement.getAttribute("target");
					for (int i = 0; i < listState.size(); i++) {
						if (listState.get(i).get(0).equals(target)) {
							String state = listState.get(i).get(1);
							listStateEdgeNeightbour.add(source);
							listStateEdgeNeightbour.add(state);
						}
					}
				}
				listStateEdge.add(listStateEdgeNeightbour);
			}
			this.cityEdge = listStateEdge;
		} catch (Exception e) {
			System.out.println(e);
		}

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
			allCityNames.add(allCity.get(i).get(1));
		}
		return allCityNames;
	}

	@Override
	public List<String> neighbours(String cityName) {
		ArrayList<String> neighbours = new ArrayList<String>();
		String j = null;;
		for (int i = 0; i < allCity.size(); i++) {
			if (cityName.equals(allCity.get(i).get(1))) {
				j = allCity.get(i).get(0);
			}
		}
		for (int k = 0; k < cityEdge.size(); k++) {
			if (j.equals(cityEdge.get(k).get(0))) {
				neighbours.add(cityEdge.get(k).get(1));
			}
		}
		return neighbours;
	}

	@Override
	public int infectionLevel(String cityName, Disease d) {
		// TODO
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isCured(Disease d) {
		// TODO
		throw new UnsupportedOperationException();
	}

	@Override
	public int infectionRate() {
		// TODO
		throw new UnsupportedOperationException();
	}

	@Override
	public GameStatus gameStatus() {
		// TODO
		throw new UnsupportedOperationException();
	}

	@Override
	public int turnDuration() {
		// TODO
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isEradicated(Disease d) {
		// TODO
		throw new UnsupportedOperationException();
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

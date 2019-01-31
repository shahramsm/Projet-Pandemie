package fr.dauphine.ja.student.pandemiage.gameengine;

import static org.junit.Assert.assertEquals;

import java.awt.List;
import java.util.ArrayList;

import org.junit.Test;
import static org.junit.Assert.*;

import fr.dauphine.ja.pandemiage.common.PlayerCardInterface;

public class TestGameEngine {

	
	GameEngine g = new GameEngine("pandemic.graphml","pandemiage-1.0-SNAPSHOT-ai.jar");
	Player p = new Player(g,new ArrayList<PlayerCardInterface>());

	
	
	
	@Test
	public void TestPlayerHandList() {
		assertEquals(false, !p.playerHand().isEmpty());
		//g.loop();
		//assertEquals(true, !p.playerHand().isEmpty());
	}
	
	@Test
	public void TestInfectionCardList() {
		assertEquals(true, !g.getInfectionCardList().isEmpty());
		
	}
	
	@Test
	public void TestPlayerCardList() {
		assertEquals(true, !g.getPlayerCardList().isEmpty());
		
	}
	
	@Test
	public void TestPlayerCardListDiscard() {
		assertEquals(true, !g.getPlayerCardListDiscard().isEmpty());
		
	} 

	@Test
	public void TestInfectionCardListDiscard() {
		assertEquals(true, !g.getInfectionCardListDiscard().isEmpty());
		
	}
	
	@Test
	public void TestNbEpidemi() {
		assertTrue( g.getNbEpidemi() >= 3);

	}
	
	@Test
	public void TestBlueDiscoveredCure(){
		assertEquals(false, g.isBlueDiscoveredCure());
	}
	
	@Test
	public void TestBlackDiscoveredCure(){
		assertEquals(false, g.isBlackDiscoveredCure());
	}
	
	@Test
	public void TestRedDiscoveredCure(){
		assertEquals(false, g.isRedDiscoveredCure());
	}
 
	@Test
	public void TestYellowDiscoveredCure(){
		assertEquals(false, g.isYellowDiscoveredCure());
	}
 

	@Test
	public void TestNbOutbreaks() {
		assertTrue( g.getNbOutbreaks() == 0);
	}
	
	
	
}

package fr.dauphine.ja.student.pandemiage.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.dauphine.ja.pandemiage.common.AiInterface;
import fr.dauphine.ja.pandemiage.common.GameInterface;
import fr.dauphine.ja.pandemiage.common.PlayerCardInterface;
import fr.dauphine.ja.pandemiage.common.PlayerInterface;
import fr.dauphine.ja.pandemiage.common.UnauthorizedActionException;
import fr.dauphine.ja.pandemiage.common.Disease;

public class IA implements AiInterface {

	@Override
	public void playTurn(GameInterface g, PlayerInterface p) {
		Random random = new Random();
		int d = random.nextInt(5 - 1 + 1) + 1;
		switch (d) {
		case 1:

			if (g.neighbours(p.playerLocation()).isEmpty()) {
				// si la main de joueur contient 1 carte et cette carte est la
				// ville actuele je choisit une des ville dans la carte au
				// hasard et je me déplace
				if (p.playerHand().size() == 1 && p.playerHand().get(0).getCityName().equals(p.playerLocation())) {
					int m = random.nextInt(48 - 1 + 1) + 1;
					String cityName = g.allCityNames().get(m);
					try {
						p.flyToCharter(cityName);
						break;
					} catch (UnauthorizedActionException e) {
						e.printStackTrace();
					}
				} else if (!p.playerHand().isEmpty()) {
					List<String> ls = new ArrayList();
					for (int i = 0; i < p.playerHand().size(); i++) {
						if (!p.playerHand().get(i).getCityName().equals(p.playerLocation())) {
							ls.add(p.playerHand().get(i).getCityName());
						}
					}
					try {
						int m = random.nextInt(ls.size() - 1 + 1) + 1;
						String cityName = ls.get(m - 1);
						p.flyTo(cityName);
					} catch (UnauthorizedActionException e) {
						e.printStackTrace();
					}
				} else if (p.playerHand().isEmpty() ){
					if(g.infectionLevel(p.playerLocation(), Disease.BLUE) > 0){
						try {
							p.treatDisease(Disease.BLUE);
							break;
						} catch (UnauthorizedActionException e) {
							e.printStackTrace();
						}
					} else if( g.infectionLevel(p.playerLocation(), Disease.BLACK) > 0){
						try {
							p.treatDisease(Disease.BLACK);
							break;
						} catch (UnauthorizedActionException e) {
							e.printStackTrace();
						}
					} else if( g.infectionLevel(p.playerLocation(), Disease.RED) > 0){
						try {
							p.treatDisease(Disease.RED);
							break;
						} catch (UnauthorizedActionException e) {
							e.printStackTrace();
						}
					} else if( g.infectionLevel(p.playerLocation(), Disease.YELLOW) > 0) {
						try {
							p.treatDisease(Disease.YELLOW);
							break;
						} catch (UnauthorizedActionException e) {
							e.printStackTrace();
						}
					} else{
						p.skipTurn();
						break;
					}
				}
			} else {
				try {
					int f = random.nextInt(g.neighbours(p.playerLocation()).size() - 1 + 1) + 1;
					String cityName = g.neighbours(p.playerLocation()).get(f - 1);
					p.moveTo(cityName);
				} catch (UnauthorizedActionException e) {
					e.printStackTrace();
				}
			}
			break;

		case 2:
			break;

		case 3:
			break;

		case 4:
			break;

		case 5:
			break;
		}
	}

	@Override
	public List<PlayerCardInterface> discard(GameInterface g, PlayerInterface p, int maxHandSize, int nbEpidemicCards) {
		// TODO Auto-generated method stub
		return null;
	}

}

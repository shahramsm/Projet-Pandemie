package fr.dauphine.ja.vong_touahri_mahdavi.pandemiage.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.dauphine.ja.pandemiage.common.AiInterface;
import fr.dauphine.ja.pandemiage.common.Disease;
import fr.dauphine.ja.pandemiage.common.GameInterface;
import fr.dauphine.ja.pandemiage.common.PlayerCardInterface;
import fr.dauphine.ja.pandemiage.common.PlayerInterface;
import fr.dauphine.ja.pandemiage.common.UnauthorizedActionException;

public class Ia2 implements AiInterface {

	@Override
	public void playTurn(GameInterface g, PlayerInterface p) {
		int b = 0;
		while (b < 4) {
			int nbTreatCardBlue = 0;
			int nbTreatCardYellow = 0;
			int nbTreatCardBlack = 0;
			int nbTreatCardRed = 0;
			for (int i = 0; i < p.playerHand().size() - 1; i++) {
				switch (p.playerHand().get(i).getDisease()) {
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
			if (nbTreatCardBlue == 5 || nbTreatCardYellow == 5 || nbTreatCardBlack == 5 || nbTreatCardRed == 5) {
				b++;
				try {
					p.discoverCure(p.playerHand());
				} catch (UnauthorizedActionException e) {
					e.printStackTrace();
				}
			} else if (p.playerHand().size() >= 2 && b < 4) {
				
				try {
					for (int i = 0; i < p.playerHand().size(); i++) {
						
						if(b < 4) {
							b++;
							p.flyTo(p.playerHand().get(i).getCityName());
						}
						
						if(b < 4) {
							b++;
							p.flyToCharter(p.playerHand().get(i).getCityName());
						}
					}
				} catch (UnauthorizedActionException e) {
					e.printStackTrace();
				}
			}
		}
		Random random = new Random();
		int d = random.nextInt(6 - 1 + 1) + 1;

		switch (d) {
		case 1:
			b++;
			if (!g.neighbours(p.playerLocation()).isEmpty()) {
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
			b++;
			if (p.playerHand().size() > 0) {
				try {
					List<String> ls = new ArrayList();
					for (int i = 0; i < p.playerHand().size(); i++) {
						if (p.playerHand().size() == 1
								&& p.playerHand().get(0).getCityName().equals(p.playerLocation())) {
							playTurn(g, p);
						} else if (!p.playerHand().get(i).getCityName().equals(p.playerLocation())) {
							ls.add(p.playerHand().get(i).getCityName());
						}
					}
					int m = random.nextInt(ls.size() - 1 + 1) + 1;
					String cityName = ls.get(m - 1);
					p.flyTo(cityName);
				} catch (UnauthorizedActionException e) {
					e.printStackTrace();
				}
			}
			break;

		case 3:
			b++;
			if (p.playerHand().size() > 0) {
				boolean r = false;
				for (int i = 0; i < p.playerHand().size(); i++) {
					if (p.playerHand().get(i).getCityName().equals(p.playerLocation())) {
						r = true;
					}
				}
				if (r == true) {
					try {
						int m = random.nextInt(48 - 1 + 1) + 1;
						while (g.allCityNames().get(m - 1).equals(p.playerLocation())) {
							m = random.nextInt(48 - 1 + 1) + 1;
						}
						String cityName = g.allCityNames().get(m - 1);
						p.flyToCharter(cityName);
					} catch (UnauthorizedActionException e) {
						e.printStackTrace();
					}
				}
			}
			break;

		case 4:
			b++;
			p.skipTurn();
			break;

		case 5:
			b++;
			if (g.infectionLevel(p.playerLocation(), Disease.BLUE) > 0) {
				try {
					p.treatDisease(Disease.BLUE);
					break;
				} catch (UnauthorizedActionException e) {
					e.printStackTrace();
				}
			} else if (g.infectionLevel(p.playerLocation(), Disease.BLACK) > 0) {
				try {
					p.treatDisease(Disease.BLACK);
					break;
				} catch (UnauthorizedActionException e) {
					e.printStackTrace();
				}
			} else if (g.infectionLevel(p.playerLocation(), Disease.RED) > 0) {
				try {
					p.treatDisease(Disease.RED);
					break;
				} catch (UnauthorizedActionException e) {
					e.printStackTrace();
				}
			} else if (g.infectionLevel(p.playerLocation(), Disease.YELLOW) > 0) {
				try {
					p.treatDisease(Disease.YELLOW);
					break;
				} catch (UnauthorizedActionException e) {
					e.printStackTrace();
				}
			}
			break;

		case 6:
			b++;
			try {
				p.discoverCure(p.playerHand());
			} catch (UnauthorizedActionException e) {
				e.printStackTrace();
			}
			break;
		}
	}
	
	

	@Override
	public List<PlayerCardInterface> discard(GameInterface g, PlayerInterface p, int maxHandSize, int nbEpidemicCards) {
		List<PlayerCardInterface> discard = new ArrayList<>();
		int numdiscard = p.playerHand().size() - maxHandSize;

		for (int i = 0; i < numdiscard; i++)
			discard.add(p.playerHand().get(i));

		return discard;
	}

}

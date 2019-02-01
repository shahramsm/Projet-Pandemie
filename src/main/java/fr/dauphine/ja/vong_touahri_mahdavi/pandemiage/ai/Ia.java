package fr.dauphine.ja.vong_touahri_mahdavi.pandemiage.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.dauphine.ja.pandemiage.common.AiInterface;
import fr.dauphine.ja.pandemiage.common.GameInterface;
import fr.dauphine.ja.pandemiage.common.PlayerCardInterface;
import fr.dauphine.ja.pandemiage.common.PlayerInterface;
import fr.dauphine.ja.pandemiage.common.UnauthorizedActionException;
import fr.dauphine.ja.pandemiage.common.Disease;

public class Ia implements AiInterface {
	/*
	 * public void Shine(int p, GameEngine g){ if(p=0){ return
	 * heuristic(g);//Evalue le game engine }
	 * 
	 * for(int i=0; i<actPossible;i++){ =faire action i(= 4 actions); int
	 * temp=Shine(p-1, g);
	 * 
	 * if(max<temp){ max=temp; actionAFaire=i } }
	 * 
	 * } public int heuristic(GameEngine g){ int score=0; if(8 foyer infection)
	 * Score=-100000000; retrun score; if(4 remedes) score=100000000; return
	 * score; }
	 */

	// méthode qui permets de faire des differents actions par IA, la choix des
	// actions est au hasard
	@Override
	public void playTurn(GameInterface g, PlayerInterface p) {
		int b = 0;
		while (b < 4) {
			Random random = new Random();
			int d = random.nextInt(6 - 1 + 1) + 1;
			switch (d) {
			case 1:
				if (!g.neighbours(p.playerLocation()).isEmpty()) {
					b++;
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
				if (p.playerHand().size() > 0) {
					b++;
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
				if (p.playerHand().size() > 0) {
					b++;
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
	}

	// méthode qui permets de piocher des carts par IA
	@Override
	public List<PlayerCardInterface> discard(GameInterface g, PlayerInterface p, int maxHandSize, int nbEpidemicCards) {
		List<PlayerCardInterface> discard = new ArrayList<>();
		int numdiscard = p.playerHand().size() - maxHandSize;

		for (int i = 0; i < numdiscard; i++)
			discard.add(p.playerHand().get(i));

		return discard;
	}
}

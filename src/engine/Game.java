package engine;

import java.awt.*;
import java.awt.Point;
import java.util.ArrayList;
import model.abilities.*;
import model.effects.Effect;
import model.effects.EffectType;
import model.world.*;

import java.io.*;

public class Game {
	
	private Player firstPlayer;
	private Player secondPlayer;
	private boolean  firstLeaderAbilityUsed;
	private boolean  secondLeaderAbilityUsed;
	private Object [][] board; // [height][width]
	private static ArrayList<Champion> availableChampions;
	private static ArrayList<Ability> availableAbilities;
	private PriorityQueue turnOrder;
	private static int BOARDHEIGHT = 5;
	private static int BOARDWIDTH = 5;
	
	public Game(Player first, Player second) {
		this.firstPlayer = first;
		this.secondPlayer = second;
		board = new Object[BOARDHEIGHT][BOARDWIDTH];
	}
	
	private void placeChampions() {
		for(int i = 0; i<firstPlayer.getTeams().size();i++) {
			board[0][i+1] = firstPlayer.getTeams().get(i);
		}
		for(int i = 0; i<secondPlayer.getTeams().size();i++) {
			board[BOARDHEIGHT-1][i+1] = secondPlayer.getTeams().get(i);
		}
	}
	
	private void placeCovers() { //ben7ot 2l 8attah hena ya kes 25tak
		for(int i=0;i<5;i++){
			int h;
			int w;
			do {
				h =(int) (Math.random() * ((BOARDHEIGHT-1)-1) + 1);
				w =(int) (Math.random() * (BOARDWIDTH));
			}
			while (board [h][w]!=null);
			board [h][w] = new Cover(h,w);
		}
	}
	
	///////////////////////
	//////////////////////
	//hatedrab fy weshena (probably)
	/////////////////////
	/////////////////////
	public static void loadAbilities(String filePath) throws Exception {
		BufferedReader br= new BufferedReader(new FileReader(filePath)); //copied, no ba3basah
		String row;
		while ((row=br.readLine())!=null) {
			//metba3bas, ab is the string array of abilities from the abilities csv
			String [] ab = row.split(",");

			if (ab[0].equals("DMG"))
				availableAbilities.add(
						new DamagingAbility(
								ab[1],
								Integer.parseInt(ab[2]),
								Integer.parseInt(ab[4]),
								Integer.parseInt(ab[3]),
								AreaOfEffect.valueOf(ab[5]),
								Integer.parseInt(ab[6]),
								Integer.parseInt(ab[7])
						)
				);
			else if(ab[0].equals("HEL"))
					availableAbilities.add(
							new HealingAbility(
									ab[1],
									Integer.parseInt(ab[2]),
									Integer.parseInt(ab[4]),
									Integer.parseInt(ab[3]),
									AreaOfEffect.valueOf(ab[5]),
									Integer.parseInt(ab[6]),
									Integer.parseInt(ab[7])
							)
					);
			else
				availableAbilities.add(
						new CrowdControlAbility(
								ab[1],
								Integer.parseInt(ab[2]),
								Integer.parseInt(ab[4]),
								Integer.parseInt(ab[3]),
								AreaOfEffect.valueOf(ab[5]),
								Integer.parseInt(ab[6]),
								// Next param feels wrong? calling effect constructor instead of specific class?
								new Effect(ab[1], Integer.parseInt(ab[8]),EffectType.valueOf(ab[7]))
						)
				);
		}
	}

	public static void loadChampions(String filePath) throws  Exception {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String row;
		while ((row = br.readLine()) != null) {
			String[] champ = row.split(",");

			if(champ[0].equals("H"))
				availableChampions.add(
						new Hero(
								champ[1],
								Integer.parseInt(champ[2]),
								Integer.parseInt(champ[3]),
								Integer.parseInt(champ[4]),
								Integer.parseInt(champ[5]),
								Integer.parseInt(champ[6]),
								Integer.parseInt(champ[7])
						)
				);
			else if(champ[0].equals("A"))
				availableChampions.add(
						new AntiHero(
								champ[1],
								Integer.parseInt(champ[2]),
								Integer.parseInt(champ[3]),
								Integer.parseInt(champ[4]),
								Integer.parseInt(champ[5]),
								Integer.parseInt(champ[6]),
								Integer.parseInt(champ[7])
						)
				);
			else
				availableChampions.add(
						new Villain(
								champ[1],
								Integer.parseInt(champ[2]),
								Integer.parseInt(champ[3]),
								Integer.parseInt(champ[4]),
								Integer.parseInt(champ[5]),
								Integer.parseInt(champ[6]),
								Integer.parseInt(champ[7])
						)
				);
										////////////////////////
			// HOW TO INSERT THE CHAMP'S ABILITIES IF THE ARRAYLIST OF ABILITIES IS PRIVATE???????????
			// Champion's abilities are champ[8], champ[9] and champ[10]
										///////////////////////
		}
	}



}

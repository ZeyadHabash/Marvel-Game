package engine;

import java.awt.*;
import java.awt.Point;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import model.abilities.*;
import model.effects.*;
import model.world.*;
import engine.PriorityQueue;

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
	private static final int BOARDHEIGHT = 5;
	private static final int BOARDWIDTH = 5;
	
	public Game(Player first, Player second) {
		this.firstPlayer = first;
		this.secondPlayer = second;
		board = new Object[BOARDHEIGHT][BOARDWIDTH];
		availableChampions = new ArrayList<Champion>();
		availableAbilities = new ArrayList<Ability>();
		turnOrder = new PriorityQueue(6);
		placeChampions();
		placeCovers();
	}
	
	private void placeChampions() {
		for(int i = 0; i<firstPlayer.getTeam().size();i++) {
			board[0][i+1] = firstPlayer.getTeam().get(i);
			firstPlayer.getTeam().get(i).setLocation(new Point(0,i+1));
		}
		for(int i = 0; i<secondPlayer.getTeam().size();i++) {
			board[BOARDHEIGHT-1][i+1] = secondPlayer.getTeam().get(i);
			secondPlayer.getTeam().get(i).setLocation(new Point(BOARDHEIGHT-1,i+1));
		}
	}
	
	private void placeCovers() { //ben7ot 2l 8attah hena
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
		BufferedReader br= new BufferedReader(new FileReader(filePath)); //copied
		String row;
		while ((row=br.readLine())!=null) {
			//ab is the string array of abilities from the abilities csv
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
			else{
				Effect abilityEffect;
				int duration = Integer.parseInt(ab[8]);
				switch(ab[7]){
					case "Shield": abilityEffect = new Shield(duration);break;
					case "Disarm": abilityEffect = new Disarm(duration);break;
					case "PowerUp": abilityEffect = new PowerUp(duration);break;
					case "Silence": abilityEffect = new Silence(duration);break;
					case "SpeedUp": abilityEffect = new SpeedUp(duration);break;
					case "Embrace": abilityEffect = new Embrace(duration);break;
					case "Root": abilityEffect = new Root(duration);break;
					case "Shock": abilityEffect = new Shock(duration);break;
					case "Dodge": abilityEffect = new Dodge(duration);break;
					case "Stun": abilityEffect = new Stun(duration);break;
					default: abilityEffect = null;
				}
				availableAbilities.add(
						new CrowdControlAbility(
								ab[1],
								Integer.parseInt(ab[2]),
								Integer.parseInt(ab[4]),
								Integer.parseInt(ab[3]),
								AreaOfEffect.valueOf(ab[5]),
								Integer.parseInt(ab[6]),
								// abilityEffect corresponds to its respective ability, done using a switch case
								abilityEffect
						)
				);
			}
		}
	}

	public static void loadChampions(String filePath) throws  Exception {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String row;
		while ((row = br.readLine()) != null) {
			String[] champ = row.split(",");
			Champion newChamp;

			if(champ[0].equals("H"))
				availableChampions.add(
						newChamp = new Hero(
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
						newChamp = new AntiHero(
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
						newChamp = new Villain(
								champ[1],
								Integer.parseInt(champ[2]),
								Integer.parseInt(champ[3]),
								Integer.parseInt(champ[4]),
								Integer.parseInt(champ[5]),
								Integer.parseInt(champ[6]),
								Integer.parseInt(champ[7])
						)
				);

			Ability first_ability = retrieveAbility(availableAbilities,champ[8]);
			Ability second_ability = retrieveAbility(availableAbilities,champ[9]);
			Ability third_ability = retrieveAbility(availableAbilities,champ[10]);

			newChamp.getAbilities().add(first_ability);
			newChamp.getAbilities().add(second_ability);
			newChamp.getAbilities().add(third_ability);

		}
	}


	// FOUND IN THE PUBLIC TESTS, SEEMS GOOD IDK IF WE'RE ALLOWED TO DO THIS THO
	// method that uses the available ability array to populate each champion's individual ability array
	private static Ability retrieveAbility(ArrayList<Ability> availableAbilities, String abilityName)
			throws Exception{
		for (int i = 0; i < availableAbilities.size(); i++) {
			String name = availableAbilities.get(i).getName();
			if (name.equals(abilityName)) {
				return (Ability) availableAbilities.get(i);
			}
		}
		return null;
	}


	public Player getFirstPlayer() {
		return firstPlayer;
	}

	public Player getSecondPlayer() {
		return secondPlayer;
	}

	public boolean isFirstLeaderAbilityUsed() {
		return firstLeaderAbilityUsed;
	}

	public boolean isSecondLeaderAbilityUsed() {
		return secondLeaderAbilityUsed;
	}

	public Object[][] getBoard() {
		return board;
	}

	public static ArrayList<Champion> getAvailableChampions() {
		return availableChampions;
	}

	public static ArrayList<Ability> getAvailableAbilities() {
		return availableAbilities;
	}

	public PriorityQueue getTurnOrder() {
		return turnOrder;
	}

	public static int getBoardheight() {
		return BOARDHEIGHT;
	}

	public static int getBoardwidth() {
		return BOARDWIDTH;
	}
}

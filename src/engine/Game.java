package engine;

import model.abilities.*;
import model.effects.*;
import model.world.*;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Game {

	private Player firstPlayer;
	private Player secondPlayer;
	private boolean  firstLeaderAbilityUsed;
	private boolean  secondLeaderAbilityUsed;
	private Object [][] board; // [height][width]
	private static ArrayList<Champion> availableChampions;
	private static ArrayList<Ability> availableAbilities;
	private PriorityQueue turnOrder; //contains available characters to pick from
	private static final int BOARDHEIGHT = 5;
	private static final int BOARDWIDTH = 5;
	
	public Game(Player first, Player second) throws Exception {
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
		for(int i = 0; i<firstPlayer.getTeam().size();i++) {  //iterates the firstPlayer's team arraylist and places the champions on the board as well as sets their location (attribute of champion)
			board[0][i+1] = firstPlayer.getTeam().get(i);
			firstPlayer.getTeam().get(i).setLocation(new Point(0,i+1));
		}
		for(int i = 0; i<secondPlayer.getTeam().size();i++) {  //iterates the secondPlayer's team arraylist and places the champions on the board as well as sets their location (attribute of champion)
			board[BOARDHEIGHT-1][i+1] = secondPlayer.getTeam().get(i);
			secondPlayer.getTeam().get(i).setLocation(new Point(BOARDHEIGHT-1,i+1));
		}
	}
	
	private void placeCovers() {  //places 5 covers on the board
		for(int i=0; i<5; i++){
			int height;
			int width;
			do {
				height =(int) (Math.random() * ((BOARDHEIGHT-1)-1) + 1);  //picks a random no. for the height between 1(inc) & 4(exc) to place the covers anywhere except the bottom and top rows
				width =(int) (Math.random() * (BOARDWIDTH)); 
			}
			while (board [height][width]!=null);
			board [height][width] = new Cover(height,width);
		}
	}

	public static void loadAbilities(String filePath) throws Exception {
		BufferedReader br= new BufferedReader(new FileReader(filePath));
		String row;
		while ((row=br.readLine())!=null) {
			String [] abilities = row.split(",");


			if (abilities[0].equals("DMG"))
				availableAbilities.add(
						new DamagingAbility(
								abilities[1],
								Integer.parseInt(abilities[2]),
								Integer.parseInt(abilities[4]),
								Integer.parseInt(abilities[3]),
								AreaOfEffect.valueOf(abilities[5]),
								Integer.parseInt(abilities[6]),
								Integer.parseInt(abilities[7])
						)
				);
			else if(abilities[0].equals("HEL"))
					availableAbilities.add(
							new HealingAbility(
									abilities[1],
									Integer.parseInt(abilities[2]),
									Integer.parseInt(abilities[4]),
									Integer.parseInt(abilities[3]),
									AreaOfEffect.valueOf(abilities[5]),
									Integer.parseInt(abilities[6]),
									Integer.parseInt(abilities[7])
							)
					);
			else{
				Effect abilityEffect;
				int duration = Integer.parseInt(abilities[8]);
				switch(abilities[7]){
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
								abilities[1],
								Integer.parseInt(abilities[2]),
								Integer.parseInt(abilities[4]),
								Integer.parseInt(abilities[3]),
								AreaOfEffect.valueOf(abilities[5]),
								Integer.parseInt(abilities[6]),
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
			String[] champ = row.split(",");        //string array of champions from the csv file
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
			
            //populates the Abilities ArrayList in the Champion class using the ability names in the csv file
			newChamp.getAbilities().add(findAbility(champ[8]));
			newChamp.getAbilities().add(findAbility(champ[9]));
			newChamp.getAbilities().add(findAbility(champ[10]));

		}
	}


	//helper method that uses the available ability array to populate each champion's individual ability array list
	private static Ability findAbility(String abilityName) {
		int i = 0; 
		while (i < availableAbilities.size()) {
			String currAbility = availableAbilities.get(i).getName();
			if (abilityName.equals(currAbility)) {
				return availableAbilities.get(i);
			}
			else i++;
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

	public boolean isSecondLeaderAbilityUsed() {return secondLeaderAbilityUsed;	}

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
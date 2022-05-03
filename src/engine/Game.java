package engine;

import java.awt.*;
import java.awt.Point;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import exceptions.ChampionDisarmedException;
import exceptions.InvalidTargetException;
import exceptions.NotEnoughResourcesException;
import exceptions.UnallowedMovementException;
import model.abilities.*;
import model.effects.*;
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

	public Champion getCurrentChampion(){
		return (Champion) turnOrder.peekMin();        //return the champion who's turn is taking place
	}

	public Player checkGameOver(){
		int j=0;
		for(int i = 0; i < firstPlayer.getTeam().size(); i++){
			if (firstPlayer.getTeam().get(i).getCondition() == Condition.KNOCKEDOUT)
				j++;                                      //j counts the no. of knockedout champions on a player's team
		}
		if (j == firstPlayer.getTeam().size())           //checks if all the champions on the team are knockedout (ie. dead)
			return secondPlayer;
		else{
			j=0;                                         //resets j to use it as a counter for the second team
			for(int i = 0; i < secondPlayer.getTeam().size(); i++){
				if (secondPlayer.getTeam().get(i).getCondition() == Condition.KNOCKEDOUT)
					j++;
			}
			if (j == secondPlayer.getTeam().size())      //same check as above
				return firstPlayer;
		}
		return null;            //none of the players have lost yet
	}

	public void move(Direction d) throws UnallowedMovementException, NotEnoughResourcesException {    //exceptions not implemented yet
		try{
			Champion c = getCurrentChampion();
			if(!c.getCondition().equals(Condition.ROOTED)){          //revisit; checks if the champion is rooted before allowing the movement but this might be handled in an exception which will be thrown
				int x = c.getLocation().x;
				int y = c.getLocation().y;
				board[x][y] = null;
				switch(d) {
					case RIGHT: c.setLocation(new Point(x, y+1)); board[x][y+1] = c; break;
					case LEFT: c.setLocation(new Point(x, y-1)); board[x][y-1] = c; break;
					case UP: c.setLocation(new Point(x+1, y)); board[x+1][y] = c; break;
					case DOWN: c.setLocation(new Point(x-1, y)); board[x-1][y] = c; break;
				}
				c.setCurrentActionPoints(c.getCurrentActionPoints()-1);
			}
		}
		catch (Exception e){
			throw e;
		}
	}

	public void attack(Direction d) throws NotEnoughResourcesException, InvalidTargetException, ChampionDisarmedException {      //might have to check if the champion is attacking teammates
		Champion c = getCurrentChampion();
		int x = c.getLocation().x;
		int y = c.getLocation().y;
		int r = c.getAttackRange();
		Damageable target = null;
		//looking for the nearest target in the direction d within the attack range of the champion
		switch (d){
			case RIGHT: for(int i = 0; i<r; i++){
				if(board[x][y+1] instanceof Damageable) {
					target = (Damageable) board[x][y + 1];
					break;
				}
			};break;
			case LEFT: for(int i = 0; i<r; i++){
				if(board[x][y-1] instanceof Damageable) {
					target = (Damageable) board[x][y - 1];
					break;
				}
			};break;
			case UP: for(int i = 0; i<r; i++){
				if(board[x+1][y] instanceof Damageable) {
					target = (Damageable) board[x+1][y];
					break;
				}
			};break;
			case DOWN: for(int i = 0; i<r; i++){
				if(board[x-1][y] instanceof Damageable) {
					target = (Damageable) board[x-1][y];
					break;
				}
			};break;
		}
		c.setCurrentActionPoints(c.getCurrentActionPoints()-2);
		if (target == null)
			return;
		target.setCurrentHP(target.getCurrentHP()-(int)(c.getAttackDamage()*damageMultiplier(c,target)));   //using a helper method to determine the types of the champion and target and return the multiplication of the damage accordingly
	}

	public double damageMultiplier(Champion attacker, Damageable target){
		if (target instanceof  Cover)
			return 1;
		if (attacker.getClass().equals(target.getClass()))
			return 1;
		return 1.5;
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
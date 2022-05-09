package engine;

import exceptions.*;
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
		prepareChampionTurns();
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


	//////////////////////////////////////////////////////////////////////////////

	// Helper Methods
	// Pretty sure they should be private since they shouldn't be called anywhere else

	//////////////////////////////////////////////////////////////////////////////


	// Helper method that uses the available ability array to populate each champion's individual ability array list
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

	// Helper method to remove a champion from board once they're killed
	private void killChampion(Champion c){

	}

	// Helper method to calculate the damage multiplier of Champions when attacking
	private double damageMultiplier(Champion attacker, Damageable target){
		if (target instanceof  Cover)
			return 1;
		if (attacker.getClass().equals(target.getClass()))
			return 1;
		return 1.5;
	}

	// Helper method to get the distance between two points using the Manhattan distance calculation
	private int distanceCalculator(Point p1, int x, int y){
		int dx = ((int) p1.getX()) - x;
		int dy = ((int) p1.getY()) - y;
		return (Math.abs(dx) + Math.abs(dy));
	}

	// Helper method to get targeted team whether that be the friendly or the enemy team
	public ArrayList<Damageable> getTargetedObjects(Champion c, Ability a){
		ArrayList<Damageable> friendlyTeam = new ArrayList<Damageable>();
		ArrayList<Damageable> enemyTeam = new ArrayList<Damageable>();
		if(firstPlayer.getTeam().contains(c)) {
			//truly do not know if the syntax of populating the friendlyTeam and enemyTeam array lists is correct? no errors but very sus
			friendlyTeam = (ArrayList<Damageable>)firstPlayer.getTeam().clone();
			enemyTeam = (ArrayList<Damageable>)secondPlayer.getTeam().clone();
		}
		else{
			friendlyTeam = (ArrayList<Damageable>)secondPlayer.getTeam().clone();
			enemyTeam = (ArrayList<Damageable>)firstPlayer.getTeam().clone();
		}

		//check whether the ability is healing, damaging or cc
		if(a instanceof HealingAbility || (a instanceof CrowdControlAbility && ((CrowdControlAbility) a).getEffect().getType().equals(EffectType.BUFF)))
			return friendlyTeam;        //im not sure if the current champion should be excluded (as a target) in the case of a pos ability
		else if (a instanceof DamagingAbility) {
			//iterates through the board looking for covers to add them to the enemyTeam in case of damaging ability
			for(int i = 0; i < BOARDHEIGHT; i++){
				for (int j = 0; j < BOARDWIDTH; j++){
					if (board[i][j] != null && board[i][j] instanceof Cover)
						enemyTeam.add((Damageable)board[i][j]);
				}
			}
			return enemyTeam;
		}
		else                      //the returned team for negative CC abilities is the opposing champions (exc covers)
			return enemyTeam;
	}


	//////////////////////////////////////////////////////////////////////////////

	// Required methods

	//////////////////////////////////////////////////////////////////////////////


	public Champion getCurrentChampion(){
		return (Champion) turnOrder.peekMin();        //return the champion whose turn is taking place
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

	public void move(Direction d) throws UnallowedMovementException, NotEnoughResourcesException, ArrayIndexOutOfBoundsException {    //exceptions not implemented yet
			Champion c = getCurrentChampion();
			// trying a different take on exceptions, seems easier to understand and implement
			if(c.getCurrentActionPoints() < 1)
				throw new NotEnoughResourcesException("Not enough action points");
			else if(c.getCondition().equals(Condition.ROOTED))
				throw new UnallowedMovementException("Champion cannot move while rooted");
			else {
				int x = c.getLocation().x;
				int y = c.getLocation().y;
				// trying to avoid Array index out of bounds (player tries to move off board)
				try {
					// checks if desired cell is empty before moving
					switch (d) {
						case RIGHT:
							if (board[x][y + 1] != null)
								throw new UnallowedMovementException("Cannot move to an occupied cell");
							else {
								c.setLocation(new Point(x, y + 1));
								board[x][y + 1] = c;
								board[x][y] = null;
							}
							break;
						case LEFT:
							if (board[x][y - 1] != null)
								throw new UnallowedMovementException("Cannot move to an occupied cell");
							else {
								c.setLocation(new Point(x, y - 1));
								board[x][y - 1] = c;
								board[x][y] = null;
							}
							break;
						case UP:
							if (board[x + 1][y] != null)
								throw new UnallowedMovementException("Cannot move to an occupied cell");
							else {
								c.setLocation(new Point(x + 1, y));
								board[x + 1][y] = c;
								board[x][y] = null;
							}
							break;

						case DOWN:
							if (board[x - 1][y] != null)
								throw new UnallowedMovementException("Cannot move to an occupied cell");
							else {
								c.setLocation(new Point(x - 1, y));
								board[x - 1][y] = c;
								board[x][y] = null;
							}
							break;
					}
					c.setCurrentActionPoints(c.getCurrentActionPoints() - 1);
				}catch(ArrayIndexOutOfBoundsException e) {
					throw new UnallowedMovementException("Cannot move out of board bounds");
				}
			}
	}

	// Still need to check if champion is disarmed
	public void attack(Direction d) throws NotEnoughResourcesException, ChampionDisarmedException, ArrayIndexOutOfBoundsException {
		Champion c = getCurrentChampion();

		if (c.getCurrentActionPoints() < 1)
			throw new NotEnoughResourcesException("Not enough action points");
		else {
			int x = c.getLocation().x;
			int y = c.getLocation().y;
			int r = c.getAttackRange();
			Damageable target = null;
			try {
				//looking for the nearest target in the direction d within the attack range of the champion
				switch (d) {
					case RIGHT:
						for (int i = 1; i < r + 1; i++) {
							if (board[x][y + i] instanceof Damageable) {             //moving down the board in this direction to find a damageable object, stops when the range has been reached
								target = (Damageable) board[x][y + 1];
								break;
							}
						}
						;
						break;
					case LEFT:
						for (int i = 1; i < r + 1; i++) {
							if (board[x][y - i] instanceof Damageable) {
								target = (Damageable) board[x][y - 1];
								break;
							}
						}
						;
						break;
					case UP:
						for (int i = 1; i < r + 1; i++) {
							if (board[x + i][y] instanceof Damageable) {
								target = (Damageable) board[x + 1][y];
								break;
							}
						}
						;
						break;
					case DOWN:
						for (int i = 1; i < r + 1; i++) {
							if (board[x - i][y] instanceof Damageable) {
								target = (Damageable) board[x - 1][y];
								break;
							}
						}
						break;
				}
			}catch(ArrayIndexOutOfBoundsException e){

			}finally{
				c.setCurrentActionPoints(c.getCurrentActionPoints() - 2);
			}
			// does no damage if attacking an empty cell
			if (target == null)
				return;
			// does no damage if the attacker and the target are from the same team
			if((firstPlayer.getTeam().contains(c) && firstPlayer.getTeam().contains(target)) || (secondPlayer.getTeam().contains(c) && secondPlayer.getTeam().contains(target)))
				return;
			// does damage when target is an enemy or a cover
			target.setCurrentHP(target.getCurrentHP() - (int) (c.getAttackDamage() * damageMultiplier(c, target)));   //using a helper method to determine the types of the champion and target and return the multiplication of the damage accordingly
		}
	}


	// Cast ability methods

	// A method for casting an ability that is not limited to a direction or a particular target
	public void castAbility(Ability a) throws NotEnoughResourcesException, AbilityUseException {
		try{
			Champion c = getCurrentChampion();
			int x = c.getLocation().x;
			int y = c.getLocation().y;
			ArrayList<Damageable> targetedObjects = getTargetedObjects(c,a);      //list of valid objects to target, ie. friendly champions if positive ability or covers and enemy champions if neg one
			ArrayList<Damageable> targets = new ArrayList<Damageable>();
			switch (a.getCastArea()){
				case SELFTARGET: targets.add(c);break;
				case TEAMTARGET:
					//iterates through the board to look for any damageables
					for (int i = 0; i < BOARDHEIGHT; i++){
						for (int j = 0; j < BOARDWIDTH; j++){
							if (board[i][j] != null && targetedObjects.contains((Damageable) board[i][j])){
								int d = distanceCalculator(c.getLocation(), i,j);        //when a damageable is found the distance between it and the champion casting the ability is calculated
								if(d<=a.getCastRange())         //if the damageable is within range & part of the targeted team, it is added to the list fo targets
									targets.add((Damageable) board[i][j]);
							}
						}
					}; break;
					//not sure if there needs to be a check for the range here, seeing that the cells within range are pretty straightforward
				    //also dk if there would be an error if the cells being checked don't exist aslan, i think ah bas don't have energy to try and handle that
				case SURROUND: {            //akeed there's a more efficient way of tackling this but my pea sized brain simply can not
					if(board[x+1][y]!= null && targetedObjects.contains((Damageable) board[x+1][y]))
						targets.add((Damageable) board[x+1][y]);
					if(board[x][y+1]!= null && targetedObjects.contains((Damageable) board[x][y+1]))
						targets.add((Damageable) board[x][y+1]);
					if(board[x-1][y]!= null && targetedObjects.contains((Damageable) board[x-1][y]))
						targets.add((Damageable) board[x-1][y]);
					if(board[x][y-1]!= null && targetedObjects.contains((Damageable) board[x][y-1]))
						targets.add((Damageable) board[x][y-1]);
					if(board[x+1][y+1]!= null && targetedObjects.contains((Damageable) board[x+1][y+1]))
						targets.add((Damageable) board[x+1][y+1]);
					if(board[x-1][y-1]!= null && targetedObjects.contains((Damageable) board[x-1][y-1]))
						targets.add((Damageable) board[x-1][y-1]);
					if(board[x+1][y-1]!= null && targetedObjects.contains((Damageable) board[x+1][y-1]))
						targets.add((Damageable) board[x+1][y-1]);
					if(board[x-1][y+1]!= null && targetedObjects.contains((Damageable) board[x-1][y+1]))
						targets.add((Damageable) board[x-1][y+1]);
				}; break;
				default: break;   //throw new IllegalStateException("Unexpected value: " + a.getCastArea());    >this was the recommended line when adding the default stmt
			}
			c.setCurrentActionPoints(c.getCurrentActionPoints()-a.getRequiredActionPoints());
			c.setMana(c.getMana()-a.getManaCost());
			if(targets.isEmpty())
				return;
			a.execute(targets); //list of targets is passed to the execution method in the ability class
		}
		catch (Exception e){
			throw e;
		}
	}

	// A method for casting ability with DIRECTIONAL area of effect
	public void castAbility(Ability a, Direction d) throws NotEnoughResourcesException, AbilityUseException{
		try{
			Champion c = getCurrentChampion();
			int x = c.getLocation().x;
			int y = c.getLocation().y;
			int r = a.getCastRange();
			ArrayList<Damageable> targets = new ArrayList<Damageable>();
			int i = 1;
			switch (d){                        //the amount of redundant lines in this part makes my brain hurt
				case RIGHT:                    //the movement along the board can probably use a helper method(?) since we use the same logic a lot
					while(i<=r){
						if(board[x][y+i]!=null && getTargetedObjects(c, a).contains((Damageable) board[x][y+i]))
							targets.add((Damageable) board[x][y + i]);
						i++;
					}; break;
				case LEFT:
					while(i<=r){
						if(board[x][y-i]!=null && getTargetedObjects(c, a).contains((Damageable) board[x][y-i]))
							targets.add((Damageable) board[x][y - i]);
						i++;
					}; break;
				case UP:
					while(i<=r){
						if(board[x+i][y]!=null && getTargetedObjects(c, a).contains((Damageable) board[x+i][y]))
							targets.add((Damageable) board[x + i][y]);
						i++;
					}; break;
				case DOWN:
					while(i<=r){
						if(board[x-i][y]!=null && getTargetedObjects(c, a).contains((Damageable) board[x-i][y]))
							targets.add((Damageable) board[x-i][y]);
						i++;
					}; break;
			}
			c.setCurrentActionPoints(c.getCurrentActionPoints()-a.getRequiredActionPoints());
			c.setMana(c.getMana()-a.getManaCost());
			if(targets.isEmpty())
				return;
			a.execute(targets);
		}
		catch (Exception e){
			throw e;
		}

	}

	// A method for casting an ability with SINGLETARGET area of effect
	public void castAbility(Ability a, int x, int y) throws NotEnoughResourcesException, AbilityUseException{
		try{
			Champion c = getCurrentChampion();
			c.setCurrentActionPoints(c.getCurrentActionPoints()-a.getRequiredActionPoints());
			c.setMana(c.getMana()-a.getManaCost());
			ArrayList<Damageable> targets = new ArrayList<Damageable>();
			int d = distanceCalculator(c.getLocation(), x,y);
			//check that the cell is within the ability's castRange & the cell isn't empty & the object is actually targeted
			if(d <= a.getCastRange() && board[x][y] != null)    //dk if a check should be added to make sure the target is on the right team based on the ability's type
				targets.add((Damageable) board[x][y]);
			else
				return;
		}
		catch (Exception e){
			throw e;
		}
	}
	//Done twice for each team, need to know how to use another class's method hena
	public void useLeaderAbility() throws LeaderAbilityAlreadyUsedException, LeaderNotCurrentException, NotEnoughResourcesException, AbilityUseException {
		try {
			Champion a = getCurrentChampion();
			ArrayList<Champion> targets = new ArrayList<Champion>();
			if (a.equals(getFirstPlayer().getLeader())) {
				if (a instanceof Hero) {
					targets.add(getFirstPlayer().getTeam().get(0));
					targets.add(getFirstPlayer().getTeam().get(1));
					targets.add(getFirstPlayer().getTeam().get(2));
				} else if (a instanceof Villain) {
					for (int i = 0; i < 3; i++) {
						if (getSecondPlayer().getTeam().get(i).getCurrentHP() < (int)(0.3 * getSecondPlayer().getTeam().get(i).getMaxHP())) {
							targets.add(getSecondPlayer().getTeam().get(i));
						}
					}
				} else if (a instanceof AntiHero) {
					for (int i = 0; i < 3; i++) {
						if (getSecondPlayer().getTeam().get(i).equals(getSecondPlayer().getLeader())) {
							targets.add(getSecondPlayer().getTeam().get(i));
						}
						if (getFirstPlayer().getTeam().get(i).equals(getFirstPlayer().getLeader())) {
							targets.add(getFirstPlayer().getTeam().get(i));
						}
					}
				}
			}
		else if (a.equals(getSecondPlayer().getLeader())) {
				if (a instanceof Hero) {
					targets.add(getSecondPlayer().getTeam().get(0));
					targets.add(getSecondPlayer().getTeam().get(1));
					targets.add(getSecondPlayer().getTeam().get(2));
				} else if (a instanceof Villain) {
					for (int i = 0; i < 3; i++) {
						if (getFirstPlayer().getTeam().get(i).getCurrentHP() < getFirstPlayer().getTeam().get(i).getMaxHP()) {
							targets.add(getFirstPlayer().getTeam().get(i));
						}
					}
				} else if (a instanceof AntiHero) {
					for (int i = 0; i < 3; i++) {
						if (getFirstPlayer().getTeam().get(i).equals(getFirstPlayer().getLeader())) {
							targets.add(getFirstPlayer().getTeam().get(i));
						}
						if (getSecondPlayer().getTeam().get(i).equals(getSecondPlayer().getLeader())) {
							targets.add(getSecondPlayer().getTeam().get(i));
						}
					}
				}
			}
		//**INPUT HERE THE METHOD THING**//
		}
		catch (Exception e) {
			throw e;
		}
	}

	private void prepareChampionTurns(){
		ArrayList<Champion> list = availableChampions;
		for(int i=0;i<list.size();i++) {
			if(list.get(i).getCondition() != Condition.KNOCKEDOUT)
				turnOrder.insert(list.get(i));
		}
	}



	//////////////////////////////////////////////////////////////////////////////

	// Setters and Getters

	//////////////////////////////////////////////////////////////////////////////


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
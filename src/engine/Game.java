package engine;

import exceptions.*;
import model.abilities.*;
import model.effects.*;
import model.world.*;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;

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

	// Helper method to remove a champion/cover from board once they're killed/destroyed
	public void removeDamageable(Damageable damageable){
		// removes dead object from board
		int x = damageable.getLocation().x;
		int y = damageable.getLocation().y;
		board[x][y] = null;
		// removes dead champion from team
		if (damageable instanceof Champion) {
			turnOrder.remove(damageable);
			if (firstPlayer.getTeam().contains(damageable))
				firstPlayer.getTeam().remove(damageable);
			else
				secondPlayer.getTeam().remove(damageable);
		}
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
		int dx = p1.x - x;
		int dy = p1.y - y;
		return (Math.abs(dx) + Math.abs(dy));
	}

	// Helper method to get targeted team whether that be the friendly or the enemy team
	public ArrayList<Damageable> getTargetedObjects(Champion champion, Ability a){
		ArrayList<Damageable> friendlyTeam;
		ArrayList<Damageable> enemyTeam;
		if(firstPlayer.getTeam().contains(champion)) {
			// truly do not know if the syntax of populating the friendlyTeam and enemyTeam array lists is correct? no errors but very sus
			 friendlyTeam = (ArrayList<Damageable>)firstPlayer.getTeam().clone();
			 enemyTeam = (ArrayList<Damageable>)secondPlayer.getTeam().clone();
		}
		else{
			 friendlyTeam = (ArrayList<Damageable>)secondPlayer.getTeam().clone();
			 enemyTeam = (ArrayList<Damageable>)firstPlayer.getTeam().clone();
		}

		// check whether the ability is healing, damaging or cc
		if(a instanceof HealingAbility || (a instanceof CrowdControlAbility && ((CrowdControlAbility) a).getEffect().getType().equals(EffectType.BUFF)))
			return friendlyTeam;
		else if (a instanceof DamagingAbility) {
			//  through the board looking for covers to add them to the enemyTeam in case of damaging ability
			for(int i = 0; i < BOARDHEIGHT; i++){
				for (int j = 0; j < BOARDWIDTH; j++){
					if (board[i][j] instanceof Cover)
						enemyTeam.add((Damageable)board[i][j]);
				}
			}
		}
		// Doesnt add the covers if its a debuff cc ability
		return enemyTeam;
	}




	//////////////////////////////////////////////////////////////////////////////

	// Required methods

	//////////////////////////////////////////////////////////////////////////////


	public Champion getCurrentChampion(){
		return (Champion) turnOrder.peekMin();        // return the champion whose turn is taking place
	}

	public Player checkGameOver(){
		if(firstPlayer.getTeam().size()==0)
			return secondPlayer;
		if(secondPlayer.getTeam().size() == 0)
			return firstPlayer;
		return null;            //none of the players have lost yet
	}

	public void move(Direction direction) throws UnallowedMovementException, NotEnoughResourcesException, ArrayIndexOutOfBoundsException {    //exceptions not implemented yet
			Champion champion = getCurrentChampion();
			// trying a different take on exceptions, seems easier to understand and implement
			if(champion.getCurrentActionPoints() < 1)
				throw new NotEnoughResourcesException("Not enough action points");
			else if(champion.getCondition().equals(Condition.ROOTED))
				throw new UnallowedMovementException("Champion cannot move while rooted");
			else {
				int x = champion.getLocation().x;
				int y = champion.getLocation().y;
				// trying to avoid Array index out of bounds (player tries to move off board)
				try {
					// checks if desired cell is empty before moving
					switch (direction) {
						case RIGHT:
							if (board[x][y + 1] != null)
								throw new UnallowedMovementException("Cannot move to an occupied cell");
							else {
								champion.setLocation(new Point(x, y + 1));
								board[x][y + 1] = champion;
								board[x][y] = null;
							}
							break;
						case LEFT:
							if (board[x][y - 1] != null)
								throw new UnallowedMovementException("Cannot move to an occupied cell");
							else {
								champion.setLocation(new Point(x, y - 1));
								board[x][y - 1] = champion;
								board[x][y] = null;
							}
							break;
						case UP:
							if (board[x + 1][y] != null)
								throw new UnallowedMovementException("Cannot move to an occupied cell");
							else {
								champion.setLocation(new Point(x + 1, y));
								board[x + 1][y] = champion;
								board[x][y] = null;
							}
							break;

						case DOWN:
							if (board[x - 1][y] != null)
								throw new UnallowedMovementException("Cannot move to an occupied cell");
							else {
								champion.setLocation(new Point(x - 1, y));
								board[x - 1][y] = champion;
								board[x][y] = null;
							}
							break;
					}
					champion.setCurrentActionPoints(champion.getCurrentActionPoints() - 1);
				}catch(ArrayIndexOutOfBoundsException e) {
					throw new UnallowedMovementException("Cannot move out of board bounds");
				}
			}
	}

	public void attack(Direction direction) throws NotEnoughResourcesException, ChampionDisarmedException, ArrayIndexOutOfBoundsException, CloneNotSupportedException {
		Champion champion = getCurrentChampion();
		Player enemyPlayer;

		if(firstPlayer.getTeam().contains(champion))
			enemyPlayer = secondPlayer;
		else
			enemyPlayer = firstPlayer;

		boolean disarmed = false;
		// check if champion is disarmed
		for(int i=0;i<champion.getAppliedEffects().size();i++)
			if(champion.getAppliedEffects().get(i) instanceof Disarm)
				disarmed = true;

		if(disarmed)
			throw new ChampionDisarmedException("Champion cannot attack while disarmed");
		else if (champion.getCurrentActionPoints() < 1)
			throw new NotEnoughResourcesException("Not enough action points");
		else {
			int x = champion.getLocation().x;
			int y = champion.getLocation().y;
			int r = champion.getAttackRange();
			Damageable target = null;
			try {
				//looking for the nearest target in the direction within the attack range of the champion
				switch (direction) {
					case RIGHT:

						for (int i = 1; i < r + 1; i++) {
							if (board[x][y + i] instanceof Damageable && (board[x][y + i] instanceof Cover || enemyPlayer.getTeam().contains((Champion) board[x][y + i]))) {             //moving down the board in this direction to find a damageable object, stops when the range has been reached
								target = (Damageable) board[x][y + i];
								break;
							}
						}
						break;
					case LEFT:
						for (int i = 1; i < r + 1; i++) {
							if (board[x][y - i] instanceof Damageable && (board[x][y - i] instanceof Cover || enemyPlayer.getTeam().contains((Champion) board[x][y - i]))) {
								target = (Damageable) board[x][y - i];
								break;
							}
						}
						break;
					case UP:
						for (int i = 1; i < r + 1; i++) {
							if (board[x + i][y] instanceof Damageable && (board[x+i][y] instanceof Cover || enemyPlayer.getTeam().contains((Champion) board[x+i][y]))) {
								target = (Damageable) board[x + i][y];
								break;
							}
						}
						break;
					case DOWN:
						for (int i = 1; i < r + 1; i++) {
							if (board[x - i][y] instanceof Damageable && (board[x-i][y] instanceof Cover || enemyPlayer.getTeam().contains((Champion) board[x-i][y]))) {
								target = (Damageable) board[x - i][y];
								break;
							}
						}
						break;
				}
			}catch(ArrayIndexOutOfBoundsException ignored){
				// Don't think I'm supposed to do anything when this is caught
			}finally{
				champion.setCurrentActionPoints(champion.getCurrentActionPoints() - 2);
				// does no damage if attacking an empty cell
				if (target == null)
					return;
				// does no damage if the attacker and the target are from the same team
				if((firstPlayer.getTeam().contains(champion) && firstPlayer.getTeam().contains(target)) || (secondPlayer.getTeam().contains(champion) && secondPlayer.getTeam().contains(target)))
					return;
				// does no damage if target dodges
				if(target instanceof Champion){
					for(int i=0;i<((Champion) target).getAppliedEffects().size();i++) {
						if (((Champion) target).getAppliedEffects().get(i) instanceof Dodge) {
							// randomly obtain True or False (50% chance of each)
							Random rd = new Random();
							boolean dodgeChance = rd.nextBoolean();
							// if True don't deal damage
							if (dodgeChance)
								return;
						}
					}
					for(int i=0;i<((Champion) target).getAppliedEffects().size();i++) {
						// does no damage if target is shielded
						if(((Champion) target).getAppliedEffects().get(i) instanceof Shield){
							((Champion)target).getAppliedEffects().get(i).remove((Champion)target);
							((Champion)target).getAppliedEffects().remove(i);
							return;
						}
					}
				}
				// does damage when target is an enemy or a cover
				// using a helper method to determine the types of the champion and target and return the multiplication of the damage accordingly
				target.setCurrentHP(target.getCurrentHP() - (int) (champion.getAttackDamage() * damageMultiplier(champion, target)));
				if(target.getCurrentHP() <= 0) {
					removeDamageable(target);
				}
			}
		}
	}


	// Cast ability methods

	// A method for casting an ability that is not limited to a direction or a particular target
	public void castAbility(Ability ability) throws NotEnoughResourcesException, AbilityUseException, ArrayIndexOutOfBoundsException, IllegalStateException, CloneNotSupportedException {
		Champion champion = getCurrentChampion();
		boolean silenced = false;
		// check if champion is silenced
		for(int i=0;i<champion.getAppliedEffects().size();i++)
			if(champion.getAppliedEffects().get(i) instanceof Silence)
				silenced = true;
		if(silenced)
			throw new AbilityUseException("Champion cannot cast abilities while silenced");
		else if (ability.getCurrentCooldown()>0) {
			throw new AbilityUseException("Ability is on cooldown");
		} else if (champion.getCurrentActionPoints() < ability.getRequiredActionPoints())
			throw new NotEnoughResourcesException("Not enough action points");
		else if (champion.getMana() < ability.getManaCost()) {
			throw new NotEnoughResourcesException("Not enough mana");
		} else {
			int x = champion.getLocation().x;
			int y = champion.getLocation().y;
			ArrayList<Damageable> targetedObjects = getTargetedObjects(champion, ability);  //list of valid objects to target, ie. friendly champions if positive ability or covers and enemy champions if neg one
			ArrayList<Damageable> targets = new ArrayList<Damageable>();
			try{
				switch (ability.getCastArea()) {
					case SELFTARGET:
							targets.add(champion);
						break;
					case TEAMTARGET:
						// removed prev logic here bec it was redundant with the way they expect it to work in tests (also it included covers which is nono)
                        for(Champion c : firstPlayer.getTeam()){
                            if(targetedObjects.contains((Damageable) c) && distanceCalculator(champion.getLocation(),c.getLocation().x,c.getLocation().y) <= ability.getCastRange())
                                targets.add(c);
                        }
                        for(Champion c : secondPlayer.getTeam()){
                            if(targetedObjects.contains((Damageable) c) && distanceCalculator(champion.getLocation(),c.getLocation().x,c.getLocation().y) <= ability.getCastRange())
                                targets.add(c);
                        }
						break;
					case SURROUND: {            //akeed there's a more efficient way of tackling this but my pea sized brain simply can not
						try {
							if (board[x + 1][y] != null && targetedObjects.contains((Damageable) board[x + 1][y]))
								targets.add((Damageable) board[x + 1][y]);
						}catch(ArrayIndexOutOfBoundsException ignored){}
						try {
							if (board[x][y + 1] != null && targetedObjects.contains((Damageable) board[x][y + 1]))
								targets.add((Damageable) board[x][y + 1]);
						}catch(ArrayIndexOutOfBoundsException ignored){}
						try {
							if (board[x - 1][y] != null && targetedObjects.contains((Damageable) board[x - 1][y]))
								targets.add((Damageable) board[x - 1][y]);
						}catch(ArrayIndexOutOfBoundsException ignored){}
						try {
							if (board[x][y - 1] != null && targetedObjects.contains((Damageable) board[x][y - 1]))
								targets.add((Damageable) board[x][y - 1]);
						}catch(ArrayIndexOutOfBoundsException ignored){}
						try {
							if (board[x + 1][y + 1] != null && targetedObjects.contains((Damageable) board[x + 1][y + 1]))
								targets.add((Damageable) board[x + 1][y + 1]);
						}catch(ArrayIndexOutOfBoundsException ignored){}
						try {
							if (board[x - 1][y - 1] != null && targetedObjects.contains((Damageable) board[x - 1][y - 1]))
								targets.add((Damageable) board[x - 1][y - 1]);
						}catch(ArrayIndexOutOfBoundsException ignored){}
						try {
							if (board[x + 1][y - 1] != null && targetedObjects.contains((Damageable) board[x + 1][y - 1]))
								targets.add((Damageable) board[x + 1][y - 1]);
						}catch(ArrayIndexOutOfBoundsException ignored){}
						try {
							if (board[x - 1][y + 1] != null && targetedObjects.contains((Damageable) board[x - 1][y + 1]))
								targets.add((Damageable) board[x - 1][y + 1]);
						}catch (ArrayIndexOutOfBoundsException ignored){}
					};
					break;
					default:
						throw new IllegalStateException("Unexpected value: " + ability.getCastArea());    //this was the recommended line when adding the default stmt
				}
			}catch(ArrayIndexOutOfBoundsException ignored){
				// idk what to do here
			}finally {
				ability.execute(targets); //list of targets is passed to the execution method in the ability class
				champion.setCurrentActionPoints(champion.getCurrentActionPoints() - ability.getRequiredActionPoints());
				champion.setMana(champion.getMana() - ability.getManaCost());
				if (ability instanceof DamagingAbility) {
					for (int i = 0; i < targets.size(); i++)
						if (targets.get(i).getCurrentHP() <= 0)
							removeDamageable(targets.get(i));
				}
			}
		}
	}

	// A method for casting ability with DIRECTIONAL area of effect
	public void castAbility(Ability ability, Direction direction) throws NotEnoughResourcesException, AbilityUseException, ArrayIndexOutOfBoundsException, CloneNotSupportedException {
		Champion champion = getCurrentChampion();
		boolean silenced = false;
		// check if champion is silenced
		for(int i=0;i<champion.getAppliedEffects().size();i++)
			if(champion.getAppliedEffects().get(i) instanceof Silence)
				silenced = true;
		if(silenced)
			throw new AbilityUseException("Champion cannot cast abilities while silenced");
		else if (ability.getCurrentCooldown()>0) {
			throw new AbilityUseException("Ability is on cooldown");
		} else if (champion.getCurrentActionPoints() < ability.getRequiredActionPoints())
			throw new NotEnoughResourcesException("Not enough action points");
		else if (ability.getManaCost()> champion.getMana()) {
			throw new NotEnoughResourcesException("Not enough mana");
		} else {
			int x = champion.getLocation().x;
			int y = champion.getLocation().y;
			int r = ability.getCastRange();
			ArrayList<Damageable> targets = new ArrayList<Damageable>();
			ArrayList<Damageable> targetedObjects = getTargetedObjects(champion, ability);
			try{
				switch (direction){                        //the amount of redundant lines in this part makes my brain hurt
					case RIGHT:                    //the movement along the board can probably use a helper method(?) since we use the same logic a lot
						for(int i=1;i<=r;i++){
							if(board[x][y+i]!=null && targetedObjects.contains((Damageable) board[x][y+i]))
								targets.add((Damageable) board[x][y + i]);
						}; break;
					case LEFT:
						for(int i=1;i<=r;i++){
							if(board[x][y-i]!=null && targetedObjects.contains((Damageable) board[x][y-i]))
								targets.add((Damageable) board[x][y - i]);
						}; break;
					case UP:
						for(int i=1;i<=r;i++){
							if(board[x+i][y]!=null && targetedObjects.contains((Damageable) board[x+i][y]))
								targets.add((Damageable) board[x + i][y]);
						}; break;
					case DOWN:
						for(int i=1;i<=r;i++){
							if(board[x-i][y]!=null && targetedObjects.contains((Damageable) board[x-i][y]))
								targets.add((Damageable) board[x-i][y]);
						}; break;
				}
			}catch(ArrayIndexOutOfBoundsException e){
				// idk what to do here either
			}finally {
				ability.execute(targets);
				champion.setCurrentActionPoints(champion.getCurrentActionPoints() - ability.getRequiredActionPoints());
				champion.setMana(champion.getMana() - ability.getManaCost());
				if (ability instanceof DamagingAbility) {
					for (int i = 0; i < targets.size(); i++)
						if (targets.get(i).getCurrentHP() <= 0)
							removeDamageable(targets.get(i));
				}
			}
		}
	}

	// A method for casting an ability with SINGLETARGET area of effect
	// this one does not use up action points if the target is invalid (according to milestone description)
	public void castAbility(Ability ability, int x, int y) throws AbilityUseException, InvalidTargetException, CloneNotSupportedException, NotEnoughResourcesException {
		Champion champion = getCurrentChampion();
		boolean silenced = false;
		// check if champion is silenced
		for(int i=0;i<champion.getAppliedEffects().size();i++)
			if(champion.getAppliedEffects().get(i) instanceof Silence)
				silenced = true;
		if(silenced)
			throw new AbilityUseException("Champion cannot cast abilities while silenced");
		else if (ability.getCurrentCooldown()>0) {
			throw new AbilityUseException("Ability is on cooldown");
		} else if (champion.getCurrentActionPoints() < ability.getRequiredActionPoints())
			throw new NotEnoughResourcesException("Not enough action points");
		else if (ability.getManaCost()> champion.getMana()) {
			throw new NotEnoughResourcesException("Not enough mana");
		} else {
			ArrayList<Damageable> targets = new ArrayList<Damageable>();
			int distance = distanceCalculator(champion.getLocation(), x, y);
			boolean sameTeam = board[x][y] instanceof Champion &&
					((firstPlayer.getTeam().contains((Champion)board[x][y]) && firstPlayer.getTeam().contains(getCurrentChampion())) ||
					(secondPlayer.getTeam().contains((Champion)board[x][y]) && secondPlayer.getTeam().contains(getCurrentChampion())));
			//check that the cell is within the ability's castRange & the cell isn't empty & the object is actually targeted
			if(board[x][y] == null)
				throw new InvalidTargetException("Cannot target empty cell");
			else if((ability instanceof HealingAbility || (ability instanceof CrowdControlAbility && ((CrowdControlAbility)ability).getEffect().getType().equals(EffectType.BUFF))) && !sameTeam)
				throw new InvalidTargetException("Can only cast this ability on an allied champion");
			else if((ability instanceof DamagingAbility || (ability instanceof CrowdControlAbility && ((CrowdControlAbility)ability).getEffect().getType().equals(EffectType.DEBUFF))) && sameTeam)
				throw new InvalidTargetException("Cannot cast this ability on an allied champion");
			else if(ability instanceof CrowdControlAbility && board[x][y] instanceof Cover)
				throw new InvalidTargetException("Cannot cast this ability on a cover");
			else if(distance > ability.getCastRange())
				throw new AbilityUseException("Target out of range");
			else{    //dk if a check should be added to make sure the target is on the right team based on the ability's type
				targets.add((Damageable) board[x][y]);
				champion.setCurrentActionPoints(champion.getCurrentActionPoints()-ability.getRequiredActionPoints());
				champion.setMana(champion.getMana()-ability.getManaCost());
				ability.execute(targets);
				if(ability instanceof DamagingAbility) {
					for (int i = 0; i < targets.size(); i++)
						if (targets.get(i).getCurrentHP() <= 0)
							removeDamageable(targets.get(i));
				}
			}
		}
	}


	public void useLeaderAbility() throws LeaderAbilityAlreadyUsedException, LeaderNotCurrentException, AbilityUseException, CloneNotSupportedException {
		Champion champion = getCurrentChampion();

		Player currPlayer;
		Player enemyPlayer;
		ArrayList<Champion> friendlyTeam;
		ArrayList<Champion> enemyTeam;
		if(champion.equals(firstPlayer.getLeader())) {
			currPlayer = firstPlayer;
			enemyPlayer = secondPlayer;
			if(firstLeaderAbilityUsed)
				throw new LeaderAbilityAlreadyUsedException("Player 1 has already used their leader ability");
		}
		else if(champion.equals(secondPlayer.getLeader())) {
			currPlayer = secondPlayer;
			enemyPlayer = firstPlayer;
			if(secondLeaderAbilityUsed)
				throw new LeaderAbilityAlreadyUsedException("Player 2 has already used their leader ability");
		}
		else
			throw new LeaderNotCurrentException("Can only cast leader ability with team leader");

		friendlyTeam = currPlayer.getTeam();
		enemyTeam = enemyPlayer.getTeam();


		ArrayList<Champion> targets = new ArrayList<Champion>();
			if (champion instanceof Hero) {
				for (int i = 0; i < friendlyTeam.size(); i++) {
					if(friendlyTeam.get(i).getCondition()!=Condition.KNOCKEDOUT)
						targets.add(friendlyTeam.get(i));
				}
			} else if (champion instanceof Villain) {
				for (int i = 0; i < enemyTeam.size(); i++) {
					if(enemyTeam.get(i).getCondition()!=Condition.KNOCKEDOUT && enemyTeam.get(i).getCurrentHP() < (int)(0.3*enemyTeam.get(i).getMaxHP())) {
						targets.add(enemyTeam.get(i));
						removeDamageable(enemyTeam.get(i));
					}
				}
			} else if (champion instanceof AntiHero) {
				for (int i = 0; i < enemyTeam.size(); i++) {
					if (enemyTeam.get(i).getCondition() != Condition.KNOCKEDOUT && !enemyTeam.get(i).equals(enemyPlayer.getLeader())) {
						targets.add(enemyTeam.get(i));
					}
				}
				for (int i = 0; i < friendlyTeam.size(); i++) {
					if (friendlyTeam.get(i).getCondition() != Condition.KNOCKEDOUT && !friendlyTeam.get(i).equals(currPlayer.getLeader())) {
						targets.add(friendlyTeam.get(i));
					}
				}
			}
			champion.useLeaderAbility(targets);
			if(currPlayer == firstPlayer)
				firstLeaderAbilityUsed = true;
			else
				secondLeaderAbilityUsed = true;
		}

    private void prepareChampionTurns () {
        for (int i=0;i<turnOrder.size();i++)
            turnOrder.remove();
        ArrayList<Champion> list = firstPlayer.getTeam();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getCondition() != Condition.KNOCKEDOUT)
                turnOrder.insert(list.get(i));
        }
        list = secondPlayer.getTeam();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getCondition() != Condition.KNOCKEDOUT)
                turnOrder.insert(list.get(i));
        }
    }

	public void endTurn() throws CloneNotSupportedException {
		Player winner = checkGameOver();
		Champion currChamp;
		Condition currChampCondition;
		if (winner == null){
			do {
				turnOrder.remove();		//removes the current champion
				if(turnOrder.isEmpty()) {		//checks if the turn order queue is empty to reset it
					prepareChampionTurns();
				}
				// moved this here bec it needs to update effects/ability cooldown of stunned champions too
				currChamp = getCurrentChampion();
				currChampCondition = currChamp.getCondition();
				// reset the champion's current action points
				currChamp.setCurrentActionPoints(currChamp.getMaxActionPointsPerTurn());
				// decrease the current Cooldown of each ability in the current champion's abilities array list
				for(int i =0; i< currChamp.getAbilities().size(); i++){
					Ability a = currChamp.getAbilities().get(i);
					a.setCurrentCooldown(a.getCurrentCooldown()-1);
				}
				//updating the effect counter for the current champion only
				for(int i =0; i< currChamp.getAppliedEffects().size(); i++){
					currChamp.getAppliedEffects().get(i).setDuration(currChamp.getAppliedEffects().get(i).getDuration() - 1);
					if(currChamp.getAppliedEffects().get(i).getDuration() <= 0){
						currChamp.getAppliedEffects().get(i).remove(currChamp);
						currChamp.getAppliedEffects().remove(i);
						i--;
					}
				}
			}while (currChampCondition.equals(Condition.INACTIVE));              //checks whether the current champion is inactive to remove it until it reaches an active champion
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
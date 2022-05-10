package model.world;

import exceptions.LeaderAbilityAlreadyUsedException;
import exceptions.LeaderNotCurrentException;
import model.abilities.Ability;
import model.effects.Effect;
import model.effects.EffectType;
import model.effects.Embrace;
import model.effects.Stun;

import java.awt.*;
import java.util.ArrayList;

public abstract class Champion implements Damageable, Comparable {
	
	private String name;
	private int maxHP;
	private int currentHP;
	private int mana;
	private int maxActionPointsPerTurn;
	private int currentActionPoints;
	private int attackRange;
	private int attackDamage;
	private int speed;
	private ArrayList<Ability> abilities;
	private ArrayList<Effect> appliedEffects;
	private Condition condition;
	private Point location;

	// not in milestone, just helps
	private boolean disarmed;
	private boolean silenced;
	private boolean shielded;
	private boolean dodging;


	public Champion(String name, int maxHP, int mana, int maxActions, int speed, int attackRange, int attackDamage) {
		this.condition = Condition.ACTIVE;
		this.name = name;
		this.maxHP = maxHP;
		this.currentHP = maxHP;  //starts with the maximum value and decreases with each attack made on the champion
		this.mana = mana;
		this.maxActionPointsPerTurn = maxActions;
		this.currentActionPoints = maxActions;  //starts with the maximum value and decreases with each action the champion makes
		this.speed = speed;
		this.attackRange = attackRange;
		this.attackDamage = attackDamage;
		abilities = new ArrayList<Ability>();
		appliedEffects = new ArrayList<Effect>();
	}
	//////////////////////////////////////////////////////////
	//Catch the rest of the exceptions when they're actually implemented
	//////////////////////////////////////////////////////////
		public void useLeaderAbility(ArrayList<Champion> targets) throws CloneNotSupportedException {
			Champion champion = this;
			// check if "this" would work this way
			if (champion instanceof Hero) {
				for (int i = 0; i < targets.size(); i++) {
					Champion target = targets.get(i);
					for (int j = 0; j < target.getAppliedEffects().size(); j++) {
						// changed this from "Debuff" to EffectType.DEBUFF bec we're not comparing strings
						if (target.getAppliedEffects().get(j).getType().equals(EffectType.DEBUFF)) {
							getAppliedEffects().remove(j);
						}
					}
					// adding embrace effect
					Embrace embrace = new Embrace(2);
					embrace.apply(target);
					target.getAppliedEffects().add(embrace);
				}

			} else if (champion instanceof Villain) {
				for (int i = 0; i < targets.size(); i++) {
					Champion target = targets.get(i);
					if (target.getCurrentHP() < (int) (0.3 * target.getMaxHP())) {
						target.setCurrentHP(0);
					}
				}
			} else if (champion instanceof AntiHero) {
				for (int i = 0; i < targets.size(); i++) {
					Champion target = targets.get(i);
					Stun stun = new Stun(2);
					stun.apply(target);
				}
			}
		}

	
	public int getCurrentHP() {
		return currentHP;
	}

	public void setCurrentHP(int currentHP) {
		if(currentHP<=0) {
			this.currentHP = 0;
			// Knocking out a champion when their hp reaches 0
			// might need to remove later
			this.condition = Condition.KNOCKEDOUT;
		}
		else if(currentHP > maxHP)
			this.currentHP = maxHP;
		else
			this.currentHP = currentHP;
	}

	public int getMaxActionPointsPerTurn() {
		return maxActionPointsPerTurn;
	}

	public void setMaxActionPointsPerTurn(int maxActionPointsPerTurn) {
		this.maxActionPointsPerTurn = maxActionPointsPerTurn;
	}

	public int getAttackDamage() {
		return attackDamage;
	}

	public void setAttackDamage(int attackDamage) {
		this.attackDamage = attackDamage;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public int getMaxHP() {
		return maxHP;
	}

	public int getMana() {
		return mana;
	}

	public void setMana(int mana) { this.mana = mana; }

	public int getCurrentActionPoints() {
		return currentActionPoints;
	}

	public void setCurrentActionPoints(int currentActionPoints) {
		if(currentActionPoints<0)
			this.currentActionPoints = 0;
		else if(currentActionPoints>maxActionPointsPerTurn)
			this.currentActionPoints = maxActionPointsPerTurn;
		else
			this.currentActionPoints = currentActionPoints;
	}

	public int getAttackRange() {
		return attackRange;
	}

	public ArrayList<Ability> getAbilities() {
		return abilities;
	}

	public ArrayList<Effect> getAppliedEffects() {
		return appliedEffects;
	}

	public boolean isDisarmed(){ return disarmed; }
	public void setDisarmed(boolean disarmed){ this.disarmed = disarmed; }

	public boolean isSilenced() { return silenced; }
	public void setSilenced(boolean silenced) { this.silenced = silenced; }

	public boolean isShielded() { return shielded; }
	public void setShielded(boolean shielded) { this.shielded = shielded; }

	public boolean isDodging() { return dodging; }
	public void setDodging(boolean dodging) { this.dodging = dodging; }

	//Lama n-run hanefham bena3mel eh :3
	public int compareTo(Object o) {
		try {
			Champion c = (Champion) o;
			if (this.getSpeed() < c.getSpeed()) {
				return 1;
			} else if (this.getSpeed() > c.getSpeed()) {
				return -1;
			}
			return this.getName().compareTo(c.getName());
		}
		catch(Exception e){
			throw e;
		}
	}

}

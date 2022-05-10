package model.abilities;

import model.effects.Shield;
import model.world.Champion;
import model.world.Damageable;

import java.util.ArrayList;

public class DamagingAbility extends Ability{
	
	private int damageAmount;
	
	public DamagingAbility (String name, int cost, int baseCoolDown, int castRange, AreaOfEffect area, int required, int damageAmount){
		super(name, cost, baseCoolDown, castRange, area, required);
		this.damageAmount = damageAmount;
	}

	public int getDamageAmount() {
		return damageAmount;
	}

	public void setDamageAmount(int damageAmount) {
		this.damageAmount = damageAmount;
	}

	public void execute(ArrayList<Damageable> targets) throws CloneNotSupportedException {
		boolean shield = false;
		this.setCurrentCooldown(this.getBaseCooldown());
		for(int i=0; i < targets.size(); i++){
			Damageable damageable = targets.get(i);
			if(damageable instanceof Champion) {
				for (int j = 0; j < ((Champion) damageable).getAppliedEffects().size(); j++)
					if (((Champion) damageable).getAppliedEffects().get(j) instanceof Shield) {
						((Champion) damageable).getAppliedEffects().get(j).remove((Champion) targets.get(i));
						((Champion) damageable).getAppliedEffects().remove(j);
						shield = true;
						break;
					}
			}
			if(!shield)
				damageable.setCurrentHP(damageable.getCurrentHP() - this.damageAmount);	//deal the damage amount to the champion or cover
		}
	}
}

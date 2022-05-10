package model.world;

import model.effects.Stun;

import java.util.ArrayList;

public class AntiHero extends Champion{

	public AntiHero(String name, int maxHP, int mana, int maxActions, int speed, int attackRange, int attackDamage) {
		super(name, maxHP, mana, maxActions, speed, attackRange, attackDamage);
	}

	@Override
	public void useLeaderAbility(ArrayList<Champion> targets) throws CloneNotSupportedException {
		AntiHero champion = this;
		for (int i = 0; i < targets.size(); i++) {
			Champion target = targets.get(i);
			Stun stun = new Stun(2);
			stun.apply(target);
			target.getAppliedEffects().add(stun);
		}
	}


}

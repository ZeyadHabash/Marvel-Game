package model.world;

import model.effects.Effect;
import model.effects.EffectType;
import model.effects.Embrace;

import java.util.ArrayList;

public class Hero extends Champion{

	public Hero(String name, int maxHP, int mana, int maxActions, int speed, int attackRange, int attackDamage) {
		super(name, maxHP, mana, maxActions, speed, attackRange, attackDamage);
	}

	@Override
	public void useLeaderAbility(ArrayList<Champion> targets) throws CloneNotSupportedException {
		Hero champion = this;
		for (int i = 0; i < targets.size(); i++) {
			Champion target = targets.get(i);
			for (int j = 0; j < target.getAppliedEffects().size(); j++) {
				// changed this from "Debuff" to EffectType.DEBUFF bec we're not comparing strings
				Effect effect = target.getAppliedEffects().get(j);
				if (effect.getType() == EffectType.DEBUFF) {
					effect.remove(target);
					target.getAppliedEffects().remove(effect);
					j--;
				}
			}
			// adding embrace effect
			Embrace embrace = new Embrace(2);
			embrace.apply(target);
			target.getAppliedEffects().add(embrace);
		}
	}

}

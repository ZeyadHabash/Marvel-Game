package model.abilities;

import exceptions.EffectNotAppliedException;
import model.effects.Effect;
import model.world.Champion;
import model.world.Damageable;

import java.util.ArrayList;

public class CrowdControlAbility extends Ability{

    private Effect effect;

    public CrowdControlAbility(String name, int cost, int baseCoolDown, int castRange, AreaOfEffect area, int required, Effect effect) {
        super(name, cost, baseCoolDown, castRange, area, required);
        this.effect = effect;
    }

    public Effect getEffect() {
        return effect;
    }

    @Override
    public void execute(ArrayList<Damageable> targets) throws EffectNotAppliedException, CloneNotSupportedException {
        for(int i=0; i < targets.size(); i++){
            Champion a = (Champion) targets.get(i);
            a.getAppliedEffects().add(this.effect);     //adds the ability's effect to the champion's appliedEffects array list
            this.effect.apply(a);                       //applies the ability's effect on the champion
        }
    }
}

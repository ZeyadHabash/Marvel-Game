package model.effects;

import model.world.Champion;
import model.world.Condition;

public class Stun extends Effect {
    public Stun(int duration) {
        super("Stun", duration, EffectType.DEBUFF);
    }

    @Override
    public void apply(Champion c) throws CloneNotSupportedException {
        super.apply(c);
        for(int i=0;i<c.getAppliedEffects().size();i++)
            if(c.getAppliedEffects().get(i).getName().equals(this.getName()))
                return;
        if(c.getCondition() != Condition.KNOCKEDOUT)
            c.setCondition(Condition.INACTIVE);
    }

    // need to handle if there's a root/stun with longer duration later
    @Override
    public void remove(Champion c) throws CloneNotSupportedException {
        super.remove(c);
        for(int i=0;i<c.getAppliedEffects().size();i++)
            if(c.getAppliedEffects().get(i).getName().equals(this.getName()))
                return;
        if(c.getCondition() != Condition.KNOCKEDOUT)
            c.setCondition(Condition.ACTIVE);
    }

    public Stun clone() throws CloneNotSupportedException{
        Stun clone = null;
        try{
            clone = (Stun) super.clone();
        }catch (CloneNotSupportedException cns){
            throw new CloneNotSupportedException("Cannot clone this Object");
        }
        return clone;
    }
}

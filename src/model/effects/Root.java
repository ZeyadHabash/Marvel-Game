package model.effects;

import model.world.Champion;
import model.world.Condition;

public class Root extends Effect{
	
	public Root(int duration) {
		super("Root", duration, EffectType.DEBUFF);
	}

	@Override
	public void apply(Champion c) throws CloneNotSupportedException {
		if(c.getCondition() == Condition.ACTIVE)
			c.setCondition(Condition.ROOTED);
	}

	@Override
	public void remove(Champion c) throws CloneNotSupportedException {
		for(int i=0;i<c.getAppliedEffects().size();i++)
			if(c.getAppliedEffects().get(i) instanceof Root && c.getAppliedEffects().get(i).getAppliedCounter()<c.getAppliedEffects().get(i).getDuration())
				return;
		if(c.getCondition() == Condition.ROOTED)
			c.setCondition(Condition.ACTIVE);
	}

	public Object clone() throws CloneNotSupportedException{
		Root clone = null;
		try{
			clone = (Root) super.clone();
		}catch (CloneNotSupportedException cns){
			throw new CloneNotSupportedException("Cannot clone this Object");
		}
		return clone;
	}
}

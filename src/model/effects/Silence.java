package model.effects;

import model.world.Champion;

public class Silence  extends Effect{
	
	public Silence (int duration) {
		super("Silence", duration, EffectType.DEBUFF);
	}


	// handle the "can't use abilities" part later
	@Override
	public void apply(Champion c) throws CloneNotSupportedException {
		super.apply(c);
		c.setSilenced(true);
		c.setCurrentActionPoints(c.getCurrentActionPoints()+2);
		c.setMaxActionPointsPerTurn(c.getMaxActionPointsPerTurn()+2);
	}

	@Override
	public void remove(Champion c) throws CloneNotSupportedException {
		super.remove(c);
		c.setCurrentActionPoints(c.getCurrentActionPoints()-2);
		c.setMaxActionPointsPerTurn(c.getMaxActionPointsPerTurn()-2);
		for(int i=0;i<c.getAppliedEffects().size();i++)
			if(c.getAppliedEffects().get(i).getName().equals(this.getName()))
				return;
		c.setSilenced(false);
	}


	public Silence clone() throws CloneNotSupportedException{
		Silence clone = null;
		try{
			clone = (Silence) super.clone();
		}catch (CloneNotSupportedException cns){
			throw new CloneNotSupportedException("Cannot clone this Object");
		}
		return clone;
	}
}

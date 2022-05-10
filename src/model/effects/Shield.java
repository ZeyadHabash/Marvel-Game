package model.effects;

import model.world.Champion;

public class Shield extends Effect{
	
	public Shield (int duration) {
		super("Shield", duration, EffectType.BUFF);
	}


	// handle the blocking of next attack later
	@Override
	public void apply(Champion c) throws CloneNotSupportedException {
		super.apply(c);
		c.setShielded(true);
		c.setSpeed((int) Math.round(c.getSpeed()*1.02));
	}

	@Override
	public void remove(Champion c) throws CloneNotSupportedException {
		super.remove(c);
		c.setSpeed((int) Math.round(c.getSpeed()/1.02));
		for(int i=0;i<c.getAppliedEffects().size();i++)
			if(c.getAppliedEffects().get(i).getName().equals(this.getName()))
				return;
		c.setShielded(false);
	}

	public Shield clone() throws CloneNotSupportedException{
		Shield clone = null;
		try{
			clone = (Shield) super.clone();
		}catch (CloneNotSupportedException cns){
			throw new CloneNotSupportedException("Cannot clone this Object");
		}
		return clone;
	}
}

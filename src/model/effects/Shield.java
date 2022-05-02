package model.effects;

import model.world.Champion;

public class Shield extends Effect{
	
	public Shield (int duration) {
		super("Shield", duration, EffectType.BUFF);
	}


	// handle the blocking of next attack later
	@Override
	public void apply(Champion c) {
		c.setSpeed((int)(c.getSpeed()*1.02));
	}

	@Override
	public void remove(Champion c) {
		c.setSpeed((int)(c.getSpeed()/1.02));
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

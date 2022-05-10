package model.effects;

import model.world.Champion;

public class SpeedUp extends Effect{
	
	public SpeedUp(int duration) {
		super("SpeedUp", duration, EffectType.BUFF);
	}

	@Override
	public void apply(Champion c) throws CloneNotSupportedException {
		c.setSpeed((int) (c.getSpeed()*1.15));
		c.setCurrentActionPoints(c.getCurrentActionPoints()+1);
		c.setMaxActionPointsPerTurn(c.getMaxActionPointsPerTurn()+1);
	}

	@Override
	public void remove(Champion c) throws CloneNotSupportedException {
		c.setSpeed((int) (c.getSpeed()/1.15));
		c.setCurrentActionPoints(c.getCurrentActionPoints()-1);
		c.setMaxActionPointsPerTurn(c.getMaxActionPointsPerTurn()-1);
	}

	public SpeedUp clone() throws CloneNotSupportedException{
		SpeedUp clone = null;
		try{
			clone = (SpeedUp) super.clone();
		}catch (CloneNotSupportedException cns){
			throw new CloneNotSupportedException("Cannot clone this Object");
		}
		return clone;
	}
}

package model.effects;

public class PowerUp extends Effect {
	
	public PowerUp(int duration) {
		super("PowerUp", duration, EffectType.BUFF);
	}
	public PowerUp clone() throws CloneNotSupportedException{
		PowerUp clone = null;
		try{
			clone = (PowerUp) super.clone();
		}catch (CloneNotSupportedException cns){
			throw new CloneNotSupportedException("Cannot clone this Object");
		}
		return clone;
	}
}

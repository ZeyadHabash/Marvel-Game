package model.effects;

public class SpeedUp extends Effect{
	
	public SpeedUp(int duration) {
		super("SpeedUp", duration, EffectType.BUFF);
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

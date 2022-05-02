package model.effects;

public class Silence  extends Effect{
	
	public Silence (int duration) {
		super("Silence", duration, EffectType.DEBUFF);
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

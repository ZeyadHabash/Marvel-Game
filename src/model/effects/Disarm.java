package model.effects;

public class Disarm extends Effect {
	
	public Disarm (int duration) {
		super("Disarm", duration, EffectType.DEBUFF);
	}
	public Disarm clone() throws CloneNotSupportedException{
		Disarm clone = null;
		try{
			clone = (Disarm) super.clone();
		}catch (CloneNotSupportedException cns){
			throw new CloneNotSupportedException("Cannot clone this Object");
		}
		return clone;
	}
}

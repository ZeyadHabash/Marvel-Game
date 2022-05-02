package model.effects;

public class Shield extends Effect{
	
	public Shield (int duration) {
		super("Shield", duration, EffectType.BUFF);
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

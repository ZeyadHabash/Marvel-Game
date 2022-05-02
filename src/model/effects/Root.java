package model.effects;

public class Root extends Effect{
	
	public Root(int duration) {
		super("Root", duration, EffectType.DEBUFF);
	}
	public Root clone() throws CloneNotSupportedException{
		Root clone = null;
		try{
			clone = (Root) super.clone();
		}catch (CloneNotSupportedException cns){
			throw new CloneNotSupportedException("Cannot clone this Object");
		}
		return clone;
	}
}

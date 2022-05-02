package model.effects;

public class Shock extends Effect{

    public Shock(int duration) {
        super("Shock", duration, EffectType.DEBUFF);
    }
    public Shock clone() throws CloneNotSupportedException{
        Shock clone = null;
        try{
            clone = (Shock) super.clone();
        }catch (CloneNotSupportedException cns){
            throw new CloneNotSupportedException("Cannot clone this Object");
        }
        return clone;
    }
}

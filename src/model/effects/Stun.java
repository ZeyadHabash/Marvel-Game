package model.effects;

public class Stun extends Effect {
    public Stun(int duration) {
        super("Stun", duration, EffectType.DEBUFF);
    }
    public Stun clone() throws CloneNotSupportedException{
        Stun clone = null;
        try{
            clone = (Stun) super.clone();
        }catch (CloneNotSupportedException cns){
            throw new CloneNotSupportedException("Cannot clone this Object");
        }
        return clone;
    }
}

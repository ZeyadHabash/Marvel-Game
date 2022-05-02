package model.effects;

public class Dodge extends Effect{

    public Dodge(int duration) {
        super("Dodge", duration, EffectType.BUFF);
    }
    public Dodge clone() throws CloneNotSupportedException{
        Dodge clone = null;
        try{
            clone = (Dodge) super.clone();
        }catch (CloneNotSupportedException cns){
            throw new CloneNotSupportedException("Cannot clone this Object");
        }
        return clone;
    }
}

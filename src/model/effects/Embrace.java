package model.effects;

public class Embrace extends Effect{
    public Embrace(int duration) {
        super("Embrace", duration, EffectType.BUFF);
    }
    public Embrace clone() throws CloneNotSupportedException{
        Embrace clone = null;
        try{
            clone = (Embrace) super.clone();
        }catch (CloneNotSupportedException cns){
            throw new CloneNotSupportedException("Cannot clone this Object");
        }
        return clone;
    }
}

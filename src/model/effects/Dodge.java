package model.effects;

import exceptions.EffectNotAppliedException;
import model.world.Champion;

public class Dodge extends Effect{

    public Dodge(int duration) {
        super("Dodge", duration, EffectType.BUFF);
    }


    // handle 50% dodge chance part later
    @Override
    public void apply(Champion c) throws CloneNotSupportedException {
        try {
            super.apply(c);
        }catch(EffectNotAppliedException e) {
            int currSpeed = c.getSpeed();
            int newSpeed = (int) (currSpeed * 1.05);
            c.setSpeed(newSpeed);
        }
    }

    @Override
    public void remove(Champion c) throws CloneNotSupportedException {
        super.remove(c);
        int currSpeed = c.getSpeed();
        int newSpeed = (int)(currSpeed / 1.05);
        c.setSpeed(newSpeed);
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

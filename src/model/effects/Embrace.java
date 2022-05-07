package model.effects;

import exceptions.EffectNotAppliedException;
import model.world.Champion;

public class Embrace extends Effect{
    public Embrace(int duration) {
        super("Embrace", duration, EffectType.BUFF);
    }

    @Override
    public void apply(Champion c) throws CloneNotSupportedException {
        // Permanent
        int newHP = c.getCurrentHP() + (int)(c.getMaxHP()*0.2);
        c.setCurrentHP(newHP);

        int newMana = (int)(c.getMana()*1.2);
        c.setMana(newMana);

        // Temporary
        try {
            super.apply(c);
        }catch(EffectNotAppliedException e) {
            int newSpeed = (int)(c.getSpeed()*1.2);
            int newDamage = (int)(c.getAttackDamage()*1.2);
            c.setSpeed(newSpeed);
            c.setAttackDamage(newDamage);
        }
    }

    @Override
    public void remove(Champion c) throws CloneNotSupportedException {
        // Remove Temporary buffs
        super.remove(c);
        int newSpeed = (int)(c.getSpeed()/1.2);
        int newDamage = (int)(c.getAttackDamage()/1.2);
        c.setSpeed(newSpeed);
        c.setAttackDamage(newDamage);
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

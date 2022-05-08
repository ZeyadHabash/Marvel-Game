package model.effects;

import model.world.Champion;

public class Shock extends Effect{

    public Shock(int duration) {
        super("Shock", duration, EffectType.DEBUFF);
    }

    @Override
    public void apply(Champion c) throws CloneNotSupportedException {
        super.apply(c);
        c.setSpeed((int) Math.round(c.getSpeed()/1.1));
        c.setAttackDamage((int) Math.round(c.getAttackDamage()/1.1));
        c.setCurrentActionPoints(c.getCurrentActionPoints()-1);
        c.setMaxActionPointsPerTurn(c.getMaxActionPointsPerTurn()-1);
    }

    @Override
    public void remove(Champion c) throws CloneNotSupportedException {
        super.remove(c);
        c.setSpeed((int) Math.round(c.getSpeed()*1.1));
        c.setAttackDamage((int) Math.round(c.getAttackDamage()*1.1));
        c.setCurrentActionPoints(c.getCurrentActionPoints()+1);
        c.setMaxActionPointsPerTurn(c.getMaxActionPointsPerTurn()+1);
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

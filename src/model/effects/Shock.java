package model.effects;

import model.world.Champion;

public class Shock extends Effect{

    public Shock(int duration) {
        super("Shock", duration, EffectType.DEBUFF);
    }

    @Override
    public void apply(Champion c) throws CloneNotSupportedException {
        c.setSpeed((int) (c.getSpeed()*0.9));
        c.setAttackDamage((int) (c.getAttackDamage()*0.9));
        c.setCurrentActionPoints(c.getCurrentActionPoints()-1);
        c.setMaxActionPointsPerTurn(c.getMaxActionPointsPerTurn()-1);
    }

    @Override
    public void remove(Champion c) throws CloneNotSupportedException {
        c.setSpeed((int) (c.getSpeed()/0.9));
        c.setAttackDamage((int) (c.getAttackDamage()/0.9));
        c.setCurrentActionPoints(c.getCurrentActionPoints()+1);
        c.setMaxActionPointsPerTurn(c.getMaxActionPointsPerTurn()+1);
    }

    public Object clone() throws CloneNotSupportedException{
        Shock clone = null;
        try{
            clone = (Shock) super.clone();
        }catch (CloneNotSupportedException cns){
            throw new CloneNotSupportedException("Cannot clone this Object");
        }
        return clone;
    }
}

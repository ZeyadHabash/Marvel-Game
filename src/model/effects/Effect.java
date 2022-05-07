package model.effects;

import exceptions.EffectNotAppliedException;
import model.world.Champion;

import java.util.ArrayList;

public abstract class Effect implements Cloneable {
	private String name;
	private int duration;
	private EffectType type;
	
	public Effect (String name, int duration, EffectType type) {
		this.name = name;
		this.duration = duration;
		this.type = type;
	}

	// will prob need to take another look at apply and remove in case duration doesn't stack or if buffs/debuffs stack
	public void apply(Champion champion) throws CloneNotSupportedException, EffectNotAppliedException{
		Effect effect = this.clone();
		// only stacking the duration for same effects
		for(int i=0;i<champion.getAppliedEffects().size();i++){
			Effect currEffect = champion.getAppliedEffects().get(i);
			if(currEffect.getName().equals(effect.getName())) {
				currEffect.setDuration(currEffect.getDuration() + effect.getDuration());
				return;
			}
		}
		// if effect wasn't already applied just add it
		champion.getAppliedEffects().add(effect);
		// throws an exception so it can be caught in subclass and if effect is not already applied, apply it
		throw new EffectNotAppliedException();
	}
	public void remove(Champion champion) throws CloneNotSupportedException {
		Effect effect = this.clone();

		// all of this will have to change if effects do indeed stack (or if duration doesn't stack)
		for(int i=0;i<champion.getAppliedEffects().size();i++){
			ArrayList<Effect> champEffects = champion.getAppliedEffects();
			if(champEffects.get(i).getName().equals(effect.getName())) {
				champEffects.remove(i);
				return;
			}
		}
	}

	// not sure if this is correct, just testing stuff out
	public Effect clone() throws CloneNotSupportedException{
		Effect clone;
		try{
			clone = (Effect) super.clone();
			clone.duration = duration;
			clone.name = name;
			clone.type = type;
		}catch (CloneNotSupportedException cns){
			throw new CloneNotSupportedException("Cannot clone this Object");
		}
		return clone;
	}

	public String getName() { return name; }

	public int getDuration() { return duration; }

	public void setDuration(int duration) { this.duration = duration; }

	public EffectType getType() { return type; }

}

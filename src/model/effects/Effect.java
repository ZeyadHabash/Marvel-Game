package model.effects;

import model.world.Champion;

import java.util.ArrayList;

public abstract class Effect implements Cloneable {
	private String name;
	private int duration;
	private EffectType type;

	private int appliedCounter;

	public Effect (String name, int duration, EffectType type) {
		this.name = name;
		this.duration = duration;
		this.type = type;
	}


	public void apply(Champion champion) throws CloneNotSupportedException{
		Effect effect = this.clone();
		champion.getAppliedEffects().add(effect);
	}
	public void remove(Champion champion) throws CloneNotSupportedException {
		Effect effect = this.clone();
		ArrayList<Effect> champEffects = champion.getAppliedEffects();

		for(int i=0;i<champEffects.size();i++){
			if(champEffects.get(i).getName().equals(effect.getName())) {
				champEffects.remove(i);
				return;
			}
		}
	}

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

	public int getAppliedCounter() { return appliedCounter; }

	public void setAppliedCounter(int appliedCounter) {
		this.appliedCounter = appliedCounter;
	}

	public void increaseAppliedCounter() {
		this.appliedCounter++;
	}

}

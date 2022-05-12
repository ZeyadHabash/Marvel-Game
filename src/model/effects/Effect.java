package model.effects;

import model.world.Champion;

public abstract class Effect implements Cloneable {
	private String name;
	private int duration;
	private EffectType type;


	public Effect (String name, int duration, EffectType type) {
		this.name = name;
		this.duration = duration;
		this.type = type;
	}


	public abstract void apply(Champion champion) throws CloneNotSupportedException;
		/*
		Effect effect = this.clone();
		champion.getAppliedEffects().add(effect);
		*/

	public abstract void remove(Champion champion) throws CloneNotSupportedException;
		/*
		Effect effect = this.clone();
		ArrayList<Effect> champEffects = champion.getAppliedEffects();

		for(int i=0;i<champEffects.size();i++){
			if(champEffects.get(i).getName().equals(effect.getName())) {
				champEffects.remove(i);
				return;
			}
		}
		 */

	@Override
	public Object clone() throws CloneNotSupportedException{
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

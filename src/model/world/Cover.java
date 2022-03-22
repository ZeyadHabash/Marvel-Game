package model.world;

import java.awt.*;

public class Cover {
	
	private int currentHP;
	private Point location;
	
	public Cover (int x, int y) {
		this.currentHP = (int) (Math.random() * (1000 - 100)) + 100;
	    location = new Point(x,y);
	}

	public int getCurrentHP() {
		return currentHP;
	}

	public void setCurrentHP(int currentHP) {
		this.currentHP = currentHP;
	}

	public Point getLocation() {
		return location;
	}
	
}

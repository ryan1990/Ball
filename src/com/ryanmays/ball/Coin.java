package com.ryanmays.ball;

import android.graphics.Color;

public class Coin {
	public int type;
	private int speed;
	public boolean hit;
	public static float WIDTH = 40;
    public static float HEIGHT = 40;
    float x, y;
    
    public Coin(int type, float x, float y) {
    	this.type = type;
    	this.x = x;
    	this.y = y;
    	this.speed = 0;
    	this.hit = false;
    }
    
    public int getColor() {
    	int red = 10;
    	int green = 0;
    	int blue = 50;
    	
    	if (speed > 0) {
    		green += getSpeedAdjustedForColor(speed)*12;
    	} else if (speed < 0) {
    		red += getSpeedAdjustedForColor(-1*speed)*12;
    	}
    	
    	return new Color().rgb(red, green, blue);
    }
    
    public int getSpeed() {
    	return this.speed;
    }
    
    public void setSpeed(int speed) {
    	// keep speed between -10 and 10
    	if (speed > 10) this.speed = 10;
    	else if (speed < -10) this.speed = -10;
    	else this.speed = speed;
    }
    
    // returns a modified speed to improve color contrast
    private float getSpeedAdjustedForColor(int speed) {
    	float startingValue = 3;
    	int max = 10;
    	float difference = max - startingValue;
    	
    	return startingValue + ((1.0f*speed)/(1.0f*max))*difference;
    }
}

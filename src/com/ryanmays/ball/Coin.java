package com.ryanmays.ball;

import android.graphics.Color;

public class Coin {
	public int type;
	public boolean visible;
	public int speed;
	public static float WIDTH = 40;
    public static float HEIGHT = 40;
    float x, y;
    
    public Coin(int type, float x, float y, boolean visible) {
    	this.type = type;
    	this.x = x;
    	this.y = y;
    	this.visible = visible;
    	this.speed = 0;
    }
    
    public int getColor() {
    	int red = 10;
    	int green = 0;
    	int blue = 50;
    	
    	if (speed > 0) {
    		green += getSpeedAdjustedForColor(speed)*10;
    	} else if (speed < 0) {
    		red += (-1*speed)*20;
    	}
    	
    	return new Color().rgb(red, green, blue);
    }
    
    private float getSpeedAdjustedForColor(int speed) {
    	float startingValue = 3;
    	int max = 10;
    	float difference = max - startingValue;
    	
    	return startingValue + ((1.0f*speed)/(1.0f*max))*difference;
    }
}

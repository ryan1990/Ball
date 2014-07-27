package com.ryanmays.ball;

import android.graphics.Color;
import android.util.Log;

public class Coin {
	public int type;
	private float speed;
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
    
    public float getColor() {
    	float red = 10;
    	float green = 0;
    	float blue = 50;
    	
    	if (speed > 0) {
    		green += getSpeedAdjustedForColor(speed)*13;
    	} else if (speed < 0) {
    		red += getSpeedAdjustedForColor(-1*speed)*13;
    	}
    	
    	Log.d("MyApp","COLOR r="+red+", g="+green+"b="+blue);
    	return new Color().rgb((int)red, (int)green, (int)blue);
    }
    
    public float getSpeed() {
    	return this.speed;
    }
    
    public void setSpeed(float speed) {
    	// keep speed between -10 and 10
    	if (speed > 10) this.speed = 10;
    	else if (speed < -10) this.speed = -10;
    	else this.speed = speed;
    }
    
    // returns a modified speed to improve color contrast
    private float getSpeedAdjustedForColor(float speed) {
    	float startingValue = 3;
    	float max = 10;
    	float difference = max - startingValue;
    	
    	return startingValue + (speed/max)*difference;
    }
}

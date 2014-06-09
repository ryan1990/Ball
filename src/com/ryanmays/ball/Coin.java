package com.ryanmays.ball;

public class Coin {
	public int type;
	public boolean visible;
	public static float WIDTH = 40;
    public static float HEIGHT = 40;
    float x, y;
    
    public Coin(int type, float x, float y, boolean visible) {
    	this.type = type;
    	this.x = x;
    	this.y = y;
    	this.visible = visible;
    }
}

package com.ryanmays.ball;

import java.util.Random;

import android.util.Log;

public class Matrix {	
	public int width;
	public int heightFull;
	public int windowHeight;
	public int pixelsInBlock;
	//float x = 160-WIDTH/2, y = 50;
	float y = 10;
	public float initvy = 40;
    float vy = initvy;
    float acceleration = 2;
	public Coin[][] array;
	
	public Matrix(int width, int heightFull, int windowHeight, int pixelsInBlock) {
		this.width = width;
		this.heightFull = heightFull;
		this.windowHeight = windowHeight;
		this.pixelsInBlock = pixelsInBlock;
		
		this.array = new Coin[heightFull][width];
		fill();
	}
	
	private void fill() {
		for(int row=0; row<5; row++) {
			for(int col=0; col<width; col++) {
				array[row][col] = new Coin(-1, col*pixelsInBlock, row*pixelsInBlock, false);
			}
		}
		for(int row=5; row<heightFull; row++) {
			boolean rowIsEmpty = true; // used to ensure only one coin is placed on each row
			// pick random column to place death coin on
			Random rand = new Random();
			int randomColumn = rand.nextInt(width);
			array[row][randomColumn] = new Coin(0, randomColumn*pixelsInBlock, row*pixelsInBlock, true);
			
			for(int col=0; col<width; col++) {
				// -1 : empty space
				//  0 : death coin
				//  1 : bronze coin
				//  2 : silver coin
				//  3 : gold coin
				if (col!=randomColumn) { // make sure this isn't the random column we placed death coin on
					if (prob(100)) {
						array[row][col] = new Coin(3, col*pixelsInBlock, row*pixelsInBlock, true);
						//rowIsEmpty = false;
					} else if (prob(30)) {
						array[row][col] = new Coin(2, col*pixelsInBlock, row*pixelsInBlock, true);
						//rowIsEmpty = false;
					} else if (prob(10)) {
						array[row][col] = new Coin(1, col*pixelsInBlock, row*pixelsInBlock, true);
						//rowIsEmpty = false;
					} else {
						array[row][col] = new Coin(-1, col*pixelsInBlock, row*pixelsInBlock, false);
					}
				}
				/*
				if (col%2==0 && row%2==0) {
					array[row][col] = new Coin(1, col*pixelsInBlock, row*pixelsInBlock);
				} else {
					array[row][col] = new Coin(0, col*pixelsInBlock, row*pixelsInBlock);
				}
				if (col==0) {
					array[row][col] = new Coin(2, col*pixelsInBlock, row*pixelsInBlock);
				}
				if (col==1) {
					array[row][col] = new Coin(4, col*pixelsInBlock, row*pixelsInBlock);
				}
				if (col==2) {
					array[row][col] = new Coin(3, col*pixelsInBlock, row*pixelsInBlock);
				}
				if (row==0) {
					array[row][col] = new Coin(0, col*pixelsInBlock, row*pixelsInBlock);
				}
				*/
			}
		}
		//array[0][0].visible = false;
	}
	
	private boolean prob(int range) {
		Random rand = new Random();
		int n = rand.nextInt(range);
		return n == 0;
	}
	
	public int windowHeightPixels() {
		return pixelsInBlock * windowHeight;
	}
	
	public int getTopRow() {
		return (int)y / pixelsInBlock;
	}
	
	public int getOffset() {
		return (int)y % pixelsInBlock;
	}
}

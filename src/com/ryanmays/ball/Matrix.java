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
	public float initvy = 30;
    float vy = initvy;
    float acceleration = .3f;
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
		Random rand = new Random();
		for(int row=5; row<heightFull; row++) {
			//boolean rowIsEmpty = true; // used to ensure only one coin is placed on each row
			int maxPerRow = row/50 + 2;
			if (maxPerRow > 3) maxPerRow = 3;
			int n = rand.nextInt(maxPerRow+1);
			
			// pick random column to place death coin on
			boolean[] rowArray = randomPlacementOnRow(n, width);
			//array[row][randomColumn] = new Coin(0, randomColumn*pixelsInBlock, row*pixelsInBlock, true);
			
			for(int col=0; col<width; col++) {
				// -1 : empty space
				//  0 : death coin
				//  1 : bronze coin
				//  2 : silver coin
				//  3 : gold coin
				
				if (rowArray[col] == true) {
					array[row][col] = new Coin(0, col*pixelsInBlock, row*pixelsInBlock, true);
					continue; // if we have placed death coin here, move on
				}
				
				if (prob(50)) {
					array[row][col] = new Coin(3, col*pixelsInBlock, row*pixelsInBlock, true);
					//rowIsEmpty = false;
				} else if (prob(15)) {
					array[row][col] = new Coin(2, col*pixelsInBlock, row*pixelsInBlock, true);
					//rowIsEmpty = false;
				} else if (prob(5)) {
					array[row][col] = new Coin(1, col*pixelsInBlock, row*pixelsInBlock, true);
					//rowIsEmpty = false;
				} else {
					array[row][col] = new Coin(-1, col*pixelsInBlock, row*pixelsInBlock, false);
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
	
	private boolean[] randomPlacementOnRow(int number, int width) {
		if (number>width) {
			throw new IllegalArgumentException("EXCEPTION: number>width");
		}
		boolean[] row = new boolean[width];
		if (number==width) {
			for(int i=0; i<width; i++) {
				row[i] = true;
			}
			return row;
		}
		
		Random rand = new Random();
		
		for(int i=0; i<width; i++) {
			row[i] = false;
		}
		
		
		for(int i=0; i<number; i++) {
			int n = rand.nextInt(width);
			for(int j=0; j<width; j++) {
				if (row[n] == false) {
					row[n] = true;
					break;
				}
				if (n<width-1) {
					n++;
				} else {
					n = 0;
				}
			}
		}
		return row;
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

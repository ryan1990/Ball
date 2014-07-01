package com.ryanmays.ball;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.graphics.Point;
import android.util.Log;

public class Matrix {	
	public int width;
	public int heightFull;
	public int windowHeight;
	public int pixelsInBlock;
	//float x = 160-WIDTH/2, y = 50;
	float y = 10;
    float vy;
    float acceleration = 0;//.3f;
	public Coin[][] array;
	int[][] levels;
	int lastKnownLevel;
	List<Point> leftConnected;
	List<Point> rightConnected;
	
	public Matrix(int width, int heightFull, int windowHeight, int pixelsInBlock) {
		this.width = width;
		this.heightFull = heightFull;
		this.windowHeight = windowHeight;
		this.pixelsInBlock = pixelsInBlock;
		
		this.array = new Coin[heightFull][width];
		this.levels = new int[30][2];
		this.lastKnownLevel = -1;
		
		this.leftConnected = new ArrayList<Point>(); // stores coordinates of blocks connected to left side
		this.rightConnected = new ArrayList<Point>(); // stores coordinates of blocks connected to right side
		
		buildLevels();
		fill();
	}
	
	// set up each level that will be stored in levels array
	private void buildLevels() {
		int velocity = 50;
		int maxBlocks = 2;
		for(int i=0; i<levels.length/3; i++) {
			for(int j=0; j<3; j++) {
				levels[i*3+j][0] = velocity;
				levels[i*3+j][1] = maxBlocks;
				
				velocity += 20;
				//maxBlocks -= 1;
			}
			
			//velocity += 100;
			maxBlocks = 2;
		}
	}
	///////////////////////////////////
	
	// game runs while matrix built!!!
	
	//////////////////////////////////
	private void fill() {
		Log.d("MyApp", "FILLED");
		for(int row=0; row<heightFull/*5*/; row++) {
			for(int col=0; col<width; col++) {
				array[row][col] = new Coin(-1, col*pixelsInBlock, row*pixelsInBlock, false);
			}
		}
		
		if (isSpotLegal(6,0)) { Log.d("MyApp", "60 legal"); array[6][0].type = 0;}// = new Coin(0, 0*pixelsInBlock, 6*pixelsInBlock, true);
		if (isSpotLegal(6,1)) { Log.d("MyApp", "61 legal"); array[6][1].type = 0;}// = new Coin(0, 1*pixelsInBlock, 6*pixelsInBlock, true);
		if (isSpotLegal(6,2)) { Log.d("MyApp", "62 legal"); array[6][2].type = 0;}// = new Coin(0, 2*pixelsInBlock, 6*pixelsInBlock, true);
		if (isSpotLegal(6,3)) { Log.d("MyApp", "63 legal"); array[6][3].type = 0;}// = new Coin(0, 3*pixelsInBlock, 6*pixelsInBlock, true);
		if (isSpotLegal(6,4)) { Log.d("MyApp", "64 legal"); array[6][4].type = 0;}// = new Coin(0, 4*pixelsInBlock, 6*pixelsInBlock, true);
		if (isSpotLegal(6,5)) { Log.d("MyApp", "65 legal"); array[6][5].type = 0;}// = new Coin(0, 5*pixelsInBlock, 6*pixelsInBlock, true);
		
		if (isSpotLegal(6,7)) { Log.d("MyApp", "67 legal"); array[6][7].type = 0;}// = new Coin(0, 7*pixelsInBlock, 6*pixelsInBlock, true);
		if (isSpotLegal(6,6)) { Log.d("MyApp", "66 legal"); array[6][6].type = 0;}// = new Coin(0, 6*pixelsInBlock, 6*pixelsInBlock, true);
		
		/*
		if (foundLeftConnection(6, 0, 0, new ArrayList<Point>())) Log.d("MyApp", "FOUND 6 0");
		if (foundLeftConnection(6, 1, 0, new ArrayList<Point>())) Log.d("MyApp", "FOUND 6 1");
		if (foundLeftConnection(6, 2, 0, new ArrayList<Point>())) Log.d("MyApp", "FOUND 6 3");
		*/
		
		
		
		if (true) return;
		Random rand = new Random();
		int level = 0;
		int maxPerRow = levels[level][1];
		for(int row=5; row<heightFull; row++) {
			//boolean rowIsEmpty = true; // used to ensure only one coin is placed on each row
			if (row % 100 == 0) { // we have advanced 100 rows, time to move up a level
				level++;
				maxPerRow = levels[level][1];
			}
			
			//if (maxPerRow > 3) maxPerRow = 3;
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
				if (row <= 10) {
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
				} else {
					array[row][col] = new Coin(-1, col*pixelsInBlock, row*pixelsInBlock, false);
				}
			}
		}
		//array[0][0].visible = false;
	}
	
	// adds a block if placing a block here will not complete a trail
	// of adjacently and diagonally connected blocks to an edge
	/*
	private void addBlockIfAppropriate(int initRow, int initCol) {
		
		// base case
		
		// recursive case
		// if exists, then explore
		if (initRow >= 0 && initRow >= upperLimit)
		if (initCol >= 0 && initCol < this.width) {
			
			if (array[initRow][initCol].type != -1) {
				
			}
		}
	}
	*/
	// return whether placing a block here would still allow player to be unblocked
	private boolean isSpotLegal(int row, int col) {
		int upperLimit = row-8;
		if (upperLimit < 0) upperLimit = 0;
		return !(foundLeftConnection(row, col, upperLimit, new ArrayList<Point>(), true) && foundRightConnection(row, col, upperLimit, new ArrayList<Point>(), true));
	}
	
	private boolean foundLeftConnection(int row, int col, int upperLimit, ArrayList<Point> exploredBlocks, boolean isRoot) {
		// base cases
		if (row < 0 || row < upperLimit) {
			Log.d("MyApp", "a1");
			return true;
		} // changed???
		if (col < 0 || col >= this.width) {
			Log.d("MyApp", "a2");
			return false;
		}
		if (array[row][col].type == -1 && !isRoot) {
			Log.d("MyApp", "a3 : row="+row+", col="+col);
			return false;
		}
		Log.d("MyApp", "NOW");
		// CHECK CONTAINS METHOD!!!
		if (exploredBlocks.contains(new Point(row, col))) {
			Log.d("MyApp", "a4");
			return false;
		}
		if (col == 0 || leftConnected.contains(new Point(row, col))) {
			exploredBlocks.add(new Point(row, col));
			leftConnected.add(new Point(row, col));
			Log.d("MyApp", "a5");
			return true;
		} else { // explore all 8 surrounding spots
			Log.d("MyApp", "a6");
			exploredBlocks.add(new Point(row, col));
			boolean isConnected = foundLeftConnection(row, col-1, upperLimit, exploredBlocks, false)
					|| foundLeftConnection(row-1, col-1, upperLimit, exploredBlocks, false)
					|| foundLeftConnection(row-1, col, upperLimit, exploredBlocks, false)
					|| foundLeftConnection(row-1, col+1, upperLimit, exploredBlocks, false)
					|| foundLeftConnection(row, col+1, upperLimit, exploredBlocks, false)
					|| foundLeftConnection(row+1, col+1, upperLimit, exploredBlocks, false)
					|| foundLeftConnection(row+1, col, upperLimit, exploredBlocks, false)
					|| foundLeftConnection(row+1, col-1, upperLimit, exploredBlocks, false);
			Log.d("MyApp", "isConnected Left = "+isConnected+", row="+row+", col="+col);
			if (isConnected) {
				leftConnected.add(new Point(row, col));
				Log.d("MyApp", "a61");
				return true;
			} else {
				Log.d("MyApp", "a62");
				return false;
			}
		}
	}
	
	private boolean foundRightConnection(int row, int col, int upperLimit, ArrayList<Point> exploredBlocks, boolean isRoot) {
		Log.d("MyApp", "RIGHT");
		// base cases
		if (row < 0 || row < upperLimit) return true; // changed???
		if (col < 0 || col >= this.width) return false;
		if (array[row][col].type == -1 && !isRoot) return false;
		
		// CHECK CONTAINS METHOD!!!
		if (exploredBlocks.contains(new Point(row, col))) return false;
		if (col == this.width-1 || rightConnected.contains(new Point(row, col))) {
			exploredBlocks.add(new Point(row, col));
			rightConnected.add(new Point(row, col));
			return true;
		} else { // explore all 8 surrounding spots
			Log.d("MyApp", "YO");
			exploredBlocks.add(new Point(row, col));
			boolean isConnected = foundRightConnection(row, col-1, upperLimit, exploredBlocks, false)
					|| foundRightConnection(row-1, col-1, upperLimit, exploredBlocks, false)
					|| foundRightConnection(row-1, col, upperLimit, exploredBlocks, false)
					|| foundRightConnection(row-1, col+1, upperLimit, exploredBlocks, false)
					|| foundRightConnection(row, col+1, upperLimit, exploredBlocks, false)
					|| foundRightConnection(row+1, col+1, upperLimit, exploredBlocks, false)
					|| foundRightConnection(row+1, col, upperLimit, exploredBlocks, false)
					|| foundRightConnection(row+1, col-1, upperLimit, exploredBlocks, false);
			Log.d("MyApp", "isConnected Right = "+isConnected+", row="+row+", col="+col);
			if (isConnected) {
				rightConnected.add(new Point(row, col));
				return true;
			} else {
				return false;
			}
		}
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
	
	public void updateMovement(float deltaTime) {
		if (getLevel() > lastKnownLevel) {
			vy = levels[getLevel()][0];
			lastKnownLevel = getLevel();
			Log.d("MyApp", "NEXT LEVEL!");
		}
	    y += vy*deltaTime;
	    vy += acceleration*deltaTime;
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
	
	public int getLevel() {
		return getTopRow() / 100;
	}
}

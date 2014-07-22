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
	public Coin[][] levelArray;
	float[][] levels;
	int lastKnownLevel;
	public int currentLevel;
	List<Point> leftConnected;
	List<Point> rightConnected;
	
	public Matrix(int width, int heightFull, int windowHeight, int pixelsInBlock) {
		this.width = width;
		this.heightFull = heightFull;
		this.windowHeight = windowHeight;
		this.pixelsInBlock = pixelsInBlock;
		
		this.levelArray = new Coin[heightFull][width];
		this.levels = new float[30][2];
		this.lastKnownLevel = -1;
		this.currentLevel = -1;
		
		this.leftConnected = new ArrayList<Point>(); // stores coordinates of blocks connected to left side
		this.rightConnected = new ArrayList<Point>(); // stores coordinates of blocks connected to right side
		
		buildLevels();
	}
	
	// set up each level that will be stored in levels array
	private void buildLevels() {
		// 0
		levels[0][0] = 70;
		levels[0][1] = 1;
		// 1
		levels[1][0] = 110;
		levels[1][1] = 1;
		// 2
		levels[2][0] = 60;
		levels[2][1] = 2.5f;
		// 3
		levels[3][0] = 160;
		levels[3][1] = 1.5f;
		// 4
		levels[4][0] = 70;
		levels[4][1] = 3;
		// 5
		levels[5][0] = 200;
		levels[5][1] = 1.5f;
		// 6
		levels[6][0] = 110;
		levels[6][1] = 3.5f;
		// 7
		levels[7][0] = 240;
		levels[7][1] = 1.5f;
		// 8
		levels[8][0] = 130;
		levels[8][1] = 3.5f;
		// 9
		levels[9][0] = 260;
		levels[9][1] = 1.5f;
		// 10
		levels[10][0] = 160;
		levels[10][1] = 3;
		// 11
		levels[11][0] = 280;
		levels[11][1] = 1.5f;
		// 12
		levels[12][0] = 170;
		levels[12][1] = 3;
		/*
		levels[0][0] = 90;
		levels[0][1] = 3;
		
		levels[0][0] = 110;
		levels[0][1] = 4; //good
		
		levels[4][0] = 150;
		levels[4][1] = 1.5f;
		/*
		levels[5][0] = 50;
		levels[5][1] = 3;
		*/
		
		/*
		int velocity = 80;
		int averageBlocks = 0;
		for(int i=0; i<levels.length/3; i++) {
			for(int j=0; j<3; j++) {
				levels[i*3+j][0] = velocity;
				levels[i*3+j][1] = averageBlocks;
				
				velocity += 10;
				averageBlocks = 7;
			}
			
			//velocity += 100;
			//averageBlocks = 2;
		}
		*/
	}
	
	
	///////////////////////////////////
	
	// game runs while matrix built!!!
	
	//////////////////////////////////
	
	// adds and sets up the next level
	public void buildNextLevel() {
		currentLevel++;
		
		for(int row=0; row<heightFull; row++) {
			for(int col=0; col<width; col++) {
				levelArray[row][col] = new Coin(-1, col*pixelsInBlock, row*pixelsInBlock);
			}
		}
		/*
		if (isSpotLegal(6,0)) { Log.d("MyApp", "60 legal"); array[6][0].type = 0;}// = new Coin(0, 0*pixelsInBlock, 6*pixelsInBlock, true);
		if (isSpotLegal(6,1)) { Log.d("MyApp", "61 legal"); array[6][1].type = 0;}// = new Coin(0, 1*pixelsInBlock, 6*pixelsInBlock, true);
		if (isSpotLegal(6,2)) { Log.d("MyApp", "62 legal"); array[6][2].type = 0;}// = new Coin(0, 2*pixelsInBlock, 6*pixelsInBlock, true);
		if (isSpotLegal(6,3)) { Log.d("MyApp", "63 legal"); array[6][3].type = 0;}// = new Coin(0, 3*pixelsInBlock, 6*pixelsInBlock, true);
		if (isSpotLegal(6,4)) { Log.d("MyApp", "64 legal"); array[6][4].type = 0;}// = new Coin(0, 4*pixelsInBlock, 6*pixelsInBlock, true);
		if (isSpotLegal(6,5)) { Log.d("MyApp", "65 legal"); array[6][5].type = 0;}// = new Coin(0, 5*pixelsInBlock, 6*pixelsInBlock, true);
		
		if (isSpotLegal(6,7)) { Log.d("MyApp", "67 legal"); array[6][7].type = 0;}// = new Coin(0, 7*pixelsInBlock, 6*pixelsInBlock, true);
		if (isSpotLegal(6,6)) { Log.d("MyApp", "66 legal"); array[6][6].type = 0;}// = new Coin(0, 6*pixelsInBlock, 6*pixelsInBlock, true);
		*/
		
		/*
		if (foundLeftConnection(6, 0, 0, new ArrayList<Point>())) Log.d("MyApp", "FOUND 6 0");
		if (foundLeftConnection(6, 1, 0, new ArrayList<Point>())) Log.d("MyApp", "FOUND 6 1");
		if (foundLeftConnection(6, 2, 0, new ArrayList<Point>())) Log.d("MyApp", "FOUND 6 3");
		*/
		vy = levels[currentLevel][0];
		float averagePerRow = levels[currentLevel][1];
		Random rand = new Random();
		
		for(int row=5; row<heightFull-windowHeight; row++) {
			//boolean rowIsEmpty = true; // used to ensure only one coin is placed on each row
			
			//if (maxPerRow > 3) maxPerRow = 3;
			
			int numberOnRow = rand.nextInt((int) ((averagePerRow*2)+1));
			if (numberOnRow > 8) numberOnRow = 8;
			// pick random column to place death coin on
			boolean[] rowArray = randomPlacementOnRow(numberOnRow, width);
			//array[row][randomColumn] = new Coin(0, randomColumn*pixelsInBlock, row*pixelsInBlock, true);
			
			for(int col=0; col<width; col++) {
				// -1 : empty space
				//  0 : death coin
				//  1 : bronze coin
				//  2 : silver coin
				//  3 : gold coin
				
				if (rowArray[col]) {
					if (isSpotLegal(row, col)) {
						levelArray[row][col] = new Coin(0, col*pixelsInBlock, row*pixelsInBlock);
					}
				} else { // !rowArray[col]
					if (prob(50)) {
						levelArray[row][col] = new Coin(3, col*pixelsInBlock, row*pixelsInBlock);
						//rowIsEmpty = false;
					} else if (prob(15)) {
						levelArray[row][col] = new Coin(2, col*pixelsInBlock, row*pixelsInBlock);
						//rowIsEmpty = false;
					} else if (prob(5)) {
						levelArray[row][col] = new Coin(1, col*pixelsInBlock, row*pixelsInBlock);
						//rowIsEmpty = false;
					} else {
						levelArray[row][col] = new Coin(-1, col*pixelsInBlock, row*pixelsInBlock);
					}
				}
				//levelArray[row][col].speed = 0-col;
				//if (col == 6) levelArray[row][col].speed = -9;
				//if (col == 7) levelArray[row][col].speed = -10;
			}
		}
		//createSpeedRectangle(0,this.heightFull/2,this.width,10,.15f,.15f,3);
		
		//createSpeedRectangle(7,10,1,40,.75f,10,4);
		//buildAlternatingRectangles();
		//buildConcave();
		buildSpine();
		
		
		/*
		createSpeedRectangle(1,20,2,8,1,1,-2);
		createSpeedRectangle(5,20,2,8,4,4,8);
		
		createSpeedRectangle(1,30,2,8,4,4,8);
		createSpeedRectangle(5,30,2,8,1,1,-2);
		
		createSpeedRectangle(1,40,2,8,1,1,-2);
		createSpeedRectangle(5,40,2,8,4,4,8);
		
		createSpeedRectangle(1,50,2,8,4,4,8);
		createSpeedRectangle(5,50,2,8,1,1,-2);
		
		createSpeedRectangle(1,60,2,8,1,1,-2);
		createSpeedRectangle(5,60,2,8,4,4,8);
		*/
		
		//createSpeedRectangle(0,20,this.width,1,1,1,8); // less death blocks in green?
		//createSpeedRectangle(0,40,this.width,1,1,1,-8);
		
		
		//createSpeedRectangle(5,10,2,3,1,1,4);
		//createSpeedRectangle(4,20,2,3,1,1,-10);
		//createSpeedRectangle(5,30,2,3,1,1,10);
	}
	
	private void buildAlternatingRectangles() {
		createSpeedRectangle(1,10,2,5,2,2,4);
		createSpeedRectangle(0,10+6,4,3,0,0,-2);
		
		createSpeedRectangle(1+4,20,2,5,2,2,4);
		createSpeedRectangle(0+4,20+6,4,3,0,0,-2);
		
		createSpeedRectangle(1,30,2,5,2,2,4);
		createSpeedRectangle(0,30+6,4,3,0,0,-2);
		
		createSpeedRectangle(1+4,40,2,5,2,2,4);
		createSpeedRectangle(0+4,40+6,4,3,0,0,-2);
		
		createSpeedRectangle(1,50,2,5,2,2,4);
		createSpeedRectangle(0,50+6,4,3,0,0,-2);
		
		createSpeedRectangle(1+4,60,2,5,2,2,4);
		createSpeedRectangle(0+4,60+6,4,3,0,0,-2);
	}
	
	private void buildConcave() {
		createSpeedRectangle(0,10,1,this.heightFull-20,1,0,2);
		createSpeedRectangle(7,10,1,this.heightFull-20,1,0,2);
	}
	
	private void buildSpine() {
		createSpeedRectangle(3,10,2,this.heightFull-20,0,0,1);
	}
	
	// return whether placing a block here would still allow player to be unblocked
	private boolean isSpotLegal(int row, int col) {
		int upperLimit = row-8;
		if (upperLimit < 0) upperLimit = 0;
		return !(foundLeftConnection(row, col, upperLimit, new ArrayList<Point>(), true) && foundRightConnection(row, col, upperLimit, new ArrayList<Point>(), true));
	}
	
	private boolean foundLeftConnection(int row, int col, int upperLimit, ArrayList<Point> exploredBlocks, boolean isRoot) {
		// base cases
		if (row >= levelArray.length) return false;
		if (row < 0 || row < upperLimit) {
			//Log.d("MyApp", "a1");
			return true;
		} // changed???
		if (col < 0 || col >= this.width) {
			//Log.d("MyApp", "a2");
			return false;
		}
		if (levelArray[row][col].type != 0 && !isRoot) { // type is NULL!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			//Log.d("MyApp", "a3 : row="+row+", col="+col);
			return false;
		}
		//Log.d("MyApp", "NOW");
		// CHECK CONTAINS METHOD!!!
		if (exploredBlocks.contains(new Point(row, col))) {
			//Log.d("MyApp", "a4");
			return false;
		}
		if (col == 0 || leftConnected.contains(new Point(row, col))) {
			exploredBlocks.add(new Point(row, col));
			leftConnected.add(new Point(row, col));
			//Log.d("MyApp", "a5");
			return true;
		} else {
			exploredBlocks.add(new Point(row, col));
			boolean isConnected;
			if (isRoot) { // only explore up and left, since these are the only place the blocks could be
				isConnected = foundLeftConnection(row, col-1, upperLimit, exploredBlocks, false)
						|| foundLeftConnection(row-1, col-1, upperLimit, exploredBlocks, false)
						|| foundLeftConnection(row-1, col, upperLimit, exploredBlocks, false)
						|| foundLeftConnection(row-1, col+1, upperLimit, exploredBlocks, false);
			} else { // explore all 8 surrounding spots
				isConnected = foundLeftConnection(row, col-1, upperLimit, exploredBlocks, false)
						|| foundLeftConnection(row-1, col-1, upperLimit, exploredBlocks, false)
						|| foundLeftConnection(row-1, col, upperLimit, exploredBlocks, false)
						|| foundLeftConnection(row-1, col+1, upperLimit, exploredBlocks, false)
						|| foundLeftConnection(row, col+1, upperLimit, exploredBlocks, false)
						|| foundLeftConnection(row+1, col+1, upperLimit, exploredBlocks, false)
						|| foundLeftConnection(row+1, col, upperLimit, exploredBlocks, false)
						|| foundLeftConnection(row+1, col-1, upperLimit, exploredBlocks, false);
			}
			if (isConnected) {
				leftConnected.add(new Point(row, col));
				//Log.d("MyApp", "a61");
				return true;
			} else {
				//Log.d("MyApp", "a62");
				return false;
			}
		}
	}
	
	private boolean foundRightConnection(int row, int col, int upperLimit, ArrayList<Point> exploredBlocks, boolean isRoot) {
		//Log.d("MyApp", "RIGHT");
		// base cases
		if (row >= levelArray.length) return false;
		if (row < 0 || row < upperLimit) return true; // changed???
		if (col < 0 || col >= this.width) return false;
		if (levelArray[row][col].type != 0 && !isRoot) return false;
		
		// CHECK CONTAINS METHOD!!!
		if (exploredBlocks.contains(new Point(row, col))) return false;
		if (col == this.width-1 || rightConnected.contains(new Point(row, col))) {
			exploredBlocks.add(new Point(row, col));
			rightConnected.add(new Point(row, col));
			return true;
		} else {
			boolean isConnected;
			if (isRoot) { // only explore up and left, since these are the only place the blocks could be
				exploredBlocks.add(new Point(row, col));
				isConnected = foundRightConnection(row, col-1, upperLimit, exploredBlocks, false)
						|| foundRightConnection(row-1, col-1, upperLimit, exploredBlocks, false)
						|| foundRightConnection(row-1, col, upperLimit, exploredBlocks, false)
						|| foundRightConnection(row-1, col+1, upperLimit, exploredBlocks, false);
			} else { // explore all 8 surrounding spots
				exploredBlocks.add(new Point(row, col));
				isConnected = foundRightConnection(row, col-1, upperLimit, exploredBlocks, false)
						|| foundRightConnection(row-1, col-1, upperLimit, exploredBlocks, false)
						|| foundRightConnection(row-1, col, upperLimit, exploredBlocks, false)
						|| foundRightConnection(row-1, col+1, upperLimit, exploredBlocks, false)
						|| foundRightConnection(row, col+1, upperLimit, exploredBlocks, false)
						|| foundRightConnection(row+1, col+1, upperLimit, exploredBlocks, false)
						|| foundRightConnection(row+1, col, upperLimit, exploredBlocks, false)
						|| foundRightConnection(row+1, col-1, upperLimit, exploredBlocks, false);
			}
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
	
	// modifies the speed property of certain coins to create areas shaded red or green
	private void createSpeedRectangle(int coreX, int coreY, int coreWidth, int coreHeight, float horDecay, float vertDecay, int magnitude) {
		// build core of blocks with speed of magnitude
		for(int col=coreX; col<coreX+coreWidth; col++) {
			for(int row=coreY; row<coreY+coreHeight; row++) {
				Coin c = levelArray[row][col];
				c.setSpeed(c.getSpeed() + magnitude);
			}
		}
		
		boolean isMagnitudePositive = (magnitude >= 0);
		if (!isMagnitudePositive) magnitude *= -1;
		
		if (horDecay!=0) {
			// build surrounding region that decays in magnitude as it moves outward
			// build left region
			float leftRemaining = magnitude-horDecay;
			int col = coreX-1;
			int row;
			int sectionHeight;
			if (vertDecay!=0) {
				row = coreY-1;
				sectionHeight = coreHeight+2;
			} else {
				row = coreY;
				sectionHeight = coreHeight;
			}
			while(leftRemaining > 0) {
				if (col < 0) break; // don't continue past left side of matrix
				int localRow = row;
				while(localRow < row+sectionHeight) {
					if (row >= 0 && row < this.heightFull) {
						Coin c = levelArray[localRow][col];
						// set speed positive if magnitude parameter was positive
						if (isMagnitudePositive) c.setSpeed(c.getSpeed() + (int)leftRemaining);
						// set speed negative if magnitude parameter was negative
						else c.setSpeed(c.getSpeed() + -1*(int)leftRemaining);
					}
					localRow++;
				}
				leftRemaining -= horDecay;
				col--;
				if (vertDecay!=0) {
					row--;
					sectionHeight += 2;
				}
			}
			
			// build right region
			float rightRemaining = magnitude-horDecay;
			col = coreX+coreWidth;
			
			if (vertDecay!=0) {
				row = coreY-1;
				sectionHeight = coreHeight+2;
			}
			else {
				row = coreY;
				sectionHeight = coreHeight;
			}
			while(rightRemaining > 0) {
				if (col >= this.width) break; // don't continue past right side of matrix
				int localRow = row;
				while(localRow < row+sectionHeight) {
					if (row >= 0 && row < this.heightFull) {
						Coin c = levelArray[localRow][col];
						if (isMagnitudePositive) c.setSpeed(c.getSpeed() + (int)rightRemaining);
						else c.setSpeed(c.getSpeed() + -1*(int)rightRemaining);
					}
					localRow++;
				}
				rightRemaining -= horDecay;
				col++;
				if (vertDecay!=0) {
					row--;
					sectionHeight += 2;
				}
			}
		}
		
		if (vertDecay!=0) {
			// build top region
			float topRemaining = magnitude-vertDecay;
			int col = coreX;
			int row = coreY-1;
			int sectionWidth = coreWidth;
			
			while(topRemaining > 0) {
				if (row < 0) break; // don't continue past top of matrix
				int localCol = col;
				while(localCol < col+sectionWidth) {
					if (localCol >= 0 && localCol < this.width) {
						Coin c = levelArray[row][localCol];
						if (isMagnitudePositive) c.setSpeed(c.getSpeed() + (int)topRemaining);
						else c.setSpeed(c.getSpeed() + -1*(int)topRemaining);
					}
					localCol++;
				}
				topRemaining -= vertDecay;
				row--;
				if (horDecay!=0) {
					col--;
					sectionWidth += 2;
				}
			}
			
			// build bottom region
			float bottomRemaining = magnitude-vertDecay;
			col = coreX;
			row = coreY+coreHeight;
			
			sectionWidth = coreWidth;
			
			while(bottomRemaining > 0) {
				if (row >= this.heightFull) break; // don't continue past bottom of matrix
				int localCol = col;
				while(localCol < col+sectionWidth) {
					if (localCol >= 0 && localCol < this.width) {
						Coin c = levelArray[row][localCol];
						if (isMagnitudePositive) c.setSpeed(c.getSpeed() + (int)bottomRemaining);
						else c.setSpeed(c.getSpeed() + -1*(int)bottomRemaining);
					}
					localCol++;
				}
				bottomRemaining -= vertDecay;
				row++;
				if (horDecay!=0) {
					col--;
					sectionWidth += 2;
				}
			}
		}
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
	    
	    // see if we hit bottom of level
	    if (getTopRow() >= heightFull-windowHeight-1) {
	    	Log.d("MyApp", "ENDED");
	    	y = 0;
	    	buildNextLevel();
	    }
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

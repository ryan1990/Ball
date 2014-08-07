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
	public List<Level> levels;
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
		this.levels = new ArrayList<Level>();//new float[30][2];
		this.lastKnownLevel = -1;
		this.currentLevel = -1;
		
		this.leftConnected = new ArrayList<Point>(); // stores coordinates of blocks connected to left side
		this.rightConnected = new ArrayList<Point>(); // stores coordinates of blocks connected to right side
				
		buildLevels();
	}
	
	public class Level {
		public float velocity;
		public float density;
		
		public Level(float velocity, float density) {
			this.velocity = velocity;
			this.density = density;
		}
	}
	
	// high density, slow.  high density => low movement choice, have bigger patches. patch speed is harsh.
	
		// less dense, big patches, tough color
		
		// less dense, fast, long patches
			// Concave
			// Spine
			// Lines
		
		// less dense, slow, tough patches // small or big
			// Spots
			// Checkers
		
		
		/* Blank
		 * 
		 * AlternatingRectangles
		 * Concave - G 180, 1f
		 * Spine - G
		 * Spots - G 80, 1.5f
		 * Roller
		 * Checkers - G 80, 1.5f
		 * Blobs
		 * CenteredBlobs
		 * Fader
		 * Lines - G
		 */
	
	// set up each level that will be stored in levels array
	private void buildLevels() {
		// 80,1.5 blank
		
		// fast // spine
		
		// slow // checkers
		
		// fast // concave
		
		// 100,2.5 blank
		
		// slow spots, altern
		
		levels.add(new Level(80,1.5f));
		levels.add(new Level(120,1));
		levels.add(new Level(100,2f));
		levels.add(new Level(180,1));
		
		/*
		levels.add(new Level(23280,0));
		levels.add(new Level(333120,0));
		levels.add(new Level(333100,0));
		levels.add(new Level(333222,0));
		levels.add(new Level(332333,0));
		*/
		
		levels.add(new Level(70,2));
		levels.add(new Level(100,2.5f));
		levels.add(new Level(50000,0));
		levels.add(new Level(50000,0));
		levels.add(new Level(50000,0));
		levels.add(new Level(100,2.5f));//60,2.5f centered blobs //100,1 blobs
		levels.add(new Level(110,1.5f));
	}
	
	
	///////////////////////////////////
	
	// game runs while matrix built!!!
	
	//////////////////////////////////
	
	// adds and sets up the next level
	public void buildNextLevel() {
		List<Point> greenBlocks20 = new ArrayList<Point>();
		
		currentLevel++;
		
		for(int row=0; row<heightFull; row++) {
			for(int col=0; col<width; col++) {
				levelArray[row][col] = new Coin(-1, col*pixelsInBlock, row*pixelsInBlock);
			}
		}
		
		vy = levels.get(currentLevel).velocity;
		float averagePerRow = levels.get(currentLevel).density;
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
					if (prob20(currentLevel)) {
						levelArray[row][col] = new Coin(4, col*pixelsInBlock, row*pixelsInBlock);
						greenBlocks20.add(new Point(col,row));
					} else if (prob(50)) {
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
			}
		}
		
		buildSpeedBlockMap(currentLevel);
		buildBlocks20(greenBlocks20);
	}
	
	private void buildSpeedBlockMap(int level) {
		switch (level) {
			case 1:
				buildSpine();
				break;
			case 2:
				buildCheckers();
				break;
			case 3:
				buildConcave();//buildCenteredBlobs();//buildCheckers();
				break;
			case 4:
				//buildConcave();
				break;
			case 5:
				buildAlternatingRectangles(); //buildSpots();
				break;
		}
	}
	
	private void buildBlocks20(List<Point> greenBlocks20) {
		for (int i=0; i<greenBlocks20.size(); i++) {
			/*
			Point block = greenBlocks20.get(i);
			createSpeedRectangle(0,0,1,3,4,4,8);
			createSpeedRectangle(this.width-3, -1,2,2,0,0,7);
			*/
			Point block = greenBlocks20.get(i);
			
			if (block.x >= 0 && block.x < this.width && block.y >= 0 && block.y < this.heightFull) {
				if (block.x >= 1) { // left side death blocks
					levelArray[block.y-2][block.x-1].type = -1;
					levelArray[block.y-1][block.x-1].type = 0;
					levelArray[block.y][block.x-1].type = 0;
					levelArray[block.y+1][block.x-1].type = 0;
					levelArray[block.y-4][block.x-1].type = 0;
				}
				if (block.x <= this.width-2) { // right side death blocks
					levelArray[block.y-2][block.x+1].type = -1;
					levelArray[block.y-1][block.x+1].type = 0;
					levelArray[block.y][block.x+1].type = 0;
					levelArray[block.y+1][block.x+1].type = 0;
					levelArray[block.y-4][block.x+1].type = 0;
				}
				
				if (block.y >= 4) { // top death blocks
					levelArray[block.y-4][block.x].type = 0;
					levelArray[block.y-3][block.x].type = 0;
					levelArray[block.y-2][block.x].type = -1; // make blank spots above coin
					levelArray[block.y-1][block.x].type = -1;
				}
				if (block.y <= this.heightFull-4) { // make blank spots below coin
					levelArray[block.y+1][block.x].type = -1;
					levelArray[block.y+2][block.x].type = -1;
					levelArray[block.y+3][block.x].type = -1;
				}
			}
		}
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
		createSpeedRectangle(0,10,1,this.heightFull-20,2,0,6);
		createSpeedRectangle(7,10,1,this.heightFull-20,2,0,6);
	}
	
	private void buildSpine() {
		createSpeedRectangle(3,10,2,this.heightFull-20,0,0,3);
		
		createSpeedRectangle(0,10,this.width,4,0,0,10);
		createSpeedRectangle(0,25,this.width,4,0,0,-10);
		
		createSpeedRectangle(0,35,this.width,2,0,0,-10);
		createSpeedRectangle(0,45,this.width,2,0,0,10);
	}
	
	private void buildSpots() {
		createSpeedRectangle(0,10,this.width,this.heightFull-20,0,0,2);
		createSpeedRectangle(0,12,1,2,2,2,-6);
		createSpeedRectangle(5,15,2,1,2,2,8);
		createSpeedRectangle(5,20,2,5,2,2,-6);
		createSpeedRectangle(3,27,3,1,2,2,8);
		createSpeedRectangle(0,33,2,7,2,2,-4);
		createSpeedRectangle(5,42,2,1,2,2,6);
		createSpeedRectangle(4,48,3,7,1,1,-2);
		createSpeedRectangle(0,60,2,6,2,2,-4);
		//createSpeedRectangle(2,5,2,2,0,0,-6f);
	}
	
	private void buildRoller() {
		createSpeedRectangle(0,15,this.width,4,7,5,10);
		createSpeedRectangle(0,30,this.width,4,7,5,-10);
		createSpeedRectangle(0,45,this.width,4,7,5,10);
		//createSpeedRectangle(0,,this.width,6,5,5,-10);
	}
	
	private void buildCheckers() {
		createSpeedRectangle(0,10,this.width,this.heightFull,0,0,-2);
		for (int i=10; i<70; i+=8) {
			//createSpeedRectangle(0,i,4,4,0,0,1);
			//createSpeedRectangle(4,i+4,4,4,0,0,1);
			
			createSpeedRectangle(0,i,4,4,0,0,5);
			createSpeedRectangle(4,i+4,4,4,0,0,5);
			
			createSpeedRectangle(1,i+1,2,2,0,0,3);
			createSpeedRectangle(5,i+5,2,2,0,0,3);
		}
	}
	
	private void buildBlobs() {
		createSpeedRectangle(0,0,this.width,this.heightFull,0,0,2);
		
		createSpeedRectangle(6,5,2,3,.4f,.5f,-3);
		createSpeedRectangle(6,15,2,3,.25f,.25f,-2);
		createSpeedRectangle(1,20,2,2,1,.5f,-3);
		createSpeedRectangle(6,27,1,2,3,2,6);
		createSpeedRectangle(3,30,1,2,.5f,.3f,2);
		createSpeedRectangle(4,33,1,1,.75f,.5f,-2);
		createSpeedRectangle(7,37,2,6,1,1,-3);
		createSpeedRectangle(0,45,1,6,2,1.5f,-5);
		createSpeedRectangle(7,45,1,6,2,1.5f,-5);
		createSpeedRectangle(1,50,1,8,.5f,.5f,-3);
		createSpeedRectangle(7,57,1,2,.3f,.5f,2);
		// FIX 20 appearing too early
	}
	
	private void buildCenteredBlobs() {
		createSpeedRectangle(0,0,this.width,this.heightFull,0,0,3);
		
		createSpeedRectangle(3,10,2,2,.75f,.75f,-4);
		createSpeedRectangle(2,20,3,3,1,1,-4);
		createSpeedRectangle(3,30,2,2,.75f,.75f,-5);
		createSpeedRectangle(3,40,2,2,0,0,-5);
		createSpeedRectangle(3,50,2,2,1,1,-5);
		createSpeedRectangle(3,60,2,2,0,0,-5);
	}
	
	private void buildFader() {
		//createSpeedRectangle(0,0,this.width,this.heightFull,0,0,3);
		
		createSpeedRectangle(6,12,1,2,.75f,.5f,3);
		createSpeedRectangle(1,15,1,4,1.5f,.75f,-3);
		createSpeedRectangle(4,17,1,1,1.5f,1.5f,5);
		createSpeedRectangle(7,18,1,3,2.5f,2,-5);
		createSpeedRectangle(1,22,1,4,2,1,7);
		createSpeedRectangle(7,34,1,2,2,.5f,2);
		createSpeedRectangle(2,38,2,2,1.5f,.75f,3f);
		createSpeedRectangle(2,42,2,5,1.5f,2,-4);
		createSpeedRectangle(5,50,2,1,.8f,.5f,3.5f);
		createSpeedRectangle(0,53,1,2,3,3,-8);
		
		createSpeedRectangle(6,62,2,2,1,.5f,-3);
		createSpeedRectangle(0,64,1,2,.75f,.5f,3);
	}
	
	private void buildLines() {
		createSpeedRectangle(0,10,1,this.heightFull,0,0,2);
		createSpeedRectangle(2,10,1,this.heightFull,0,0,2);
		createSpeedRectangle(4,10,1,this.heightFull,0,0,2);
		createSpeedRectangle(6,10,1,this.heightFull,0,0,2);
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
	private void createSpeedRectangle(int coreX, int coreY, int coreWidth, int coreHeight, float horDecay, float vertDecay, float magnitude) {
		if (horDecay < 0) throw new IllegalArgumentException("horDecay must be >= 0");
		else if (vertDecay < 0) throw new IllegalArgumentException("vertDecay must be >= 0");
		else if (magnitude < -10 || magnitude > 10) throw new IllegalArgumentException("magnitude must be between -10 and 10");
		
		// build core of blocks with speed of magnitude
		for(int col=coreX; col<coreX+coreWidth; col++) {
			if (col >= 0 && col < this.width) {
				for(int row=coreY; row<coreY+coreHeight; row++) {
					if (row >= 0 && row < this.heightFull) {
						Coin c = levelArray[row][col];
						c.setSpeed(c.getSpeed() + magnitude);
					}
				}
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
						if (isMagnitudePositive) c.setSpeed(c.getSpeed() + leftRemaining);
						// set speed negative if magnitude parameter was negative
						else c.setSpeed(c.getSpeed() + -1*leftRemaining);
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
						if (isMagnitudePositive) c.setSpeed(c.getSpeed() + rightRemaining);
						else c.setSpeed(c.getSpeed() + -1*rightRemaining);
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
						if (isMagnitudePositive) c.setSpeed(c.getSpeed() + topRemaining);
						else c.setSpeed(c.getSpeed() + -1*topRemaining);
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
						if (isMagnitudePositive) c.setSpeed(c.getSpeed() + bottomRemaining);
						else c.setSpeed(c.getSpeed() + -1*bottomRemaining);
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
	
	private boolean prob20(int level) {
		int threshold = 2; // to prevent 20s up through level 2
		if (level <= threshold) return false;
		int adjustedLevel = level - threshold;
		int totalOpportunities = this.width*this.heightFull;
		int customOpportunities = totalOpportunities/adjustedLevel;
		
		Random rand = new Random();
		int n = rand.nextInt(customOpportunities);
		return n == 0;
	}
	
	public void updateMovement(float deltaTime) {
		if (getLevel() > lastKnownLevel) {
			vy = levels.get(getLevel()).velocity;
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

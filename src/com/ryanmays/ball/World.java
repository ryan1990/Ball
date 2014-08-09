/*******************************************************************************
 * Copyright 2011 Mario Zechner
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.ryanmays.ball;

import java.util.ArrayList;
import java.util.List;

import com.ryanmays.ball.GameScreen.State;

import android.graphics.Color;
import android.util.Log;

/**
 * The World class is responsible for managing and updating the
 * game world. Keeps references to all game objects, updates them
 * and checks for collisions. Additionally calls a registered {@link CollisionListener}
 * to inform it of any events that happened in the game world.
 * @author mzechner
 *
 */
public class World {
    public static float MIN_X = 0, MIN_Y = 40;
    public static float MAX_X = 319, MAX_Y = 479;
    public Ball ball;
    public Man man;
    public Matrix matrix;
    public int score;
    public int maxScore;
    private float barColor;
    
    public long beginFlashTime;
    
    //Paddle paddle = new Paddle();
    //List<Block> blocks = new ArrayList<Block>();
    boolean gameOver = false;
    int points = 0;
    //CollisionListener listener;
    
    public World(/*CollisionListener listener*/) {
    	this.ball = new Ball();
    	this.man = new Man();
    	this.matrix = new Matrix(8, 80, 11, 40);
    	this.score = 0;
    	this.maxScore = 40;
    	
    	this.beginFlashTime = 0;
    	
    	updateBarColor(this.score, this.maxScore);
    	this.matrix.buildNextLevel();
        //generateBlocks();
        //this.listener = listener;
    }
    
    /*private void generateBlocks() {
        blocks.clear();
        for(int y = 60, type = 0; y < 60 + 8 * Block.HEIGHT; y+= Block.HEIGHT, type++) {
            for(int x = 20; x < 320 - Block.WIDTH / 2; x+= Block.WIDTH) {                              
                blocks.add(new Block(x, y, type));
            }
        }
    }*/
    
    public void update(float deltaTime, float accelX, float accelY) {
    	//if (collideRects(man.x, man.y, man.WIDTH, man.HEIGHT, ball.x, man.y, ball.WIDTH, ball.HEIGHT))
    		//Log.d("MyApp", "HIT333");
    	
        //if(blocks.size() == 0) generateBlocks();
        
        //ball.x +=2;// ball.vx * deltaTime;
        //ball.y += ball.vy * deltaTime;
        //if(ball.x < MIN_X) { ball.vx = -ball.vx; ball.x = MIN_X; listener.collisionWall(); }
        //if(ball.x > MAX_X - Ball.WIDTH) { ball.vx = -ball.vx; ball.x = MAX_X - Ball.WIDTH; listener.collisionWall(); }
        //if(ball.y < MIN_Y) { ball.vy = -ball.vy; ball.y = MIN_Y; listener.collisionWall(); }
        if(ball.y > MAX_Y - Ball.HEIGHT) { gameOver = true; return; }
        
        // man movement
        if (accelX>.1 || accelX<-.1) {
        	man.x += -accelX * 120 * deltaTime; //* matrix.vy / matrix.initvy;
        	//ball.x += -accelX * 150 * deltaTime;
        }
        
        /*
        if (accelY>.5+3) { // down
        	man.y += (accelY-3) * 120 * deltaTime; //* matrix.vy / matrix.initvy;
        	//ball.x += -accelY * 150 * deltaTime;
        } else if (accelY<-.5+2) { // up
        	man.y += (accelY-2) * 120 * deltaTime; //* matrix.vy / matrix.initvy;
        	//ball.x += -accelY * 150 * deltaTime;
        }
        */
        
        // stop man at edges
        if (man.x < MIN_X) man.x = MIN_X;
        else if (man.x > MAX_X - man.WIDTH) man.x = MAX_X - man.WIDTH;
        
        if (man.y < MIN_Y) man.y = MIN_Y;
        else if (man.y > MAX_Y*2/3 - man.HEIGHT) man.y = MAX_Y*2/3 - Man.HEIGHT;
        
        // matrix movement
        matrix.updateMovement(deltaTime);
        
        
        collideManCoins();
        //collideTopCoins();
    }
    
    private boolean collideRects(float x, float y, float width, float height,
                                 float x2, float y2, float width2, float height2) {
        if(x < x2 + width2 &&
           x + width  > x2 &&
           y < y2 + height2 &&
           y + height > y2)// changed
            return true;
        return false;
    }             
    /*
    private void collideTopCoins() {
    	//for(int row=0; row<1; row++) {
    	for(int col=0; col<matrix.width; col++) {
			Coin coin = matrix.levelArray[matrix.getTopRow()][col];
			//Log.d("MyApp", "coin.y="+coin.y+", matrix.y="+matrix.y);
			//Log.d("MyApp", "TOPROW:"+matrix.getTopRow());
			if (coin.y - matrix.y <= 0) {
				//Log.d("MyApp", "COLLIDE"+" TOPROW="+matrix.getTopRow());
				//score = 0;
			}
    	}
		//}
    }
    */
    
    private void collideManCoins() {
    	int count = 0;
		for(int row=0; row<3; row++) {
	    	for(int col=0; col<matrix.width; col++) {
				Coin coin = matrix.levelArray[row+matrix.getTopRow()][col];
				//Log.d("MyApp", "TOPROW:"+matrix.getTopRow());
    			if (collideRects(man.x, man.y, man.WIDTH, man.HEIGHT, coin.x, coin.y-matrix.y, coin.WIDTH, coin.HEIGHT)) {
    				// handle acceleration from blocks with a particular speed
    				if (coin.getSpeed() != 0 && !coin.hit) {	
    					matrix.vy = adjustedVY(matrix.vy, coin.getSpeed());
    					// set all coins in row to hit so we only change vy once per row
    					for(int i=0; i<matrix.levelArray[row+matrix.getTopRow()].length; i++) {
    						matrix.levelArray[row+matrix.getTopRow()][i].hit = true;
    					}
    				}
    				
    				if (coin.type != -1) {
	    				// handle collisions with special blocks
	    				if (coin.type == 1) score++;
	    				else if (coin.type == 2) score += 2;
	    				else if (coin.type == 3) score += 3;
	    				else if (coin.type == 4) score += 20;
	    				else if (coin.type == 0) {
	    					score -= 10;
	    					flash();
	    					
	    					if (score < 0 || score < maxScore - 40) {
	    						gameOver = true;
	    					}
	    				}
	    				
	    				if (score > maxScore) maxScore = score;
	    				coin.type = -1;
	    				updateBarColor(this.score, this.maxScore);
    				}
				count++;
    			}
	    	}
		}
    }
    
 // return top bar color in a range of green, yellow, or red based on difference of score and maxScore
    public float getBarColor() {
    	return this.barColor;
    }
    
    private void updateBarColor(int score, int maxScore) {
    	float red = 0;
    	float green = 0;
    	float blue = 0;
    	
    	int diff = maxScore - score;
    	if (diff > 40) diff = 40;
    	
    	int adjustedDiff = diff - 20; // can range from -20 to 20.  -20 will be green, 0 will be yellow, 20 will be red.
    	
    	float colorDiff = adjustedDiff * (255.0f/20); // can range from -255 to 255
    	
    	if (colorDiff <= 0) {
    		green = 255;
    		red = 255 - Math.abs(colorDiff);
    	} else {
    		green = 255 - Math.abs(colorDiff);
    		red = 255;
    	}
    	Log.d("MyApp", "adjustedDiff="+adjustedDiff);
    	Log.d("MyApp", "colorDiff="+colorDiff);
    	Log.d("MyApp","COLOR r="+red+", g="+green+"b="+blue);
    	this.barColor = new Color().rgb((int)red, (int)green, (int)blue);
    }
    
    private float adjustedVY(float vy, float coinSpeed) {
    	// weight is used to make vy change more proportionally consistent between positive and negative
    	float weight;
    	if (coinSpeed > 0) weight = vy;
    	else weight = vy + vy*.012f*coinSpeed; // we add because coinSpeed is negative. weight will be less.
    	
    	float newVY = vy + weight*.012f*coinSpeed;
    	
    	float levelVY = matrix.levels.get(matrix.currentLevel).velocity;
    	float minLevelVY = 30;
    	
    	if (newVY < minLevelVY) newVY = minLevelVY;
    	//else if (newVY > maxLevelVY) newVY = maxLevelVY;
    	return newVY;
    }
    
    private void flash() {
    	beginFlashTime = System.nanoTime();
    	
    }
    
    /*private void collideBallBlocks(float deltaTime) {
        for(int i = 0; i < blocks.size(); i++) {
            Block block = blocks.get(i);
            if(collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT,
                            block.x, block.y, Block.WIDTH, Block.HEIGHT)) {
                blocks.remove(i);
                float oldvx = ball.vx;
                float oldvy = ball.vy;
                reflectBall(ball, block);
                ball.x = ball.x - oldvx * deltaTime * 1.01f;
                ball.y = ball.y - oldvy * deltaTime * 1.01f;
                points += 10 - block.type;
                listener.collisionBlock();
                break;
            }
        }
    }*/
    
    /*private void reflectBall(Ball ball, Block block) {
        if(collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT, 
                        block.x, block.y, 1, 1)) {
            if(ball.vx > 0) ball.vx = -ball.vx;
            if(ball.vy > 0) ball.vy = -ball.vy;
            return;
        }
        if(collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT, 
                        block.x + Block.WIDTH, block.y, 1, 1)) {
            if(ball.vx < 0) ball.vx = -ball.vx;
            if(ball.vy > 0) ball.vy = -ball.vy;
            return;
        }
        
        if(collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT, 
                        block.x, block.y + Block.HEIGHT, 1, 1)) {
            if(ball.vx > 0) ball.vx = -ball.vx;
            if(ball.vy < 0) ball.vy = -ball.vy;
            return;
        }
        
        if(collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT, 
                        block.x + Block.WIDTH, block.y + Block.HEIGHT, 1, 1)) {
            if(ball.vx < 0) ball.vx = -ball.vx;
            if(ball.vy < 0) ball.vy = -ball.vy;
            return;
        }
        
        if(collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT,
                        block.x, block.y, Block.WIDTH, 1)) {
            ball.vy = -ball.vy;
            return;
        }
        
        if(collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT,
                        block.x, block.y + Block.HEIGHT, Block.WIDTH, 1)) {
            ball.vy = -ball.vy;
            return;
        }
        
        if(collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT,
                        block.x, block.y, 1, Ball.HEIGHT)) {
            ball.vx = -ball.vx;
            return;
        }
 
        if(collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT,
                block.x + Block.WIDTH, block.y, 1, Ball.HEIGHT)) {
            ball.vx = -ball.vx;
            return;
        }
    }*/
    
    /*private void collideBallPaddle(float deltaTime) {
        if(ball.y > paddle.y) return;
        if(ball.x >= paddle.x && 
           ball.x < paddle.x + Paddle.WIDTH &&
           ball.y + Ball.HEIGHT >= paddle.y) {
            ball.y = paddle.y - Ball.HEIGHT - 2;
            ball.vy = -ball.vy;   
            listener.collisionPaddle();
        }
    }*/       
}

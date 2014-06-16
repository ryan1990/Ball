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
    
    public long beginFlashTime;
    
    //Paddle paddle = new Paddle();
    //List<Block> blocks = new ArrayList<Block>();
    boolean gameOver = false;
    int points = 0;
    //CollisionListener listener;
    
    public World(/*CollisionListener listener*/) {
    	Log.d("MyApp","ball before");
    	this.ball = new Ball();
    	this.man = new Man();
    	this.matrix = new Matrix(8, 3000, 11, 40);
    	this.score = 0;
    	this.maxScore = 0;
    	this.beginFlashTime = 0;
    	Log.d("MyApp","ball created");
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
        matrix.y += matrix.vy*deltaTime;
        matrix.vy += matrix.acceleration*deltaTime;
        
        Log.d("MyApp", "acc"+accelX+", matrix.y="+matrix.y);
        
        collideManCoins();
        collideTopCoins();
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
    
    private void collideTopCoins() {
    	//for(int row=0; row<1; row++) {
    	for(int col=0; col<matrix.width; col++) {
			Coin coin = matrix.array[matrix.getTopRow()][col];
			//Log.d("MyApp", "coin.y="+coin.y+", matrix.y="+matrix.y);
			//Log.d("MyApp", "TOPROW:"+matrix.getTopRow());
			if (coin.visible) {
    			if (coin.y - matrix.y <= 0) {
    				Log.d("MyApp", "COLLIDE"+" TOPROW="+matrix.getTopRow());
    				//score = 0;
    				coin.visible = false;
    			}
			}
    	}
		//}
    }
    
    private void collideManCoins() {
    	int count = 0;
		for(int row=0; row<9; row++) {
	    	for(int col=0; col<matrix.width; col++) {
				Coin coin = matrix.array[row+matrix.getTopRow()][col];
				Log.d("MyApp", "coin.y="+coin.y+", matrix.y="+matrix.y);
				//Log.d("MyApp", "TOPROW:"+matrix.getTopRow());
				if (coin.visible) {
	    			if (collideRects(man.x, man.y, man.WIDTH, man.HEIGHT, coin.x, coin.y-matrix.y, coin.WIDTH, coin.HEIGHT)) {
	    				Log.d("MyApp", "COLLIDE1"+" TOPROW="+matrix.getTopRow()+": coin.y="+coin.y+": matrix.y="+matrix.y);
	    				Log.d("MyApp", "COLLIDE2 man.x="+man.x+", man.y="+man.y+", coin.x="+coin.x);
	    				if (coin.type == 1) score++;
	    				else if (coin.type == 2) score += 2;
	    				else if (coin.type == 3) score += 3;
	    				else if (coin.type == 0) {
	    					score -= 10;
	    					matrix.vy *= 1.2;
	    					
	    					if (score < 0) {
	    						gameOver = true;
	    					}
	    					flash();
	    				}
	    				
	    				if (score > maxScore) maxScore = score;
	    				coin.visible = false;
	    			}
				}
				count++;
	    	}
		}
    	Log.d("MyApp", "COUNTTT:"+count);
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

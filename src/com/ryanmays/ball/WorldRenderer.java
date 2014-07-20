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

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.Log;

import com.ryanmays.framework.Game;

/**
 * The WorldRenderer class takes a {@link World} instance and draws
 * its current state with the help of a Game instance.
 * @author mzechner
 *
 */
public class WorldRenderer {
    Game game;
    World world;
    Bitmap ballImage;
    Bitmap manImage;
    Bitmap beamImage;
    Bitmap coinBlackImage;
    Bitmap coinGoldImage;
    Bitmap coinSilverImage;
    Bitmap coinBronzeImage;
    Bitmap topBarImage;
    Bitmap paddleImage;
    Bitmap blocksImage;
    Bitmap purpleBackImage;
    boolean done;
    
    public WorldRenderer(Game game, World world) {
        this.game = game;
        this.world = world;
        this.ballImage = game.loadBitmap("ball.png");
        this.manImage = game.loadBitmap("man2.png");
        this.beamImage = game.loadBitmap("beam.png");
        this.coinBlackImage = game.loadBitmap("deathblock.png");
        this.coinGoldImage = game.loadBitmap("coingold.png");
        this.coinSilverImage = game.loadBitmap("coinsilver.png");
        this.coinBronzeImage = game.loadBitmap("coinbronze.png");
        this.topBarImage = game.loadBitmap("topBar.png");
        this.paddleImage = game.loadBitmap("paddle.png");
        this.blocksImage = game.loadBitmap("blocks.png");
        this.purpleBackImage = game.loadBitmap("purpleback.png");        
        
        Log.d("MyApp","marked done");
    }
    
    public void render() {
    	//game.drawBitmap(ballImage, (int)world.ball.x, (int)world.ball.y);
    	//game.drawBitmap(beamImage, (int)world.man.x, (int)world.man.y);
    	
    	
    	// draw blocks and coins in visible region
    	int count=0;
    	for(int row=0; row<world.matrix.windowHeight+2; row++) {
        	for(int col=0; col<world.matrix.width; col++) {
        		/////////////////////////
        		// handle loop
        		//////////////////////////
        		Coin coin = world.matrix.levelArray[row+world.matrix.getTopRow()][col];
        		
        		if (coin.speed!=0) {
					game.drawRectangle(col*world.matrix.pixelsInBlock, (row+world.matrix.getTopRow())*world.matrix.pixelsInBlock - (int)world.matrix.y, world.matrix.pixelsInBlock, world.matrix.pixelsInBlock, coin.getColor());
				}
        		
        		if (System.nanoTime() - world.beginFlashTime < 1000000000/10) {
            		game.drawRectangle(0, 0, (int)world.MAX_X, (int)world.MAX_Y, new Color().rgb(180, 0, 255));
            	}
        		//Log.d("MyApp", "ARRAY: "+world.matrix.array[row+world.matrix.getTopRow()][col]+", VAR: "+coin);
        		//Coin c2 = world.matrix.array[0][0];
        		//if (c2.visible == false) Log.d("MyApp", "FALSE!!!!!!!!!!!!!!!!!!!!!!");
        		if (true/*coin.visible*/) {
	        		switch (coin.type) {
	        			case 0:
	        				// start at 1+row to skip bar at top
	        				game.drawBitmap(coinBlackImage, col*world.matrix.pixelsInBlock, (row+world.matrix.getTopRow())*world.matrix.pixelsInBlock - (int)world.matrix.y/*0*(int)world.matrix.getOffset()*/);
	        				break;
	        			case 1:
	        				game.drawBitmap(coinBronzeImage, col*world.matrix.pixelsInBlock, (row+world.matrix.getTopRow())*world.matrix.pixelsInBlock - (int)world.matrix.y);
	        				break;
	        			case 2:
	        				game.drawBitmap(coinSilverImage, col*world.matrix.pixelsInBlock, (row+world.matrix.getTopRow())*world.matrix.pixelsInBlock - (int)world.matrix.y);
	        				break;
	        			case 3:
	        				game.drawBitmap(coinGoldImage, col*world.matrix.pixelsInBlock, (row+world.matrix.getTopRow())*world.matrix.pixelsInBlock - (int)world.matrix.y);
	        				break;
	        		}
        		}
        	}
        	
        	// draw man in visible region
        	game.drawBitmap(manImage, (int)world.man.x, (int)world.man.y);
        }
    	
    	//Log.d("MyApp", "count: "+count);
    	game.drawRectangle(0, 0, (int)world.MAX_X, (int)world.MIN_Y, Color.BLUE);
    	
        
        //game.drawBitmap(paddleImage, (int)world.paddle.x, (int)world.paddle.y);
        //for(int i = 0; i < world.blocks.size(); i++) {
            //Block block = world.blocks.get(i);
            //game.drawBitmap(blocksImage, (int)block.x, (int)block.y,
                            //0, (int)(block.type * Block.HEIGHT),
                            //(int)Block.WIDTH, (int)Block.HEIGHT);
        //}
    }       
}

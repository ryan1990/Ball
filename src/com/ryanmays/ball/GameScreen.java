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

import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;

import com.ryanmays.framework.Game;
import com.ryanmays.framework.Screen;
import com.ryanmays.framework.Sound;
import com.ryanmays.framework.TouchEvent;
import com.ryanmays.framework.TouchEvent.TouchEventType;

/**
 * The game screen implementation, responsible for drawing the UI
 * and simulating and drawing the game world based on user input.
 * @author mzechner
 *
 */
public class GameScreen extends Screen {
    enum State { Paused, Running, GameOver }
    
    Bitmap background, resume, gameOver;    
    Typeface font;
    Sound bounceSound, blockSound;
    State state = State.Running;
    World world;
    WorldRenderer renderer;
    
    public GameScreen(Game game) {
        super(game);
        background = game.loadBitmap("blueback.png");
        resume = game.loadBitmap("resume.png");
        gameOver = game.loadBitmap("gameover.png");
        font = game.loadFont("font.ttf");
        bounceSound = game.loadSound("bounce.wav");
        blockSound = game.loadSound("blocksplosion.wav");
        world = new World(/*new CollisionListener() {
            public void collisionWall() { bounceSound.play(1); }                      
            public void collisionPaddle() { bounceSound.play(1); }                        
            public void collisionBlock() { blockSound.play(1); }
        }*/);
        renderer = new WorldRenderer(game, world);
    }
    
    public void update(float deltaTime) {
        if(world.gameOver) state = State.GameOver;
        
        if(state == State.Paused && game.getTouchEvents().size() > 0) {
            state = State.Running;
        }
        if(state == State.GameOver) {
            List<TouchEvent> events = game.getTouchEvents();
            for(int i = 0; i < events.size(); i++) {
                if(events.get(i).type == TouchEventType.Up) {
                    game.setScreen(new MainMenuScreen(game));
                    return; 
                }                   
            }
        }
        if(state == State.Running && game.getTouchY(0) < 32 && game.getTouchX(0) > 320 - 32) {
            state = State.Paused;
            Log.d("MyApp","I am here");
            return;
        }        
        if(state == State.Running) world.update(deltaTime, game.getAccelerometer()[0], game.getAccelerometer()[1]);
        
        //game.drawBitmap(background, 0, 0);
        game.drawRectangle(0, 0, (int)world.MAX_X, (int)world.MAX_Y, new Color().rgb(10, 0, 50));
        renderer.render();
        if(state == State.Paused)
            game.drawBitmap(resume, 160 - resume.getWidth() / 2, 240 - resume.getHeight() / 2);
        if(state == State.GameOver)
            game.drawBitmap(gameOver, 160 - gameOver.getWidth() / 2, 240 - gameOver.getHeight() / 2);
        game.drawText(font, ""+world.matrix.getTopRow() /*Integer.toString(world.points)*/, 24, 12, Color.BLACK, 12);        
        game.drawText(font, ""+world.score, 220, 12, Color.BLACK, 12);
        game.drawText(font, ""+world.maxScore, 120, 12, Color.BLACK, 12);
    }

    public void pause() {
        if(state != State.GameOver) state = State.Paused;
    }
   
    public void resume() {
    }

    public void dispose() {         
    }
}

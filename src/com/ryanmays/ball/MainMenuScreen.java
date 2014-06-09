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

import com.ryanmays.framework.Game;
import com.ryanmays.framework.Screen;

/**
 * The main menu screen. 
 * @author mzechner
 *
 */
public class MainMenuScreen extends Screen {
    Bitmap mainmenu, insertCoin;   
    float passedTime = 0;    
    
    public MainMenuScreen(Game game) {
        super(game);
        mainmenu = game.loadBitmap("mainmenu.png");
        insertCoin = game.loadBitmap("insertcoin.png");        
    }
    
    public void update(float deltaTime) {        
        if(game.isTouchDown(0)) { game.setScreen(new GameScreen(game)); return; }
        
        passedTime += deltaTime;        
        game.drawBitmap(mainmenu, 0, 0);
        if((passedTime - (int)passedTime) > 0.5f)
            game.drawBitmap(insertCoin, 160 - insertCoin.getWidth() / 2, 360);        
    }
        
    public void dispose() {        
    }
    
    public void pause() { }    
    public void resume() { }
}

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

import android.util.Log;

import com.ryanmays.framework.Game;
import com.ryanmays.framework.Music;
import com.ryanmays.framework.Screen;

/**
 * The Game implementation of Droidanoid. Responsible for
 * creating the MainMenuScreen and instantiating and managing
 * the Music instance.
 * @author mzechner
 *
 */
public class BallGame extends Game {
    //Music music;
    
    public Screen createStartScreen() {
        //System.out.println("hey");
    	//music = this.loadMusic("music.ogg");
        //music.setLooping(true);
    	Log.d("MyApp", "HHHEEEEYYYY");
        return new MainMenuScreen(this);
    }       
    
    public void onPause() {
        super.onPause();
        //music.pause();
    }
    
    public void onResume() {
        super.onResume();
        //music.play();
    }
}

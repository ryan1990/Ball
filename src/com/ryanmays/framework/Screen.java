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
package com.ryanmays.framework;

/**
 * <p>A Screen is one phase of a game, e.g. the main menu screen,
 * the highscores screen and so on. A {@link Game} stores
 * one currently active screen and will call its {@link Screen#update(float)}
 * method on the main loop thread as often as possible. The Screen can then
 * execute any logic in that method as well as render itself via the methods
 * provided by the Game class.</p>
 * <p>The Game class will also report any life-cycle events (pause/resume/dispose) to
 * the currently active Screen</p>
 * <p>Simply derrive from this class and implement the methods appropriately. Use
 * {@link Game#setScreen(Screen)} to set a screen as the currently active screen.</p>
 * @author mzechner
 *
 */
public abstract class Screen {
    /** reference to the Game instance **/
    protected final Game game;
    
    /**
     * Constructor, setting the reference to the {@link Game} instance.
     * @param game the Game instance.
     */
    public Screen(Game game) {
        this.game = game;
    }
    
    /**
     * Called on the main loop thread as often as possible. The delta time
     * is the time passed since the last call to this method in seconds.
     * @param deltaTime the delta time in seconds.
     */
    public abstract void update(float deltaTime);
    
    /**
     * Called on the main loop thread in case the application got paused.
     */
    public abstract void pause();
    
    /**
     * Called on the main loop thread in case the application got resumed.
     */
    public abstract void resume();
    
    /**
     * Called on the main loop thread in case the application got destroyed.
     */
    public abstract void dispose();
}

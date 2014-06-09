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

import android.media.SoundPool;

/**
 * Class abstracing a sound effect stored in a {@link SoundPool}. An instance
 * of this class holds a reference to the SoundPool as well as the ID of the
 * sound effect in the SoundPool for playback. Call the {@link Sound#dispose()}
 * method when you no longer need the sound effect to clean up resources.
 * 
 * @author mzechner
 *
 */
public class Sound {
    /** the id of the sound effect **/
    int soundId;
    /** the SoundPool this sound effect is stored in **/
    SoundPool soundPool;

    /**
     * Constructor, taking the SoundPool and ID of the sound effect.
     * @param soundPool the {@link SoundPool}.
     * @param soundId the ID of the sound effect in the SoundPool.
     */
    public Sound(SoundPool soundPool, int soundId) {
        this.soundId = soundId;
        this.soundPool = soundPool;
    }
    
    /**
     * Plays the sound effect with the given volume in the range 0 (silent) to 1 (full volume).
     * @param volume the volume.
     */
    public void play(float volume) {
        soundPool.play(soundId, volume, volume, 0, 0, 1);
    }
    
    /**
     * Cleans up all resources of this sound effect. Call
     * this method if you no longer need this sound effect.
     */
    public void dispose() {
        soundPool.unload(soundId);
    }

}

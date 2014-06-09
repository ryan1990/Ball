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

import java.io.IOException;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

/**
 * Class wrapping a {@link MediaPlayer} instance. The MediaPlayer instance
 * is initialized from an asset file given as an {@link AssetFileDescriptor} in
 * the constructor. The class provides methods to play, pause, stop and dispose
 * the instance. Make sure you pause any Music instances before the application
 * is paused or destroyed, otherwise the music will continue to play in the
 * background.
 * 
 * @author mzechner
 *
 */
public class Music implements OnCompletionListener {
    /** the MediaPlayer instance responsible for actual audio playback **/
    private MediaPlayer mediaPlayer;
    /** flag used to decide whether the MediaPlayer is prepared. Necessary so we don't call
     * any MediaPlayer methods that aren't allowed to be called in specific states. See
     * {@link MediaPlayer} and its state diagram for more information
     */
    private boolean isPrepared = false;

    /**
     * Constructor, creating a new {@link MediaPlayer} from the given {@link AssetFileDescriptor}. 
     * Will throw a {@link RuntimeException} in case something went wrong.  
     * @param assetDescriptor
     */
    public Music(AssetFileDescriptor assetDescriptor) {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(assetDescriptor.getFileDescriptor(),
                    assetDescriptor.getStartOffset(),
                    assetDescriptor.getLength());
            mediaPlayer.prepare();
            isPrepared = true;
            mediaPlayer.setOnCompletionListener(this);
        } catch (Exception e) {
            throw new RuntimeException("Couldn't load music");
        }
    }

    /**
     * Releases any resources associated with this instance. Call this
     * when you no longer need the instance!
     */
    public void dispose() {
        if (mediaPlayer.isPlaying())
            mediaPlayer.stop();
        mediaPlayer.release();
    }

    /**     
     * @return whether the instance is set to loop.
     */
    public boolean isLooping() {
        return mediaPlayer.isLooping();
    }
    
    /**
     * @return whether the instance is currently playing.
     */
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    /**
     * @return whether the instance is stopped.
     */
    public boolean isStopped() {
        return !isPrepared;
    }

    /**
     * Pauses the playback.
     */
    public void pause() {
        if (mediaPlayer.isPlaying())
            mediaPlayer.pause();
    }

    /**
     * Starts/resumes the playback.
     */
    public void play() {
        if (mediaPlayer.isPlaying())
            return;

        try {
            synchronized (this) {
                if (!isPrepared)
                    mediaPlayer.prepare();
                mediaPlayer.start();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets whether the playback should loop if it 
     * reaches the end of the music file.
     * @param isLooping whether this instance should loop.
     */
    public void setLooping(boolean isLooping) {
        mediaPlayer.setLooping(isLooping);
    }

    /**
     * Sets the volume in the range 0 (silent) to 1 (full volume).
     * @param volume the volume.
     */
    public void setVolume(float volume) {
        mediaPlayer.setVolume(volume, volume);
    }

    /**
     * Stops the playback.
     */
    public void stop() {        
        synchronized (this) {
            if(!isPrepared) return;
            mediaPlayer.stop();
            isPrepared = false;
        }
    }
   
    /**
     * Implementation of the {@link OnCompletionListener#onCompletion} method.
     * Sets the isPrepared flag to guarantee we are in proper states when
     * other {@link MediaPlayer} methods are called.
     */
    public void onCompletion(MediaPlayer arg0) {
        synchronized (this) {
            isPrepared = false;
        }
    }
}

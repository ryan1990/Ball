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

import java.util.List;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.ryanmays.framework.TouchEvent.TouchEventType;

/**
 * This class implements touch event handling for Android 1.5/1.6. 
 * It implements the {@link TouchHandler} interface and reports the current state
 * and the chronological events to the API client.
 * 
 * @author mzechner
 *
 */
public class SingleTouchHandler implements TouchHandler, OnTouchListener {
    /** whether the screen is touched or not **/
    private volatile boolean isTouched = false;
    /** the last touch's x-coordinate **/
    private volatile int touchX = 0;
    /** the last touch's y-coordinate **/
    private volatile int touchY = 0;
    /** the buffer to which to write the incoming touch events **/
    private List<TouchEvent> touchEventBuffer;
    /** the {@link TouchEventPool} to fetch new {@link TouchEvent} instances from **/
    private TouchEventPool touchEventPool;
    
    /**
     * Constructor, setting this SingleTouchHandler as the {@link OnTouchListener} of the
     * provided {@link View}. The touchEventBuffer argument is used to return {@link TouchEvent}
     * instances to the user of this class. The {@link TouchEventPool} is used to obtain and recycle new
     * {@link TouchEvent}. 
     * @param view the View to attach this handler to.
     * @param touchEventBuffer the buffer to write new touch events to.
     * @param touchEventPool the pool to create new TouchEvent instances with.
     */
    public SingleTouchHandler(View view, List<TouchEvent> touchEventBuffer, TouchEventPool touchEventPool) {
        view.setOnTouchListener(this);
        this.touchEventBuffer = touchEventBuffer;
        this.touchEventPool = touchEventPool;       
    }

    /**
     * {@inheritDoc}
     */
    public boolean isTouchDown(int pointer) {
        if(pointer != 0) return false;
        return isTouched;
    }

    /**
     * {@inheritDoc}
     */
    public int getTouchX(int pointer) {
        if(pointer != 0) return 0;
        return touchX;
    }
    
    /**
     * {@inheritDoc}
     */
    public int getTouchY(int pointer) {
        if(pointer != 0) return 0;
        return touchY;
    }

    /**
     * Implementation of the {@link OnTouchListener#onTouch} method. Decodes the touch event(s) stored
     * in the MotionEvent argument, updates the internal members recording the current state of the
     * touch screen (touchDown, touchX, touchY), and writes new {@link TouchEvent} instances to the
     * touch event buffer provided by the user of this class. The method is synchronized on the touch 
     * event buffer as the buffer is filled on the UI thread (when this method is called) and emptied
     * on the main loop thread in the {@link Game#run} method (see {@link Game#fillBuffers}). 
     */
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        synchronized(touchEventBuffer) {
            TouchEvent touchEvent = touchEventPool.obtain();
            switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:            
                isTouched = true;
                touchEvent.type = TouchEventType.Down;
                break;
            case MotionEvent.ACTION_MOVE:            
                isTouched = true;
                touchEvent.type = TouchEventType.Dragged;
                break;
            case MotionEvent.ACTION_CANCEL:                
            case MotionEvent.ACTION_UP:            
                isTouched = false;
                touchEvent.type = TouchEventType.Up;
                break;
            }
            
            touchEvent.x = touchX = (int)(event.getX());
            touchEvent.y = touchY = (int)(event.getY());
            touchEvent.pointer = 0;
            touchEventBuffer.add(touchEvent);
            return true;            
        }
    }
}

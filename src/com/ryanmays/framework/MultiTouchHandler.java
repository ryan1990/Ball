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
 * This class implements touch event handling for Android 2.0 and
 * above. It supports multi-touch events as oposed to the {@link SingleTouchHandler}.
 * It implements the {@link TouchHandler} interface and reports the current state
 * and the chronological events to the API client.
 * 
 * @author mzechner
 *
 */
public class MultiTouchHandler implements TouchHandler, OnTouchListener {
    /** stores the state of the first 20 touch pointers **/
    private boolean[] isTouched = new boolean[20];
    /** stores the x-coordinate of the first 20 touch pointers **/
    private int[] touchX = new int[20];
    /** stores the y-coordinate of the first 20 touch pointers **/
    private int[] touchY = new int[20];
    /** the buffer to which to write the incoming touch events **/
    private List<TouchEvent> touchEventBuffer;
    /** the {@link TouchEventPool} to fetch new {@link TouchEvent} instances from **/ 
    private TouchEventPool touchEventPool;

    /**
     * Constructor, setting this MultiTouchHandler as the {@link OnTouchListener} of the
     * provided {@link View}. The touchEventBuffer argument is used to return {@link TouchEvent}
     * instances to the user of this class. The {@link TouchEventPool} is used to obtain and recycle new
     * {@link TouchEvent}. 
     * @param view the View to attach this handler to.
     * @param touchEventBuffer the buffer to write new touch events to.
     * @param touchEventPool the pool to create new TouchEvent instances with.
     */
    public MultiTouchHandler(View view, List<TouchEvent> touchEventBuffer, TouchEventPool touchEventPool) {
        view.setOnTouchListener(this);
        this.touchEventBuffer = touchEventBuffer;
        this.touchEventPool = touchEventPool;       
    }

    /**
     * Implementation of the {@link OnTouchListener#onTouch} method. Decodes the touch event(s) stored
     * in the MotionEvent argument, updates the internal members recording the current state of the
     * touch screen (touchDown, touchX, touchY), and writes new {@link TouchEvent} instances to the
     * touch event buffer provided by the user of this class. The method is synchronized on the touch 
     * event buffer as the buffer is filled on the UI thread (when this method is called) and emptied
     * on the main loop thread in the {@link Game#run} method (see {@link Game#fillBuffers}). 
     */
    public synchronized boolean onTouch(View v, MotionEvent event) {
        synchronized(touchEventBuffer) {
            TouchEvent touchEvent = null;
            int action = event.getAction() & MotionEvent.ACTION_MASK;
            int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_ID_MASK) >> MotionEvent.ACTION_POINTER_ID_SHIFT;
            int pointerId = event.getPointerId(pointerIndex);
    
            switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                touchEvent = touchEventPool.obtain();
                touchEvent.type = TouchEventType.Down;
                touchEvent.pointer = pointerId;
                touchEvent.x = touchX[pointerId] = (int) (event.getX(pointerIndex));
                touchEvent.y = touchY[pointerId] = (int) (event.getY(pointerIndex));
                isTouched[pointerId] = true;            
                touchEventBuffer.add(touchEvent);
                break;
    
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                touchEvent = touchEventPool.obtain();
                touchEvent.type = TouchEventType.Up;
                touchEvent.pointer = pointerId;
                touchEvent.x = touchX[pointerId] = (int) (event.getX(pointerIndex));
                touchEvent.y = touchY[pointerId] = (int) (event.getY(pointerIndex));
                isTouched[pointerId] = false;
                touchEventBuffer.add(touchEvent);
                break;
    
            case MotionEvent.ACTION_MOVE:
                int pointerCount = event.getPointerCount();
                for (int i = 0; i < pointerCount; i++) {
                    touchEvent = touchEventPool.obtain();
                    touchEvent.type = TouchEventType.Dragged;
                    pointerIndex = i;
                    pointerId = event.getPointerId(pointerIndex);
                    touchEvent.pointer = pointerId;
                    touchEvent.x = touchX[pointerId] = (int) (event.getX(pointerIndex));
                    touchEvent.y = touchY[pointerId] = (int) (event.getY(pointerIndex));
                    touchEventBuffer.add(touchEvent);
                }
                break;
            }
            return true;
        }
    }

    /**
     * {@inheritDoc}
     */
    public synchronized boolean isTouchDown(int pointer) {
        if (pointer < 0 || pointer >= 20)
            return false;
        else
            return isTouched[pointer];
    }

    /**
     * {@inheritDoc}
     */
    public synchronized int getTouchX(int pointer) {
        if (pointer < 0 || pointer >= 20)
            return 0;
        else
            return touchX[pointer];
    }

    /**
     * {@inheritDoc}
     */
    public synchronized int getTouchY(int pointer) {
        if (pointer < 0 || pointer >= 20)
            return 0;
        else
            return touchY[pointer];
    }
}

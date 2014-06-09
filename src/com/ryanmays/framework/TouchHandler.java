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
 * An interface to be implemented by various backends, e.g.
 * for Android 1.5/1.6 or Android 2.0.
 * @author mzechner
 *
 */
public interface TouchHandler {
    /** 
     * Returns whether the finger with the given pointer ID
     * is down. The first finger to touch the screen will 
     * get ID 0, the next will get ID 1 and so on. In general,
     * any finger going down will get the first free ID available.
     * 
     * @param pointer the pointer ID
     * @return whether the finger with the ID is down.
     */
    public boolean isTouchDown(int pointer);
    
    /** 
     * Returns the x-coordinate of the finger with the given pointer ID
     * is down. The first finger to touch the screen will 
     * get ID 0, the next will get ID 1 and so on. In general,
     * any finger going down will get the first free ID available.
     * 
     * @param pointer the pointer ID
     * @return the x-coordinate in the coordinate system of the View this handler is registered with.
     */
    public int getTouchX(int pointer);
    
    /** 
     * Returns the y-coordinate of the finger with the given pointer ID
     * is down. The first finger to touch the screen will 
     * get ID 0, the next will get ID 1 and so on. In general,
     * any finger going down will get the first free ID available.
     * 
     * @param pointer the pointer ID
     * @return the y-coordinate in the coordinate system of the View this handler is registered with.
     */
    public int getTouchY(int pointer);
}

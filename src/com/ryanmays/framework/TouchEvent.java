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
 * Class encoding a touch event. A {@link TouchEvent} can have
 * one of three types: down, up and dragged. The down event 
 * happens when a finger goes down on the screen. The up event
 * happens when a finger is lifted from the screen. The dragged
 * event happens when a finger is moved across the screen. A
 * touch event has a pointer ID which identifies the finger it 
 * belongs to. The first finger that goes down gets the ID 0, the
 * next finger gets the ID 1 and so on. A new finger will get the
 * first free ID.
 * 
 * @author mzechner
 *
 */
public class TouchEvent {
    public enum TouchEventType {
        Down,
        Up,
        Dragged
    }
    /** the type of this event **/
    public TouchEventType type;
    /** the x- and y-coordinate of this event in the coordinate system of the View **/
    public int x, y;
    /** the pointer ID **/
    public int pointer;
}

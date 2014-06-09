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
 * Class for storing a key event as reported by the operating system.
 * A KeyEvent can have one of the two types in {@link KeyEventType}:
 * down and up. The former signals that a key just went down, the later
 * signals that a key went up. In the former case only the key code is
 * set (see {@link android.view.KeyEvent} for a list of key codes). In 
 * the later case the unicode character is set as well.
 * 
 * @author mzechner
 *
 */
public class KeyEvent {
    public enum KeyEventType {
        Down,
        Up,        
    }
    
    public KeyEventType type;
    public int keyCode;
    public char character;
}

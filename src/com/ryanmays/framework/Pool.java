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

import java.util.ArrayList;
import java.util.List;

/**
 * A simple class for pooling (recycling) short lived objects.
 * Derrive from this class and implement the {@link Pool#newItem} method.
 * Use {@link Pool#obtain} to fetch a new object instance from the pool,
 * use {@link Pool#free} to reinsert an object into the pool (recycling).
 * @author mzechner
 *
 * @param <T>
 */
public abstract class Pool<T> {
    private List<T> items = new ArrayList<T>();
    
    protected abstract T newItem();
    
    public T obtain() {
        if(items.size() == 0) return newItem();
        return items.remove(0);
    }
    
    public void free(T item) {
        items.add(item);
    }
}

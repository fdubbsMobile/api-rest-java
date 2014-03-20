package pool4j;

import java.util.Collection;
import java.util.Stack;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

/**
 * Link : https://code.google.com/p/pool4j
 */

/**
 * A resource pool implementation with a finite number of resources.  This
 * is the type of resource pool you should use when it is expensive to create
 * new resources.
 *
 * This implementation is thread-safe.
 *
 * @author Evan Worley
 */
public final class FiniteResourcePool<T> implements ResourcePool<T> {
   
    // We will use a stack as our underlying story.  We can rely on the
    // Stack implementation being thread-safe on the method level
    private Stack<T> resources = new Stack<T>();
   
    /**
     * Creates a <code>FiniteResourcePool</code> with a collection of resources
     * @param resources The resources that compose the pool
     * @throws EmptyResourcePoolException if resources is null or empty
     */
    public FiniteResourcePool(Collection<T> resources) throws EmptyResourcePoolException {
        if (resources == null || resources.isEmpty()) {
            throw new EmptyResourcePoolException(this);
        }
        this.resources.addAll(resources);
    }
   
    public boolean isEmpty() {
        return resources.isEmpty();
    }

    public T getResource() throws EmptyResourcePoolException {
        // We must explicitly synchronize on the resources here to ensure that the
        // resources collection has not been modified after we check if it's empty
        synchronized (resources) {
            if (resources.isEmpty()) {
                throw new EmptyResourcePoolException(this);
            }
            return resources.pop();
        }
    }

    public void releaseResource(T resource) {
        if (resource == null) {
            throw new IllegalArgumentException("resource cannot be null");
        }
        resources.push(resource);
    }
   
    @Override
    public String toString() {
        return "FiniteResourcePool";
    }
}


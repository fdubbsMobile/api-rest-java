package pool4j;

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
 * A resource pool
 * @author Evan Worley
 * @param <T> The type of the resource
 */
public interface ResourcePool<T> {
       
    /**
     * Retrieves a resource from the pool
     *
     * @throws EmptyResourcePoolException If a resource cannot be retrieved from
     * the pool
     *
     * @return a non-null resource of type <code>T</code>
     */
    public T getResource() throws EmptyResourcePoolException;
   
    /**
     * @return <code>true</code> if the pool is empty, else false
     */
    public boolean isEmpty();

    /**
     * Releases a resource back into the pool
     *
     * @param resource The resource to put back into the pool
     * @throws IllegalArgumentException if resource is null
     */
    public void releaseResource(T resource) throws IllegalArgumentException;

}

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
 * Thrown when a request is made for a resource in a pool when
 * the pool is empty
 * @author Evan Worley
 */
public class EmptyResourcePoolException extends RuntimeException {
   
    /**
	 * 
	 */
	private static final long serialVersionUID = -9090962064664090351L;
	private static final String TYPED_MSG = "Resource pool of type %s is empty";
    private static final String TYPED_NAMED_MSG = "Resource pool %s of type %s is empty";

    /**
     * Default constructor
     */
    public EmptyResourcePoolException() {
        super("Resource pool is empty");
    }

    /**
     * Constructs an exception with a message that includes the empty resource pool's
     * class name
     * @param pool The resource pool that is empty
     */
    public EmptyResourcePoolException(ResourcePool<?> pool) {
        super(String.format(TYPED_MSG, pool.getClass().getName()));
    }
   
    /**
     * Constructs an exception with a message that includes the empty resource pool's
     * class name
     * @param logicalName A logical name for the resource pool to aid in debugging
     * @param pool The resource pool that is empty
     */
    public EmptyResourcePoolException(String logicalName, ResourcePool<?> pool) {
        super(String.format(TYPED_NAMED_MSG, logicalName, pool.getClass().getName()));
    }
}

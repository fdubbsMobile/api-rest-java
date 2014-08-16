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
 * 
 * A factory that creates new resources of type T. ResourceFactories are often
 * used with auto expanding resource pools in the creation of new resources for
 * the pool.
 * 
 * @author Evan Worley
 * @param <T>
 *            The type of resource that this factory creates
 */
public interface ResourceFactory<T> {
	/**
	 * @return A new instance of <code>T</code>
	 */
	T createResource();
}

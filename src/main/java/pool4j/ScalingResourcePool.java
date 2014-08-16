package pool4j;

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
 * A resource pool implementation which scales the number of resources based on
 * pool usage. As resource demand increases, the pool will create new resources
 * as needed. As demand decreases, the pool will free resources and scale itself
 * down. This resource pool is ideal for managing resources that are cheap to
 * create, but have a large memory footprint.
 * 
 * This implementation is thread-safe.
 * 
 * @author Evan Worley
 * @param <T>
 *            The type of resource this pool will manage
 */
public final class ScalingResourcePool<T> implements ResourcePool<T> {

	private static final int DEFAULT_CHECK_INTERVAL = 1000;

	// We will use a stack as our underlying story. We can rely on the
	// Stack implementation being thread-safe on the method level
	private Stack<T> resources = new Stack<T>();
	private ResourceFactory<T> factory;
	private ScaleDownRunner scaleDownRunner;

	/**
	 * @param factory
	 *            The factory to create new resources with
	 * @param scaleCheckInterval
	 *            The time in milliseconds in between checks for scale down
	 *            opportunities.
	 * @throws IllegalArgumentException
	 *             If factory is null
	 */
	public ScalingResourcePool(ResourceFactory<T> factory,
			int scaleCheckInterval) throws IllegalArgumentException {

		if (factory == null) {
			throw new IllegalArgumentException("factory cannot be null");
		}

		this.factory = factory;

		// Create the scale down thread
		scaleDownRunner = new ScaleDownRunner(scaleCheckInterval);
		Thread scaleDownThread = new Thread(scaleDownRunner);
		scaleDownThread.start();
	}

	/**
	 * Creates a ScalingResourcePool with a default of 1000ms between scale down
	 * checks
	 * 
	 * @param factory
	 *            The factory to create new resources with
	 * @throws IllegalArgumentException
	 *             If factory is null
	 */
	public ScalingResourcePool(ResourceFactory<T> factory)
			throws IllegalArgumentException {
		this(factory, DEFAULT_CHECK_INTERVAL);
	}

	public T getResource() throws EmptyResourcePoolException {
		synchronized (resources) {
			if (isEmpty()) {
				return factory.createResource();
			}
			return resources.pop();
		}
	}

	public boolean isEmpty() {
		return resources.isEmpty();
	}

	public void releaseResource(T resource) throws IllegalArgumentException {
		resources.push(resource);
	}

	@Override
	protected void finalize() throws Throwable {
		// Disable the scaling down thread
		scaleDownRunner.disable();

		super.finalize();
	}

	/**
	 * Manages the scaling down of the resource pool. If this runner observes an
	 * up-trend or static amount of available resources it will begin freeing
	 * resources
	 * 
	 * @author Evan Worley
	 */
	private class ScaleDownRunner implements Runnable {
		// the number of free resources last time we checked
		private int lastCheckFreeResources = 0;

		// The number of milliseconds we'll sleep between checks
		private int sleepTimeMS;

		public ScaleDownRunner(int scaleDownCheckInterval) {
			this.sleepTimeMS = scaleDownCheckInterval;
		}

		private boolean enabled = true;

		public void run() {
			while (enabled) {
				// If we have some resources, and there are more or the same
				// as when we checked last time, let's free one
				int freeResources = resources.size();
				if (freeResources > 0
						&& freeResources >= lastCheckFreeResources) {
					resources.pop();
					lastCheckFreeResources = freeResources - 1;
				} else {
					lastCheckFreeResources = freeResources;
				}

				try {
					Thread.sleep(sleepTimeMS);
				} catch (InterruptedException e) {
					// Nothing we can do
				}
			}
		}

		public void disable() {
			enabled = false;
		}
	}
}

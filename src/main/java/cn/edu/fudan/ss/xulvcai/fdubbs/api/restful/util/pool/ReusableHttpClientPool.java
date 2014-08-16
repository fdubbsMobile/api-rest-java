package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.pool;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.http.ReusableHttpClient;

/**
 * A generic ReusableHttpClient pool implementation based on factories. This
 * implementation is thread safe.
 * 
 * @author hidennis
 */
public class ReusableHttpClientPool implements ResourcePool<ReusableHttpClient> {

	/** the factory */
	private ResourceFactory<ReusableHttpClient> httpClientFactory;

	/** counters */
	private int capacity;
	private int occupied;
	private boolean quit;

	/** clients occupied */
	private Set<ReusableHttpClient> occupiedClients;

	/** clients available */
	private List<ReusableHttpClient> availableClients;

	public ReusableHttpClientPool(
			ResourceFactory<ReusableHttpClient> httpClientFactory, int capacity) {
		this.httpClientFactory = httpClientFactory;
		this.capacity = capacity;

		this.occupied = 0;
		this.occupiedClients = new HashSet<ReusableHttpClient>(capacity);
		this.availableClients = new LinkedList<ReusableHttpClient>();
		this.quit = false;
	}

	@Override
	public synchronized ReusableHttpClient getResource() {
		while (!quit) {

			// first, try to return a client from the pool
			if (!availableClients.isEmpty()) {
				ReusableHttpClient client = availableClients.remove(0);

				// if the client is invalid, create a replacement
				if (!httpClientFactory.validateResource(client))
					client = httpClientFactory.createResource();

				occupiedClients.add(client);
				return client;
			}

			// next, create a new client if we haven't
			// reached the limit yet
			if (occupied < capacity) {
				ReusableHttpClient client = httpClientFactory.createResource();
				occupiedClients.add(client);
				occupied++;

				return client;
			}

			// if no client are available, wait until one
			// is returned
			try {
				wait();
			} catch (Exception ex) {
			}
		}

		// pool is destroyed
		return null;
	}

	@Override
	public synchronized void returnResource(ReusableHttpClient client) {
		// Something is wrong. Just give up.
		if (!occupiedClients.remove(client))
			return;

		availableClients.add(client);
		notify();
	}

	@Override
	public synchronized boolean hasAvailableResource() {
		return occupied < capacity;
	}

	@Override
	public synchronized void destroy() {

		if (quit)
			return;

		quit = true;
		notifyAll();
	}

}

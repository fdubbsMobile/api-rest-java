package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.util.pool;

/**
 * The interface implemented by specified resource pool.
 * 
 * @author hidennis
 * @param <T>
 *            The type of resource that this pool manages
 */
public interface ResourcePool<T> {

	/**
	 * Retrieve a resource from the pool
	 * 
	 * @return available resource in the pool
	 */
	public T getResource();

	/**
	 * Return a resource back into the pool
	 * 
	 * @param resource
	 *            Resource that returned back into the pool
	 */
	public void returnResource(T resource);

	/**
	 * Check if there is any available resource exists in the pool
	 * 
	 * @return <code>true</code> if any available resource exists in the pool,
	 *         <code>false</code> otherwise
	 */
	public boolean hasAvailableResource();

	/**
	 * Destroy the pool and release all resource held in the pool
	 */
	public void destroy();
}

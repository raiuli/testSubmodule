/*
 * 
 */
package se.ltu.ssr.adapter.interfaces;

import java.util.concurrent.Callable;

// TODO: Auto-generated Javadoc
/**
 * The Interface RetriverAdapterInterface.
 */
public interface RetriverAdapterInterface {
	
	/**
	 * Data retrieved.
	 *
	 * @return the data packet
	 */
	public DataPacket dataRetrieved();
	
	/**
	 * Gets the threshold per minute.
	 *
	 * @return the threshold per minute
	 */
	public int getThresholdPerMinute();
	
	/**
	 * Sets the threshold per minute.
	 *
	 * @param i the new threshold per minute
	 */
	public void setThresholdPerMinute(int i);
}

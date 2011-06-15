/*******************************************************************************
 * Copyright 2011 The Regents of the University of California
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
package org.ohmage.cache;

/**
 * Singleton cache for the indices and String values for campaign running
 * states.
 * 
 * @author John Jenkins
 */
public class CampaignRunningStateCache extends StringAndIdCache{
	// The column IDs for the query.
	private static final String ID_COLUMN = "id";
	private static final String STATE_COLUMN = "running_state";
	
	// The SQL that will retrieve the desired values.
	private static final String SQL_GET_CAMPAIGN_RUNNING_STATES_AND_IDS = "SELECT " + ID_COLUMN + ", " + STATE_COLUMN + " " +
																		  "FROM campaign_running_state";
	
	// When we are requesting a cache in the Spring files, we use this
	// to reference which key we want.
	public static final String CACHE_KEY = "campaignRunningStateCache";
	
	// Known campaign running states.
	public static final String RUNNING_STATE_RUNNING = "running";
	public static final String RUNNING_STATE_STOPPED = "stopped";

	// A reference to the only instance of this class for the Singleton
	// pattern.
	private static CampaignRunningStateCache _self = new CampaignRunningStateCache();
	
	/**
	 * Default constructor set private to make this a Singleton.
	 */
	private CampaignRunningStateCache() {
		super(SQL_GET_CAMPAIGN_RUNNING_STATES_AND_IDS, ID_COLUMN, STATE_COLUMN);
	}
	
	/**
	 * Returns the instance of this class. This should be used to get at all
	 * the cache's methods.
	 * 
	 * @return The only instance of this class.
	 */
	public static CampaignRunningStateCache instance() {
		return _self;
	}
	
	/**
	 * Returns a human-readable name for this cache.
	 */
	@Override
	public String getName() {
		return CACHE_KEY;
	}
}
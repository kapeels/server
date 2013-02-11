/*******************************************************************************
 * Copyright 2012 The Regents of the University of California
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
package org.ohmage.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.ohmage.cache.PreferenceCache;
import org.ohmage.exception.CacheMissException;
import org.ohmage.exception.DomainException;

/**
 * Utilities to help get or set information from and to cookies.
 * 
 * @author John Jenkins
 */
public final class CookieUtils {
	/**
	 * Default constructor. Private so that it cannot be instantiated.
	 */
	private CookieUtils() {}
	
	/**
	 * Takes an array of HTTP Cookies and the first occurrence of a cookie with
	 * the name 'name'. If no such cookie exists, null is returned. It doesn't
	 * check if multiple cookies with the same name exist. 
	 *  
	 * @param cookies An array of Cookies that are to be searched for the first 
	 * 				  item with the name 'name'.
	 * 
	 * @param name The name, or key, to search for in the list of Cookies of
	 * 			   which zero or more may be present.
	 * 
	 * @return Returns the first cookie found with the name 'name'. If no such
	 * 		   cookie is found, null is returned.
	 */
	public static String getCookieValue(Cookie[] cookies, String name) {
		if(cookies != null) {
			for(int i = 0; i < cookies.length; i++) {
				if(cookies[i].getName().equals(name)) {
					return cookies[i].getValue();
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Takes an array of HTTP Cookies and returns a list of values for each of
	 * the Cookie Objects whose name was 'name'.
	 *  
	 * @param cookies An array of Cookies that are to be searched for items 
	 * 				  with the name 'name'.
	 * 
	 * @param name The name, or key, to search for in the list of Cookies of
	 * 			   which more than one may be present.
	 * 
	 * @return Returns a possibly empty list of values for the 'name' from the
	 * 		   'cookies' array.
	 */
	public static List<String> getCookieValues(Cookie[] cookies, String name) {
		List<String> results = new LinkedList<String>();
		
		if(cookies != null) {
			for(int i = 0; i < cookies.length; i++) {
				if(cookies[i].getName().equals(name)) {
					results.add(cookies[i].getValue());
				}
			}
		}
		
		return results;
	}
	
	/**
	 * Adds a HTTP Cookie to the response with the given name and value and a
	 * lifetime.
	 *  
	 * @param httpResponse The HttpServletResponse to which the Cookie should 
	 * 					   be added.
	 * 
	 * @param name The name of the Cookie.
	 * 
	 * @param value The value for the Cookie.
	 * 
	 * @param lifetimeInSeconds The lifetime of the Cookie in seconds.
	 */
	public static void setCookieValue(HttpServletResponse httpResponse, String name, String value, int lifetimeInSeconds) {
		Cookie authTokenCookie = new Cookie(name, value);
		authTokenCookie.setHttpOnly(false);
		authTokenCookie.setMaxAge(lifetimeInSeconds);
		authTokenCookie.setPath("/");
		httpResponse.addCookie(authTokenCookie);
	}
	
	/**
	 * Creates a new base URL for the current domain with no path. 
	 * 
	 * @return The base URL for the current domain without a path.
	 * 
	 * @throws DomainException There was an error creating the URL.
	 */
	public static URL buildServerRootUrl() throws DomainException {
		// Create a string builder to use to build the URL.
		StringBuilder urlBuilder = new StringBuilder();
		
		// Determine if HTTPS is being used on this server.
		try {
			if(StringUtils.decodeBoolean(
				PreferenceCache.instance().lookup(
					PreferenceCache.KEY_SSL_ENABLED))) {
				urlBuilder.append("https://");
			}
			else {
				urlBuilder.append("http://");
			}
		}
		catch(CacheMissException e) {
			throw
				new DomainException("Error reading the SSL enabled value.", e);
		}
		
		// Append the server's fully qualified domain name.
		try {
			urlBuilder.append(
				PreferenceCache.instance().lookup(
					PreferenceCache.KEY_FULLY_QUALIFIED_DOMAIN_NAME));
		}
		catch(CacheMissException e) {
			throw
				new DomainException(
					"Error reading the fully qualified domain name value.",
					e);
		}
		
		try {
			return new URL(urlBuilder.toString());
		}
		catch(MalformedURLException e) {
			throw new DomainException("The URL is not valid.");
		}
	}
}

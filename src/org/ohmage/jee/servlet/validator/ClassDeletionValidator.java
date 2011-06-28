package org.ohmage.jee.servlet.validator;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.ohmage.request.InputKeys;
import org.ohmage.util.CookieUtils;
import org.ohmage.util.StringUtils;

/**
 * Basic validation that the required parameters exist.
 * 
 * @author John Jenkins
 */
public class ClassDeletionValidator extends AbstractHttpServletRequestValidator {
	private static final Logger _logger = Logger.getLogger(ClassDeletionValidator.class);
	
	/**
	 * Default constructor.
	 */
	public ClassDeletionValidator() {
		// Do nothing.
	}

	/**
	 * Checks that the required parameters exist. 
	 */
	@Override
	public boolean validate(HttpServletRequest httpRequest) throws MissingAuthTokenException {
		String classUrn = httpRequest.getParameter(InputKeys.CLASS_URN);

		if(StringUtils.isEmptyOrWhitespaceOnly(classUrn)) {
			_logger.warn("Missing required key: " + InputKeys.CLASS_URN);
			return false;
		}
		
		// Get the authentication / session token from the header.
		List<String> tokens = CookieUtils.getCookieValue(httpRequest.getCookies(), InputKeys.AUTH_TOKEN);
		if(tokens.size() == 0) {
			if(httpRequest.getParameter(InputKeys.AUTH_TOKEN) == null) {
				throw new MissingAuthTokenException("The required authentication / session token is missing.");
			}		}
		else if(tokens.size() > 1) {
			throw new MissingAuthTokenException("More than one authentication / session token was found in the request.");
		}
		
		return true;
	}
}
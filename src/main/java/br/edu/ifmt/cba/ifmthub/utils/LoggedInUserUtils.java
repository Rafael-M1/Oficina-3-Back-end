package br.edu.ifmt.cba.ifmthub.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class LoggedInUserUtils {
	/** Check whether User is Anonymous or not
	 * 
	 * @return true if User is Anonymous
	 */
	public static boolean checkIfUserIsAnonymous() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication.getPrincipal().equals("anonymousUser")) {
			return true;
		}
		return false;
	}
}

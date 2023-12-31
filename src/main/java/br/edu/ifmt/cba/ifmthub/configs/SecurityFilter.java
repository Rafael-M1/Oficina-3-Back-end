package br.edu.ifmt.cba.ifmthub.configs;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.edu.ifmt.cba.ifmthub.model.User;
import br.edu.ifmt.cba.ifmthub.services.UserService;
import br.edu.ifmt.cba.ifmthub.utils.AuthorizedRoutes;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

	@Autowired
	TokenService tokenService;
	@Autowired
	UserService userService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		boolean isPublic = false;
		String[] allowedPublicGetRoutes = AuthorizedRoutes.PUBLIC_GET_ROUTES;
		if (request.getMethod().equals("GET")) {			
			for (String route : allowedPublicGetRoutes) {
				if (route.endsWith("/**")) {
					String prefix = route.substring(0, route.length() - 3);
					if (request.getRequestURI().startsWith(prefix)) {
						isPublic = true;
					}
				} else if (route.equals(request.getRequestURI())) {
					isPublic = true;
				}
			}
		}
		
		var token = this.recoverToken(request);
		if (token != null) {
			if (!request.getRequestURI().equals("/auth/login")) {
				var login = tokenService.validateToken(token);
				Optional<User> userOpt = userService.findByEmailOptional(login);
				if (userOpt.isPresent()) {
					User user = userOpt.get();
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user,
							null, user.getAuthorities());
					SecurityContextHolder.getContext().setAuthentication(authentication);
				} else if (isPublic) {
					SecurityContextHolder.getContext().setAuthentication(null);
				} else {
					response.setContentType("application/json");
					response.setStatus(HttpStatus.UNAUTHORIZED.value());
					response.getWriter().write("\"message\":\"" + "Unauthorized" + "\"");
					return;					
				}
			}
		}
		filterChain.doFilter(request, response);
	}

	private String recoverToken(HttpServletRequest request) {
		var authHeader = request.getHeader("Authorization");
		if (authHeader == null)
			return null;
		return authHeader.replace("Bearer ", "");
	}

}

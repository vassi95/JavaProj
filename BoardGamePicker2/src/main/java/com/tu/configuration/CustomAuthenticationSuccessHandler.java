package com.tu.configuration;

import static com.tu.util.Constants.ADMIN_HOME_VIEW;
import static com.tu.util.Constants.USER_HOME_VIEW;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		response.setStatus(HttpServletResponse.SC_OK);

		boolean admin = false;

		for (GrantedAuthority auth : authentication.getAuthorities()) {
			if ("ADMIN".equals(auth.getAuthority())) {
				admin = true;
			}
		}

		if (admin) {
			response.sendRedirect(ADMIN_HOME_VIEW);
		} else {
			response.sendRedirect(USER_HOME_VIEW);
		}

	}

}

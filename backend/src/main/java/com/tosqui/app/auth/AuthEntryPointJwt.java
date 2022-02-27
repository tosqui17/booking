package com.tosqui.app.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		if (authException.toString().contains("locked")) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User account is locked. Please contact"
					+ " our team at tosqui17@gmail.com.");
		} else {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error: Unauthorized Access");
		}
	}

}

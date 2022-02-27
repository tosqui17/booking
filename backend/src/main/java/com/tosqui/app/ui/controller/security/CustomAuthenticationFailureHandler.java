package com.tosqui.app.ui.controller.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void onAuthenticationFailure(
			HttpServletRequest request,
			HttpServletResponse response,
			AuthenticationException exception) 
					throws IOException, ServletException {
		String error = exception.getMessage();
		response.setStatus(HttpStatus.BAD_REQUEST.value());
		Map<String, Object> data = new HashMap<>();
		data.put("exception", error);
		response.getOutputStream().println(objectMapper.writeValueAsString(data));
	}
}

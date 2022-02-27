package com.tosqui.app.utility;

import java.util.Date;

import com.tosqui.app.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtils {
	@Autowired
	UserService userService;
	@Value("${tosqui.app.jwtSecret}")
	private String jwtSecret;

	@Value("${tosqui.app.jwtExpiry}")
	private int jwtExpirationMs;

	public String generateJwtToken(Authentication authentication) {

		UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

		return Jwts.builder()
				.setSubject((userPrincipal.getUsername()))
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();
	}

	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}

	public boolean isUser(String token) {
		if (validateJwtToken(token)) {
			String username = getUserNameFromJwtToken(token);
			UserDetails userDetails = userService.loadUserByUsername(username);
			for (GrantedAuthority ga : userDetails.getAuthorities()) {
				if (ga.getAuthority().toString().equals("USER") ||
						ga.getAuthority().toString().equals("MANAGER") ||
						ga.getAuthority().toString().equals("ADMIN"))
					return true;
			}
		}
		return false;
	}

	public boolean isManager(String token) {
		if (validateJwtToken(token)) {
			String username = getUserNameFromJwtToken(token);
			UserDetails userDetails = userService.loadUserByUsername(username);
			for (GrantedAuthority ga : userDetails.getAuthorities()) {
				if (ga.getAuthority().toString().equals("MANAGER") ||
						ga.getAuthority().toString().equals("ADMIN"))
					return true;
			}
		}
		return false;
	}

	public boolean isAdmin(String token) {
		if (validateJwtToken(token)) {
			String username = getUserNameFromJwtToken(token);
			UserDetails userDetails = userService.loadUserByUsername(username);
			for (GrantedAuthority ga : userDetails.getAuthorities()) {
				if (ga.getAuthority().toString().equals("ADMIN"))
					return true;
			}
		}
		return false;
	}

	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
package com.tosqui.app.service;

import org.springframework.stereotype.Service;

@Service
public interface AdminService {
	String addRole(String username, String role);

	String escalatePrivilege(String username);

	boolean addHoliday(String holiday);

}

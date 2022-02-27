package com.tosqui.app.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.tosqui.app.io.UserEntity;
import com.tosqui.app.ui.model.response.ReservationJSONResponse;
import com.tosqui.app.ui.share.dto.UserDto;

@Service
public interface UserService extends UserDetailsService {
	boolean createUser(UserDto user);

	boolean updateUserVerificationStatus(String id, String token);

	void resetUserFailedCounterOnValidLogin(String username);

	int incrementUserFailedCounterOnValidLogin(String username);

	Collection<GrantedAuthority> getAuthorities(String username);

	boolean addReservation(String username, LocalDate date, LocalTime time);

	// void changePassword(String username, String token,String newPassword);
	
	List<LocalTime> availableHours(String date);

	UserDetails loadUserByUsername(String string);

	List<ReservationJSONResponse> loadReservationsByUsername(String username);

	void save(UserEntity ue);

	void saveAndFlush(UserEntity ue);

	UserEntity findUserByUsernameOrEmail(String string);

}

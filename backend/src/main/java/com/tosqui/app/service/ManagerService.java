package com.tosqui.app.service;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.stereotype.Service;

@Service
public interface ManagerService {
	boolean resetLockedUser(String username); // TO-DO

	boolean deleteReservation(LocalDate date, LocalTime time);

	boolean addReservation(String username, LocalDate date, LocalTime time);

}

package com.tosqui.app.service;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import com.tosqui.app.exception.InvalidUserException;
import com.tosqui.app.exception.UserAlreadyUnlockedException;
import com.tosqui.app.exception.UserNotFoundException;
import com.tosqui.app.io.ReservationEntity;
import com.tosqui.app.io.UserEntity;

@Configuration
@Service
public class ManagerServiceImpl implements ManagerService {

	@Autowired
	private UserService userService;

	@Autowired
	private ReservationService reservationService;

	@Autowired
	private HolidayService holidayService;

	@Override
	public boolean resetLockedUser(String string) {
		// Send secret token for recovering,
		UserEntity userEntity = userService.findUserByUsernameOrEmail(string);
		if (userEntity == null)
			throw new UserNotFoundException("No user found");
		if (userEntity.isAccountNonLocked())
			throw new UserAlreadyUnlockedException("This user is already unlocked");
		userEntity.setFailedLoginAttempts(0);
		userEntity.setAccountNonLocked(true);
		userService.saveAndFlush(userEntity);
		return true;
	}

	@Override
	public boolean deleteReservation(LocalDate date, LocalTime time) {
		try {
			reservationService.deleteReservation(date, time);
		} catch (Exception e) {
			return false;
		}
		return true;
	}



	@Override
	public boolean addReservation(String string, LocalDate date, LocalTime hour) {
		if (!holidayService.isHoliday(date) && reservationService.notExistReservation(date, hour, null)) {
			UserEntity userEntity = userService.findUserByUsernameOrEmail(string);
			if (userEntity == null)
				throw new UserNotFoundException("No user found");
			if (!userEntity.isAccountNonLocked() || !userEntity.isVerified())
				throw new InvalidUserException("This user is locked or unverified");
			ReservationEntity reservationEntity = reservationService.findByDateAndHourAndUserEntity(date, hour,
					null);
			reservationEntity.setUserEntity(userEntity);
			reservationService.saveAndFlush(reservationEntity);
			return true;
		}
		return false;

	}

		/*
	@Override
	public boolean deleteUser(String string) {
		try {
			userService.delete(userService.findUserByUsernameOrEmail(string, string));
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	*/


}

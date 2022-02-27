package com.tosqui.app.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import com.tosqui.app.exception.AlreadyManagerException;
import com.tosqui.app.exception.InvalidUserException;
import com.tosqui.app.exception.UserNotFoundException;
import com.tosqui.app.io.ReservationEntity;
import com.tosqui.app.io.RoleEntity;
import com.tosqui.app.io.UserEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

@Configuration
@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	private UserService userService;

	@Autowired
	private HolidayService holidayService;

	@Autowired
	private ReservationService reservationService;

	@Override
	public String escalatePrivilege(String string) {
		return addRole(string, "MANAGER");
	}

	@Override
	public String addRole(String string, String role) {
		UserEntity userEntity = userService.findUserByUsernameOrEmail(string);
		if (userEntity == null)
			throw new UserNotFoundException("No user found");
		if (!userEntity.isAccountNonLocked() || !userEntity.isVerified())
			throw new InvalidUserException("This user is locked or unverified");
		RoleEntity rolesEntity = new RoleEntity();
		rolesEntity.setRole(new SimpleGrantedAuthority(role));
		for (RoleEntity re : userEntity.getRoles()) {
			if (re.getRole().toString().equals(role))
				throw new AlreadyManagerException("This user is already a manager");
		}
		userEntity.getRoles().add(rolesEntity);
		userService.saveAndFlush(userEntity);
		return string + " " + "is now manager!";
	}

	@Override
	public boolean addHoliday(String holiday) {
		reservationService.deleteReservations(LocalDate.parse(holiday));
		addNextReservations();
		return holidayService.addHoliday(LocalDate.parse(holiday));
	}

	private void addNextReservations() {
		ArrayList<ReservationEntity> list = new ArrayList<>();
		LocalDate date = LocalDate.now().plusDays(5);
		int i = 0;
		boolean added = false;
		while (!added) {
			if (!holidayService.isHoliday(date.plusDays(i))
					&& reservationService.isPresent(date.plusDays(i)) == 0) {
				ArrayList<LocalTime> hours = hoursToAddOnInit();
				for (LocalTime lt : hours) {
					ReservationEntity re = new ReservationEntity();
					re.setDate(date.plusDays(i));
					re.setHour(lt);
					re.setUserEntity(null);
					list.add(re);
				}
				added = true;
			}
			i++;
		}
		reservationService.saveAllAndFlush(list);
	}

	private ArrayList<LocalTime> hoursToAddOnInit() {
		ArrayList<LocalTime> result = new ArrayList<LocalTime>();
		LocalTime current = LocalTime.of(8, 0);
		LocalTime latest = LocalTime.of(18, 0); // excluded
		while (current.isBefore(latest)) {
			result.add(current);
			current = current.plusMinutes(30);
		}
		return result;
	}

}

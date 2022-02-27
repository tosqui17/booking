package com.tosqui.app.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tosqui.app.exception.CustomersInTroubleException;
import com.tosqui.app.exception.DateAlreadyHolidayException;
import com.tosqui.app.exception.DateReservedException;
import com.tosqui.app.exception.UserAlreadyVerifiedException;
import com.tosqui.app.exception.UserExistException;
import com.tosqui.app.exception.UserNotFoundException;
import com.tosqui.app.io.ReservationEntity;
import com.tosqui.app.io.RoleEntity;
import com.tosqui.app.io.UserEntity;
import com.tosqui.app.repository.UserRepository;
import com.tosqui.app.ui.model.response.ReservationJSONResponse;
import com.tosqui.app.ui.share.dto.UserDto;
import com.tosqui.app.utility.Confirmation;
import com.tosqui.app.utility.Criterias;

@Configuration
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	ReservationService reservationService;

	@Autowired
	HolidayService holidayService;

	@Override
	public boolean createUser(UserDto userDto) {
		boolean result=false;
		if (userRepository.findUserByEmail(userDto.getEmail()) != null
				|| userRepository.findUserByUsername(userDto.getUsername()) != null)
			throw new UserExistException("A record with same email or username already exists");
		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(userDto, userEntity);
		String salt = BCrypt.gensalt();
		String bCryptHex = BCrypt.hashpw(userDto.getPassword(), salt);
		userEntity.setEncryptedPassword(bCryptHex);
		salt = BCrypt.gensalt();
		String verificationToken = ((String) (new Random()).ints(6, 48, 58)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString());
		String _verificationToken = verificationToken;
			
		userEntity.setVerificationToken(BCrypt.hashpw(verificationToken, salt));
		userEntity.setAccountNonLocked(true);
		userEntity.setFailedLoginAttempts(0);
		RoleEntity rolesEntity = new RoleEntity();
		rolesEntity.setRole(new SimpleGrantedAuthority("USER"));
		userEntity.getRoles().add(rolesEntity);
		try {
			userRepository.saveAndFlush(userEntity);
			Confirmation.show(userEntity.getUsername(), _verificationToken);
			result=true;

		} catch (Exception e) {
			throw new CustomersInTroubleException("Username must be min:4 chars and max 16 chars, firstName and lastName must be min:4 chars max:32 chars, email must be min:4 chars max 64:chars and password min: 8 chars");
		}
		return result;
		
	}

	@Override
	public UserDetails loadUserByUsername(String string) {
		UserEntity returnValue = userRepository.findUserByUsernameOrEmail(string, string);
		if (returnValue == null)
			throw new UserNotFoundException("No user found for " + string);
		ArrayList<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for (RoleEntity re : returnValue.getRoles()) {
			authorities.add((re.getRole()));
		}

		return new User(returnValue.getUsername(), returnValue.getEncryptedPassword(), returnValue.isVerified(), true,
				true, returnValue.isAccountNonLocked(), authorities);

	}
	
	@Transactional
	@Override
	public boolean updateUserVerificationStatus(String username, String token) {
		boolean result = false;
		UserEntity userEntity = userRepository.findUserByUsernameOrEmail(username, username);
		if (userEntity == null || userEntity.isSoftDeleted())
			throw new UserNotFoundException("No user found for " + username);
		// add deleting account on 5 failed attempts

		else if (userEntity.isVerified())
			throw new UserAlreadyVerifiedException("This is user is already verified");

		// User tries multiple times to verify
		else if (!userEntity.isVerified() && userEntity.getFailedLoginAttempts() == 5) {
			softDelete(userEntity);
		}

		else if (bCryptPasswordEncoder.matches(token, userEntity.getVerificationToken()) && !userEntity.isVerified()) {
			userEntity.setVerified(true);
			userEntity.setFailedLoginAttempts(0);
			result = true;
		} else {
				String salt = BCrypt.gensalt();
				String verificationToken = ((String) (new Random()).ints(6, 48, 58)
						.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString());
				userEntity.setVerificationToken(BCrypt.hashpw(verificationToken, salt));
				userEntity.setFailedLoginAttempts(userEntity.getFailedLoginAttempts() + 1);
				result = false;
				try {
					Confirmation.show(userEntity.getUsername(), verificationToken);
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
		userRepository.saveAndFlush(userEntity);
		return result;
	}

	@Override
	public void resetUserFailedCounterOnValidLogin(String string) { // on valid login, this counter will be reset
		UserEntity userEntity = userRepository.findUserByUsernameOrEmail(string, string);
		if (userEntity == null)
			throw new UserNotFoundException("No user found for " + string);
		userEntity.setFailedLoginAttempts(0);
		userRepository.saveAndFlush(userEntity);
	}

	@Override
	public int incrementUserFailedCounterOnValidLogin(String string) { // after 5 attempts account get locked
		UserEntity userEntity = userRepository.findUserByUsernameOrEmail(string, string);
		if (userEntity == null)
			throw new UserNotFoundException("No user found for " + string);
		int counter = userEntity.getFailedLoginAttempts() + 1;
		userEntity.setFailedLoginAttempts(counter);
		if (counter > 5) {
			userEntity.setAccountNonLocked(false);
		}
		userRepository.saveAndFlush(userEntity);
		return counter;
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities(String string) {
		UserEntity userEntity = userRepository.findUserByUsernameOrEmail(string, string);
		if (userEntity == null)
			throw new UserNotFoundException("No user found for " + string);
		ArrayList<GrantedAuthority> returnValue = new ArrayList<GrantedAuthority>();
		for (RoleEntity re : userEntity.getRoles()) {
			returnValue.add((re.getRole()));
		}
		return returnValue;
	}

	public boolean addReservation(String string, LocalDate date, LocalTime hour) throws RuntimeException {
		boolean isHoliday = holidayService.isHoliday(date);
		boolean notExistReservation = reservationService.notExistReservation(date, hour, null);
		boolean meetsCriteria = Criterias.dateTimeCriteria(date, hour); 
		if (!isHoliday && meetsCriteria && notExistReservation) {
			UserEntity userEntity = userRepository.findUserByUsernameOrEmail(string, string);
			if (userEntity == null)
				throw new UserNotFoundException("No user found for " + string);
			ReservationEntity reservationEntity = reservationService.findByDateAndHourAndUserEntity(date, hour,
					null);
			reservationEntity.setUserEntity(userEntity);
			reservationService.saveAndFlush(reservationEntity);
			return true;
		}
		else if (isHoliday)
			throw new DateAlreadyHolidayException("You can't because we're closed");
		else if (!meetsCriteria)
			throw new CustomersInTroubleException("You can't because we don't work in this period");
		else if (notExistReservation)
			throw new DateReservedException("Sorry, it is already reserved");
		return false;
	}

	@Override
	public List<LocalTime> availableHours(String date) {
		return reservationService.availableHours(LocalDate.parse(date));
	}

	@Override
	public List<ReservationJSONResponse> loadReservationsByUsername(String string) {
		UserEntity userEntity = userRepository.findUserByUsernameOrEmail(string, string);
		if (userEntity == null)
			throw new UserNotFoundException("No user found for " + string);
		List<ReservationJSONResponse> list = reservationService.findAllByUserEntity(userEntity);
		return list;
	}

	@Override
	public void save(UserEntity ue) {
		userRepository.save(ue);
	}

	@Override
	public void saveAndFlush(UserEntity ue) {
		userRepository.saveAndFlush(ue);

	}

	@Override
	public UserEntity findUserByUsernameOrEmail(String string) {
		return userRepository.findUserByUsernameOrEmail(string, string);

	}

	public void softDelete(UserEntity ue){
		ue.setSoftDeleted(true);
		save(ue);
	}


	

	/*
	 * @Override
	 * public void changePassword(String username, String token, String newPassword)
	 * {
	 * UserEntity userEntity = userRepository.findUserByEmailOrUsername(username);
	 * if(userEntity == null) throw new UserNotFoundException("No user found for " +
	 * username);
	 * if(userEntity.isAccountNonLocked() && bCryptPasswordEncoder.matches(token,
	 * userEntity.getVerificationToken())){
	 * String salt = BCrypt.gensalt();
	 * userEntity.setEncryptedPassword(BCrypt.hashpw(newPassword, salt));
	 * userRepository.saveAndFlush(userEntity);
	 * }
	 * }
	 */

}

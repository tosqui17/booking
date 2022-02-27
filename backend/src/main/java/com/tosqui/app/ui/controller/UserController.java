package com.tosqui.app.ui.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.tosqui.app.exception.CustomersInTroubleException;
import com.tosqui.app.exception.DateReservedException;
import com.tosqui.app.service.ReservationService;
import com.tosqui.app.service.UserService;
import com.tosqui.app.ui.model.request.UserDetailsRequestModel;
import com.tosqui.app.ui.model.response.BookingJSONResponse;
import com.tosqui.app.ui.model.response.JSONResponse;
import com.tosqui.app.ui.model.response.ReservationJSONResponse;
import com.tosqui.app.ui.share.dto.UserDto;
import com.tosqui.app.utility.JwtUtils;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	JwtUtils jwtUtils;
	@Autowired
	UserService userService;
	@Autowired
	ReservationService reservationService;

	@PostMapping
	public ResponseEntity<JSONResponse> createUser(@RequestBody UserDetailsRequestModel userDetails) {
		if(userDetails.getPassword().length() < 8 )  throw new CustomersInTroubleException("Username must be min:4 chars and max 16 chars, firstName and lastName must be min:4 chars max:32 chars, email must be min:4 chars max 64:chars and password min: 8 chars");
		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userDetails, userDto);
		if(userService.createUser(userDto)){
		JSONResponse response = new JSONResponse(
				"Your registration is successful! Please check your email to verify your account!");
		return new ResponseEntity<JSONResponse>(response, HttpStatus.CREATED);}
		return null;
	}

	@GetMapping(path = "/verify/{username}/{token}")
	public ResponseEntity<?> updateUser(@PathVariable String username, @PathVariable String token) {
		if (userService.updateUserVerificationStatus(username, token)) {
			JSONResponse response = new JSONResponse(username + " account is now activated");
			return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
		} else {
			throw new CustomersInTroubleException("Something goes wrong, try again.");
		}
	}

	@GetMapping(path = "/book/{date}/{hour}")
	public ResponseEntity<JSONResponse> book(@RequestHeader("Authorization") String bearer, @PathVariable String date,
			@PathVariable String hour) {
		LocalDate booking_date = LocalDate.parse(date); // ISO date formatter yyyy-mm-dd
		LocalTime booking_time = LocalTime.parse(hour); // hh:mm
		String token = bearer.substring(7);
		if (userService.addReservation(jwtUtils.getUserNameFromJwtToken(token), booking_date, booking_time)) {
			JSONResponse response = new JSONResponse("Reservation on " + date + " - " + hour + " is confirmed");
			return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
		}
		else{
			throw new DateReservedException("Sorry, it is already reserved");
		}
	}

	@GetMapping(path = "/available")
	public List<BookingJSONResponse> available() {
		return reservationService.availableDatesHours();
	}

	@PreAuthorize("hasAnyAuthority('USER','ADMIN','MANAGER')")
	@PostMapping(path = "/test")
	public ResponseEntity<Boolean> test(@RequestHeader("Authorization") String bearer) {
		String token = bearer.substring(7);
		if (jwtUtils.isUser(token))
			return new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.OK);
		return new ResponseEntity<Boolean>(Boolean.FALSE, HttpStatus.UNAUTHORIZED);
	}

	@PostMapping(path = "/reservations")
	public ResponseEntity<?> reservations(@RequestHeader("Authorization") String bearer) {
		String token = bearer.substring(7);
		if (jwtUtils.isUser(token))
			return new ResponseEntity<List<ReservationJSONResponse>>(
					userService.loadReservationsByUsername(jwtUtils.getUserNameFromJwtToken(token)), HttpStatus.OK);
		return null;
	}

}

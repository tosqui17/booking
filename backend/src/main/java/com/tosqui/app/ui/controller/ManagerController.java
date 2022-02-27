package com.tosqui.app.ui.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.tosqui.app.exception.DateReservedException;
import com.tosqui.app.service.ManagerService;
import com.tosqui.app.service.ReservationService;
import com.tosqui.app.ui.model.response.BookingJSONResponse;
import com.tosqui.app.ui.model.response.JSONResponse;
import com.tosqui.app.utility.JwtUtils;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/managers")
public class ManagerController {

	@Autowired
	private ManagerService managerService;
	@Autowired
	JwtUtils jwtUtils;
	@Autowired
	ReservationService reservationService;

	@GetMapping(path = "/book/{username}/{date}/{time}")
	public ResponseEntity<JSONResponse> book(@RequestHeader("Authorization") String bearer,
			@PathVariable String username, @PathVariable String date, @PathVariable String time) {
		JSONResponse response;
		LocalDate booking_date = LocalDate.parse(date); // ISO date formatter yyyy-mm-dd
		LocalTime booking_time = LocalTime.parse(time);
		String token = bearer.substring(7);
		if (jwtUtils.isManager(token) && managerService.addReservation(username, booking_date, booking_time)) {
			response = new JSONResponse(
					"Reservation for customer " + username + " confirmed on " + date + " " + time + ".");
			return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
		} else {
			throw new DateReservedException("Sorry, it is already reserved");
		}

	}

	@GetMapping(path = "/available")
	public List<BookingJSONResponse> availableDatesHours() {
		return reservationService.availableDatesHours();
	}

	@GetMapping(path = "/reserved")
	public List<BookingJSONResponse> reservedQuest() {
		return reservationService.reservedDateHours();
	}

	/*
	 * @DeleteMapping
	 * public ResponseEntity<Boolean> deleteAccount(@PathVariable String username){
	 * boolean res = managerService.deleteUser(username);
	 * if(res) return new ResponseEntity<Boolean>(res,HttpStatus.OK);
	 * return new ResponseEntity<Boolean>(res,HttpStatus.METHOD_NOT_ALLOWED);
	 * }
	 */
	@PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
	@DeleteMapping(path = "/book/{date}/{time}")
	public ResponseEntity<JSONResponse> deleteReservation(@RequestHeader("Authorization") String bearer,
			@PathVariable String date, @PathVariable String time) {
		JSONResponse response;
		boolean res = managerService.deleteReservation(LocalDate.parse(date), LocalTime.parse(time));
		if (res) {
			response = new JSONResponse("Reservation on " + date + " at " + time + " is now deleted");
			return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
		}
		response = new JSONResponse("Something goes wrong");
		return new ResponseEntity<JSONResponse>(response, HttpStatus.NOT_ACCEPTABLE);
	}

	@PatchMapping(path = "/unlock/")
	public ResponseEntity<JSONResponse> unlockUser(@RequestHeader("Authorization") String bearer,
			@RequestBody String data) {
		String user = data.substring(5);
		JSONResponse response;
		String token = bearer.substring(7);
		if (jwtUtils.isManager(token)) {
			boolean res = managerService.resetLockedUser(user);
			if (res) {

				response = new JSONResponse("User " + user + " is now unlocked");
				return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
			}
		}
		response = new JSONResponse("Something goes wrong");
		return new ResponseEntity<JSONResponse>(response, HttpStatus.METHOD_NOT_ALLOWED);
	}

	@PostMapping(path = "/test")
	public ResponseEntity<Boolean> test(@RequestHeader("Authorization") String bearer) {
		String token = bearer.substring(7);
		if (jwtUtils.isManager(token))
			return new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.OK);
		return new ResponseEntity<Boolean>(Boolean.FALSE, HttpStatus.UNAUTHORIZED);
	}

}
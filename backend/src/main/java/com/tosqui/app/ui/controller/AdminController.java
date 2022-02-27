package com.tosqui.app.ui.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.tosqui.app.service.AdminService;
import com.tosqui.app.service.HolidayService;
import com.tosqui.app.service.ReservationService;
import com.tosqui.app.ui.model.response.JSONResponse;
import com.tosqui.app.ui.model.response.ReservationJSONResponse;
import com.tosqui.app.utility.JwtUtils;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admins")
public class AdminController {

	@Autowired
	ReservationService reservationService;
	@Autowired
	AdminService adminService;
	@Autowired
	HolidayService holidayService;
	@Autowired
	JwtUtils jwtUtils;

	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@PatchMapping(path = "/escalate")
	public ResponseEntity<JSONResponse> escalatePrivilege(@RequestHeader("Authorization") String bearer,
			@RequestBody String data) {
		JSONResponse response;
		if (data.substring(0, 5).equals("user=")) {
			response = new JSONResponse(adminService.escalatePrivilege(data.substring(5)));
			return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
		}
		response = new JSONResponse("Something goes wrong");
		return new ResponseEntity<JSONResponse>(response, HttpStatus.BAD_REQUEST);
	}

	@PostMapping(path = "/addHoliday")
	public ResponseEntity<JSONResponse> addHoliday(@RequestHeader("Authorization") String bearer,
			@RequestBody String data) {
		String token = bearer.substring(7);
		JSONResponse response;
		if (adminService.addHoliday(data.substring(5)) && jwtUtils.isAdmin(token)) {
			response = new JSONResponse(data.substring(5) + " is now a holiday.");
			return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
		}
		response = new JSONResponse("Something goes wrong");
		return new ResponseEntity<JSONResponse>(response, HttpStatus.BAD_REQUEST);
	}

	@PostMapping(path = "/test")
	public ResponseEntity<Boolean> test(@RequestHeader("Authorization") String bearer) {
		String token = bearer.substring(7);
		if (jwtUtils.isAdmin(token))
			return new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.OK);
		return new ResponseEntity<Boolean>(Boolean.FALSE, HttpStatus.UNAUTHORIZED);
	}

	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@GetMapping(path = "/retrieve/{date}")
	public ResponseEntity<List<ReservationJSONResponse>> retrieve(@RequestHeader("Authorization") String bearer,
			@PathVariable String date) {
		List<ReservationJSONResponse> response = reservationService.findReservedHoursOnDateWithUserInfo(date);
		return new ResponseEntity<List<ReservationJSONResponse>>(response, HttpStatus.OK);
	}

}
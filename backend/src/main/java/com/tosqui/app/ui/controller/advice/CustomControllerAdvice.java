package com.tosqui.app.ui.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.tosqui.app.exception.UserNotFoundException;

import java.time.DateTimeException;

import com.tosqui.app.exception.AlreadyManagerException;
import com.tosqui.app.exception.BackToTheFutureException;
import com.tosqui.app.exception.CustomersInTroubleException;
import com.tosqui.app.exception.DateAlreadyHolidayException;
import com.tosqui.app.exception.DateReservedException;
import com.tosqui.app.exception.UserExistException;
import com.tosqui.app.exception.NoReservationException;
import com.tosqui.app.exception.UserAlreadyUnlockedException;
import com.tosqui.app.exception.UserAlreadyVerifiedException;
import com.tosqui.app.ui.model.response.JSONResponse;

@ControllerAdvice
public class CustomControllerAdvice extends ResponseEntityExceptionHandler{

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<JSONResponse> handleUserNotFoundException(UserNotFoundException userNotFoundException) {
		return new ResponseEntity<JSONResponse>(new JSONResponse("User not found"), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(AlreadyManagerException.class)
	public ResponseEntity<JSONResponse> handleAlreadyManagerException(AlreadyManagerException alreadyManagerException) {
		return new ResponseEntity<JSONResponse>(new JSONResponse(alreadyManagerException.getMessage()),
				HttpStatus.CONFLICT);
	}

	@ExceptionHandler(DateAlreadyHolidayException.class)
	public ResponseEntity<JSONResponse> handleDateAlreadyHolidayException(
			DateAlreadyHolidayException dateAlreadyHolidayException) {
		return new ResponseEntity<JSONResponse>(new JSONResponse(dateAlreadyHolidayException.getMessage()),
				HttpStatus.CONFLICT);
	}

	@ExceptionHandler(NoReservationException.class)
	public ResponseEntity<JSONResponse> handleNoReservationException(NoReservationException noReservationException) {
		return new ResponseEntity<JSONResponse>(new JSONResponse(noReservationException.getMessage()),
				HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UserAlreadyUnlockedException.class)
	public ResponseEntity<JSONResponse> handleUserAlreadyUnlockedException(
			UserAlreadyUnlockedException userAlreadyUnlockedException) {
		return new ResponseEntity<JSONResponse>(new JSONResponse(userAlreadyUnlockedException.getMessage()),
				HttpStatus.CONFLICT);
	}

	@ExceptionHandler(UserAlreadyVerifiedException.class)
	public ResponseEntity<JSONResponse> handleUserAlreadyVerifiedException(
			UserAlreadyVerifiedException userAlreadyVerifiedException) {
		return new ResponseEntity<JSONResponse>(new JSONResponse(userAlreadyVerifiedException.getMessage()),
				HttpStatus.CONFLICT);
	}

	@ExceptionHandler(UserExistException.class)
	public ResponseEntity<JSONResponse> handleUserExistException(UserExistException userExistException) {
		return new ResponseEntity<JSONResponse>(new JSONResponse(userExistException.getMessage()), HttpStatus.CONFLICT);
	}

	@ExceptionHandler(CustomersInTroubleException.class)
	public ResponseEntity<JSONResponse> handleCustomersInTroubleException(
			CustomersInTroubleException customersInTroubleException) {
		return new ResponseEntity<JSONResponse>(new JSONResponse(customersInTroubleException.getMessage()),
				HttpStatus.CONFLICT);
	}

	@ExceptionHandler(BackToTheFutureException.class)
	public ResponseEntity<JSONResponse> handleBackToTheFutureException(
			BackToTheFutureException backToTheFutureException) {
		return new ResponseEntity<JSONResponse>(new JSONResponse(backToTheFutureException.getMessage()),
				HttpStatus.CONFLICT);
	}

	@ExceptionHandler(DateTimeException.class)
	public ResponseEntity<JSONResponse> handleDateTimeException(DateTimeException dateTimeException) {
		return new ResponseEntity<JSONResponse>(new JSONResponse("AHAH nice try"), HttpStatus.CONFLICT);
	}

	@ExceptionHandler(DateReservedException.class)
	public ResponseEntity<JSONResponse> handleDateReservedException(DateReservedException dateReservedException) {
		return new ResponseEntity<JSONResponse>(new JSONResponse(dateReservedException.getMessage()),
				HttpStatus.CONFLICT);
	}

	@ExceptionHandler(ObjectOptimisticLockingFailureException.class)
	public ResponseEntity<JSONResponse> handleObjectOptimisticLockingFailureException(
			ObjectOptimisticLockingFailureException objectOptimisticLockingFailureException) {
		return new ResponseEntity<JSONResponse>(
				new JSONResponse("Oh no! Someone tries to perform a request and was faster than you"),
				HttpStatus.CONFLICT);
	}

	@ExceptionHandler(RequestRejectedException.class)
	public ResponseEntity<JSONResponse> handleRequestRejectedException(
		RequestRejectedException requestRejectedException) {
		return new ResponseEntity<JSONResponse>(
				new JSONResponse("Malformed request !"),
				HttpStatus.CONFLICT);
	}
	

}

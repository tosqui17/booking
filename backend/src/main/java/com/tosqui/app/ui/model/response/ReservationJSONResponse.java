package com.tosqui.app.ui.model.response;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationJSONResponse {


	private String firstName="";
	private String lastName="";
	private LocalDate date;
	private LocalTime hour;

	public ReservationJSONResponse(String firstName, String lastName, LocalDate date, LocalTime hour) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.date = date;
		this.hour = hour;
	}

	
	public ReservationJSONResponse(LocalDate date, LocalTime hour) {
		this.date = date;
		this.hour = hour;
	}
	


	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public LocalTime getHour() {
		return this.hour;
	}

	public void setHour(LocalTime hour) {
		this.hour = hour;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

}

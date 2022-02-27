package com.tosqui.app.ui.model.response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class BookingJSONResponse {
	private LocalDate date;
	private List<LocalTime> hours;

	public List<LocalTime> getHours() {
		return this.hours;
	}

	public void setHours(List<LocalTime> hours) {
		this.hours = hours;
	}

	public BookingJSONResponse(LocalDate date, List<LocalTime> hours) {
		this.date = date;
		this.hours = hours;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

}

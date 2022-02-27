package com.tosqui.app.io;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "holidays")
public class HolidayEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(nullable = false, unique = true)
	private LocalDate date;

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalDate getDate() {
		return date;
	}

}

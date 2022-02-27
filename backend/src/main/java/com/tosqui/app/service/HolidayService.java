package com.tosqui.app.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

@Service
public interface HolidayService {
	public boolean addHoliday(LocalDate date);

	public boolean isHoliday(LocalDate date);
}

package com.tosqui.app.service;

import java.time.DayOfWeek;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import com.tosqui.app.exception.BackToTheFutureException;
import com.tosqui.app.exception.CustomersInTroubleException;
import com.tosqui.app.exception.DateAlreadyHolidayException;
import com.tosqui.app.io.HolidayEntity;
import com.tosqui.app.repository.HolidaysRepository;

@Configuration
@Service
public class HolidayServiceImpl implements HolidayService {

	@Autowired
	HolidaysRepository holidayRepository;
	@Autowired
	ReservationService reservationService;

	public boolean isHoliday(LocalDate date) {
		if (holidayRepository.findByDate(date) != null ||
				date.getDayOfWeek() == DayOfWeek.SUNDAY ||
				date.getDayOfWeek() == DayOfWeek.SATURDAY)
			return true;
		return false;
	}

	public boolean addHoliday(LocalDate date) {
		if (date.isBefore(LocalDate.now()))
			throw new BackToTheFutureException("Sorry, we can't afford a DeLorean ;)");
		if (reservationService.hasReservationOnDate(date))
			throw new CustomersInTroubleException(
					"This day can't be an holiday, because customers reserved on that day");
		if (isHoliday(date))
			throw new DateAlreadyHolidayException("This is already a holiday");
		
		HolidayEntity he = new HolidayEntity();
		he.setDate(date);
		holidayRepository.saveAndFlush(he);
		return true;
	}

}

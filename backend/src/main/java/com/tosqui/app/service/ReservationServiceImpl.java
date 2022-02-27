package com.tosqui.app.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tosqui.app.exception.NoReservationException;
import com.tosqui.app.io.ReservationEntity;
import com.tosqui.app.io.UserEntity;
import com.tosqui.app.repository.ReservationRepository;
import com.tosqui.app.ui.model.response.BookingJSONResponse;
import com.tosqui.app.ui.model.response.ReservationJSONResponse;

@Configuration
@Service
public class ReservationServiceImpl implements ReservationService {

	@Autowired
	ReservationRepository reservationRepository;
	@Autowired
	HolidayService holidayService;

	/*
	public List<LocalTime> availableHours(LocalDate date) {

		List<LocalTime> availableHours ;
		availableHours = (LocalDate.now().isEqual(date)) ? reservationRepository.findAvailableHoursToday(date,LocalTime.now()) : reservationRepository.findAvailableHoursOnDate(date);
		return availableHours;
	}
	*/

	public List<LocalTime> availableHours(LocalDate date) {
		List<LocalTime> availableHours = reservationRepository.findAvailableHoursOnDate(date, LocalTime.now(),LocalDate.now());
		return availableHours;
	}

	public ArrayList<BookingJSONResponse> availableDatesHours() {
		
		List<LocalDate> availableDate = reservationRepository.findReservableDate(LocalDate.now());
		Collections.sort(availableDate);
		ArrayList<BookingJSONResponse> result = new ArrayList<BookingJSONResponse>();
		for (LocalDate ld : availableDate) {
			result.add(new BookingJSONResponse(ld, availableHours(ld)));
		}
		return result;
	}

	public List<BookingJSONResponse> reservedDateHours() {
		List<LocalDate> reservedDates = reservationRepository.findDistinctReservedDate();
		List<BookingJSONResponse> reservedDatesHours = new ArrayList<BookingJSONResponse>();
		Collections.sort(reservedDates);
		for (LocalDate date : reservedDates) {
			List<LocalTime> reservedHoursOnDate = reservationRepository.findReservedHoursOnDate(date);
			reservedDatesHours.add(new BookingJSONResponse(date, reservedHoursOnDate));
		}
		return reservedDatesHours;

	}
	@Transactional
	@Modifying
	public boolean deleteReservation(LocalDate date, LocalTime hour) {
		ReservationEntity re = reservationRepository.findReservation(date, hour);
		if (re == null)
			throw new NoReservationException("No reservation exist with these parameters");
		re.setUserEntity(null);
		reservationRepository.saveAndFlush(re);
		return true;
		
	}

	@Override
	public boolean notExistReservation(LocalDate date, LocalTime hour, UserEntity userEntity) {
		return reservationRepository.findByDateAndHourAndUserEntity(date, hour, userEntity) != null;
	}

	@Override
	public boolean hasReservationOnDate(LocalDate date) {
		return !reservationRepository.findAllReservationOnDate(date).isEmpty();
	}

	@Override
	public ReservationEntity findByDateAndHourAndUserEntity(LocalDate date, LocalTime hour, UserEntity ue) {
		return reservationRepository.findByDateAndHourAndUserEntity(date, hour, ue);
	}

	@Override
	public void save(ReservationEntity re) {
		reservationRepository.save(re);
		
	}

	@Override
	public void saveAndFlush(ReservationEntity re) {
		reservationRepository.saveAndFlush(re);
		
	}

	@Override
	public List<ReservationJSONResponse> findAllByUserEntity (UserEntity userEntity) {
		return reservationRepository.findAllByUserEntity(userEntity, LocalDate.now());
	}

	@Override
	public List<ReservationJSONResponse> findReservedHoursOnDateWithUserInfo(String date) {
		return reservationRepository.findReservedHoursOnDateWithUserInfo(LocalDate.parse(date));
	}

	@Override
	public void deleteReservations(LocalDate date) {
		reservationRepository.deleteReservations(date);
		
	}

	@Override
	public int isPresent(LocalDate date) {
		return reservationRepository.isPresent(date);
	}

	@Override
	public void saveAllAndFlush(List<ReservationEntity> list) {
		reservationRepository.saveAllAndFlush(list);		
	}

}

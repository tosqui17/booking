package com.tosqui.app.service;

import java.time.LocalDate;

import java.time.LocalTime;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import com.tosqui.app.io.ReservationEntity;
import com.tosqui.app.io.UserEntity;
import com.tosqui.app.ui.model.response.BookingJSONResponse;
import com.tosqui.app.ui.model.response.ReservationJSONResponse;

@Configuration
@Service
public interface ReservationService {
	
	boolean notExistReservation(LocalDate date, LocalTime time, UserEntity userEntity);

	List<LocalTime> availableHours(LocalDate localDate);

	boolean deleteReservation(LocalDate date, LocalTime time);

	List<BookingJSONResponse> availableDatesHours();

	List<BookingJSONResponse> reservedDateHours();

	boolean hasReservationOnDate(LocalDate date);

	ReservationEntity findByDateAndHourAndUserEntity(LocalDate date, LocalTime hour, UserEntity ue);

	void save(ReservationEntity re);

	void saveAndFlush(ReservationEntity re);

	List<ReservationJSONResponse> findAllByUserEntity(UserEntity userEntity);

	List<ReservationJSONResponse> findReservedHoursOnDateWithUserInfo(String date);

	void deleteReservations(LocalDate date);

    int isPresent(LocalDate plusDays);

    void saveAllAndFlush(List<ReservationEntity> list);

}

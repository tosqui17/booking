package com.tosqui.app.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tosqui.app.io.ReservationEntity;
import com.tosqui.app.io.UserEntity;
import com.tosqui.app.ui.model.response.ReservationJSONResponse;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
	
	ReservationEntity findByDateAndHourAndUserEntity(LocalDate date, LocalTime hour, UserEntity userEntity);

	@Query("SELECT new com.tosqui.app.ui.model.response.ReservationJSONResponse(r.userEntity.firstName, r.userEntity.lastName, r.date, r.hour) FROM reservations r where r.userEntity = ?1 and r.date >= ?2")
	List<ReservationJSONResponse> findAllByUserEntity(UserEntity userEntity, LocalDate date);

	@Query("SELECT DISTINCT r.date FROM reservations r WHERE r.date >= ?1 ")
	List<LocalDate> findReservableDate(LocalDate date);

	@Query("SELECT r.hour FROM reservations r where r.userEntity = null and ((r.date = ?1 and r.hour >=?2) OR (r.date = ?1 and r.date != ?3))")
	List<LocalTime> findAvailableHoursOnDate(LocalDate date, LocalTime hour, LocalDate date1);

	@Query("SELECT r FROM reservations r where r.date=?1 and r.userEntity != null")
	List<ReservationEntity> findAllReservationOnDate(LocalDate date);

	@Query("SELECT DISTINCT r.date FROM reservations r where r.userEntity != null")
	List<LocalDate> findDistinctReservedDate();

	@Query("SELECT r.hour FROM reservations r where r.userEntity != null and r.date=?1")
	List<LocalTime> findReservedHoursOnDate(LocalDate date);

	@Query("SELECT new com.tosqui.app.ui.model.response.ReservationJSONResponse(r.userEntity.firstName, r.userEntity.lastName, r.date, r.hour) FROM reservations r where r.date = ?1")
	List<ReservationJSONResponse> findReservedHoursOnDateWithUserInfo(LocalDate date);

	@Query("SELECT r FROM reservations r where r.date=?1 and r.userEntity != null and r.hour=?2")
	ReservationEntity findReservation(LocalDate date, LocalTime hour);

	@Transactional
	@Modifying
	@Query("DELETE FROM reservations r where r.date <= ?1")
	void deleteOldReservations(LocalDate date);

	@Transactional
	@Modifying
	@Query("DELETE FROM reservations r where r.date = ?1")
	void deleteReservations(LocalDate date);

	@Query("SELECT COUNT (DISTINCT r.date) FROM reservations r")
	int findNumDistinctDate();

	@Query("SELECT COUNT(r) FROM reservations r where r.date=?1")
	int isPresent(LocalDate date);

}

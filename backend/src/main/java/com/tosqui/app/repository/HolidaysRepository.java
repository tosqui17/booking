package com.tosqui.app.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tosqui.app.io.HolidayEntity;

@Repository
public interface HolidaysRepository extends JpaRepository<HolidayEntity, Long> {
	HolidayEntity findByDate(LocalDate Date);
}

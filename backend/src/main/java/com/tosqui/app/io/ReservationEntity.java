package com.tosqui.app.io;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Version;



@Entity(name = "reservations")
public class ReservationEntity implements Comparable<ReservationEntity> {

	@Id
	@GeneratedValue
	private Long identifier;

	public Long getIdentifier() {
		return this.identifier;
	}

	public void setIdentifier(Long identifier) {
		this.identifier = identifier;
	}
	private LocalDate date;
	private LocalTime hour;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user", referencedColumnName = "id")
	private UserEntity userEntity;
	@Version
	private Long version;

	public LocalTime getHour() {
		return this.hour;
	}

	public void setHour(LocalTime hour) {
		this.hour = hour;
	}

	public UserEntity getUserEntity() {
		return this.userEntity;
	}

	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
	}

	public Long getVersion() {
		return this.version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	@Override
	public int compareTo(ReservationEntity o) {
		if (this.date.isAfter(o.getDate()))
			return 1;
		else if (this.date.isBefore(o.getDate()))
			return -1;
		else if (this.hour.isAfter(o.hour))
			return 1;
		else if (this.hour.isBefore(o.hour))
			return -1;
		else
			return 0;
	}

}

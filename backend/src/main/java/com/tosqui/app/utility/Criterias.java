package com.tosqui.app.utility;

import java.time.LocalDate;
import java.time.LocalTime;

public class Criterias {

	public static boolean dateTimeCriteria(LocalDate date, LocalTime hour) {
		LocalDate today = LocalDate.now();
		int _hour = hour.getHour();
		int _minute = hour.getMinute();

		if (date.equals(today) && _hour >= 8 && _hour <= 17) {
			if (_hour == LocalTime.now().getHour() &&
					_minute % 30 == 0 && _minute % 60 != 0 &&
					LocalTime.now().getMinute() < _minute) {
				return true;
			} else if (_hour > LocalTime.now().getHour() &&
					_minute % 30 == 0) {
				return true;
			} else if (_hour > LocalTime.now().getHour() &&
					_minute % 60 == 0) {
				return true;
			}
			return false;

		}

		else {
			if (date.isAfter(today) && _hour >= 8 && _hour <= 17 &&
					_minute % 30 == 0) {
				return true;
			}
			return false;

		}
	}

}

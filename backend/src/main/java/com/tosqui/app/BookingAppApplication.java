package com.tosqui.app;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;

import com.tosqui.app.io.ReservationEntity;
import com.tosqui.app.io.RoleEntity;
import com.tosqui.app.io.UserEntity;
import com.tosqui.app.repository.ReservationRepository;
import com.tosqui.app.repository.UserRepository;
import com.tosqui.app.service.AdminService;
import com.tosqui.app.service.HolidayService;
import com.tosqui.app.service.ManagerService;
import com.tosqui.app.service.ReservationService;
import com.tosqui.app.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
@EnableScheduling
public class BookingAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookingAppApplication.class, args);
	}

	@Autowired
	UserService userService;
	@Autowired
	UserRepository userRepository;
	@Autowired
	ReservationService reservationService;
	@Autowired
	ReservationRepository reservationRepository;
	@Autowired
	ManagerService managerService;
	@Autowired
	AdminService adminService;
	@Autowired
	HolidayService holidayService;

	@Transactional
	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() {
		addUsersOnInit();
		reservationRepository.deleteOldReservations(LocalDate.now().minusDays(1));
		addReservationsOnInit(LocalDate.now());
	}

	private void addUsersOnInit() {
		createUser("admin", "ADMIN");
		createUser("manager", "MANAGER");
		createUser("user", "USER");
	}

	private void createUser(String username, String role) {

		if (userRepository.findUserByUsernameOrEmail(username, username) != null) {
			System.out.println("Test user with " + role + " authority exists");
			return;
		}
		UserEntity userEntity = new UserEntity();
		userEntity.setFirstName("First Name");
		userEntity.setLastName("Last Name");
		userEntity.setUsername(username);
		String random = ((String) (new Random()).ints(6, 48, 58)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString());
		userEntity.setEmail("random@" + random + ".it");
		String salt = BCrypt.gensalt();
		String bCryptHex = BCrypt.hashpw(username + "123", salt);
		userEntity.setEncryptedPassword(bCryptHex);

		salt = BCrypt.gensalt();
		String verificationToken = ((String) (new Random()).ints(6, 48, 58)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString());
		userEntity.setVerificationToken(BCrypt.hashpw(verificationToken, salt));
		userEntity.setAccountNonLocked(true);
		userEntity.setFailedLoginAttempts(0);
		userEntity.setVerified(true);

		RoleEntity rolesEntity = new RoleEntity();
		rolesEntity.setRole(new SimpleGrantedAuthority(role));
		userEntity.getRoles().add(rolesEntity);

		userRepository.saveAndFlush(userEntity);
		System.out.println(role + " created");
	}

	private void addReservationsOnInit(LocalDate _date) {
		int days = reservationRepository.findNumDistinctDate();
		if (days == 7) {
			System.out.println("Reservations table is already populated");
			return;
		}

		LocalDate date = _date; 
		ArrayList<ReservationEntity> toAddOnInit = new ArrayList<>();
		int i = 0;
		int j = 0;
		if (holidayService.isHoliday(date.plusDays(i)) ||
				LocalTime.now().getHour() > 18 ||
				(reservationRepository.isPresent(date.plusDays(i)) == 0)) {
			date =_date.plusDays(1);
		}

		while (j < (7 - days)) {

			if (!holidayService.isHoliday(date.plusDays(i))
					&& reservationRepository.isPresent(date.plusDays(i)) == 0) {
				ArrayList<LocalTime> hours = hoursToAddOnInit();
				for (LocalTime lt : hours) {
					ReservationEntity re = new ReservationEntity();
					re.setDate(date.plusDays(i));
					re.setHour(lt);
					re.setUserEntity(null);
					toAddOnInit.add(re);
				}
				j++;
			}
			i++;
		}
		reservationRepository.saveAllAndFlush(toAddOnInit);
		System.out.println("Dates and hours added");
	}

	private ArrayList<LocalTime> hoursToAddOnInit() {
		ArrayList<LocalTime> result = new ArrayList<LocalTime>();
		LocalTime current = LocalTime.of(8, 0);
		LocalTime latest = LocalTime.of(18, 0); // excluded
		while (current.isBefore(latest)) {
			result.add(current);
			current = current.plusMinutes(30);
		}
		return result;
	}
	
	@Scheduled(cron = "58 59 23 * * *")
	void cleanAndAdd() {
		reservationRepository.deleteOldReservations(LocalDate.now());
		System.out.println("Old reservations removed");
		addReservationsOnInit(LocalDate.now());
		System.out.println("New dates available");
	}

	@Scheduled(cron = "58 59 23 28 * *")
	void deleteUsersSoftDeleted() {
		userRepository.deleteAllBySoftDeleted(true);
	}

	@Bean
	@Primary
	public BCryptPasswordEncoder newIstance() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public JavaMailSender getJavaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(587);

		mailSender.setUsername("my.gmail@gmail.com");
		mailSender.setPassword("password");

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.debug", "true");

		return mailSender;
	}

}

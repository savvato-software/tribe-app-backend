package com.savvato.tribeapp.services;

import java.util.*;

import com.savvato.tribeapp.controllers.dto.UserRequest;
import com.savvato.tribeapp.dto.UserDTO;
import com.savvato.tribeapp.entities.User;
import com.savvato.tribeapp.entities.UserRole;
import com.savvato.tribeapp.repositories.UserRepository;
import com.savvato.tribeapp.utils.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepo;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	SMSChallengeCodeService smsccs;

	// TODO: Implement the preferredContactMethod behavior
	public Optional<User> createNewUser(UserRequest request, String preferredContactMethod) {
		if (request.name == null || request.name.length() < 3)
			throw new IllegalArgumentException("name");

		if (!ValidationUtil.isPhoneValid(request.phone))
			throw new IllegalArgumentException("phone");

		if (!ValidationUtil.isEmailValid(request.email))
			throw new IllegalArgumentException("email");

		if (request.password == null || request.password.isEmpty())
			throw new IllegalArgumentException("password");

		Optional<User> opt = this.userRepo.findByNamePhoneOrEmail(request.name, request.phone, request.email);

		if (!opt.isPresent()) {
			User user = new User(request.name, passwordEncoder.encode(request.password), request.phone, request.email);

			Set<UserRole> set = new HashSet<>();
			set.add(UserRole.ROLE_ACCOUNTHOLDER);
			user.setRoles(set);

			User rtn = userRepo.save(user);

			this.sendNewUserSMS();
			this.sendNewUserEmail();

			return Optional.of(rtn);
		} else {
			throw new IllegalArgumentException("This user already exists: " + request.name + " " + request.phone + " " + request.email);
		}
	}

	public Optional<User> update(UserRequest request) {
		if (request.name == null || request.name.length() < 3)
			throw new IllegalArgumentException("name");

		if (!ValidationUtil.isPhoneValid(request.phone))
			throw new IllegalArgumentException("phone");

		if (!ValidationUtil.isEmailValid(request.email))
			throw new IllegalArgumentException("email");

//		if (request.password == null || request.password.isEmpty())
//			throw new IllegalArgumentException("password");

		Optional<User> opt = this.userRepo.findById(request.id);

		if (opt.isPresent()) {
			User user = opt.get();

			user.setName(request.name);
			user.setPhone(request.phone);
			user.setEmail(request.email);

			user.setLastUpdated();

			User rtn = userRepo.save(user);

			return Optional.of(rtn);
		} else {
			return Optional.empty();
		}

	}

	public Optional<User> find(String query) {
		Optional<User> opt = userRepo.findByPhoneOrEmail(query);
		return opt;
	}

	public Optional<User> findById(Long id) {
		Optional<User> opt = userRepo.findById(id);
		return opt;
	}

	private void sendNewUserSMS() {
		// if the preferred contact method is sms, they can respond to the link in this sms.
		// that will confirm their account, set their preference.

		// when they log in next time, we will confirm their attendance by sending an
		// sms.
	}

	private void sendNewUserEmail() {
		// if the preferred contact method is email, they can respond to the link in this email.
		// that will confirm their account, set their preference.

		// when they log in next time, we will confirm their attendance by sending an
		// email.
	}

	public User changePassword(String pw, String phoneNumber, String smsChallengeCode) {
		// This method is for when the user wants to change there password.
		//  It does not require authentication, but the sms challenge code helps to ensure that
		//  at least this request came from the phone associated with the account.

		User rtn = null;

		if (!phoneNumber.startsWith("0"))
			phoneNumber = "1" + phoneNumber;

		if (smsccs.isAValidSMSChallengeCode(phoneNumber, smsChallengeCode)) {
			Optional<List<User>> opt = this.userRepo.findByPhone(phoneNumber);

			if (opt.isPresent()) {
				User user = opt.get().get(0);
				user.setPassword(passwordEncoder.encode(pw));
				this.userRepo.save(user);

				rtn = user;
			}
		}

		return rtn;
	}

	public List<UserDTO> getAllUsers() {
		Iterable<User> users = userRepo.findAll();
		List<UserDTO> rtn = new ArrayList<>();
		for (User user : users){
			UserDTO userDTO = UserDTO.builder()
					.name(user.getName())
					.password(user.getPassword())
					.phone(user.getPhone())
					.email(user.getEmail())
					.enabled(user.getEnabled())
					.created(user.getCreated().toString())
					.lastUpdated(user.getLastUpdated().toString())
					.build();
			rtn.add(userDTO);
		}
		return rtn;
	}

}

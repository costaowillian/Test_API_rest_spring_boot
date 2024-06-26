package br.com.willian.services;

import br.com.willian.model.User;
import br.com.willian.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class UserServices implements UserDetailsService {

	private Logger logger = Logger.getLogger(UserServices.class.getName());
	
	@Autowired
	private UserRepository repository;

	public UserServices(UserRepository repository) {
		this.repository = repository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("Finding one user by name...");
		User user = repository.findByUserName(username);
		if(user != null) {
			return  user;
		} else {
			throw new UsernameNotFoundException("Username " + username + " not found!");
		}
	}
}

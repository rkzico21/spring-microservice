package authserver.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import authserver.entities.Permission;
import authserver.entities.User;
import authserver.repositories.RoleRepository;
import authserver.repositories.UserRepository;

public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository repository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public User addUser(User entity) {
		String encryptedPassword = passwordEncoder.encode(entity.getPassword());
		User user = new User(entity.getId(), entity.getName(), entity.getFullName(), entity.getEmail(), encryptedPassword);
		user = repository.save(user);
		Permission role = roleRepository.findRoleByName("user");
		role.getUsers().add(user);
		return user;
	}

	@Override
	public User findUserByName(String name) {
		return repository.findUserByName(name);
	}
}
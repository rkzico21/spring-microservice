package userservice.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import userservice.entities.User;
import userservice.repositories.UserRepository;


@Service("userService")
public class UserServiceImpl implements UserService{
 
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
    private UserRepository repository;

	@Override
	public Iterable<User> findAll() {
		return repository.findAll();
	}

	@Override
	public User add(User entity) {
		String encryptedPassword = passwordEncoder.encode(entity.getPassword());
		User user = new User(entity.getId(), entity.getName(), entity.getFullName(), entity.getEmail(), encryptedPassword);
	    return repository.save(user);
	}

	@Override
	public User findOne(Long id) {
		return repository.findOne(id);
	}

}

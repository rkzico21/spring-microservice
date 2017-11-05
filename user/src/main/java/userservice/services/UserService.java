package userservice.services;

import userservice.entities.User;

public interface UserService{

	Iterable<User> findAll();
	
	User add(User user);

	User findOne(Long id);
}

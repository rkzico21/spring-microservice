package userservice.services;

import userservice.dtos.User;

public interface UserService{

	Iterable<User> findAll();
	
	User add(User user);

	User findOne(Long id);
	
	User findByName(String name);
	
	Iterable<User> search(String query);
}

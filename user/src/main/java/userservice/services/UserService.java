package userservice.services;

import userservice.dtos.User;

public interface UserService{

	Iterable<User> findAll();
	
	User add(User user);

	User findOne(Long id);
	
	Iterable<User> search(String query);
}

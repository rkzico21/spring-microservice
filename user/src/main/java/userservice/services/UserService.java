package userservice.services;

import userservice.dtos.SearchResult;
import userservice.dtos.User;
import userservice.dtos.UserSearchQuery;

public interface UserService{

	Iterable<User> findAll();
	
	User add(User user);

	User findOne(Long id);
	
	User findByName(String name);
	
	Iterable<User> search(String query); 
	
	SearchResult<User> search(UserSearchQuery query); 
}

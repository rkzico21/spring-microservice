package authserver.Service;

import authserver.entities.User;

public interface UserService{

	User addUser(User user);

	User findUserByName(String name);

}

package userservice.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import userservice.entities.User;

@Repository
public interface UserRepository  extends CrudRepository<User, Long>   {
	User getUserByEmail(String email);
}

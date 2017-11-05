package authserver.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import authserver.entities.User;

@Repository
public interface UserRepository  extends CrudRepository<User, Long>   {
	User findUserByName(String name);
}

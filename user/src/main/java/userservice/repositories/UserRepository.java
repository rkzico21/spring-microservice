package userservice.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import userservice.entities.UserEntity;

@Repository
public interface UserRepository  extends CrudRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity>   {
	UserEntity getUserByEmail(String email);
	UserEntity findUserByName(String name);
	UserEntity findUserByFullName(String fullName);
}

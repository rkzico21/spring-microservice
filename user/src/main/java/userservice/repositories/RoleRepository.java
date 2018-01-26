package userservice.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import userservice.entities.RoleEntity;

@Repository
public interface RoleRepository  extends CrudRepository<RoleEntity, Long>   {
	RoleEntity findRoleByName(String name);
}

package userservice.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import userservice.entities.RoleEntity;

@RepositoryRestResource(exported=false)
@Repository
public interface RoleRepository  extends CrudRepository<RoleEntity, Long>   {
	RoleEntity findRoleByName(String name);
}

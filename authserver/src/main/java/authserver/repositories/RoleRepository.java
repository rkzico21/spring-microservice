package authserver.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import authserver.entities.Permission;

@Repository
public interface RoleRepository  extends CrudRepository<Permission, Long>   {
	Permission findRoleByName(String name);
}

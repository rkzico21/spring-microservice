package todolistservice.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import todolistservice.entities.TodoListItem;

@RepositoryRestResource(exported=false)
@Repository
public interface TodoListRepository  extends CrudRepository<TodoListItem, Long>   {
	Iterable<TodoListItem> findByUserId(@Param("userid") Long userid);
}

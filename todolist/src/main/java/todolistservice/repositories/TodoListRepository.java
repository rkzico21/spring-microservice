package todolistservice.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import todolistservice.entities.TodoListItem;

@Repository
public interface TodoListRepository  extends CrudRepository<TodoListItem, Long>   {
	Iterable<TodoListItem> findByUserId(@Param("userid") Long userid);
}

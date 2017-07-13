package todolistservice.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import todolistservice.entities.TodoListItem;

@Repository
public interface TodoListItemRepository  extends CrudRepository<TodoListItem, Long>   {
	Iterable<TodoListItem> findByTodolistId(@Param("todolistid") Long todolistid);
}

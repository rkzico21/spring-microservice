package todolistservice.repositories;


import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import todolistservice.entities.TodoList;

@Repository
public interface TodoListRepository  extends CrudRepository<TodoList, Long>   {
 
	Iterable<TodoList> findByUserId(@Param("userid") Long userid);
}

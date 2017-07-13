package todolistservice.repositories;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;


import todolistservice.entities.TodoList;

@Service("todolistService")
@Repository
public class TodolistServiceImpl implements TodolistService{
 
	@Autowired
    private TodoListRepository repository;
   
	@Override
    @Cacheable(cacheNames = "usertodolistcache", key="#userId")
    public Iterable<TodoList> findByUserId(Long userId) {
    	 Iterable<TodoList> todolists = repository.findByUserId(userId);
    	 
        return todolists;
    }

	@Override
	@CacheEvict(cacheNames = "usertodolistcache", key="#entity.UserId")
	public TodoList add(TodoList entity) {
		TodoList todoList = repository.save(entity);
		return todoList;
	}

	@Override
	@Cacheable(cacheNames = "usertodolistcache", key="T(todolistservice.entities.TodoList).hash(#id.toString())") //TODO: check if it is possible to use same cache
    public TodoList findOne(Long id) {
		return repository.findOne(id);
	}

	@Override
	@CacheEvict(cacheNames = "usertodolistcache", allEntries=true)
	public void delete(Long id) {

		repository.delete(id);
		
	}

}

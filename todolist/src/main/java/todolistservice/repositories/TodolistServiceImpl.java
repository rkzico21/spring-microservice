package todolistservice.repositories;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;


import todolistservice.entities.TodoListItem;

@Service
@Repository
public class TodolistServiceImpl implements TodolistService{
 
	@Autowired
    private TodoListRepository repository;
   
	@Override
    public Iterable<TodoListItem> findByUserId(Long userId) {
    	 return repository.findByUserId(userId);
    }

	@Override
	public TodoListItem add(TodoListItem entity) {
		return repository.save(entity);
	}

	@Override
	public void delete(Long id) {
		repository.delete(id);
	}

	@Override
	public TodoListItem findOne(Long id) {
		return repository.findOne(id);
	}

}

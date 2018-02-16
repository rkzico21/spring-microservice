package todolistservice.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import todolistservice.dtos.PageResult;
import todolistservice.entities.TodoListItem;

@Service
@Repository
public class TodolistServiceImpl implements TodolistService{
 
	@Autowired
    private TodoListRepository repository;
   
	@Override
    public PageResult<TodoListItem> findByUserId(Long userId, int pageNumber, int size) {
    	Page<TodoListItem> page = repository.findByUserId(userId, createPageRequest(pageNumber, size));
		
		PageResult<TodoListItem> result = new PageResult<TodoListItem>();
		result.setPage(page.getNumber());
		result.setTotalResults(page.getTotalElements());
		result.setTotalPages(page.getTotalPages());
		result.setSize(page.getSize());
		result.setResults(page.getContent());
	    
		return result;
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
	
	
	private Pageable createPageRequest(int page, int size) {
	    return new PageRequest(page, size);
	}
	
	
}

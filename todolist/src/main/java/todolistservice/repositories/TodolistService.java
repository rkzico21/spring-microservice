package todolistservice.repositories;


import todolistservice.dtos.PageResult;
import todolistservice.entities.TodoListItem;

public interface TodolistService{

	PageResult<TodoListItem> findByUserId(Long userId, int page, int size);
	
	TodoListItem add(TodoListItem item);

	TodoListItem findOne(Long id);
	
	void delete(Long id);
}

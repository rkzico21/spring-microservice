package todolistservice.repositories;


import todolistservice.entities.TodoListItem;

public interface TodolistService{

	Iterable<TodoListItem> findByUserId(Long userId);
	
	TodoListItem add(TodoListItem item);

	TodoListItem findOne(Long id);
	
	void delete(Long id);
}

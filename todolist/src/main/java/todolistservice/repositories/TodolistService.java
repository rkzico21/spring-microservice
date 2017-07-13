package todolistservice.repositories;



import todolistservice.entities.TodoList;

public interface TodolistService{

	Iterable<TodoList> findByUserId(Long userId);
	
	TodoList add(TodoList todolist);

	TodoList findOne(Long id);

	void delete(Long id);
}

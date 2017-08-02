package todolistservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

 public class TodolistNotFoundException extends ResourceNotFoundException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2323232;
     // ...
	
	public TodolistNotFoundException(Long id){
		super("No Todolist found with id "+id);
	}
 }
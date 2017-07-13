package todolistservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="User does not exist")  // 404
 public class TodolistNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2323232;
     // ...
	
	public TodolistNotFoundException(Long id){
		super("TodolistNotFoundException with id="+id);
	}
 }
package todolistservice.exceptions;

public class TodolistItemNotFoundException extends ResourceNotFoundException {

	private static final long serialVersionUID = -232323223;
	
	public TodolistItemNotFoundException(Long id){
		super("No Item found with id "+id);
	}
 }
package userservice.exceptions;


public class UserExistsException extends RuntimeException {

	public UserExistsException(String name){
		super(String.format("User with name:%s already exists", name));
	}
 }
package userservice.exceptions;


public class UserNotFoundException extends RuntimeException {

	public UserNotFoundException(String key, String value){
		super(String.format("No user found with %s:%s ",key, value));
	}
 }
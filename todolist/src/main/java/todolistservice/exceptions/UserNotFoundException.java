package todolistservice.exceptions;



public class UserNotFoundException extends ResourceNotFoundException {

	private static final long serialVersionUID = 23231232;
	
	public UserNotFoundException(Long id){
		super("UserNotFoundException n with id="+id);
	}
 }
package meetingservice.exceptions;


 public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -2323232;
	
	public ResourceNotFoundException(String message){
		super(message);
	}
 }
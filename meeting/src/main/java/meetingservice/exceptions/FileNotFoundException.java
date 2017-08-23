package meetingservice.exceptions;


public class FileNotFoundException extends ResourceNotFoundException {

	public FileNotFoundException(Long id){
		super("No file found with id "+id);
	}
 }
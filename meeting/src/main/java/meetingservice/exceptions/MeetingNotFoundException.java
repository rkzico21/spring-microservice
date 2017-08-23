package meetingservice.exceptions;


public class MeetingNotFoundException extends ResourceNotFoundException {

	public MeetingNotFoundException(Long id){
		super("No meeting found with id "+id);
	}
 }
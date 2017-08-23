package meetingservice.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import meetingservice.entities.Meeting;
import meetingservice.repositories.MeetingRepository;


@Service("meetingService")
@Repository
public class MeetingServiceImpl implements MeetingService{
 
	@Autowired
    private MeetingRepository repository;

	@Override
	public Iterable<Meeting> getMeetings() {
		 Iterable<Meeting> meetings = repository.findAll();
    	 return meetings;
	}

	@Override
	public Meeting add(Meeting entity) {
		Meeting meeting = repository.save(entity);
		
		
		return meeting;
	}

	@Override
	public Meeting findOne(Long id) {
		return repository.findOne(id);
	}
	
	public void delete(Long id) {
		repository.delete(id);
	}

	@Override
	public Meeting updateMeeting(Meeting currentMeeting, Meeting newMeeting) {
		currentMeeting.setSubject(newMeeting.getSubject());
		currentMeeting.setLocation(newMeeting.getLocation());
		return repository.save(currentMeeting);
	}

}

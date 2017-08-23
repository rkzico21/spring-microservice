package meetingservice.services;

import meetingservice.entities.Meeting;

public interface MeetingService{

	Iterable<Meeting> getMeetings();
	
	Meeting add(Meeting meeting);

	Meeting findOne(Long id);

	void delete(Long id);

	Meeting updateMeeting(Meeting currentMeeting, Meeting newMeeting);
}

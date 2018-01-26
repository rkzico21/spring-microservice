package meetingservice.services;

import java.util.List;

import meetingservice.entities.Meeting;
import meetingservice.entities.Participant;

public interface MeetingService{

	Iterable<Meeting> getMeetings();
	
	Meeting add(Meeting meeting);

	Meeting findOne(Long id);

	void delete(Long id);

	Meeting updateMeeting(Meeting currentMeeting, Meeting newMeeting);

	Iterable<Participant> updateParticipants(long meetingId, List<Participant> participants);

	Iterable<Participant> findParticipants(long meetingId);

}

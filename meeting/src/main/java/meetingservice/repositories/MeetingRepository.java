package meetingservice.repositories;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import meetingservice.entities.Meeting;




@Repository
public interface MeetingRepository  extends CrudRepository<Meeting, Long>   {
 
}

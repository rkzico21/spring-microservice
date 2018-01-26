package meetingservice.repositories;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import meetingservice.entities.Participant;




@Repository
public interface ParticipantRepository  extends CrudRepository<Participant, Long>   {
 
}

package meetingservice.repositories;


import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import meetingservice.entities.Meeting;


@RepositoryRestResource(exported=false)
@Repository
public interface MeetingRepository  extends CrudRepository<Meeting, Long>   {
 
}

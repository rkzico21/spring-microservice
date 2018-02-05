package meetingservice.repositories;


import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import meetingservice.entities.File;;

@RepositoryRestResource(exported=false)
@Repository
public interface FileRepository  extends CrudRepository<File, Long>   {
 
	Iterable<File> findByMeetingId(@Param("meeting_id") Long meetingId);
}

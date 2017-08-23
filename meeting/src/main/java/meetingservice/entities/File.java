package meetingservice.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "file")
public class File{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
	
	private String name;
	
	@JsonIgnore
	private String path;

	private String contentType;

	private Long meetingId;

	File() {}

    public File(Long id, String name, String path, String contentType,  Long meetingId) {
        this(name, path, contentType, meetingId);
    	this.id = id;
    }

    
    public File(String name, String path, String contentType, Long meetingId) {
    	this.name = name;
    	this.path = path;
    	this.contentType = contentType;
    	this.meetingId = meetingId;
	}

	public Long getId() {
        return id;
    }

    public String getName() {
    	return this.name;
    }
    
    public String getPath() {
    	return this.path;
    }
    
    public String getContentType() {
    	return this.contentType;
    }
 
    public Long getMeetingId() {
    	return this.meetingId;
    }

    @Override
    public String toString() {
        return String.format("Id:%d Name:%s Path:%s", this.id, this.name, this.path);
    }
}

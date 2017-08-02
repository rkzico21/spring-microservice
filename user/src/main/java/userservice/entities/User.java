package userservice.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "\"user\"")
public class User{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long Id;
	
	@NotEmpty(message = "name is required field")
	private String name;
	
	@NotEmpty(message = "email is required field.")
	private String email;

    User() {}

    public User(Long id ,String name, String email) {
        this.Id = id;
    	this.name = name;
    	this.email = email;
    }

    
    public Long getId() {
        return Id;
    }

    public String getName() {
        return name;
    }
    
    public String getEmail() {
        return email;
    }
    
    @Override
    public String toString() {
        return String.format("Id:%d , Name:%s, Email:%s", this.Id, this.name, this.email);
    }
}



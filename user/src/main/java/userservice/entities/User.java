package userservice.entities;

import javax.persistence.Column;
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
	
	
	@NotEmpty(message = "fullName is required field.")
	@Column(name = "fullname")
	private String fullName;

    User() {}

    public User(Long id, String name, String fullName, String email) {
        this.Id = id;
    	this.name = name;
    	this.fullName = fullName;
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
    
    public String getFullName() {
        return fullName;
    }
    
    @Override
    public String toString() {
        return String.format("Id:%d , Name:%s, Full Name:%s, Email:%s", this.Id, this.name, this.fullName, this.email);
    }
}



package userservice.dtos;

import org.hibernate.validator.constraints.NotEmpty;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;

@Data
public class User {

	private Long  Id;
	
	@NotEmpty(message = "name is required field")
	private String name;
	
	@NotEmpty(message = "email is required field.")
	private String email;
	
	
	@NotEmpty(message = "fullName is required field.")
	private String fullName;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	@NotEmpty(message = "password is required field.")
	private String password;
	
	private String department;
	
	private String designation;
	
	private Address address;
	
	private Role roles; 
	
	User() {}

    
    public User(Long id, String name, String fullName, String email, String password) {
    	this.Id = id;
    	this.name = name;
    	this.fullName = fullName;
    	this.email = email;
    	this.password = password;
    }

    @Override
    public String toString() {
        return String.format("Id:%d , Name:%s, Full Name:%s, Email:%s, Department:%s, Designation:%s", this.Id, this.name, this.fullName, this.email, this.department, this.designation);
    }
}



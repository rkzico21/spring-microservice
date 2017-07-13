package userservice.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "\"user\"")
public class User{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long Id;
	
	@NotNull
	private String name;
	
	@NotNull
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
}



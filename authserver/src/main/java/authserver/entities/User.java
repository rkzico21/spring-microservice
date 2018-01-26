package authserver.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Immutable;
import org.hibernate.validator.constraints.NotEmpty;


@Entity
@Table(name = "\"user\"")
@Immutable
public class User{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long Id;
	
	@NotEmpty(message = "name is required field")
	@Column(unique=true)
	private String name;
	
	@NotEmpty(message = "email is required field.")
	private String email;
	
	
	@NotEmpty(message = "fullName is required field.")
	@Column(name = "fullname")
	private String fullName;
	
	@NotEmpty(message = "password is required field.")
	private String password;
	
	@ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_role", 
        joinColumns = { @JoinColumn(name = "user_id") }, 
        inverseJoinColumns = { @JoinColumn(name = "role_id") }
    )
	private Set<Permission> roles = new HashSet<Permission>(0);

    User() {}

    public User(Long id, String name, String fullName, String email, String password) {
        this.Id = id;
    	this.name = name;
    	this.fullName = fullName;
    	this.email = email;
    	this.password = password;
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
    
    public String getPassword() {
    	return this.password;
    }
    
    public Set<Permission> getRoles() {
    	return this.roles;
    }
    
    @Override
    public String toString() {
        return String.format("Id:%d , Name:%s, Full Name:%s, Email:%s", this.Id, this.name, this.fullName, this.email);
    }
}



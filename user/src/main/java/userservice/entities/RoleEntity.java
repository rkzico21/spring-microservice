package userservice.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Immutable;
import org.hibernate.validator.constraints.NotEmpty;


@Entity
@Table(name = "\"role\"")
@Immutable
public class RoleEntity{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long Id;
	
	@NotEmpty(message = "name is required field")
	private String name;
	
	@ManyToMany(fetch = FetchType.EAGER, mappedBy = "roles")
	private Set<UserEntity> users = new HashSet<UserEntity>(0);

    RoleEntity() {}

    public RoleEntity(Long id, String name) {
        this.Id = id;
    	this.name = name;    
    }
    
    public RoleEntity(Long id, String name, Set<UserEntity> users) {
        this(id, name);    
    	this.users = users;
    }

    
    public Long getId() {
        return Id;
    }

    public String getName() {
        return name;
    }
    
    public Set<UserEntity> getUsers() {
    	return this.users;
    }
    
        
    @Override
    public String toString() {
        return String.format("Id:%d , Name:%s", this.Id, this.name);
    }
}



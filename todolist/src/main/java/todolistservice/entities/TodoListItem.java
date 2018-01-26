package todolistservice.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "todolist")
public class TodoListItem{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long Id;
	
	@Min(value=1, message="user id is missing")
	@Column(name = "userid")
	private Long userId;
	
	@NotEmpty(message="title is missing.")
	private String title;

	TodoListItem() {}

    public TodoListItem(Long id, String title, Long userId) {
        this.Id = id;
    	this.title = title;
    	this.userId = userId;
    	
    }

    
    public Long getId() {
        return this.Id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    @Override
    public String toString() {
        return String.format("Id:%d Title:%s userId:%d", this.Id, this.title, this.userId);
    }
}

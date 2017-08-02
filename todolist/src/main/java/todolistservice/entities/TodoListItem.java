package todolistservice.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "todolistitem")
public class TodoListItem{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
	
	@NotEmpty(message="todolistid is missing.")
	@Column(name = "todolistid")
	private Long todolistId;
	
	@NotEmpty(message="title is missing.")
	private String title;

	TodoListItem() {}

    public TodoListItem(Long id, Long todolistid, String title) {
        this.id = id;
    	this.todolistId = todolistid;
    	this.title = title;
    }

    
    public Long getId() {
        return id;
    }

    public Long getTodolistId() {
        return todolistId;
    }
    
    public String getTitle() {
    	return this.title;
    }
    
    @Override
    public String toString() {
        return String.format("Id:%d Title:%s TodolistId:%d", this.id, this.title, this.todolistId);
    }
}

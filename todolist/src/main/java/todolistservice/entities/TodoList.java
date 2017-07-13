package todolistservice.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "todolist")
public class TodoList implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	
	@NotNull
	private String title;
	
	@NotNull
	@Column(name = "userid")
	private Long userId;
	
	TodoList() {}

    public TodoList(Long id, String title, Long userId) {
        this.id = id;
    	this.title = title;
    	this.userId = userId;
    	
    }

    
    public Long getId() {
        return id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public Long getUserId() {
        return userId;
    }
 }



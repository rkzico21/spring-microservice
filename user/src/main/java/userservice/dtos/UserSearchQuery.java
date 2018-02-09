package userservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
public class UserSearchQuery {

	private final String fullName;
	
	private final String department;
	
	private final String designation;
	
	private final int page;
	 
	private final int size;
	
	
	
	
	
    @Override
    public String toString() {
        return String.format("Name:%s, Department:%s, Designation:%s",  this.fullName, this.department, this.designation);
    }
}



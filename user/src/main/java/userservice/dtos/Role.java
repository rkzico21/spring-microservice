package userservice.dtos;


import lombok.Data;

@Data
public class Role{

    private Long Id;
	
	private String name;
	
	@Override
    public String toString() {
        return String.format("Id:%d , Name:%s", this.Id, this.name);
    }
}



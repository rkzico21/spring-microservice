package gateway.domain;


public class User{

	private String name;
	
	private String email;
	
	private String fullName;
	
	private String password;

	User() {}

    public User(String name, String fullName, String email, String password) {
    	this.name = name;
    	this.fullName = fullName;
    	this.email = email;
    	this.password = password;
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
}



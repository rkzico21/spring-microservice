package userservice.Specifications;

public class SearchCriteria {
    private String key;
    private String operation;
    private Object value;
    
    
    public SearchCriteria(String key, String operation, Object value) {
    	this.key = key;
    	this.operation = operation;
    	this.value = value;
    }
    
    public String getKey() {
    	return this.key;
    }
    
    public String getOperation() {
    	return this.operation;
    }
    
    public Object getValue() {
    	return this.value;
    }
}
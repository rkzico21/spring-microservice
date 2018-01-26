package userservice.dtos;

import lombok.Data;

@Data
public class Address{

	private String presentAddress;
	private String permanantAddress;

    Address() {}

    public Address(String permanantAddress, String presentAddress) {
    	this.permanantAddress = permanantAddress;
    	this.presentAddress = presentAddress;
    }

    @Override
    public String toString() {
        return String.format("Permanant address:%s, Present address%s", this.permanantAddress, this.presentAddress);
    }
}



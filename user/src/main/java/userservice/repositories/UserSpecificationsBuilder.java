package userservice.repositories;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import userservice.entities.UserEntity;

import java.util.ArrayList;

public class UserSpecificationsBuilder {
     
    private final List<SearchCriteria> params;
 
    public UserSpecificationsBuilder() {
        params = new ArrayList<SearchCriteria>();
    }
 
    public UserSpecificationsBuilder with(String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }
 
    public Specification<UserEntity> build() {
        if (params.size() == 0) {
            return null;
        }
 
        List<Specification<UserEntity>> specs = new ArrayList<Specification<UserEntity>>();
        for (SearchCriteria param : params) {
            specs.add(new UserSpecification(param));
        }
 
        Specification<UserEntity> result = specs.get(0);
        for (int i = 1; i < specs.size(); i++) {
            result = Specifications.where(result).and(specs.get(i));
        }
        
        return result;
    }
}
package userservice.services;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import userservice.entities.RoleEntity;
import userservice.entities.UserEntity;
import userservice.exceptions.UserExistsException;
import userservice.dtos.User;
import userservice.repositories.RoleRepository;
import userservice.repositories.UserRepository;
import userservice.repositories.UserSpecificationsBuilder;


@Service("userService")
public class UserServiceImpl implements UserService{
 
	@Autowired
	private ModelMapper modelMapper;
	
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
    private UserRepository repository;
	
	@Autowired
    private RoleRepository roleRepository;

	@Override
	public Iterable<User> findAll() {
		Iterable<UserEntity> usersEntities = repository.findAll();
	    List<User> users = new ArrayList<User>();
		usersEntities.forEach(u->users.add(modelMapper.map(u, User.class)));
		return users;
	}

	@Override
	public User add(User user) {
		
		UserEntity currentUser = repository.findUserByName(user.getName());
		if(currentUser != null) {
			throw new UserExistsException(user.getName());
		}
		
		String encryptedPassword = passwordEncoder.encode(user.getPassword());
		UserEntity userEntity = modelMapper.map(user, UserEntity.class);
		userEntity.setPassword(encryptedPassword);
	    
		RoleEntity role = roleRepository.findRoleByName("user_update");
	    userEntity.getRoles().add(role);
		UserEntity entity = repository.save(userEntity);
		
		return modelMapper.map(entity, User.class);
	}

	@Override
	public User findOne(Long id) {
		UserEntity entity = repository.findOne(id);
		return modelMapper.map(entity, User.class);
	}
	
	@Override
	public User findByName(String name) {
		UserEntity entity = repository.findUserByName(name);
		return modelMapper.map(entity, User.class);
	}

	@Override
	public Iterable<User> search(String query) {
		UserSpecificationsBuilder builder = new UserSpecificationsBuilder();
        Pattern pattern = Pattern.compile("(\\w+?)(=)(\\w+?),");
        Matcher matcher = pattern.matcher(query + "&");
        while (matcher.find()) {
            builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
        }
        
        Specification<UserEntity> spec = builder.build();
         
        List<User> users = new ArrayList<User>();
        repository.findAll(spec).forEach(u->users.add(modelMapper.map(u, User.class)));
        
        return users;
	}
}

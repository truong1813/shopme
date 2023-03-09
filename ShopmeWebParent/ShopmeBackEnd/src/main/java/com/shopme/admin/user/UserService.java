package com.shopme.admin.user;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Service
@Transactional
public class UserService {
	public static final Integer USERS_PER_PAGE =5;
	@Autowired private UserRepository userRepo;
	@Autowired private PasswordEncoder passwordEncoder;
	@Autowired private RoleRepository roleRepo;
	
	
	public  User getByEmail(String email) {
		return userRepo.findByEmail(email);
	}
	public List<Role> listRoles(){
		return (List<Role>) roleRepo.findAll();
	}
	
	public List<User> listAll(){
		return (List<User>) userRepo.findAll(Sort.by("firstName").ascending());
	}
	public void listByPage(Integer pageNum,PagingAndSortingHelper helper){
		helper.listEntities(pageNum, USERS_PER_PAGE, userRepo);
	}
	public User save(User user) {
		boolean isUpdatingUser = (user.getId() !=null);
		if(isUpdatingUser) {
			User userExitings = userRepo.findById(user.getId()).get();
			if(user.getPassword().isEmpty()) {
				user.setPassword(userExitings.getPassword());
			}else {
				encodePassword(user);
			}
		}else {
			encodePassword(user);
		}
		return userRepo.save(user);
	}
	
	private void encodePassword(User user) {
		String password = passwordEncoder.encode(user.getPassword());
		user.setPassword(password);
	}
	public boolean checkEmailUnique(Integer id,String email) {
		User userByEmail = userRepo.findByEmail(email);
		if (userByEmail==null)return true;
		boolean isCreateNew = (id==null);
		if(isCreateNew) {
			if(userByEmail!=null) {
				return false;
				}
		}else {
			if(userByEmail.getId()!=id) return false;
			}
	
		return true;
	}
	public void updateEnabledStatus(Integer id,boolean enabled) {
		userRepo.updateEnabledStatus(id, enabled);
	}
		
	public User get(Integer id) throws UserNotFoundException  {
		try {
			return userRepo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new UserNotFoundException("Could not find user with Id " +id);
		}
		
	}
	
	public void deleteUser(Integer id) throws UserNotFoundException {
		Long count = userRepo.countById(id);
		if(count==null ||count==0) {
			throw new UserNotFoundException("Could not find user with Id " +id);
		}
		userRepo.deleteById(id);
	}
	
	public User userUpdateDetails(User userInForm) {
		User userDB = userRepo.findById(userInForm.getId()).get();
		if(!userInForm.getPassword().isEmpty()) {
			userDB.setPassword(userInForm.getPassword());
			encodePassword(userDB);
		}
		if(userInForm.getPhoto() != null) {
			userDB.setPhoto(userInForm.getPhoto());
		}
		userDB.setFirstName(userInForm.getFirstName());
		userDB.setLastName(userInForm.getLastName());
		
		return userRepo.save(userDB);
	}

}

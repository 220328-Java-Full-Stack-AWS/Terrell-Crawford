package com.revature.services;

import java.util.Optional;

import com.revature.models.User;
import com.revature.repositories.UserDAO;

/**
 * The UserService should handle the processing and retrieval of Users for the ERS application.
 *
 * {@code getByUsername} is the only method required;
 * however, additional methods can be added.
 *
 * Examples:
 * <ul>
 *     <li>Create User</li>
 *     <li>Update User Information</li>
 *     <li>Get Users by ID</li>
 *     <li>Get Users by Email</li>
 *     <li>Get All Users</li>
 * </ul>
 */
public class UserService {
	UserDAO uDAO = new UserDAO();
	/**
	 *     Should retrieve a User with the corresponding username or an empty optional if there is no match.
     */
	public Optional<User> getByUsername(String username) {
		 Optional<User> temp = Optional.empty();
		 temp = uDAO.getByUsername(username);
		 if(temp.equals(Optional.empty())){
			 return Optional.empty();
		}
		return temp;
	}


	public Optional<User> getByUserID(int Id) {
		Optional<User> temp = Optional.empty();
		temp = uDAO.getByUserID(Id);
		if(temp.equals(Optional.empty())){
			return Optional.empty();
		}
		return temp;
	}

}

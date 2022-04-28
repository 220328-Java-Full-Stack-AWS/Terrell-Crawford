package com.revature.services;

import com.revature.exceptions.*;
import com.revature.models.User;
import com.revature.repositories.UserDAO;

import java.util.Optional;

/**
 * The AuthService should handle login and registration for the ERS application.
 *
 * {@code login} and {@code register} are the minimum methods required; however, additional methods can be added.
 *
 * Examples:
 * <ul>
 *     <li>Retrieve Currently Logged-in User</li>
 *     <li>Change Password</li>
 *     <li>Logout</li>
 * </ul>
 */
public class AuthService {
    UserService userService= new UserService();

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * <ul>
     *     <li>Needs to check for existing users with username/email provided.</li>
     *     <li>Must throw exception if user does not exist.</li>
     *     <li>Must compare password provided and stored password for that user.</li>
     *     <li>Should throw exception if the passwords do not match.</li>
     *     <li>Must return user object if the user logs in successfully.</li>
     * </ul>
     */
    public User login(String username, String password){
        //checks for users with the same username as given in parameters then stores any found into temp user object
        Optional<User> tempOp =userService.getByUsername(username);
        User temp = tempOp.get();
        System.out.println(temp.getUsername());
        //checks if result of username check returned anything and throws exception if it didn't
        if(temp.getUsername()==null){
           throw new NoSuchUserException("User doesn't exist");
        //If username was found checks if password given matches password associated with username. Throws error if they don't match
        }else if(!temp.getPassword().equals(password)){
            throw new NoSuchPassword("Incorrect password");
        //If username was found and password matches, then return the temp user
        }else{
            return temp;
        }

    }

    /**
     * <ul>
     *     <li>Should ensure that the username/email provided is unique.</li>
     *     <li>Must throw exception if the username/email is not unique.</li>
     *     <li>Should persist the user object upon successful registration.</li>
     *     <li>Must throw exception if registration is unsuccessful.</li>
     *     <li>Must return user object if the user registers successfully.</li>
     *     <li>Must throw exception if provided user has a non-zero ID</li>
     * </ul>
     *
     * Note: userToBeRegistered will have an id=0, additional fields may be null.
     * After registration, the id will be a positive integer.
     */
    public User register(User userToBeRegistered) {
        String userName = userToBeRegistered.getUsername();
        int userID = userToBeRegistered.getId();
        User temp = new User();
        //stores user from getByUsername output into temporary user
        if(userService.getByUsername(userName).isPresent()) {
            temp = userService.getByUsername(userName).get();
        }
         //Checks if the temp user is the same as user passed into method and throws exception if it is
        if(temp.getUsername()!=null){
            if(temp.getUsername().equals(userName)) {
            throw new UsernameNotUniqueException("Username already exists");
            }
        //If new user was passed in, checks to see if their user ID is non-zero and throws exception if it is
        }else if(userID!=0){
            throw new NewUserHasNonZeroIdException();
        //If new user with ID = 0, then register the user and return that user
        }else{
            temp=userService.create(userToBeRegistered);
            return temp;
        }
        return temp;
    }


    /**
     * This is an example method signature for retrieving the currently logged-in user.
     * It leverages the Optional type which is a useful interface to handle the
     * possibility of a user being unavailable.
     */
    public Optional<User> exampleRetrieveCurrentUser() {
        return Optional.empty();
    }
}

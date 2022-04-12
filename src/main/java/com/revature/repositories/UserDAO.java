package com.revature.repositories;

import com.revature.exceptions.RegistrationUnsuccessfulException;
import com.revature.exceptions.UsernameNotUniqueException;
import com.revature.models.User;
import com.revature.services.AuthService;
import com.revature.util.ConnectionFactory;
import java.sql.*;
import java.util.Optional;
import java.util.Random;
import java.util.Random.*;

public class UserDAO {
    ConnectionFactory con = ConnectionFactory.getInstance();
    Optional<User> resultOp = Optional.empty();


    /**
     * Should retrieve a User from the DB with the corresponding username or an empty optional if there is no match.
     */
    public Optional<User> getByUsername(String username) {
        //Stores SQL QUERY
        try {
            String query ="SELECT * FROM ers_users WHERE ers_username=?";
            User tempUser = new User();
            //creates an SQL statement and tries to execute it
            PreparedStatement st = con.getConnection().prepareStatement(query);
            st.setString(1,username);
            //stores result into a ResultSet
            ResultSet rs = st.executeQuery();
            System.out.println(rs.getInt(1));
            //Store retrieved values into user object
            tempUser.setId(rs.getInt("ers_users_id"));
            tempUser.setUsername(username);
            tempUser.setPassword(rs.getString("ers_password"));
            tempUser.setFirstName(rs.getString("user_first_name"));
            tempUser.setLastName(rs.getString("user_last_name"));
            tempUser.setEmail(rs.getString("user_email"));
            tempUser.setRoleID(rs.getInt("user_role_id"));
            System.out.println(tempUser.getId());
            System.out.println(tempUser.getUsername());
            System.out.println(tempUser.getPassword());
            //Store user object into Optional
            resultOp=Optional.of(tempUser);

        } catch (SQLException e) {
            System.out.println("Couldn't find user '\n' " + e.getMessage()+" "+e.getErrorCode());
            //e.printStackTrace();
           // return Optional.empty();
        }
        return resultOp;
    }
           //Get User out of DB

           //if user doesn't exist return Optional.empty if it does exist, return Optional.of(user)
           //Optional.get to get object out of the Optional

    /**
     * <ul>
     *     <li>Should Insert a new User record into the DB with the provided information.</li>
     *     <li>Should throw an exception if the creation is unsuccessful.</li>
     *     <li>Should return a User object with an updated ID.</li>
     * </ul>
     */
    public User create(User userToBeRegistered) {
        try {
            //Insert user into DB
            String query ="INSERT INTO ers_users(ers_users_id, ers_username, ers_password, user_first_name, user_last_name, user_email, user_role_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement st = con.getConnection().prepareStatement(query);
            st.setInt(1, userToBeRegistered.getId());
            st.setString(2, userToBeRegistered.getUsername());
            st.setString(3, userToBeRegistered.getPassword());
            st.setString(4, userToBeRegistered.getFirstName());
            st.setString(5, userToBeRegistered.getLastName());
            st.setString(6, userToBeRegistered.getEmail());
            st.setInt(7,userToBeRegistered.getRoleID());
            ResultSet rs = st.executeQuery();
        }catch (SQLException e) {
            System.out.println("Couldn't add user ");
            throw new RegistrationUnsuccessfulException();
        }
        userToBeRegistered.setId(1);
        //userToBeRegistered.setId(ran.nextInt(1000000000));
        return userToBeRegistered;
    }
}

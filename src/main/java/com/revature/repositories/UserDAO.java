package com.revature.repositories;

import com.revature.exceptions.RegistrationUnsuccessfulException;
import com.revature.exceptions.UsernameNotUniqueException;
import com.revature.models.User;
import com.revature.services.AuthService;
import com.revature.util.ConnectionFactory;
import java.sql.*;
import java.util.Optional;
import java.util.OptionalInt;

public class UserDAO {
    Connection con = ConnectionFactory.getInstance().getConnection();
    Optional<User> resultOp = Optional.empty();
    static int k=0;
    static int rowChecker=0;
    /**
     * Should retrieve a User from the DB with the corresponding username or an empty optional if there is no match.
     */
    public Optional<User> getByUsername(String username) {
        User temp = new User();
        String query = "SELECT * FROM ers_users WHERE ers_username=?";
        try {
            PreparedStatement preparedStatement= con.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet resultSet= preparedStatement.executeQuery();
            while(resultSet.next()) {
                temp.setId(resultSet.getInt(1));
                temp.setUsername(resultSet.getString(2));
                temp.setPassword(resultSet.getString(3));
                temp.setFirstName(resultSet.getString(4));
                temp.setLastName(resultSet.getString(5));
                temp.setEmail(resultSet.getString(6));
                temp.setRoleID(resultSet.getInt(7));
            }
            resultOp= Optional.of(temp);
            return resultOp;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultOp;
    }


    /**
     * <ul>
     *     <li>Should Insert a new User record into the DB with the provided information.</li>
     *     <li>Should throw an exception if the creation is unsuccessful.</li>
     *     <li>Should return a User object with an updated ID.</li>
     * </ul>
     */
    public User create(User userToBeRegistered) {
        //counter for user ID
        k++;
        //SQL query to store User information to DB
        String query ="INSERT INTO ers_users(ers_users_id, ers_username, ers_password, user_first_name, user_last_name, user_email, user_role_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            //Stores prepared SQL statement into st
            PreparedStatement st = con.prepareStatement(query);
            //sets vales called sent into prepared statement
            st.setInt(1, userToBeRegistered.getId());
            st.setString(2, userToBeRegistered.getUsername());
            st.setString(3, userToBeRegistered.getPassword());
            st.setString(4, userToBeRegistered.getFirstName());
            st.setString(5, userToBeRegistered.getLastName());
            st.setString(6, userToBeRegistered.getEmail());
            st.setInt(7,userToBeRegistered.getRoleID());
            rowChecker=st.executeUpdate();
        }catch (SQLException e) {
            System.out.println("Couldn't add user "+ e.getMessage() +"\n"+ e.getErrorCode());
            throw new RegistrationUnsuccessfulException("Error occured when registering account");
        }
        userToBeRegistered.setId(k);
        return userToBeRegistered;
    }
}

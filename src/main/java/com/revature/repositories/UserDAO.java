package com.revature.repositories;

import com.revature.exceptions.NoSuchUserException;
import com.revature.exceptions.RegistrationUnsuccessfulException;
import com.revature.exceptions.UsernameNotUniqueException;
import com.revature.models.Role;
import com.revature.models.User;
import com.revature.services.AuthService;
import com.revature.util.ConnectionFactory;
import java.sql.*;
import java.util.Optional;
import java.util.OptionalInt;

public class UserDAO {
    Connection con = ConnectionFactory.getInstance().getConnection();
    Optional<User> resultOp = Optional.empty();
    static int k=1;
    static int rowChecker=0;
    /**
     * Should retrieve a User from the DB with the corresponding username or an empty optional if there is no match.
     */
    public Optional<User> getByUsername(String username) {
        User temp = new User();
        //SQL query to retrieve users with matching username
        String query = "SELECT * FROM ers_users WHERE ers_username=?";
        try {
            //initialize prepared statement set ? to username value passed in, then execute the query and store it in a result set
            PreparedStatement preparedStatement= con.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet resultSet= preparedStatement.executeQuery();
            //Load result set values into a temp user
            while(resultSet.next()) {
                temp.setId(resultSet.getInt(1));
                temp.setUsername(resultSet.getString(2));
                temp.setPassword(resultSet.getString(3));
                temp.setFirstName(resultSet.getString(4));
                temp.setLastName(resultSet.getString(5));
                temp.setEmail(resultSet.getString(6));
                temp.setRoleID(resultSet.getInt(7));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        //SQL query to retrieve user roles with matching role id
        String query2 = "SELECT * FROM ers_user_roles WHERE ers_user_role_id=?";
        //initialize prepared statement set ? to the RoleID value stored in temp User, then execute the query and store it in a result set
        try {
            PreparedStatement preparedStatement= con.prepareStatement(query2);
            preparedStatement.setInt(1,temp.getRoleID());
            ResultSet resultSet=preparedStatement.executeQuery();
            //Convert role string from DB into Role, then store it into temp User
            while(resultSet.next()){
                String role = resultSet.getString(2);
                switch(role){
                    case "Employee":
                        temp.setRole(Role.EMPLOYEE);
                        break;
                    case "Finance Manager":
                        temp.setRole(Role.FINANCE_MANAGER);
                        break;
                }
            }
            //Store user into empty Optional then return it
            resultOp= Optional.ofNullable(temp);
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
            System.out.println("Couldn't add user \n"+ e.getMessage() +"\n"+ e.getErrorCode());
           /* if(rowChecker==0&& e.getErrorCode()==0){
            }else */
            throw new RegistrationUnsuccessfulException("Error occured when registering account");
        }
        //SQL query to store User Role ID and Role into DB
        String query2 ="INSERT INTO ers_user_roles VALUES (?, ?)";
        try {
            //Set values in SQL statement to Role ID and Role of User that was passed in
            PreparedStatement preparedStatement=con.prepareStatement(query2);
            preparedStatement.setInt(1, userToBeRegistered.getRoleID());
            System.out.println(userToBeRegistered.getRole().toString());
            preparedStatement.setString(2, userToBeRegistered.getRole().toString());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        //sets User's ID to int k which starts at 1, then increment k and return the User that was registered
        userToBeRegistered.setId(k);
        k++;
        return userToBeRegistered;
    }
}

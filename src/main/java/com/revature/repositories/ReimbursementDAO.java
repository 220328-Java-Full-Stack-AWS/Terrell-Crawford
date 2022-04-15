package com.revature.repositories;

import com.revature.models.Reimbursement;
import com.revature.models.Role;
import com.revature.models.Status;
import com.revature.models.User;
import com.revature.services.UserService;
import com.revature.util.ConnectionFactory;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ReimbursementDAO {
    Connection con = ConnectionFactory.getInstance().getConnection();
    UserService userService= new UserService();
    /**
     * Should retrieve a Reimbursement from the DB with the corresponding id or an empty optional if there is no match.
     */
    public Optional<Reimbursement> getById(int id) {
        Reimbursement temp = new Reimbursement();
        //SQL query to retrieve reimbursements with matching reimbursement id
        String query = "SELECT * FROM ers_reimbursement WHERE reimb_id=?";
        try {
            //initialize prepared statement, set ? to reimbursement id value passed in, then execute the query and store it in a result set
            PreparedStatement preparedStatement= con.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet= preparedStatement.executeQuery();
            //Load result set values into a temp reimbursement
            while(resultSet.next()) {
                temp.setId(resultSet.getInt(1));
                temp.setAmount(resultSet.getDouble(2));
                temp.setCreationDate(resultSet.getTimestamp(3));
                temp.setResolutionDate(resultSet.getTimestamp(4));
                temp.setDescription(resultSet.getString(5));
                //temp.setReciept((Image) resultSet.getBlob(6));
                temp.setAuthor(userService.getByUserID(resultSet.getInt(7)).get());
                temp.setResolver(userService.getByUserID(resultSet.getInt(8)).get());
                temp.setStatusID(resultSet.getInt(9));
                temp.setReimbTypeID(resultSet.getInt(10));


            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        String query2 = "SELECT * FROM ers_reimbursement_status WHERE reimb_status_id=?";
        try {
            PreparedStatement preparedStatement= con.prepareStatement(query2);
            preparedStatement.setInt(1, temp.getStatusID());
            ResultSet resultSet= preparedStatement.executeQuery();
            while(resultSet.next()){
                String role = resultSet.getString(2);
                switch(role){
                    case "Pending":
                        temp.setStatus(Status.PENDING);
                        break;
                    case "Approved":
                        temp.setStatus(Status.APPROVED);
                        break;
                    case "Denied":
                        temp.setStatus(Status.DENIED);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Should retrieve a List of Reimbursements from the DB with the corresponding Status or an empty List if there are no matches.
     */
    public List<Reimbursement> getByStatus(Status status) {
        return Collections.emptyList();
    }

    /**
     * <ul>
     *     <li>Should Update an existing Reimbursement record in the DB with the provided information.</li>
     *     <li>Should throw an exception if the update is unsuccessful.</li>
     *     <li>Should return a Reimbursement object with updated information.</li>
     * </ul>
     */
    public Reimbursement update(Reimbursement unprocessedReimbursement) {
    	return null;
    }
}

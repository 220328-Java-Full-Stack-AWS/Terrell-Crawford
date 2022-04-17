package com.revature.repositories;

import com.revature.exceptions.UnableToCreateReimbursementException;
import com.revature.models.Reimbursement;
import com.revature.models.Role;
import com.revature.models.Status;
import com.revature.models.User;
import com.revature.services.UserService;
import com.revature.util.ConnectionFactory;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
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
        Optional<Reimbursement>tempOp = Optional.empty();
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
        //Stores the status of the reimbursement into temp reimbursement
        String query2 = "SELECT * FROM ers_reimbursement_status WHERE reimb_status_id=?";
        String query3 ="SELECT * FROM ers_reimbursement_type WHERE reimb_type_id=?";

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
            preparedStatement=con.prepareStatement(query3);
            preparedStatement.setInt(1, temp.getReimbTypeID());
            ResultSet resultSet1=preparedStatement.executeQuery();
            while(resultSet1.next()){
                temp.setReimbType(resultSet1.getString(2));
            }
            //store temp reimbursement into an optional and return the optional
            tempOp=Optional.ofNullable(temp);
            return tempOp;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tempOp;
    }

    /**
     * Should retrieve a List of Reimbursements from the DB with the corresponding Status or an empty List if there are no matches.
     */
    public List<Reimbursement> getByStatus(Status status) {
        List<Reimbursement>tempList= new ArrayList<Reimbursement>();
        Reimbursement temp = new Reimbursement();
        String getStatus = "SELECT * FROM ers_reimbursement_status WHERE reimb_status =?";
        String getReimb = "SELECT * FROM ers_reimbursement WHERE reimb_status_id =?";
        try {
            //Get the Reimbursements that are of the desired status
            PreparedStatement preparedStatement= con.prepareStatement(getStatus);
            preparedStatement.setString(1, status.toString());
            ResultSet resultSet= preparedStatement.executeQuery();
            //While there are reimbursements with the desired status
            while(resultSet.next()){
                //store their status and status id in the Reimbursement
                int counter = resultSet.getRow()-1;
                System.out.println(resultSet.getRow());
                temp.setStatusID(resultSet.getInt(1));
                String status2= resultSet.getString(2);
                switch (status2){
                    case "Pending":
                        temp.setStatus(Status.PENDING);
                        break;
                    case "Approved":
                        temp.setStatus(Status.APPROVED);
                        break;
                    case "Denied":
                        temp.setStatus(Status.DENIED);
                        break;
                }
                //Then get the reimbursement with the matching status ID
                preparedStatement= con.prepareStatement(getReimb);
                preparedStatement.setInt(1, temp.getStatusID());
                ResultSet reimbInfo= preparedStatement.executeQuery();
                //Store the reimb info in that reimb
                while(reimbInfo.next()){
                    temp.setId(reimbInfo.getInt("reimb_id"));
                    temp.setAmount(reimbInfo.getDouble("reimb_ammount"));
                    temp.setCreationDate(reimbInfo.getTimestamp("reimb_submitted"));
                    temp.setResolutionDate(reimbInfo.getTimestamp(4));
                    temp.setDescription(reimbInfo.getString(5));
                    temp.setAuthor(userService.getByUserID(reimbInfo.getInt(7)).get());
                    temp.setResolver(userService.getByUserID(reimbInfo.getInt(8)).get());
                    temp.setReimbTypeID(reimbInfo.getInt(10));
                }
                String getReimType = "SELECT reimb_type FROM ers_reimbursement_type WHERE reimb_type_id=?";
                preparedStatement=con.prepareStatement(getReimType);
                preparedStatement.setInt(1, temp.getReimbTypeID());
                ResultSet resultSet2= preparedStatement.executeQuery();
                while(resultSet2.next()){
                    temp.setReimbType(resultSet2.getString(1));
                    tempList.add(counter, temp);
                }


            }
            return tempList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * <ul>
     *     <li>Should Create a Reimbursement record in the DB with the provided information.</li>
     *     <li>Should throw an exception if the creation is unsuccessful.</li>
     *     <li>Should return a Reimbursement object with updated information.</li>
     * </ul>
     */
    public Reimbursement create(Reimbursement newReimb){
        String storeType= "INSERT INTO ers_reimbursement_type (reimb_type) VALUES(?)";
        String storeStatus= "INSERT INTO ers_reimbursement_status (reimb_status) VALUES(?)";
        String storeReimb= "INSERT INTO ers_reimbursement (reimb_ammount, reimb_submitted, reimb_resolved, reimb_description, reimb_author) VALUES(?, ?, ?, ?, ?)";
        String loadInTypeID= "UPDATE ers_reimbursement SET reimb_type_id = ers_reimbursement_type.reimb_type_id FROM ers_reimbursement_type WHERE ers_reimbursement.reimb_type_id = ers_reimbursement_type.reimb_type_id";
        String getTypeID="SELECT reimb_type_id FROM ers_reimbursement WHERE reimb_id=?";
        String loadInStatusID="UPDATE ers_reimbursement SET reimb_status_id = ers_reimbursement_status.reimb_status_id FROM ers_reimbursement_status WHERE ers_reimbursement.reimb_status_id = ers_reimbursement_status.reimb_status_id";
        String getStatusID="SELECT reimb_status_id FROM ers_reimbursement WHERE reimb_id=?";
        String loadID="SELECT reimb_id FROM ers_reimbursement WHERE reimb_id=?";
        try {
            //Store the Type of Reimbursement
            PreparedStatement preparedStatement = con.prepareStatement(storeType);
            preparedStatement.setString(1, newReimb.getReimbType());
            preparedStatement.executeUpdate();
            //Store the Reimbursement Status
            preparedStatement=con.prepareStatement(storeStatus);
            preparedStatement.setString(1, newReimb.getStatus().toString());
            preparedStatement.executeUpdate();
            //Store the Amount, Creation Date, Resolution Date, Description, Author, and Resolver
            preparedStatement= con.prepareStatement(storeReimb);
            preparedStatement.setDouble(1, newReimb.getAmount());
            preparedStatement.setTimestamp(2, newReimb.getCreationDate());
            preparedStatement.setTimestamp(3, newReimb.getResolutionDate());
            preparedStatement.setString(4, newReimb.getDescription());
            preparedStatement.setInt(5, newReimb.getAuthor().getId());
            //preparedStatement.setInt(6, newReimb.getResolver().getId());
            preparedStatement.executeUpdate();
            //Store and get the StatusID into the Reimbursement
            preparedStatement= con.prepareStatement(loadInStatusID);
            preparedStatement.executeUpdate();
            preparedStatement= con.prepareStatement(getStatusID);
            preparedStatement.setInt(1, newReimb.getId());
            ResultSet storeStatusID= preparedStatement.executeQuery();
            while(storeStatusID.next()){
                newReimb.setStatusID(storeStatusID.getInt("reimb_status_id"));
            }
            //Store and get the TypeID into the Reimbursement
            preparedStatement= con.prepareStatement(loadInTypeID);
            preparedStatement.executeUpdate();
            preparedStatement= con.prepareStatement(getTypeID);
            preparedStatement.setInt(1, newReimb.getId());
            ResultSet storeTypeID= preparedStatement.executeQuery();
            while(storeTypeID.next()){
                newReimb.setReimbTypeID(storeStatusID.getInt("reimb_status_id"));
            }
            preparedStatement=con.prepareStatement(loadID);
            preparedStatement.setInt(1, newReimb.getReimbTypeID());
            ResultSet loadIDSet= preparedStatement.executeQuery();
            while(loadIDSet.next()){
                newReimb.setId(loadIDSet.getInt(1));
            }


        } catch (SQLException e) {
            e.printStackTrace();
            throw new UnableToCreateReimbursementException();
        }
        return newReimb;
    }


    /**
     * <ul>
     *     <li>Should Update an existing Reimbursement record in the DB with the provided information.</li>
     *     <li>Should throw an exception if the update is unsuccessful.</li>
     *     <li>Should return a Reimbursement object with updated information.</li>
     * </ul>
     */
    public Reimbursement update(Reimbursement unprocessedReimbursement) {
        String updateType="UPDATE ers_reimbursement_type SET reimb_type =? WHERE reimb_type_id=?";
        String updateStatus="UPDATE ers_reimbursement_status SET reimb_status=? WHERE reimb_status_id=?";
        String updateRest="UPDATE ers_reimbursement SET reimb_ammount=?, reimb_submitted=?, reimb_resolved=?, reimb_description=?, reimb_resolver=? WHERE reimb_id=?";
        try {
            PreparedStatement updater = con.prepareStatement(updateType);
            updater.setString(1, unprocessedReimbursement.getReimbType());
            updater.setInt(2, unprocessedReimbursement.getReimbTypeID());
            updater.executeUpdate();

            updater= con.prepareStatement(updateStatus);
            updater.setString(1, unprocessedReimbursement.getStatus().toString());
            updater.setInt(1, unprocessedReimbursement.getStatusID());
            updater.executeUpdate();

            updater= con.prepareStatement(updateRest);
            updater.setDouble(1, unprocessedReimbursement.getAmount());
            updater.setTimestamp(2, unprocessedReimbursement.getCreationDate());
            updater.setTimestamp(3, unprocessedReimbursement.getResolutionDate());
            updater.setString(4, unprocessedReimbursement.getDescription());
            updater.setInt(5, unprocessedReimbursement.getResolver().getId());
            updater.setInt(6, unprocessedReimbursement.getId());
            updater.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            //throw exception here
        }

        return unprocessedReimbursement;
    }

    /**
     * <ul>
     *     <li>Should Delete an existing Reimbursement record in the DB with the provided information.</li>
     *     <li>Should throw an exception if the delete is unsuccessful.</li>
     * </ul>
     */
    public void delete(Reimbursement reimbToDelete){
        String typeDelete = "DELETE FROM  ers_reimbursement_type WHERE reimb_type_id=?";
        String statusDelete= "DELETE FROM ers_reimbursement_status WHERE reimb_status_id=?";
        String reimbDelete ="DELETE FROM ers_reimbursement WHERE reimb_id=?";
        try {
            PreparedStatement preparedStatement= con.prepareStatement(reimbDelete);
            preparedStatement.setInt(1, reimbToDelete.getReimbTypeID());
            preparedStatement.executeUpdate();
            preparedStatement= con.prepareStatement(statusDelete);
            preparedStatement.setInt(1, reimbToDelete.getStatusID());
            preparedStatement.executeUpdate();
            preparedStatement= con.prepareStatement(typeDelete);
            preparedStatement.setInt(1, reimbToDelete.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }
}

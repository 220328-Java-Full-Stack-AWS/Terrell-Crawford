package com.revature.services;

import com.revature.exceptions.UnableToProcessException;
import com.revature.models.Reimbursement;
import com.revature.models.Role;
import com.revature.models.Status;
import com.revature.models.User;
import com.revature.repositories.ReimbursementDAO;
import jdk.nashorn.internal.runtime.options.Option;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * The ReimbursementService should handle the submission, processing,
 * and retrieval of Reimbursements for the ERS application.
 *
 * {@code process} and {@code getReimbursementsByStatus} are the minimum methods required;
 * however, additional methods can be added.
 *
 * Examples:
 * <ul>
 *     <li>Create Reimbursement</li>
 *     <li>Update Reimbursement</li>
 *     <li>Get Reimbursements by ID</li>
 *     <li>Get Reimbursements by Author</li>
 *     <li>Get Reimbursements by Resolver</li>
 *     <li>Get All Reimbursements</li>
 * </ul>
 */
public class ReimbursementService {
     ReimbursementDAO reimbDAO=new ReimbursementDAO();
    /**
     * <ul>
     *     <li>Should ensure that the user is logged in as a Finance Manager</li>
     *     <li>Must throw exception if user is not logged in as a Finance Manager</li>
     *     <li>Should ensure that the reimbursement request exists</li>
     *     <li>Must throw exception if the reimbursement request is not found</li>
     *     <li>Should persist the updated reimbursement status with resolver information</li>
     *     <li>Must throw exception if persistence is unsuccessful</li>
     * </ul>
     *
     * Note: unprocessedReimbursement will have a status of PENDING, a non-zero ID and amount, and a non-null Author.
     * The Resolver should be null. Additional fields may be null.
     * After processing, the reimbursement will have its status changed to either APPROVED or DENIED.
     */
    public Reimbursement process(Reimbursement unprocessedReimbursement, Status finalStatus, User resolver) {
        if(resolver.getRole()== Role.FINANCE_MANAGER&&unprocessedReimbursement.getStatus()==Status.PENDING) {
            unprocessedReimbursement.setStatus(finalStatus);
            unprocessedReimbursement.setResolver(resolver);
            LocalDateTime now = LocalDateTime.now();
            Timestamp today = Timestamp.valueOf(now);
            unprocessedReimbursement.setResolutionDate(today);
            unprocessedReimbursement = reimbDAO.update(unprocessedReimbursement);
            return unprocessedReimbursement;
        }else throw new UnableToProcessException("Only Finance Managers can process reimbursement request. And only PENDING requests may be processed");
    }

    public Reimbursement create(Reimbursement reimbToBeCreated){
        Reimbursement returnVal=reimbDAO.create(reimbToBeCreated);
        return returnVal;
    }

    /**
     * Should retrieve all reimbursements with the correct status.
     */
    public List<Reimbursement> getReimbursementsByStatus(Status status) {
        List<Reimbursement>result=reimbDAO.getByStatus(status);
        return result;
    }

    public Optional<Reimbursement> getReimbursementByID(int ID){
        Optional<Reimbursement>returnVal=reimbDAO.getById(ID);
        if(returnVal.equals(Optional.empty())){
            return Optional.empty();
        }
        return returnVal;
    }

    /**
     * Deletes the given reimbursement from the DB
     */
    public void delete(Reimbursement reimbToDelete){
        reimbDAO.delete(reimbToDelete);
    }

    /**
     * Updates the given reimbursement in the DB
     */
    public Reimbursement update(Reimbursement reimbToUpdate){
        Reimbursement returnVal=reimbDAO.update(reimbToUpdate);
        return returnVal;
    }
}


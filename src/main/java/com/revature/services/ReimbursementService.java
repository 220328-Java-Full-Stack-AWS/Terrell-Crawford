package com.revature.services;

import com.revature.models.Reimbursement;
import com.revature.models.Status;
import com.revature.repositories.ReimbursementDAO;
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

    ReimbursementDAO reimbDAO= new ReimbursementDAO();
     public ReimbursementService(){this.reimbDAO=new ReimbursementDAO();}


    public void setReimbDAO(ReimbursementDAO rDAO){
         this.reimbDAO =rDAO;
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


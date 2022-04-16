package com.revature.models;

import java.sql.Timestamp;

/**
 * This concrete Reimbursement class can include additional fields that can be used for
 * extended functionality of the ERS application.
 *
 * Example fields:
 * <ul>
 *     <li>Description</li>
 *     <li>Creation Date</li>
 *     <li>Resolution Date</li>
 *     <li>Receipt Image</li>
 * </ul>
 *
 */
public class Reimbursement extends AbstractReimbursement {
    private int  reimbTypeID;
    private int  statusID;
    //private Image reciept;
    private String description;
    private String reimbType;
    private Timestamp creationDate;
    private Timestamp resolutionDate;

    public Reimbursement() {
        super();
    }

    /**
     * This includes the minimum parameters needed for the {@link com.revature.models.AbstractReimbursement} class.
     * If other fields are needed, please create additional constructors.
     */
    public Reimbursement(int id, Status status, User author, User resolver, double amount) {
        super(id, status, author, resolver, amount);
    }

    public Reimbursement(int id, Status status, User author, User resolver, double amount, String description, String reimbType, Timestamp creationDate, Timestamp resolutionDate, int reimbTypeID, int statusID) {
        super(id, status, author, resolver, amount);
        this.reimbType=reimbType;
        this.statusID=statusID;
        this.reimbTypeID=reimbTypeID;
        //this.reciept=reciept;
        this.description=description;
        this.creationDate=creationDate;
        this.resolutionDate=resolutionDate;
    }

    //Getters and Setters


    public String getReimbType() {
        return reimbType;
    }

    public void setReimbType(String reimbType) {
        this.reimbType = reimbType;
    }

    public int getStatusID() {
        return statusID;
    }

    public void setStatusID(int statusID) {
        this.statusID = statusID;
    }

    public int getReimbTypeID() {
        return reimbTypeID;
    }

    public void setReimbTypeID(int reimbTypeID) {
        this.reimbTypeID = reimbTypeID;
    }

   /* public Image getReciept() {
        return reciept;
    }

    public void setReciept(Image reciept) {
        this.reciept = reciept;
    }*/

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public Timestamp getResolutionDate() {
        return resolutionDate;
    }

    public void setResolutionDate(Timestamp resolutionDate) {
        this.resolutionDate = resolutionDate;
    }
}

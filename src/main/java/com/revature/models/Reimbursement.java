package com.revature.models;

import java.awt.*;
import java.util.Date;

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
    private Date creationDate;
    private Date resolutionDate;

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

    public Reimbursement(int id, Status status, User author, User resolver, double amount, String description, Date creationDate, Date resolutionDate, int reimbTypeID, int statusID) {
        super(id, status, author, resolver, amount);
        this.statusID=statusID;
        this.reimbTypeID=reimbTypeID;
        //this.reciept=reciept;
        this.description=description;
        this.creationDate=creationDate;
        this.resolutionDate=resolutionDate;
    }

    //Getters and Setters


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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getResolutionDate() {
        return resolutionDate;
    }

    public void setResolutionDate(Date resolutionDate) {
        this.resolutionDate = resolutionDate;
    }
}

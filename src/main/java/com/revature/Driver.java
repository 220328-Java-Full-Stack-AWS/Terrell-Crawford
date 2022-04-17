package com.revature;

import com.revature.models.Reimbursement;
import com.revature.models.Role;
import com.revature.models.Status;
import com.revature.models.User;
import com.revature.services.AuthService;
import com.revature.services.ReimbursementService;
import com.revature.services.UserService;
import com.revature.util.ConnectionFactory;
import java.sql.SQLOutput;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Driver {

    public static void main(String[] args) {
        //Services
        ReimbursementService reimbServ = new ReimbursementService();
        AuthService loginCheck= new AuthService();
        String userIn;
        int userNum;
        double userNumb;
        UserService userService= new UserService();
        String username;
        String pass;
        //Using console as UI until we go over how to make a web UI

        //Setup Scanner for early UI implementation
        Scanner scan = new Scanner(System.in);
        //Prompt user to log in or to register then read input to determine choice
        System.out.println("Welcome to the ERS system!");
        System.out.println("Please press 1 to login, or 2 to register for an account:");
        String newOrReturning = scan.next();

        //If user has account ask for log in credentials else user must create an account
        if(newOrReturning.equals("1")){
            System.out.println("Enter Username:");
            userIn= scan.next();
            username=userIn;
            System.out.println("Enter Password for "+ userIn);
            userIn= scan.next();
             pass=userIn;

            //Check if user exists and handles what happens if it doesn't
             loginCheck.login(username, pass);
             User currentUser = userService.getByUsername(username).get();
             System.out.println("Welcome To the Employee Reimbursement System, "+username+"!");
             //If finance manager has logged in, present the appropriate menus
             if(currentUser.getRole()==Role.FINANCE_MANAGER){
                 System.out.println("Press 1 to view ALL Reimbursement Requests, 2 to View Reimbursement by status, logout");
                 userIn= scan.next();
                 if(userIn.equals("1")){

                 }
              //If Employee has logged in, present the appropriate menus
             }else{
                 System.out.println("Press 1 to create a new Reimbursement Request, 2 to view Past & Pending Reimbursement Request");
                 userIn= scan.next();
                 //create new Reimbursement
                 if(userIn.equals("1")){
                     System.out.println("Please enter the amount you are requesting(Just put the value, no $)");
                     userNumb=scan.nextDouble();
                     Reimbursement newReimb= new Reimbursement();
                     newReimb.setAmount(userNumb);
                     System.out.println("Press 1 for Lodging Reimbursement, 2 for Food Reimbursement, or 3 for Travel Reimbursement");
                     userIn= scan.next();
                     if(userIn.equals("1")) {
                         newReimb.setReimbType("LODGING");
                     }else if(userIn.equals("2")){
                         newReimb.setReimbType("FOOD");
                     }else if(userIn.equals("3"))newReimb.setReimbType("TRAVEL");
                     System.out.println("Please enter a description for this request");
                     userIn=scan.nextLine();
                     while(userIn==null);
                     newReimb.setDescription(userIn);
                     LocalDateTime now= LocalDateTime.now();
                     Timestamp today= Timestamp.valueOf(now);
                     newReimb.setCreationDate(today);
                     newReimb.setAuthor(currentUser);
                     newReimb.setStatus(Status.PENDING);
                     System.out.println("Attempting to create New Request");
                     newReimb=reimbServ.create(newReimb);


                 //Viewing Options for Past&Pending
                 }else if(userIn.equals("2")){
                     System.out.println("Press 1 to view pending Requests, 2 to see Completed Requests");
                     userIn= scan.next();
                     //View pending requests
                     if(userIn.equals("1")){
                         List<Reimbursement>pending=reimbServ.getReimbursementsByStatus(Status.PENDING);
                         System.out.println(pending);
                         for(Reimbursement r : pending){
                             System.out.println("Reimbursement ID:"+r.getId()+" Request of $ "+r.getAmount()+"for "+r.getReimbType()+" was made on "+r.getCreationDate());
                         }
                         System.out.println("Press 1 to edit a Request, 2 to cancel a request");
                         userIn= scan.next();
                         //Update Reimbursement
                         if(userIn.equals("1")){
                             System.out.println("Please enter the ID (The number after Reimbursement ID) of Request you wold like to edit");
                             userNum= scan.nextInt();
                             Optional<Reimbursement> temp =reimbServ.getReimbursementByID(userNum);
                             if(temp.get().getId()==0){
                                 //throw exception
                             }else {
                                 Reimbursement newReimb= temp.get();
                                 System.out.println("Please enter the amount");
                                 userNumb= scan.nextDouble();
                                 newReimb.setAmount(userNumb);
                                 System.out.println("Please enter the Type of Reimbursement");
                                 userIn= scan.next();
                                 newReimb.setReimbType(userIn);
                                 LocalDateTime now= LocalDateTime.now();
                                 Timestamp today= Timestamp.valueOf(now);
                                 newReimb.setCreationDate(today);
                                 newReimb=reimbServ.update(newReimb);
                             }
                         //Delete Reimbursement
                         }else if(userIn.equals("2")){
                             System.out.println("Please enter the ID (The number after Reimbursement ID) of Request you wold like to delete");
                             userNum= scan.nextInt();
                             Optional<Reimbursement> temp = reimbServ.getReimbursementByID(userNum);
                             if(temp.get().getId()==0){
                                 System.out.println("There is no reimbursement request with that ID");
                                 //throw exception
                             }else reimbServ.delete(temp.get());
                         }

                     //View Completed Requests
                     }else if(userIn.equals("2")){
                         List<Reimbursement> approved=reimbServ.getReimbursementsByStatus(Status.APPROVED);
                         List<Reimbursement> denied=reimbServ.getReimbursementsByStatus(Status.DENIED);
                         for(Reimbursement r :approved){
                             System.out.println("Reimbursement ID:"+r.getId()+"Request of $"+r.getAmount()+"for "+r.getReimbType()+" on "+r.getCreationDate()+" was "+r.getStatus().toString()+" by "+r.getResolver()+" on "+r.getResolutionDate());
                         }
                         for(Reimbursement r :denied){
                             System.out.println("Reimbursement ID:"+r.getId()+"Request of $"+r.getAmount()+"for "+r.getReimbType()+" on "+r.getCreationDate()+" was "+r.getStatus().toString()+" by "+r.getResolver()+" on "+r.getResolutionDate());
                         }

                     }
                 }
             }


        }else if(newOrReturning.equals("2")){
            //Prompt new user for registration information and store that data into a new User
            User newUser = new User();
            //Set all new users roles to EMPLOYEE. Admins are assumed to already be registered or will have their role updated.
            newUser.setRole(Role.EMPLOYEE);

            System.out.println("Enter First Name");
            userIn=scan.next();
            newUser.setFirstName(userIn);

            System.out.println("Enter Last Name");
            userIn=scan.next();
            newUser.setLastName(userIn);

            System.out.println("Enter email");
            userIn=scan.next();
            newUser.setEmail(userIn);

            System.out.println("Enter New username");
            userIn=scan.next();
            newUser.setUsername(userIn);

            System.out.println("Enter New Password");
            userIn= scan.next();
            newUser.setPassword(userIn);

            System.out.println("Attempting to register user now");
            //Attempt to register the new user
            newUser=loginCheck.register(newUser);

            System.out.println(userService.getByUsername(newUser.getUsername()));
            System.out.println(newUser.getRoleID());
            System.out.println(userService.getByUserID(newUser.getId()));

        }

    }
}

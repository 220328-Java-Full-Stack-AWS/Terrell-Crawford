package com.revature;

import com.revature.models.Role;
import com.revature.models.User;
import com.revature.services.AuthService;
import com.revature.services.UserService;
import com.revature.util.ConnectionFactory;
import java.sql.SQLOutput;
import java.util.Optional;
import java.util.Scanner;

public class Driver {

    public static void main(String[] args) {
        //Services
        AuthService loginCheck= new AuthService();
        String userIn;
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

            //Check if user exists and handle what happens if it doesn't
             loginCheck.login(username, pass);
            //Insert code to check if correct password was given and handle what happens if it wasn't HERE **** NOT DON YET***
            System.out.println("Welcome! Press 1 to view");

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


        }

    }
}

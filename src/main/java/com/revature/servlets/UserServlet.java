package com.revature.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.exceptions.*;
import com.revature.models.User;
import com.revature.services.AuthService;
import com.revature.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserServlet extends HttpServlet {
    private UserService uServ;
    private AuthService authServ;
    private ObjectMapper mapper;

    public void init() throws ServletException{
        this.uServ=new UserService();
        this.mapper = new ObjectMapper();
        this.authServ=new AuthService();
    }
//This is the READ operation
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //If we're trying to read a user by their user id
        if(req.getHeader("methodToBeCalled").equals("id")) {
            try {
                User user = uServ.getByUserID(Integer.parseInt(req.getHeader("id"))).get();
                String json = mapper.writeValueAsString(user);
                resp.setContentType("application/json");
                resp.getWriter().print(json);
                resp.setStatus(200);
            }catch(NoSuchUserException u){
                resp.setStatus(404);
            }
        //If we're trying to read a user by their username
        }else if(req.getHeader("methodToBeCalled").equals("username")){
            User currentUser;
            //If we are trying to read a user by their username to try and log them in
            try {
                //If we're just trying to retrieve a user from their username
                currentUser = uServ.getByUsername(req.getHeader("username")).get();
                String json = mapper.writeValueAsString(currentUser);
                resp.setContentType("application/json");
                resp.getWriter().print(json);
                resp.setStatus(200);
            }catch(NoSuchUserException u){
                resp.setStatus(404);
            }
        }
    }
    //This is the CREATE operation
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User currentUser = mapper.readValue(req.getInputStream(), User.class);
        try {
            //logging in a User that's already in the DB
            if(req.getHeader("userIsTryingTo").equals("login")){
                currentUser=authServ.login(currentUser.getUsername(), currentUser.getPassword());
                resp.setStatus(200);
                String json = mapper.writeValueAsString(currentUser);
                resp.getWriter().print(json);
                resp.setHeader("access-control-expose-headers", "authToken, userIs");
                resp.setHeader("authToken", currentUser.getUsername());

                //resp.setHeader("access-control-expose-headers", "userIs");
                resp.setHeader("userIs", currentUser.getRole().toString());

            //creating a new User and putting them in the DB
            }else if(req.getHeader("userIsTryingTo").equals("register")) {
                currentUser = authServ.register(currentUser);
                resp.setStatus(201); //status code 201: created says that we have successfully persisted this object
                String json = mapper.writeValueAsString(currentUser);
                resp.getWriter().print(json);
                resp.setHeader("access-control-expose-headers", "authToken, userIs");
                resp.setHeader("authToken", currentUser.getUsername());

                resp.setHeader("userIs", currentUser.getRole().toString());
            }
        //throw appropriate error codes if these exceptions are thrown
        }catch(UsernameNotUniqueException | RegistrationUnsuccessfulException | NewUserHasNonZeroIdException e){
            resp.setStatus(409);
        }catch(NoSuchUserException u){
            resp.setStatus(404);
        }catch(NoSuchPassword p){
            resp.setStatus(401);
        }
    }

    //This is the UPDATE operation
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        User temp = mapper.readValue(req.getInputStream(), User.class);
        User testUser = uServ.getByUsername(req.getHeader("authToken")).get();
        testUser.setUsername(temp.getUsername());
        testUser.setEmail(temp.getEmail());
        testUser.setFirstName(temp.getFirstName());
        testUser.setLastName(temp.getLastName());
        testUser.setPassword(temp.getPassword());
        System.out.println(testUser.getUsername());
        try {
            testUser = uServ.updateUser(testUser);
            resp.setStatus(200);
            String json = mapper.writeValueAsString(testUser);
            resp.getWriter().print(json);
            resp.setHeader("access-control-expose-headers", "authToken, userIs");
            resp.setHeader("authToken", testUser.getUsername());
            //resp.setHeader("access-control-expose-headers", "userIs");
            resp.setHeader("userIs", testUser.getRole().toString());
        }catch(NoSuchUserException e){
            resp.setStatus(404);
        }
    }
    //This is the DELETE operation (Duh)
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        uServ.deleteUser(uServ.getByUsername(req.getHeader("username")).get());
        resp.setStatus(200);
    }
}

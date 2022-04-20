package com.revature.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
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
        //resp.setStatus(200);
        //resp.getWriter().print("Pong!");
        System.out.println(req.getHeader("methodToBeCalled"));
        //If we're trying to read a user by their user id
        if(req.getHeader("methodToBeCalled").equals("id")) {
            User user = uServ.getByUserID(Integer.parseInt(req.getHeader("id"))).get();
            String json = mapper.writeValueAsString(user);
            resp.setContentType("application/json");
            resp.getWriter().print(json);
            resp.setStatus(200);
        //If we're trying to read a user by their username
        }else if(req.getHeader("methodToBeCalled").equals("username")){
            User currentUser=new User();
            //If we are trying to read a user by their username to try and log them in
            if(req.getHeader("LookUpMode").equals("login")) {
                currentUser = authServ.login(req.getHeader("username"), req.getHeader("password"));
            //If we're just trying to retrieve a user from their username
            }else if(req.getHeader("LookUpMode").equals("lookup")){
                currentUser = uServ.getByUsername(req.getHeader("username")).get();
            }
            String json = mapper.writeValueAsString(currentUser);
            resp.setContentType("application/json");
            resp.getWriter().print(json);
            resp.setStatus(200);

        }
    }
    //This is the CREATE operation
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //super.doPost(req, resp);
        //creating a new user in the database
        User currentUser = mapper.readValue(req.getInputStream(), User.class);;
        /*currentUser.setFirstName(req.getHeader("firstName"));
        currentUser.setLastName(req.getHeader("lastName"));
        currentUser.setUsername(req.getHeader("username"));
        currentUser.setPassword(req.getHeader("password"));
        currentUser.setEmail(req.getHeader("email"));
        currentUser.setRole(Role.EMPLOYEE);*/
        currentUser=authServ.register(currentUser);
        String json = mapper.writeValueAsString(currentUser);
        resp.setStatus(201); //status code 201: created says that we have successfully persisted this object
        resp.getWriter().print(json);
    }

    //This is the UPDATE operation
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       // super.doPut(req, resp);
        User testUser = mapper.readValue(req.getReader().toString(), User.class);
        /*User currentUser= new User();
        currentUser.setFirstName(req.getHeader("firstName"));
        currentUser.setLastName(req.getHeader("lastName"));
        currentUser.setUsername(req.getHeader("username"));
        currentUser.setPassword(req.getHeader("password"));
        currentUser.setEmail(req.getHeader("email"));
        switch(req.getHeader("role")){
            case "EMPLOYEE":
                currentUser.setRole(Role.EMPLOYEE);
                break;

            case "FINANCE_MANAGER":
                currentUser.setRole(Role.FINANCE_MANAGER);
                break;
        }*/
        testUser=uServ.updateUser(testUser);
        String json = mapper.writeValueAsString(testUser);
        resp.setStatus(201); //status code 201: created says that we have successfully persisted this object
        resp.getWriter().print(json);

    }
    //This is the DELETE operation (Duh)
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //super.doDelete(req, resp);
        uServ.deleteUser(uServ.getByUsername(req.getHeader("username")).get());
        resp.setStatus(200);
    }
}

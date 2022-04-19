package com.revature.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.models.User;
import com.revature.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserServlet extends HttpServlet {
    private UserService uServ;

    public void init() throws ServletException{
        this.uServ=new UserService();
    }
//This is the READ operation
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(200);
        resp.getWriter().print("Pong!");


       // req.getParameter("username");
        //req.getHeader("username");

        //req.getParameter("id");
        //req.getHeader("id");

       // User user =uServ.getByUserID(Integer.parseInt(req.getHeader("id"))).get();

        //ObjectMapper mapper = new ObjectMapper();
       // String json = mapper.writeValueAsString(user);
        //resp.setContentType("application/json");
        //resp.getWriter().print(json);
        //resp.setStatus(200);
    }
    //This is the CREATE operation
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    //This is the UPDATE operation
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }
    //This is the DELETE operation (Duh)
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }
}

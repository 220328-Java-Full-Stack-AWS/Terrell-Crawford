package com.revature.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.models.Reimbursement;
import com.revature.models.Role;
import com.revature.models.Status;
import com.revature.services.ReimbursementService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReimbursementServlet extends HttpServlet {
    private ReimbursementService rServ;
    private ObjectMapper mapper;
    public void init() throws ServletException{
        this.rServ= new ReimbursementService();
        this.mapper=new ObjectMapper();
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getHeader("methodToBeCalled");
        if (req.getHeader("methodToBeCalled").equals("id")){
          Reimbursement reimb= rServ.getReimbursementByID(Integer.parseInt(req.getHeader("id"))).get();
            String json = mapper.writeValueAsString(reimb);
            resp.setContentType("application/json");
            resp.getWriter().print(json);
            resp.setStatus(200);
        }else if(req.getHeader("methodToBeCalled").equals("status")){
            List<Reimbursement> reimbursementList= new ArrayList<>();
            switch(req.getHeader("status")){
                case "PENDING":
                    reimbursementList=rServ.getReimbursementsByStatus(Status.PENDING);
                    break;
                case "APPROVED":
                    reimbursementList=rServ.getReimbursementsByStatus(Status.APPROVED);
                    break;
                case "DENIED":
                    reimbursementList=rServ.getReimbursementsByStatus(Status.DENIED);
                    break;
            }
            for(Reimbursement r: reimbursementList){
                String json = mapper.writeValueAsString(r);
                resp.setContentType("application/json");
                resp.getWriter().print(json);
            }
            resp.setStatus(200);

        }


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Reimbursement reimb =  mapper.readValue(req.getInputStream(), Reimbursement.class);
        reimb = rServ.create(reimb);
        String json = mapper.writeValueAsString(reimb);
        resp.setStatus(201); //status code 201: created says that we have successfully persisted this object
        resp.getWriter().print(json);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Reimbursement reimb =  mapper.readValue(req.getInputStream(), Reimbursement.class);
        reimb = rServ.update(reimb);
        String json = mapper.writeValueAsString(reimb);
        resp.setStatus(201); //status code 201: created says that we have successfully persisted this object
        resp.getWriter().print(json);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        rServ.delete(rServ.getReimbursementByID(Integer.parseInt(req.getHeader("id"))).get());
        resp.setStatus(200);
    }
}

package com.revature.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.exceptions.*;
import com.revature.models.Reimbursement;
import com.revature.models.Role;
import com.revature.models.Status;
import com.revature.services.ReimbursementService;
import com.revature.services.UserService;
import org.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReimbursementServlet extends HttpServlet {
    private ReimbursementService rServ;
    private ObjectMapper mapper;
    private UserService uServ;
    public void init() throws ServletException{
        this.rServ= new ReimbursementService();
        this.mapper=new ObjectMapper();
        this.uServ=new UserService();
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        try {
            if (req.getHeader("rMethodToBeCalled").equals("id")) {
                Reimbursement reimb = rServ.getReimbursementByID(Integer.parseInt(req.getHeader("Id"))).get();

                String json = mapper.writeValueAsString(reimb);
                resp.setContentType("application/json");
                resp.getWriter().print(json);
                resp.setStatus(200);
            } else if (req.getHeader("rMethodToBeCalled").equals("status")) {

                if (uServ.getByUsername(req.getHeader("authToken")).get().getRole() == Role.EMPLOYEE) {

                    switch(req.getHeader("Status")) {
                        case "PENDING":
                            List<Reimbursement> reimbList = rServ.getReimbursementsByStatus(Status.PENDING);
                            ArrayList<Reimbursement> tempList = new ArrayList<Reimbursement>();
                            for(Reimbursement r:reimbList){
                                if(uServ.getByUsername(req.getHeader("authToken")).get().equals(r.getAuthor())){
                                    tempList.add(r);
                                }
                            }
                            JSONArray jsonArray= new JSONArray(tempList);
                            resp.getWriter().print(jsonArray);
                            resp.setStatus(200);
                            break;

                        case "APPROVED":
                            List<Reimbursement> reimbListA = rServ.getReimbursementsByStatus(Status.APPROVED);
                            ArrayList<Reimbursement> tempListA = new ArrayList<Reimbursement>();
                            for(Reimbursement r:reimbListA){
                                if(uServ.getByUsername(req.getHeader("authToken")).get().equals(r.getAuthor())){
                                    tempListA.add(r);
                                }
                            }
                            System.out.println(tempListA);
                            JSONArray jsonArrayA= new JSONArray(tempListA);
                            resp.getWriter().print(jsonArrayA);
                            resp.setStatus(200);
                            break;

                        case "DENIED":
                            List<Reimbursement> reimbListD = rServ.getReimbursementsByStatus(Status.DENIED);
                            ArrayList<Reimbursement> tempListD = new ArrayList<Reimbursement>();
                            for(Reimbursement r:reimbListD){
                                if(uServ.getByUsername(req.getHeader("authToken")).get().equals(r.getAuthor())){
                                    tempListD.add(r);
                                }
                            }
                            JSONArray jsonArrayD= new JSONArray(tempListD);
                            resp.getWriter().print(jsonArrayD);
                            resp.setStatus(200);
                            break;
                    }
                } else if (uServ.getByUsername(req.getHeader("authToken")).get().getRole() == Role.FINANCE_MANAGER) {
                      switch(req.getHeader("Status")){
                          case "PENDING":
                              List<Reimbursement>reimbList= rServ.getReimbursementsByStatus(Status.PENDING);
                              JSONArray jsonArray=new JSONArray(reimbList);
                              resp.getWriter().print(jsonArray);
                              resp.setStatus(200);
                              break;
                          case "APPROVED":
                              List<Reimbursement>reimbListA= rServ.getReimbursementsByStatus(Status.APPROVED);
                              JSONArray jsonArrayA=new JSONArray(reimbListA);
                              resp.getWriter().print(jsonArrayA);
                              resp.setStatus(200);
                              break;
                          case "DENIED":
                              List<Reimbursement>reimbListD= rServ.getReimbursementsByStatus(Status.DENIED);
                              JSONArray jsonArrayD=new JSONArray(reimbListD);
                              resp.getWriter().print(jsonArrayD);
                              resp.setStatus(200);
                              break;
                      }
                }
            }
        }catch(NoSuchReimbursementException e){
            resp.setStatus(404);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Reimbursement reimb =  mapper.readValue(req.getInputStream(), Reimbursement.class);
        try {
            reimb.setAuthor(uServ.getByUsername(req.getHeader("authToken")).get());
            LocalDateTime now= LocalDateTime.now();
            Timestamp today= Timestamp.valueOf(now);
            reimb.setCreationDate(today);
            reimb = rServ.create(reimb);
            String json = mapper.writeValueAsString(reimb);
            resp.setStatus(201); //status code 201: created says that we have successfully persisted this object
            resp.getWriter().print(json);
        }catch(NoSuchUserException r){
            resp.setStatus(404);
        }catch(UnableToCreateReimbursementException e){
            resp.setStatus(409);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Reimbursement tempReimb =  mapper.readValue(req.getInputStream(), Reimbursement.class);
        Reimbursement reimb= rServ.getReimbursementByID(tempReimb.getId()).get();
        try {
            if (req.getHeader("reimbIsBeing").equals("processed")) {
                    reimb.setStatus(tempReimb.getStatus());
                    reimb.setResolver(uServ.getByUsername(req.getHeader("authToken")).get());
                    LocalDateTime now = LocalDateTime.now();
                    Timestamp today = Timestamp.valueOf(now);
                    reimb.setResolutionDate(today);
                    reimb=rServ.update(reimb);
                    String json = mapper.writeValueAsString(reimb);
                    resp.setStatus(200);
                    resp.getWriter().print(json);

            }else if(req.getHeader("reimbIsBeing").equals("updated")) {
                reimb.setAmount(tempReimb.getAmount());
                reimb.setDescription(tempReimb.getDescription());
                reimb.setReimbType(tempReimb.getReimbType());
                reimb = rServ.update(reimb);
                String json = mapper.writeValueAsString(reimb);
                resp.setStatus(200);
                resp.getWriter().print(json);
            }
        }catch(UnableToProcessException e){
            resp.setStatus(400);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Reimbursement reimb = mapper.readValue(req.getInputStream(), Reimbursement.class);

        try {
            rServ.delete(rServ.getReimbursementByID(reimb.getId()).get());
            resp.setStatus(200);
        }catch(UnableToDeleteException e){
            resp.setStatus(409);
        }
    }
}

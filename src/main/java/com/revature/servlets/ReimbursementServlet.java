package com.revature.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.exceptions.*;
import com.revature.models.Reimbursement;
import com.revature.models.Role;
import com.revature.models.Status;
import com.revature.services.ReimbursementService;
import com.revature.services.UserService;

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
        req.getHeader("rMethodToBeCalled");
        try {
            if (req.getHeader("rMethodToBeCalled").equals("id")) {
                Reimbursement reimb = rServ.getReimbursementByID(Integer.parseInt(req.getHeader("id"))).get();
                String json = mapper.writeValueAsString(reimb);
                resp.setContentType("application/json");
                resp.getWriter().print(json);
                resp.setStatus(200);
            } else if (req.getHeader("rMethodToBeCalled").equals("status")) {
                req.getHeader("authToken");
                if (uServ.getByUsername(req.getHeader("authToken")).get().getRole() == Role.EMPLOYEE) {
                    List<Reimbursement> reimbursementListA = new ArrayList<>();
                    List<Reimbursement> reimbursementListD = new ArrayList<>();
                    List<Reimbursement> reimbursementListP = new ArrayList<>();
                    switch (req.getHeader("status")) {
                        case "PENDING":
                            reimbursementListP = rServ.getReimbursementsByStatus(Status.PENDING);
                            for (Reimbursement r : reimbursementListP) {
                                if (r.getAuthor().getId() == uServ.getByUsername(req.getHeader("authToken")).get().getId()) {
                                    String json = mapper.writeValueAsString(r);
                                    resp.setContentType("application/json");
                                    resp.getWriter().print(json);
                                }
                            }
                            resp.setStatus(200);
                            break;
                        case "APPROVED":
                            reimbursementListA = rServ.getReimbursementsByStatus(Status.APPROVED);
                            for (Reimbursement r : reimbursementListA) {
                                if (r.getAuthor().getId() == uServ.getByUsername(req.getHeader("authToken")).get().getId()) {
                                    String json = mapper.writeValueAsString(r);
                                    resp.setContentType("application/json");
                                    resp.getWriter().print(json);
                                }
                            }
                            resp.setStatus(200);
                            break;
                        case "DENIED":
                            reimbursementListD = rServ.getReimbursementsByStatus(Status.DENIED);
                            for (Reimbursement r : reimbursementListD) {
                                if (r.getAuthor().getId() == uServ.getByUsername(req.getHeader("authToken")).get().getId()) {
                                    String json = mapper.writeValueAsString(r);
                                    resp.setContentType("application/json");
                                    resp.getWriter().print(json);
                                }
                            }
                            resp.setStatus(200);
                            break;
                        case "ALL":
                            reimbursementListP = rServ.getReimbursementsByStatus(Status.PENDING);
                            reimbursementListD = rServ.getReimbursementsByStatus(Status.DENIED);
                            reimbursementListA = rServ.getReimbursementsByStatus(Status.APPROVED);
                            for (Reimbursement r : reimbursementListP) {
                                if (r.getAuthor().getId() == uServ.getByUsername(req.getHeader("authToken")).get().getId()) {
                                    String json = mapper.writeValueAsString(r);
                                    resp.setContentType("application/json");
                                    resp.getWriter().print(json);
                                }
                            }
                            for (Reimbursement r : reimbursementListA) {
                                if (r.getAuthor().getId() == uServ.getByUsername(req.getHeader("authToken")).get().getId()) {
                                    String json = mapper.writeValueAsString(r);
                                    resp.setContentType("application/json");
                                    resp.getWriter().print(json);
                                }
                            }
                            for (Reimbursement r : reimbursementListD) {
                                if (r.getAuthor().getId() == uServ.getByUsername(req.getHeader("authToken")).get().getId()) {
                                    String json = mapper.writeValueAsString(r);
                                    resp.setContentType("application/json");
                                    resp.getWriter().print(json);
                                }
                            }
                            resp.setStatus(200);
                            break;
                    }
                } else if (uServ.getByUsername(req.getHeader("authToken")).get().getRole() == Role.FINANCE_MANAGER) {
                    List<Reimbursement> reimbursementListA = new ArrayList<>();
                    List<Reimbursement> reimbursementListD = new ArrayList<>();
                    List<Reimbursement> reimbursementListP = new ArrayList<>();
                    switch (req.getHeader("status")) {
                        case "PENDING":
                            reimbursementListP = rServ.getReimbursementsByStatus(Status.PENDING);
                            for (Reimbursement r : reimbursementListP) {
                                String json = mapper.writeValueAsString(r);
                                resp.setContentType("application/json");
                                resp.getWriter().print(json);
                            }
                            resp.setStatus(200);
                            break;
                        case "APPROVED":
                            reimbursementListA = rServ.getReimbursementsByStatus(Status.APPROVED);
                            for (Reimbursement r : reimbursementListA) {
                                String json = mapper.writeValueAsString(r);
                                resp.setContentType("application/json");
                                resp.getWriter().print(json);

                            }
                            resp.setStatus(200);
                            break;
                        case "DENIED":
                            reimbursementListD = rServ.getReimbursementsByStatus(Status.DENIED);
                            for (Reimbursement r : reimbursementListD) {
                                String json = mapper.writeValueAsString(r);
                                resp.setContentType("application/json");
                                resp.getWriter().print(json);

                            }
                            resp.setStatus(200);
                            break;
                        case "ALL":
                            reimbursementListP = rServ.getReimbursementsByStatus(Status.PENDING);
                            reimbursementListD = rServ.getReimbursementsByStatus(Status.DENIED);
                            reimbursementListA = rServ.getReimbursementsByStatus(Status.APPROVED);
                            for (Reimbursement r : reimbursementListP) {
                                String json = mapper.writeValueAsString(r);
                                resp.setContentType("application/json");
                                resp.getWriter().print(json);

                            }
                            for (Reimbursement r : reimbursementListA) {
                                String json = mapper.writeValueAsString(r);
                                resp.setContentType("application/json");
                                resp.getWriter().print(json);

                            }
                            for (Reimbursement r : reimbursementListD) {
                                String json = mapper.writeValueAsString(r);
                                resp.setContentType("application/json");
                                resp.getWriter().print(json);

                            }
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
        Reimbursement reimb =  mapper.readValue(req.getInputStream(), Reimbursement.class);
        try {
            if (req.getHeader("reimbIsBeing").equals("processed")) {
                switch(req.getHeader("decision")){
                    case "DENIED":
                        reimb=rServ.process(reimb, Status.DENIED, uServ.getByUsername(req.getHeader("authToken")).get());
                        String json = mapper.writeValueAsString(reimb);
                        resp.setStatus(200);
                        resp.getWriter().print(json);
                        break;
                    case "APPROVED":
                        reimb=rServ.process(reimb, Status.APPROVED, uServ.getByUsername(req.getHeader("authToken")).get());
                        String json1 = mapper.writeValueAsString(reimb);
                        resp.setStatus(200);
                        resp.getWriter().print(json1);
                        break;
                    }
            }else if(req.getHeader("reimbIsBeing").equals("updated")) {
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
        try {
            rServ.delete(rServ.getReimbursementByID(Integer.parseInt(req.getHeader("id"))).get());
            resp.setStatus(200);
        }catch(UnableToDeleteException e){
            resp.setStatus(409);
        }
    }
}

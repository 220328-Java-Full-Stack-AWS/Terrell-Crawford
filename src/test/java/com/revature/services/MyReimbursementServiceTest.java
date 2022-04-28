package com.revature.services;

import com.revature.models.Reimbursement;
import com.revature.models.Role;
import com.revature.models.Status;
import com.revature.models.User;
import com.revature.repositories.ReimbursementDAO;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class MyReimbursementServiceTest {
    static ReimbursementService rServ;
    static ReimbursementDAO rDAO = mock(ReimbursementDAO.class);
    private Reimbursement REIMBURSEMENT_TO_PROCESS;
    private Reimbursement GENERIC_REIMBURSEMENT_1;
    private Reimbursement GENERIC_REIMBURSEMENT_2;
    private List<Reimbursement> GENERIC_ALL_PENDING_REIMBURSEMENTS;
    private User GENERIC_EMPLOYEE_1;
    private User GENERIC_FINANCE_MANAGER_1;

    @BeforeClass
    public static void beforeSetup()throws Exception{
        rServ=new ReimbursementService();
    }

    public void runThisBeforeEachTest()throws Exception{
        GENERIC_EMPLOYEE_1 = new User(1, "genericEmployee1", "genericPassword", Role.EMPLOYEE);
        GENERIC_FINANCE_MANAGER_1 = new User(1, "genericManager1", "genericPassword", Role.FINANCE_MANAGER);

        REIMBURSEMENT_TO_PROCESS = new Reimbursement(2, Status.PENDING, GENERIC_EMPLOYEE_1, null, 150.00);

        GENERIC_REIMBURSEMENT_1 = new Reimbursement(1, Status.PENDING, GENERIC_EMPLOYEE_1, null, 100.00);
        GENERIC_REIMBURSEMENT_2 = new Reimbursement(2, Status.APPROVED, GENERIC_EMPLOYEE_1, GENERIC_FINANCE_MANAGER_1, 150.00);

        GENERIC_ALL_PENDING_REIMBURSEMENTS = new ArrayList<Reimbursement>();
        GENERIC_ALL_PENDING_REIMBURSEMENTS.add(GENERIC_REIMBURSEMENT_1);
    }

    @Test
    public void testCreate(){

    }
    @Test
    public void testGetByStatus(){
        rServ.setReimbDAO(rDAO);
        when(rDAO.getByStatus(any())).thenReturn(GENERIC_ALL_PENDING_REIMBURSEMENTS);
        assertEquals(GENERIC_ALL_PENDING_REIMBURSEMENTS, rServ.getReimbursementsByStatus(Status.PENDING));

        verify(rDAO).getByStatus(Status.PENDING);
    }



}

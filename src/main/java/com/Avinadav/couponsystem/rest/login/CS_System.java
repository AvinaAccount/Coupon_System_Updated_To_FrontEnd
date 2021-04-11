package com.Avinadav.couponsystem.rest.login;

import com.Avinadav.couponsystem.data.repo.AdminRepository;
import com.Avinadav.couponsystem.data.repo.CompanyRepository;
import com.Avinadav.couponsystem.data.repo.CustomerRepository;
import com.Avinadav.couponsystem.entity.Admin;
import com.Avinadav.couponsystem.entity.Company;
import com.Avinadav.couponsystem.entity.Customer;
import com.Avinadav.couponsystem.rest.ex.InvalidLoginException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * This class create the 'ClientSession' by the type of the client (Admin/Company/Customer).
 */

@Service
public class CS_System {
    private final AdminRepository adminRepo;
    private final CompanyRepository companyRepo;
    private final CustomerRepository customerRepo;

    @Autowired
    private CS_System(AdminRepository adminRepo, CompanyRepository companyRepo, CustomerRepository customerRepo) {
        this.adminRepo = adminRepo;
        this.companyRepo = companyRepo;
        this.customerRepo = customerRepo;
    }

    public ClientSession createSession(String email, String password, String type) throws InvalidLoginException {

        switch (type) {
            case "admin":
                Optional<Admin> optAdmin = adminRepo.findAdminByEmailAndPassword(email, password);
                if (optAdmin.isPresent()) {
                    return ClientSession.create(optAdmin.get().getId());
                }
            case "company":
                Optional<Company> optCompany = companyRepo.findCompanyByEmail(email);
                if (optCompany.isPresent()) {
                    return ClientSession.create(optCompany.get().getId());
                }
            case "customer":
                Optional<Customer> optCustomer = customerRepo.findCustomerByEmailAndPassword(email, password);
                if (optCustomer.isPresent()) {
                    return ClientSession.create(optCustomer.get().getId());
                }
            default:
                throw new InvalidLoginException("Unable to login with the provided details!");
        }
    }
}

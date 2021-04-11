package com.Avinadav.couponsystem.rest.controller;

import com.Avinadav.couponsystem.data.repo.CouponRepository;
import com.Avinadav.couponsystem.entity.Company;
import com.Avinadav.couponsystem.entity.Coupon;
import com.Avinadav.couponsystem.entity.Customer;
import com.Avinadav.couponsystem.rest.ex.*;
import com.Avinadav.couponsystem.rest.login.ClientSession;
import com.Avinadav.couponsystem.service.CompanyService;
import com.Avinadav.couponsystem.service.CustomerService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api")
@CrossOrigin
public class AdminController {
    private final CompanyService companyService;
    private final CustomerService customerService;
    private final CouponRepository couponRepo;
    private final Map<String, ClientSession> tokensMap;

    public AdminController(CompanyService companyService,
                           CustomerService customerService,
                           CouponRepository couponRepo,
                           @Qualifier("tokens") Map<String, ClientSession> tokensMap) {

        this.companyService = companyService;
        this.customerService = customerService;
        this.couponRepo = couponRepo;
        this.tokensMap = tokensMap;
    }

    /*-> Company <-*/

    @PostMapping("/admin/company/create_company")
    public ResponseEntity<Company> create(
            @RequestParam String token,
            @RequestBody Company company)
            throws CreateException, InvalidLoginException {

        if (!accessPermission(token)) {
            throw new InvalidLoginException("Login time has expired or Incorrect login details - Try to login again ");
        }

        ClientSession session = tokensMap.get(token);
        session.access();

        company.setId(0);

        Optional<Company> optCompany = companyService.createCompany(company);
        if (optCompany.isPresent()) {
            return ResponseEntity.ok(optCompany.get());
        }
        throw new CreateException("Error during create the company! ");
    }

    @DeleteMapping("/admin/company/remove")
    public ResponseEntity<String> remove(
            @RequestParam String token,
            @RequestParam long companyId)
            throws RemoveException, InvalidLoginException {

        if (!accessPermission(token)) {
            throw new InvalidLoginException("Login time has expired or Incorrect login details - Try to login again ");
        }

        ClientSession session = tokensMap.get(token);
        session.access();

        Optional<Company> optCompany = companyService.getCompany(companyId);
        if (optCompany.isPresent()) {
            List<Coupon> coupons = optCompany.get().getCoupons();
            for (Coupon coupon : coupons) {
                coupons.remove(coupon);
            }
            companyService.removeCompany(companyId);
            return ResponseEntity.ok("The company with ID: " + companyId + " was removed from the DB.");
        }
        throw new RemoveException("Error during delete the company!");
    }

    @PostMapping("/admin/company/update")
    public ResponseEntity<Company> update(
            @RequestParam String token,
            @RequestBody Company company)
            throws UpdateException, InvalidLoginException {

        if (!accessPermission(token)) {
            throw new InvalidLoginException("Login time has expired or Incorrect login details - Try to login again ");
        }

        ClientSession session = tokensMap.get(token);
        session.access();

        Optional<Company> optCompany = companyService.getCompany(company.getId());
        if (optCompany.isPresent()) {
            Optional<Company> updatedCompany = companyService.updateCompany(company);
            if (updatedCompany.isPresent()) {
                return ResponseEntity.ok(updatedCompany.get());
            }
        }
        throw new UpdateException("Error trying update the company!");
    }

    @GetMapping("/admin/company/get_all")
    public ResponseEntity<List<Company>> getAllCompanies(
            @RequestParam String token)
            throws FetchException, InvalidLoginException {

        if (!accessPermission(token)) {
            throw new InvalidLoginException("Login time has expired or Incorrect login details - Try to login again ");
        }

        ClientSession session = tokensMap.get(token);
        session.access();

        Optional<List<Company>> optCompanies = companyService.getAllCompanies();
        if (optCompanies.isPresent()) {
            return ResponseEntity.ok(optCompanies.get());
        }
        throw new FetchException("Error during fetching the companies!");
    }

    @GetMapping("/admin/company/get_company")
    public ResponseEntity<Company> getCompany(
            @RequestParam String token,
            @RequestParam long companyId)
            throws FetchException, InvalidLoginException {

        if (!accessPermission(token)) {
            throw new InvalidLoginException("Login time has expired or Incorrect login details - Try to login again ");
        }

        ClientSession session = tokensMap.get(token);
        session.access();

        Optional<Company> optCompany = companyService.getCompany(companyId);
        if (optCompany.isPresent()) {
            return ResponseEntity.ok(optCompany.get());
        }
        throw new FetchException("Error during fetching the company!");
    }


    /*-> Customer <-*/
    @PostMapping("/admin/customers/create_customer")
    public ResponseEntity<Customer> createCustomer(
            @RequestParam String token,
            @RequestBody Customer customer)
            throws CreateException, InvalidLoginException {

        if (!accessPermission(token)) {
            throw new InvalidLoginException("Login time has expired or Incorrect login details - Try to login again ");
        }

        ClientSession session = tokensMap.get(token);
        session.access();

        customer.setId(0);
        Optional<Customer> optCustomer = customerService.createCustomer(customer);
        if (optCustomer.isPresent()) {
            return ResponseEntity.ok(optCustomer.get());
        }
        throw new CreateException("Error during create the customer!");
    }

    @PostMapping("/admin/customers/update_customer")
    public ResponseEntity<Customer> updateCustomer(
            @RequestParam String token,
            @RequestBody Customer customer)
            throws UpdateException, InvalidLoginException {

        if (!accessPermission(token)) {
            throw new InvalidLoginException("Login time has expired or Incorrect login details - Try to login again ");
        }

        ClientSession session = tokensMap.get(token);
        session.access();

        Optional<Customer> optCustomer = customerService.findCustomerById(customer.getId());
        /*Check if customer is in the DB*/
        if (optCustomer.isPresent()) {
            Optional<Customer> updateCustomer = customerService.updateCustomer(customer);
            /*Check if the received customer is not Empty*/
            if (updateCustomer.isPresent()) {
                return ResponseEntity.ok(updateCustomer.get());
            }
        }
        throw new UpdateException("Error trying update the customer!");
    }

    @DeleteMapping("/admin/customers/delete_customer")
    public String removeCustomer(
            @RequestParam String token,
            @RequestParam long customerId)
            throws RemoveException, InvalidLoginException {

        if (!accessPermission(token)) {
            throw new InvalidLoginException("Login time has expired or Incorrect login details - Try to login again ");
        }

        ClientSession session = tokensMap.get(token);
        session.access();

        Optional<Customer> optCustomer = customerService.findCustomerById(customerId);
        if (optCustomer.isPresent()) {
            customerService.removeCustomer(customerId);
            return "Customer with ID: " + customerId + " remove from the DB!";
        }
        throw new RemoveException("Error trying delete the customer!");
    }

    @GetMapping("/admin/customers/get_all_customers")
    public ResponseEntity<List<Customer>> getAllCustomers(
            @RequestParam String token)
            throws FetchException, InvalidLoginException {

        if (!accessPermission(token)) {
            throw new InvalidLoginException("Login time has expired or Incorrect login details - Try to login again ");
        }

        ClientSession session = tokensMap.get(token);
        session.access();

        Optional<List<Customer>> allCustomer = customerService.getAllCustomer();
        if (allCustomer.isPresent()) {
            List<Customer> customers = allCustomer.get();
            return ResponseEntity.ok(customers);
        }
        throw new FetchException("Error during fetching the customers!");
    }

    @GetMapping("/admin/customers/getCustomer")
    public ResponseEntity<Customer> getCustomer(
            @RequestParam String token,
            @RequestParam long customerId)
            throws FetchException, InvalidLoginException {

        if (!accessPermission(token)) {
            throw new InvalidLoginException("Login time has expired or Incorrect login details - Try to login again ");
        }

        ClientSession session = tokensMap.get(token);
        session.access();

        Optional<Customer> optCustomer = customerService.findCustomerById(customerId);
        if (optCustomer.isPresent()) {
            return ResponseEntity.ok(optCustomer.get());
        }
        throw new FetchException("Error during fetching the customer!");
    }


    /*-> Coupons <-*/

    @GetMapping("/admin/company/get_company_coupons")
    public ResponseEntity<List<Coupon>> getCompanyCoupons(
            @RequestParam String token,
            @RequestParam long companyId)
            throws FetchException, InvalidLoginException {

        if (!accessPermission(token)) {
            throw new InvalidLoginException("Login time has expired or Incorrect login details - Try to login again ");
        }

        ClientSession session = tokensMap.get(token);
        session.access();

        Optional<List<Coupon>> optCoupons = companyService.getAllCompanyCoupons(companyId);
        if (optCoupons.isPresent()) {
            return ResponseEntity.ok(optCoupons.get());
        }
        throw new FetchException("Error during fetching the coupons!");
    }

    @GetMapping("/admin/company/get_coupon")
    public ResponseEntity<Coupon> getCoupon(
            @RequestParam String token,
            @RequestParam long companyId,
            @RequestParam long couponId)
            throws FetchException, InvalidLoginException {

        if (!accessPermission(token)) {
            throw new InvalidLoginException("Login time has expired or Incorrect login details - Try to login again ");
        }

        ClientSession session = tokensMap.get(token);
        session.access();

        Optional<Coupon> optCoupon = companyService.getCoupon(companyId, couponId);
        if (optCoupon.isPresent()) {
            return ResponseEntity.ok(optCoupon.get());
        }
        throw new FetchException("Error during fetching the coupon!");
    }

    @PostMapping("/admin/company/update_coupon")
    public ResponseEntity<Coupon> updateCoupon(
            @RequestParam String token,
            @RequestBody Coupon coupon)
            throws UpdateException, InvalidLoginException {

        if (!accessPermission(token)) {
            throw new InvalidLoginException("Login time has expired or Incorrect login details - Try to login again ");
        }

        ClientSession session = tokensMap.get(token);
        session.access();

        Optional<Coupon> optCoupon = couponRepo.findById(coupon.getId());
        if (optCoupon.isPresent()) {
            companyService.updateCoupon(coupon);
            return ResponseEntity.ok(optCoupon.get());
        }
        throw new UpdateException("Error trying update the coupon!");
    }

    @DeleteMapping("/admin/company/remove_coupon")
    public ResponseEntity<String> removeCoupon(
            @RequestParam String token,
            @RequestParam long couponId)
            throws RemoveException, InvalidLoginException {

        if (!accessPermission(token)) {
            throw new InvalidLoginException("Login time has expired or Incorrect login details - Try to login again ");
        }

        ClientSession session = tokensMap.get(token);
        session.access();

        if (couponRepo.findById(couponId).isPresent()) {
            companyService.removeCoupon(couponId);
            return ResponseEntity.ok("Coupon removed!");
        }
        throw new RemoveException("Error trying remove the coupon!");
    }

    @PostMapping("/admin/company/create_coupon")
    public ResponseEntity<Coupon> createCoupon(
            @RequestParam String token,
            @RequestBody Coupon coupon)
            throws CreateException, InvalidLoginException {

        if (!accessPermission(token)) {
            throw new InvalidLoginException("Login time has expired or Incorrect login details - Try to login again ");
        }

        ClientSession session = tokensMap.get(token);
        session.access();

        coupon.setId(0);
        Optional<Coupon> optCoupon = companyService.createCoupon(coupon);
        if (optCoupon.isPresent()) {
            return ResponseEntity.ok(optCoupon.get());
        }
        throw new CreateException("Error during create the coupon!");
    }

    @GetMapping("/admin/company/coupon_by_category")
    public ResponseEntity<List<Coupon>> getCouponByCategory(
            @RequestParam String token,
            @RequestParam long companyId,
            @RequestParam int category)
            throws FetchException, InvalidLoginException {

        if (!accessPermission(token)) {
            throw new InvalidLoginException("Login time has expired or Incorrect login details - Try to login again ");
        }

        ClientSession session = tokensMap.get(token);
        session.access();

        Optional<List<Coupon>> optCoupon = companyService.getCouponByCategory(companyId, category);
        if (optCoupon.isPresent()) {
            return ResponseEntity.ok(optCoupon.get());
        }
        throw new FetchException("Error during fetching the coupon!");
    }

    @GetMapping("/admin/customers/get_customer_coupons")
    public ResponseEntity<List<Coupon>> getCoupons(
            @RequestParam String token,
            @RequestParam long customerId)
            throws FetchException, InvalidLoginException {

        if (!accessPermission(token)) {
            throw new InvalidLoginException("Login time has expired or Incorrect login details - Try to login again ");
        }

        ClientSession session = tokensMap.get(token);
        session.access();

        Optional<Customer> customer = customerService.findCustomerById(customerId);

        if (customer.isPresent()) {
            Optional<List<Coupon>> coupons = customerService.getCoupons(customerId);
            if (coupons.isPresent()) {
                return ResponseEntity.ok(coupons.get());
            }
        }
        throw new FetchException("Error during fetching the coupons!");

    }


    /**
     * Global function
     * This Global function serves the site in general
     * (Includes users who are not registered with the system)
     * The purpose of its dedicated function is to present coupons to existing customers
     * and mainly to attract customers who are not registered with the system (without token!)
     */


    @GetMapping("/admin/get_all_coupons")
    public ResponseEntity<List<Coupon>> getAllCoupons() throws InvalidLoginException, FetchException {


        List<Coupon> allCoupons = couponRepo.findAll();
        Optional<List<Coupon>> coupons = Optional.of(allCoupons);
        if (coupons.isPresent()) {
            return ResponseEntity.ok(coupons.get());
        }

        throw new FetchException("Error during fetching the coupons!");

    }

    private boolean accessPermission(String token) {
        return token.length() == 15 && tokensMap.containsKey(token);
    }



}

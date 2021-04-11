package com.Avinadav.couponsystem.rest.controller;

import com.Avinadav.couponsystem.data.repo.CouponRepository;
import com.Avinadav.couponsystem.data.repo.CustomerRepository;
import com.Avinadav.couponsystem.entity.Coupon;
import com.Avinadav.couponsystem.entity.Customer;
import com.Avinadav.couponsystem.rest.ex.*;
import com.Avinadav.couponsystem.rest.login.ClientSession;
import com.Avinadav.couponsystem.service.CustomerService;
import com.Avinadav.couponsystem.service.CustomerServicePerform;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class CustomerController {
    private final CustomerService customerService;
    private final CouponRepository couponRepo;


    private final Map<String, ClientSession> tokensMap;


    /* Save the polymorphism (because i have only one interface of customer ,i don't need to @Qualifier the constructor.*/
    public CustomerController(CustomerServicePerform customerService,
                              CouponRepository couponRepo,
                              CustomerRepository customerRepo, @Qualifier("tokens") Map<String, ClientSession> tokensMap) {

        this.customerService = customerService;
        this.couponRepo = couponRepo;
        this.tokensMap = tokensMap;
    }

    @PostMapping("/customers/purchase_coupon")
    public ResponseEntity<Coupon> purchaseCoupon(@RequestParam String token,
                                                 @RequestParam long couponId)
            throws InvalidLoginException, PurchaseCouponException {

        if (!accessPermission(token)) {
            throw new InvalidLoginException("Login time has expired or Incorrect login details - Try to login again ");
        }

        ClientSession session = tokensMap.get(token);
        session.access();

        long customerId = session.getClientId();
        Optional<Customer> optCustomer = customerService.findCustomerById(customerId);
        Optional<Coupon> optCoupon = couponRepo.findById(couponId);


        if (optCustomer.isPresent() && optCoupon.isPresent()) {
            /* No need to check again if the coupon is present */

            Coupon coupon = customerService.addCoupon(customerId, couponId).get();
            return ResponseEntity.ok(coupon);
        }
        throw new PurchaseCouponException("Error trying purchase coupon with ID : " + couponId);
    }

    @DeleteMapping("/customers/remove_coupon")
    public ResponseEntity<String> removeCoupon(@RequestParam String token,
                                               @RequestParam long couponId)
            throws InvalidLoginException, RemoveException {


        if (!accessPermission(token)) {
            throw new InvalidLoginException("Login time has expired or Incorrect login details - Try to login again ");
        }

        ClientSession session = tokensMap.get(token);
        session.access();

        long customerId = tokensMap.get(token).getClientId();
        Optional<Customer> optCustomer = customerService.findCustomerById(customerId);

        if (optCustomer.isPresent()) {
            Customer customer = optCustomer.get();
            List<Coupon> coupons = customer.getCoupons();
            List<Coupon> updatedList = coupons.stream()
                    .filter(coupon -> coupon.getId() != couponId)
                    .collect(Collectors.toList());
            customer.setCoupons(updatedList);

            /*Critical to save the customer with the new coupons ON THE DB!*/
            customerService.updateCustomer(customer);
            return ResponseEntity.ok("The coupon/s removed!");
        }
        throw new RemoveException("Unable to remove coupon/s!");
    }

    @GetMapping("/customers/get_customer_coupons")
    public ResponseEntity<List<Coupon>> getCoupons(@RequestParam String token)
            throws InvalidLoginException, FetchException {

        if (!accessPermission(token)) {
            throw new InvalidLoginException("Login time has expired or Incorrect login details - Try to login again ");
        }

        ClientSession session = tokensMap.get(token);
        session.access();

        long customerId = session.getClientId();
        Optional<Customer> customer = customerService.findCustomerById(customerId);

        if (customer.isPresent()) {
            Optional<List<Coupon>> coupons = customerService.getCoupons(customerId);
            if (coupons.isPresent()) {
                return ResponseEntity.ok(coupons.get());
            }
        }
        throw new FetchException("Error during fetching the companies!");

    }

    @GetMapping("/customers/CouponsLassThanPrice")
    public ResponseEntity<List<Coupon>> getCouponsLassThanPrice(@RequestParam String token,
                                                                @RequestParam double price)
            throws InvalidLoginException, FetchException {

        if (!accessPermission(token)) {
            throw new InvalidLoginException("Login time has expired or Incorrect login details - Try to login again ");
        }

        ClientSession session = tokensMap.get(token);
        session.access();


        long customerId = session.getClientId();
        Optional<Customer> customer = customerService.findCustomerById(customerId);
        if (customer.isPresent()) {
            Optional<List<Coupon>> couponList = customerService.findCouponsLassThanPrice(customerId, price);
            if (couponList.isPresent()) {
                return ResponseEntity.ok(couponList.get());
            }
        }
        throw new FetchException("Error during fetching the companies!");
    }

    @GetMapping("/customers/before_date")
    public ResponseEntity<List<Coupon>> findAllCouponBeforeDate(@RequestParam String token,
                                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                                @RequestParam LocalDate date)
            throws InvalidLoginException, FetchException {

        if (!accessPermission(token)) {
            throw new InvalidLoginException("Login time has expired or Incorrect login details - Try to login again ");
        }

        ClientSession session = tokensMap.get(token);
        session.access();

        long customerId = session.getClientId();
        Optional<List<Coupon>> coupons = customerService.findAllCouponBeforeDate(customerId, date);
        if (coupons.isPresent()) {
            return ResponseEntity.ok(coupons.get());
        }
        throw new FetchException("Error during fetching the companies!");
    }

    @GetMapping("/customers/coupons_by_category")
    public ResponseEntity<List<Coupon>> getCouponsByCategory(@RequestParam String token,
                                                             @RequestParam int category)
            throws InvalidLoginException, FetchException {

        if (!accessPermission(token)) {
            throw new InvalidLoginException("Login time has expired or Incorrect login details - Try to login again ");
        }

        ClientSession session = tokensMap.get(token);
        session.access();


        long customerId = session.getClientId();
        Optional<Customer> optCustomer = customerService.findCustomerById(customerId);
        if (optCustomer.isPresent()) {
            Optional<List<Coupon>> coupons = customerService.getAllCouponsByCategory(customerId, category);
            if (coupons.isPresent()) {
                return ResponseEntity.ok(coupons.get());
            }
        }
        throw new FetchException("Error during fetching the companies!");
    }


    @GetMapping("/customers/get_customer")
    public ResponseEntity<Customer> getCustomer(
            @RequestParam String token)

            throws FetchException, InvalidLoginException {

        if (!accessPermission(token)) {
            throw new InvalidLoginException("Login time has expired or Incorrect login details - Try to login again ");
        }

        ClientSession session = tokensMap.get(token);
        session.access();
        long customerId = session.getClientId();

        Optional<Customer> optCustomer = customerService.findCustomerById(customerId);
        if (optCustomer.isPresent()) {
            return ResponseEntity.ok(optCustomer.get());
        }
        throw new FetchException("Error during fetching the customer!");
    }


    @PostMapping("/customers/update_customer")
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


    private boolean accessPermission(String token) {
        return token.length() == 5 && tokensMap.containsKey(token);
    }


}

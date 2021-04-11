package com.Avinadav.couponsystem.rest.controller;

import com.Avinadav.couponsystem.data.repo.CouponRepository;
import com.Avinadav.couponsystem.entity.Company;
import com.Avinadav.couponsystem.entity.Coupon;
import com.Avinadav.couponsystem.rest.ex.*;
import com.Avinadav.couponsystem.rest.login.ClientSession;
import com.Avinadav.couponsystem.service.CompanyService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class CompanyController {

    private final CompanyService companyService;
    private final CouponRepository couponRepo;
    private final Map<String, ClientSession> tokensMap;

    public CompanyController(CompanyService companyService,
                             CouponRepository couponRepo,
                             @Qualifier("tokens") Map<String, ClientSession> tokensMap) {

        this.companyService = companyService;
        this.couponRepo = couponRepo;
        this.tokensMap = tokensMap;
    }

    @GetMapping("/company/coupon_by_category")
    public ResponseEntity<List<Coupon>> getCouponByCategory(@RequestParam String token,
                                                            @RequestParam int category)
            throws FetchException, InvalidLoginException {

        if (!accessPermission(token)) {
            throw new InvalidLoginException("Login time has expired or Incorrect login details - Try to login again ");
        }

        ClientSession session = tokensMap.get(token);
        session.access();

        long companyId = session.getClientId();
        Optional<List<Coupon>> optCoupon = companyService.getCouponByCategory(companyId, category);
        if (optCoupon.isPresent()) {
            return ResponseEntity.ok(optCoupon.get());
        }
        throw new FetchException("Error during fetching the company!");
    }

    @GetMapping("/company/get_company_coupons")
    public ResponseEntity<List<Coupon>> getCompanyCoupons(@RequestParam String token)
            throws InvalidLoginException, FetchException {

        if (!accessPermission(token)) {
            throw new InvalidLoginException("Login time has expired or Incorrect login details - Try to login again ");
        }

        ClientSession session = tokensMap.get(token);
        session.access();

        long companyId = session.getClientId();

        Optional<List<Coupon>> optCoupons = companyService.getAllCompanyCoupons(companyId);
        if (optCoupons.isPresent()) {
            return ResponseEntity.ok(optCoupons.get());
        }
        throw new FetchException("Error during fetching the company coupons!");
    }

    @PostMapping("/company/create_coupon")
    public ResponseEntity<Coupon> createCoupon(@RequestParam String token,
                                               @RequestBody Coupon coupon)
            throws InvalidLoginException, CreateException {

        if (!accessPermission(token)) {
            throw new InvalidLoginException("Login time has expired or Incorrect login details - Try to login again ");
        }

        ClientSession session = tokensMap.get(token);
        session.access();

        long companyId = session.getClientId();
        coupon.setId(0);
        Optional<Company> company = companyService.getCompany(companyId);
        if (company.isPresent()) {
            coupon.setCompany(company.get());
            companyService.createCoupon(coupon);
            return ResponseEntity.ok(coupon);
        }
        throw new CreateException("Unable to create new coupon!");
    }

    @PostMapping("/company/remove_coupon")
    public ResponseEntity<String> removeCoupon(@RequestParam String token,
                                               @RequestParam long couponId)
            throws InvalidLoginException, RemoveException {

        if (!accessPermission(token)) {
            throw new InvalidLoginException("Login time has expired or Incorrect login details - Try to login again ");
        }

        ClientSession session = tokensMap.get(token);
        session.access();

        if (couponRepo.findById(couponId).isPresent()) {
            companyService.removeCoupon(couponId);
            return ResponseEntity.ok("Remove complete!");
        }
        throw new RemoveException("Error during delete the company!");
    }

    @PostMapping("/company/update_coupon")
    public ResponseEntity<Coupon> updateCoupon(@RequestParam String token,
                                               @RequestBody Coupon coupon)
            throws InvalidLoginException, UpdateException {

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

    @GetMapping("/company/coupons_lower_than_price")
    public ResponseEntity<List<Coupon>> getCouponsLowerThanPrice(@RequestParam String token,
                                                                 @RequestParam double price)
            throws InvalidLoginException, FetchException {

        if (!accessPermission(token)) {
            throw new InvalidLoginException("Login time has expired or Incorrect login details - Try to login again ");
        }

        ClientSession session = tokensMap.get(token);
        session.access();

        long companyId = session.getClientId();
        Optional<Company> company = companyService.getCompany(companyId);
        if (company.isPresent()) {
            Optional<List<Coupon>> coupons = companyService.couponsLowerThanPrice(companyId, price);
            if (coupons.isPresent()) {
                return ResponseEntity.ok(coupons.get());
            }
        }
        throw new FetchException("Error during fetching the coupons!");
    }

    @GetMapping("/company/get_coupon")
    public ResponseEntity<Coupon> getCoupon(@RequestParam String token,
                                            @RequestParam long couponId)
            throws InvalidLoginException, FetchException {

        if (!accessPermission(token)) {
            throw new InvalidLoginException("Login time has expired or Incorrect login details - Try to login again ");
        }

        ClientSession session = tokensMap.get(token);
        session.access();

        long companyId = session.getClientId();
        Optional<Coupon> optCoupon = companyService.getCoupon(companyId, couponId);
        if (optCoupon.isPresent()) {
            return ResponseEntity.ok(optCoupon.get());
        }
        throw new FetchException("Error during fetching the company!");
    }


    @GetMapping("/company/get_company")
    public ResponseEntity<Company> getCompanyDetails(
            @RequestParam String token)
            throws FetchException, InvalidLoginException {

        if (!accessPermission(token)) {
            throw new InvalidLoginException("Login time has expired or Incorrect login details - Try to login again ");
        }

        ClientSession session = tokensMap.get(token);
        long companyId = session.getClientId();
        session.access();

        Optional<Company> optCompany = companyService.getCompany(companyId);
        if (optCompany.isPresent()) {
            return ResponseEntity.ok(optCompany.get());
        }
        throw new FetchException("Error during fetching the company!");
    }

    @PostMapping("/company/update")
    public ResponseEntity<Company> updateCompanyDetails(
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

    private boolean accessPermission(String token) {
        return token.length() == 10 && tokensMap.containsKey(token);
    }

}

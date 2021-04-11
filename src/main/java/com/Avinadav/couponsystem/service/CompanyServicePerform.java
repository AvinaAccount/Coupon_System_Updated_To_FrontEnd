package com.Avinadav.couponsystem.service;

import com.Avinadav.couponsystem.data.repo.CompanyRepository;
import com.Avinadav.couponsystem.data.repo.CouponRepository;
import com.Avinadav.couponsystem.entity.Company;
import com.Avinadav.couponsystem.entity.Coupon;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyServicePerform implements CompanyService {

    private final CompanyRepository companyRepo;
    private final CouponRepository couponRepo;

    public CompanyServicePerform(CompanyRepository companyRepo, CouponRepository couponRepo) {
        this.companyRepo = companyRepo;
        this.couponRepo = couponRepo;
    }

    @Override
    public Optional<Company> getCompany(long id) {
        return companyRepo.findById(id);
    }

    @Override
    public Optional<Company> createCompany(Company company) {
        Company newCompany = companyRepo.save(company);
        return Optional.of(newCompany);
    }

    @Override
    public void removeCompany(long id) {
        companyRepo.deleteById(id);
    }

    @Override
    public Optional<Company> updateCompany(Company company) {
        Company updatedCompany = companyRepo.save(company);
        return Optional.of(updatedCompany);
    }

    @Override
    public Optional<List<Company>> getAllCompanies() {
        List<Company> companies = companyRepo.findAll();
        return Optional.of(companies);
    }

    @Override
    public Optional<List<Coupon>> getAllCompanyCoupons(long companyId) {
        return couponRepo.findByCompanyId(companyId);
    }

    @Override
    public Optional<List<Coupon>> getCouponByCategory(long companyId, int category) {
        return couponRepo.findCouponByCategory(companyId, category);
    }
    public Optional<Coupon> createCoupon(Coupon coupon){
        Coupon newCoupon = couponRepo.save(coupon);
        return Optional.of(newCoupon);
    }

    @Override
    public void removeCoupon(long couponId) {
        couponRepo.deleteById(couponId);
    }

    @Override
    public Optional<Coupon> updateCoupon(Coupon coupon) {
        Coupon fetchCoupon = couponRepo.save(coupon);
        return Optional.of(fetchCoupon);

    }

    @Override
    public Optional<List<Coupon>> couponsLowerThanPrice(long companyId, double price) {
        return couponRepo.findCompanyCouponsByPriceLessThan(companyId, price);
    }

    @Override
    public Optional<Coupon> getCoupon(long companyId, long couponId) {
        return couponRepo.findByCompanyIdAndCouponId(companyId, couponId);
    }


}

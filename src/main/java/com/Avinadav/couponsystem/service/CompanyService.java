package com.Avinadav.couponsystem.service;

import com.Avinadav.couponsystem.entity.Company;
import com.Avinadav.couponsystem.entity.Coupon;

import java.util.List;
import java.util.Optional;

public interface CompanyService {

    /**
     * Create new company in the DB.
     *
     * @param company - The new company to insert.
     * @return Company.
     */

    Optional<Company> createCompany(Company company);

    /**
     * Remove company from the DB.
     *
     * @param id - The ID of specific company.
     */

    void removeCompany(long id);

    /**
     * Updating exist company in the DB, by getting entity of company with the same ID.
     *
     * @param company - The updated company.
     * @return - Company.
     */

    Optional<Company> updateCompany(Company company);

    /**
     * Fetching company by getting the ({@param id}.
     *
     * @param id - The ID of the company.
     * @return - Company.
     */

    Optional<Company> getCompany(long id);

    /**
     * Fetching all the companies from the DB
     *
     * @return - List of Companies.
     */

    Optional<List<Company>> getAllCompanies();

    /**
     * Fetch all the coupons of specific company.
     *
     * @param companyId - The id of the company.
     * @return - List of coupons.
     */
    Optional<List<Coupon>> getAllCompanyCoupons(long companyId);

    /**
     * Fetching one coupon of specific company by the category of the coupon.
     *
     * @param companyId - The ID of the company.
     * @param category  - The category of the coupon.
     * @return - Coupon.
     */

    Optional<List<Coupon>> getCouponByCategory(long companyId, int category);

    /**
     * Save new coupon for one specific company.
     *
     * @param coupon - The coupon.
     * @return - Copy of the coupon.
     */
    Optional<Coupon> createCoupon(Coupon coupon);

    /**
     * Remove coupon by ID.
     *
     * @param couponId - The ID of the coupon.
     */
    void removeCoupon(long couponId);

    /**
     * Updating existent coupon.
     *
     * @param coupon - The updated coupon.
     * @return - Coupon.
     */
    Optional<Coupon> updateCoupon(Coupon coupon);

    /**
     * Getting all the coupons of one specific company
     * if the price of the coupon is lower the price that received as an argument.
     * By the company ID and Price.
     *
     * @param companyId The ID of the company.
     * @param price     The top price.
     * @return Coupons list
     */
    Optional<List<Coupon>> couponsLowerThanPrice(long companyId, double price);

    /**
     * Fetching specific coupon.
     *
     * @param companyId - The ID of the company.
     * @param couponId  - The ID of the coupon.
     * @return - Coupon
     */

    Optional<Coupon> getCoupon(long companyId, long couponId);

}

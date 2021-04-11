package com.Avinadav.couponsystem.data.repo;

import com.Avinadav.couponsystem.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {


    /**
     * Getting all the coupons of one specific customer
     * if the price of the coupon is lower the price that received as an argument.
     * By the customer ID and Price.
     *
     * @param customerId - The ID of the customer.
     * @param price      - The top price.
     * @return - Optional of coupons list
     */
    /*
     * c-The return coupons.
     * ce- "Customer Entity".
     * */
    @Query("select c " +
            "from Customer as ce" +
            " join ce.coupons as c " +
            "where ce.id=:customerId and c.price<:price")
    Optional<List<Coupon>> findAllByPriceLessThan(long customerId, double price);

    /**
     * Getting all the coupons of one specific company
     * if the price of the coupon is lower the price that received as an argument.
     * By the company ID and Price.
     *
     * @param companyId - The ID of the company.
     * @param price     - The top price.
     * @return - Coupons list
     */

    @Query(value = "select * from coupon where company_id=:companyId and price<:price ", nativeQuery = true)
    Optional<List<Coupon>> findCompanyCouponsByPriceLessThan(long companyId, double price);

    /**
     * Getting all the coupons of one specific customer , only by the ({@param customerId}.
     *
     * @param customerId - The ID of the customer.
     * @return - List of Optional coupons.
     */

    @Query("select c " +
            "from Customer as ce" +
            " join ce.coupons as c " +
            "where ce.id=:customerId")
    Optional<List<Coupon>> findCouponsByCustomerId(long customerId);


    /**
     * Getting all the coupons of specific customer ,
     * that their date is before than the date received as an argument.
     *
     * @param id   - The ID of the customer
     * @param date - The date.
     * @return - List of coupons (before the received date).
     */

    @Query("select c " +
            "from Customer as ce " +
            "join ce.coupons as c " +
            "where c.endDate <=:date and ce.id=:id ")
    Optional<List<Coupon>> findByStartDateBefore(long id, LocalDate date);


    /**
     * Getting all the customer coupons by specific category.
     *
     * @param id       - The customer ID.
     * @param category - The number of category.
     * @return - Optional List of coupons by category.
     */

    @Query("select c " +
            "from Customer as ce" +
            " join ce.coupons as c " +
            "where c.category=:category and ce.id=:id")
    Optional<List<Coupon>> findByCategory(long id, int category);

    /**
     * Fetch all the coupons of specific company.
     *
     * @param companyId - The id of the company.
     * @return - List of coupons.
     */

    @Query(value = "select * from coupon where company_id=:companyId",
            nativeQuery = true)
    Optional<List<Coupon>> findByCompanyId(long companyId);

    /**
     * Fetching one coupon of specific company by the category of the coupon.
     *
     * @param companyId - The ID of the company.
     * @param category  - The category of the coupon.
     * @return - Coupon.
     */

    @Query(value = "select * from coupon where company_id=:companyId and category=:category",
            nativeQuery = true)
    Optional<List<Coupon>> findCouponByCategory(long companyId, int category);

    /**
     * Fetching specific coupon by company ID and coupon ID.
     *
     * @param companyId - The company ID.
     * @param couponId  - The coupon ID.
     * @return - Coupon.
     */

    @Query(value = "select * from coupon where company_id=:companyId and id=:couponId "
            , nativeQuery = true)
    Optional<Coupon> findByCompanyIdAndCouponId(long companyId, long couponId);

}

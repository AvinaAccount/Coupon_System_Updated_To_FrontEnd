package com.Avinadav.couponsystem.service;

import com.Avinadav.couponsystem.entity.Coupon;
import com.Avinadav.couponsystem.entity.Customer;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface CustomerService {

    /**
     * Fetch one specific customer by {@param customerId}
     *
     * @param id - The ID of the customer.
     * @return - Customer entity.
     */

    Optional<Customer> findCustomerById(long id);

    /**
     * Create new customer by, insert entity type 'Customer' to the DB.
     *
     * @param customer - The customer entity.
     * @return - Customer entity.
     */
    Optional<Customer> createCustomer(Customer customer);

    /**
     * Delete customer from the DB by {@param id}
     *
     * @param customerId - The id of the customer.
     */
    void removeCustomer(long customerId);

    /**
     * Updating the new details of customer by getting {@param customer}
     *
     * @param customer - The updated customer (The id must be the same as the id in the DB!!)
     * @return - Updated Customer in the DB.
     */
    Optional<Customer> updateCustomer(Customer customer);

    /**
     * Fetch all the customers.
     *
     * @return - Optional list of all customers.
     */

    Optional<List<Customer>> getAllCustomer();

    /**
     * Fetch all coupons for a specific customer,
     * which his/her id is specific in the first argument to the function.
     *
     * @param customerId - The id of the customer.
     * @return - The fetched list.
     */
    Optional<List<Coupon>> getCoupons(long customerId);

    /**
     * Add coupon to specific customer by {@param customerId} and {@param couponId}.
     * Amount of the coupon subtracted by 1.
     *
     * @param customerId - The id of the customer.
     * @param couponId   - The ID of the coupon.
     * @return - Coupon entity.
     */

    Optional<Coupon> addCoupon(long customerId, long couponId);


    /**
     * Getting all the coupons of specific customer ,
     * that their price is lower than the price received as an argument.
     *
     * @param customerId - The id of specific customer.
     * @param price      - The price.
     * @return - List of coupons .
     */
    Optional<List<Coupon>> findCouponsLassThanPrice(long customerId, double price);


    /**
     * Getting all the coupons of specific customer ,
     * that their date is before than the date received as an argument.
     *
     * @param id   - The ID of the customer
     * @param date - The date.
     * @return - List of coupons (before the received date).
     */

    Optional<List<Coupon>> findAllCouponBeforeDate(long id, LocalDate date);


    /**
     * Getting all coupons of specific customer by the category number of the coupons.
     * <p>
     *
     * @param id       - The id of the customer.
     * @param category - The category number.
     * @return - List of the coupons.
     */
    Optional<List<Coupon>> getAllCouponsByCategory(long id, int category);

}

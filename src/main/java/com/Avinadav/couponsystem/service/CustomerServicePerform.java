package com.Avinadav.couponsystem.service;

import com.Avinadav.couponsystem.data.repo.CouponRepository;
import com.Avinadav.couponsystem.data.repo.CustomerRepository;
import com.Avinadav.couponsystem.entity.Coupon;
import com.Avinadav.couponsystem.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerServicePerform implements CustomerService {

    private final CustomerRepository customerRepo;
    private final CouponRepository couponRepo;

    @Autowired
    public CustomerServicePerform(CustomerRepository customerRepo, CouponRepository couponRepo) {
        this.customerRepo = customerRepo;
        this.couponRepo = couponRepo;
    }

    @Override
    public Optional<Customer> findCustomerById(long id) {
        return customerRepo.findById(id);
    }

    @Override
    public Optional<Customer> createCustomer(Customer customer) {
        Customer newCustomer = customerRepo.save(customer);
        return Optional.of(newCustomer);
    }

    @Override
    public void removeCustomer(long customerId) {
        customerRepo.deleteById(customerId);
    }

    @Override
    public Optional<Customer> updateCustomer(Customer customer) {
        return Optional.of(customerRepo.save(customer));
    }

    @Override
    public Optional<List<Customer>> getAllCustomer() {
        return Optional.of(customerRepo.findAll());
    }

    @Override
    public Optional<List<Coupon>> getCoupons(long customerId) {
        return couponRepo.findCouponsByCustomerId(customerId);
    }

    @Override
    public Optional<Coupon> addCoupon(long customerId, long couponId) {

        /*The check if the customer and the coupon isPresent (Of -Optional.class)
        will be in the controller*/
        Customer fetchCustomer = customerRepo.findById(customerId).get();
        Coupon fetchCoupon = couponRepo.findById(couponId).get();

        /*save the coupon with the method "addCoupon" (From entity 'Customer')*/
        fetchCustomer.addCoupon(fetchCoupon);
        fetchCoupon.setAmount(fetchCoupon.getAmount() - 1);

        /*-> UPDATE (SAVE-JPA) CUSTOMER WITH THE COUPON! <-*/
        customerRepo.save(fetchCustomer);

        return Optional.of(fetchCoupon);
    }

    @Override
    public Optional<List<Coupon>> findCouponsLassThanPrice(long customerId, double price) {
        return couponRepo.findAllByPriceLessThan(customerId, price);
    }

    @Override
    public Optional<List<Coupon>> findAllCouponBeforeDate(long id, LocalDate date) {
        return couponRepo.findByStartDateBefore(id, date);
    }

    @Override
    public Optional<List<Coupon>> getAllCouponsByCategory(long id, int category) {
        return couponRepo.findByCategory(id, category);
    }
}

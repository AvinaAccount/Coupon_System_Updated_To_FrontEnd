package com.Avinadav.couponsystem.schedued;

import com.Avinadav.couponsystem.data.repo.CouponRepository;
import com.Avinadav.couponsystem.entity.Coupon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * - This class responsible of the coupons of all companies.
 * If the 'endDate' of coupon is passed ,the method 'cleaner()' will delete the coupon from the DB.
 */

@Component
public class CouponsCleaner {
    private final CouponRepository couponRepo;

    @Autowired
    private CouponsCleaner(CouponRepository couponRepo) {
        this.couponRepo = couponRepo;
    }


    @Scheduled(cron = "0 0 0 ? * * ")
    public void cleaner() {
        couponRepo.findAll()
                .stream()
                .filter(coupon -> coupon.getEndDate().isBefore(LocalDate.now()))
                .mapToLong(Coupon::getId)
                .forEach(couponRepo::deleteById);
    }
}

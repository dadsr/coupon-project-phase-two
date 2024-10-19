package couponsProject.couponsProject.services;

import couponsProject.couponsProject.beans.Coupon;
import couponsProject.couponsProject.beans.Customer;
import couponsProject.couponsProject.repository.CouponRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;


@AllArgsConstructor
@Slf4j
@Service
public class CouponExpirationDailyJob{
    private final AdminServices adminServices;
    private final CouponRepository couponRepository;

    @Scheduled(cron = "${app.scheduler.cron}")
    public void executeDailyJob() {
        log.info("Daily job started at {} on thread {}", LocalDateTime.now(), Thread.currentThread().getName());
        List<Coupon> coupons = adminServices.findByEndDateBefore(new Date(System.currentTimeMillis()));
        for (Coupon coupon : coupons) {
            List<Customer> customers = coupon.getCustomers();
            for (int i = 0; i < customers.size(); i++) {
                customers.get(i).removeCoupon(coupon);
            }
            coupon.getCompany().removeCoupon(coupon);
            coupon.setCompany(null);
            couponRepository.delete(coupon);
        }
        log.info("Daily job Ended at {} on thread {}", LocalDateTime.now(), Thread.currentThread().getName());

    }
}


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

    /**
     * Executes a scheduled daily job to remove expired coupons and update related entities.
     * This method is automatically triggered based on the cron expression defined in the application properties.
     *
     * The job performs the following tasks:
     * 1. Finds all coupons that have expired (end date before current date)
     * 2. Removes these expired coupons from associated customers
     * 3. Removes the expired coupons from their respective companies
     * 4. Deletes the expired coupons from the database
     *
     * The cron schedule is configured using the 'app.scheduler.cron' property in the application configuration.
     */
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


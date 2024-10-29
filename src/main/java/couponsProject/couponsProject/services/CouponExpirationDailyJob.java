package couponsProject.couponsProject.services;

import couponsProject.couponsProject.beans.Coupon;
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
    private final CompanyServices companyServices;

    /**
     * Executes a daily scheduled job to clean up expired coupons.
     * This method is scheduled to run based on the cron expression defined in the application properties.
     *
     * The job performs the following tasks:
     * 1. Logs the start time and the thread on which it's running.
     * 2. Retrieves all coupons with an end date before the current system time.
     * 3. Iterates through the expired coupons and deletes each one.
     *
     * @throws RuntimeException if there's an error during coupon deletion or database operations
     *
     * @see AdminServices#findByEndDateBefore(Date)
     * @see CompanyServices#deleteCoupon(int)
     *
     * @since 1.0
     */
    @Scheduled(cron = "${app.scheduler.cron}")
    public void executeDailyJob() {
        log.info("Daily job started at {} on thread {}", LocalDateTime.now(), Thread.currentThread().getName());
        List<Coupon> coupons = adminServices.findByEndDateBefore(new Date(System.currentTimeMillis()));
        for (Coupon coupon : coupons) {
            companyServices.deleteCoupon(coupon.getId());
        }
        log.info("Daily job Ended at {} on thread {}", LocalDateTime.now(), Thread.currentThread().getName());
    }
}


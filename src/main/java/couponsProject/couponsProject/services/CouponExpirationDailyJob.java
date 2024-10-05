package couponsProject.couponsProject.services;

import couponsProject.couponsProject.repository.CouponRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;


@AllArgsConstructor
@Slf4j
@Service
public class CouponExpirationDailyJob{
    private final AdminServices adminServices;
    private final CouponRepository couponRepository;

    @Scheduled(cron = "${app.scheduler.cron}")
    public void executeDailyJob() {

        log.info("Daily job started at {} on thread {}", LocalDateTime.now(), Thread.currentThread().getName());

        adminServices.getAllCoupons().stream()
                .filter(coupon -> coupon.getEndDate().before(new Date()))
                .forEach(coupon -> {
                    coupon.getCustomers()
                            .forEach(customer -> customer.removeCoupon(coupon));
                    coupon.getCompany().removeCoupon(coupon);
                    coupon.setCompany(null);
                    couponRepository.delete(coupon);
                });

        log.info("Daily job completed at {} on thread {}", LocalDateTime.now(), Thread.currentThread().getName());
    }
}

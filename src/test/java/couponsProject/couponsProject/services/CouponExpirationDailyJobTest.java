package couponsProject.couponsProject.services;

import couponsProject.couponsProject.TestsUtils;
import couponsProject.couponsProject.beans.Company;
import couponsProject.couponsProject.beans.Coupon;
import couponsProject.couponsProject.beans.Customer;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class CouponExpirationDailyJobTest {
    @Autowired
    private CouponExpirationDailyJob couponExpirationDailyJob;
    @Autowired
    private AdminServices adminServices;
    @Autowired
    private CompanyServices companyServices;
    @Autowired
    private CustomerServices customerServices;

    @Transactional
    @Test
    void executeDailyJob() {
        log.info("Testing executeDailyJob");
        List<Company> companies = TestsUtils.createCompanies(2);

        for (Company company : companies) {
            adminServices.addCompany(company);
            List<Coupon> coupons = TestsUtils.createCoupons(company, 3);

            for (Coupon coupon : coupons) {
                coupon.setEndDate(Date.valueOf("2024-10-07"));
                companyServices.addCoupon(coupon);
            }

            List<Customer> customers = TestsUtils.createCustomers(3);

            for (Customer customer : customers) {

                adminServices.addCustomer(customer);
            }
            for (Coupon coupon : coupons) {
                for (Customer customer : customers) {
                    if (coupon.getAmount() > 0) {
                        customerServices.couponPurchase(customer.getId(), coupon.getId());
                    }
                }
            }
        }

        couponExpirationDailyJob.executeDailyJob();
        Assertions.assertThatCode(() -> couponExpirationDailyJob.executeDailyJob())
                .as("test executeDailyJob")
                .doesNotThrowAnyException();

    }
}

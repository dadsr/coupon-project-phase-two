package couponsProject.couponsProject.services;

import couponsProject.couponsProject.TestsUtils;
import couponsProject.couponsProject.beans.Company;
import couponsProject.couponsProject.beans.Coupon;
import couponsProject.couponsProject.beans.Customer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CouponExpirationDailyJobTest {
    @Autowired
    private CouponExpirationDailyJob  couponExpirationDailyJob;
    @Autowired
    private AdminServices adminServices;
    @Autowired
    private CompanyServices companyServices;
    @Autowired
    private CustomerServices customerServices;

    @Test
    void executeDailyJob() {
        List<Company> companies = TestsUtils.createCompanies(10);

        for (Company company : companies) {
            adminServices.addCompany(company);
            List<Coupon> coupons =TestsUtils.createCoupons(company,50);

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
                    customerServices.couponPurchase(customer.getId(),coupon.getId());
                }
            }
        }
        
        couponExpirationDailyJob.executeDailyJob();
        Assertions.assertThatCode()
                .as("test executeDailyJob")
                .doesNotThrowAnyException();
    }
}
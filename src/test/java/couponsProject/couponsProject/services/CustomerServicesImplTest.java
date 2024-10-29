package couponsProject.couponsProject.services;

import couponsProject.couponsProject.TestsUtils;
import couponsProject.couponsProject.beans.CategoryEnum;
import couponsProject.couponsProject.beans.Company;
import couponsProject.couponsProject.beans.Coupon;
import couponsProject.couponsProject.beans.Customer;
import couponsProject.couponsProject.exseptions.CouponException;
import couponsProject.couponsProject.repository.CompanyRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Random;

@Slf4j
@SpringBootTest
class CustomerServicesImplTest {
    @Autowired
    private AdminServices adminServices;
    @Autowired
    private CustomerServices customerServices;
    @Autowired
    private CompanyServices companyServices;
    @Autowired
    private CompanyRepository companyRepository;
    Random rand = new Random();


    @Test
    void getCustomer() {

        log.info(" *********************** Testing getCustomer *********************** ");
        Customer customer = TestsUtils.createCustomers(1).get(0);
        adminServices.addCustomer(customer);
        Customer customerDb = customerServices.getCustomer(customer.getId());
        log.info("Testing getCustomer - *********************** getting customer *********************** ");
        Assertions.assertThat(customerDb.getId())
                .as("test get customer success")
                .isEqualTo(customer.getId());
    }
    @Test
    void couponPurchase() {
        log.info(" *********************** Testing couponPurchase *********************** ");
        Company company = TestsUtils.createCompanies(1).get(0);
        adminServices.addCompany(company);

        Coupon coupon = TestsUtils.createCoupons(company,1).get(0);
        companyServices.addCoupon(coupon);

        Customer customer = TestsUtils.createCustomers(1).get(0);
        adminServices.addCustomer(customer);

        log.info("Testing couponPurchase - *********************** purchase coupon *********************** ");
        Assertions.assertThatCode(() -> customerServices.couponPurchase(customer.getId(),coupon.getId()))
                .as("test purchase process")
                .doesNotThrowAnyException();

        log.info("Testing couponPurchase - *********************** purchase coupon for second time *********************** ");
        Assertions.assertThatThrownBy(() -> customerServices.couponPurchase(customer.getId(),coupon.getId()))
                .as("test if purchase exist coupon is failed")
                .isInstanceOf(CouponException.class)
                .hasMessageContaining("Purchase is not possible");
    }
    @Test
    void getCoupons() {
        log.info(" *********************** Testing getCoupons *********************** ");
        Company company = TestsUtils.createCompanies(1).get(0);
        adminServices.addCompany(company);

        List<Coupon> coupons = TestsUtils.createCoupons(company,20);
        for (Coupon coupon : coupons) {
            companyServices.addCoupon(coupon);
        }
        Customer customer = TestsUtils.createCustomers(1).get(0);
        adminServices.addCustomer(customer);
        int customerId = customer.getId();
        for (Coupon coupon : coupons) {
            customerServices.couponPurchase(customerId,coupon.getId());
        }

        /***************** by id *****************/
        log.info("Testing getCoupons - *********************** by id *********************** ");
        Assertions.assertThatCode(() -> customerServices.getCoupons(customerId))
                .as("test getting coupons by customer id")
                .doesNotThrowAnyException();

        List<Coupon> couponsDb = customerServices.getCoupons(customerId);

        Assertions.assertThat(couponsDb.size())
                .as("test coupons size")
                .isNotNull()
                .isEqualTo(coupons.size());

        /***************** by category *****************/
        log.info("Testing getCoupons - *********************** by category *********************** ");
        CategoryEnum category = coupons.get(0).getCategory();

        Assertions.assertThatCode(() -> customerServices.getCoupons(customerId,category))
                .as("test getting coupons by category and id")
                .doesNotThrowAnyException();

        couponsDb = customerServices.getCoupons(customerId,category);

        Assertions.assertThat(couponsDb.size())
                .as("test coupons size")
                .isNotNull()
                .isEqualTo(coupons.stream().filter(coupon -> coupon.getCategory().equals(category)).count());

        /***************** by maxPrice *****************/
        log.info("Testing getCoupons - *********************** by max price *********************** ");
        double maxPrice = coupons.get(rand.nextInt(coupons.size())).getPrice();
        Assertions.assertThatCode(() -> customerServices.getCoupons(customerId,maxPrice))
                .as("test getting coupons by max price and customer id")
                .doesNotThrowAnyException();
        couponsDb = customerServices.getCoupons(customerId,maxPrice);

        Assertions.assertThat(couponsDb.size())
                .as("test coupons size")
                .isNotNull()
                .isEqualTo(coupons.stream().filter(coupon -> coupon.getPrice() <= maxPrice).count());
    }

}
package couponsProject.couponsProject.services;

import couponsProject.couponsProject.beans.CategoryEnum;
import couponsProject.couponsProject.beans.Coupon;
import couponsProject.couponsProject.beans.Customer;
import couponsProject.couponsProject.controllers.exseptions.CouponException;
import couponsProject.couponsProject.repository.CompanyRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Calendar;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;

@SpringBootTest
class CustomerServicesImplTest {
    @Autowired
    private CustomerServices customerServices;
    @Autowired
    private CompanyServices companyServices;

    int customerId =6;
    int companyId =7;
    int couponsSize = 1;
    CategoryEnum category =CategoryEnum.ELECTRONICS;
    double maxPrice = 10;

    String customerEmail ="client@mail.co.il";
    @Autowired
    private CompanyRepository companyRepository;

    @Test
    void login() {
        Assertions.assertThat(customerServices.login(customerEmail,"987654321"))
                .as("test login success")
                .isEqualTo(customerId);

        Assertions.assertThatThrownBy(() -> customerServices.login("wrongemail@admin.com", "987654321"))
                .as("test login wrong mail Failure")
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("No such customer");

        Assertions.assertThatThrownBy(() -> customerServices.login(customerEmail, "wrongpassword"))
                .as("test login wrong password Failure")
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("No such customer");
    }

    @Test
    void getCustomer() {
        Customer customer = customerServices.getCustomer(customerId);

        Assertions.assertThat(customer.getId())
                .as("test get customer success")
                .isEqualTo(customerId);
    }

    @Test
    void couponPurchase() {
        Assertions.assertThatCode(() -> customerServices.couponPurchase(customerId,companyServices.getCompanyCoupons(companyId).get(0).getId()))
                .as("test purchase process")
                .doesNotThrowAnyException();

        Assertions.assertThatThrownBy(() -> customerServices.couponPurchase(customerId,companyServices.getCompanyCoupons(companyId).get(0).getId()))
                .as("test if purchase exist coupon is failed")
                .isInstanceOf(CouponException.class)
                .hasMessageContaining("Purchase is not possible");
    }

    @Test
    void getCoupons() {
        /***************** by id *****************/
        Assertions.assertThatCode(() -> customerServices.getCoupons(customerId))
                .as("test getting coupons by customer id")
                .doesNotThrowAnyException();
        List<Coupon> coupons = customerServices.getCoupons(customerId);

        Assertions.assertThat(coupons.size())
                .as("test coupons size")
                .isNotNull()
                .isGreaterThan(0);
        /***************** by category *****************/
        Assertions.assertThatCode(() -> customerServices.getCoupons(customerId,category))
                .as("test getting coupons by category and id")
                .doesNotThrowAnyException();
        List<Coupon> coupons = customerServices.getCoupons(customerId,category);

        Assertions.assertThat(coupons.size())
                .as("test coupons size")
                .isNotNull()
                .isGreaterThan(0);

        /***************** by maxPrice *****************/
        Assertions.assertThatCode(() -> customerServices.getCoupons(customerId,maxPrice))
                .as("test getting coupons by max price and customer id")
                .doesNotThrowAnyException();
        List<Coupon> coupons = customerServices.getCoupons(customerId,maxPrice);

        Assertions.assertThat(coupons.size())
                .as("test coupons size")
                .isNotNull()
                .isGreaterThan(0);
    }

}
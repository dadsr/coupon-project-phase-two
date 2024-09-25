package couponsProject.couponsProject.services;

import couponsProject.couponsProject.beans.CategoryEnum;
import couponsProject.couponsProject.beans.Company;
import couponsProject.couponsProject.beans.Coupon;
import couponsProject.couponsProject.controllers.exseptions.CompanyException;
import couponsProject.couponsProject.controllers.exseptions.CouponException;
import couponsProject.couponsProject.repository.CompanyRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


@SpringBootTest
class CompanyServicesImplTest {
    @Autowired
    private CompanyServices companyServices;

    @Test
    void login() {
        int companyId = 1;
        String email = "test@gmail.com";
        String password = "test123";

        Assertions.assertThat(companyServices.login(email,password))
                .as("test login success")
                .isEqualTo(1);
        Assertions.assertThat(companyServices.login("wrongemail@admin.com", password))
                .as("test login wrong mail Failure")
                .isEqualTo(0);
        Assertions.assertThat(companyServices.login(email, "wrongpassword"))
                .as("test login wrong password Failure")
                .isEqualTo(0);
    }

    @Test
    void getCompanyDetails() {
        int id =1;
        Company company = companyServices.getCompanyDetails(id);
        Assertions.assertThat(company).as("test getting company by id").isNotNull().hasFieldOrPropertyWithValue("id",id);

        Assertions.assertThatThrownBy(() -> companyServices.getCompanyDetails(9999))
                .as("test when company dose not exist")
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("no such company to get");
    }

    @Test
    void addCoupon() {
        int id =1;
        Coupon coupon = Coupon.builder()
                .company(companyServices.getCompanyDetails(id))
                .category(CategoryEnum.FASHION)
                .title("title")
                .description("desc")
                .startDate(Date.valueOf("01-11-2024"))
                .endDate(Date.valueOf("01-12-2024"))
                .amount(30)
                .price(99.99)
                .image(null)
                .build();

        Assertions.assertThatCode(() -> companyServices.addCoupon(coupon))
                .as("test if adding coupon does not throw any exception")
                .doesNotThrowAnyException();

        Assertions.assertThat(
                        companyServices.getCompanyCoupons(id).stream().filter(c -> c.getTitle().equals(coupon.getTitle())
                                && c.getDescription().equals(coupon.getDescription())).count())
                .as("test if adding coupon was successful")
                .isEqualTo(1);


        Assertions.assertThatThrownBy(() -> companyServices.addCoupon(coupon))
                .as("test if adding exist coupon does throw exception")
                .isInstanceOf(CouponException.class)
                .hasMessageContaining("Coupon already exists");
    }

    @Test
    void updateCoupon() {
        int id =1;
        Coupon coupon = companyServices.getCompanyCoupons(id).get(0);
        coupon.setPrice(coupon.getPrice()/2);

        Assertions.assertThatCode(() -> companyServices.updateCoupon(coupon)).doesNotThrowAnyException();
        Assertions.assertThat(
                companyServices.getCompanyCoupons(id).get(0).getPrice())
                .as("test if updated coupon was updated successful")
                .isEqualTo(coupon.getPrice());

        //NoSuchElementException
        //id is empty
        Assertions.assertThatThrownBy(() -> companyServices.updateCoupon(coupon))
                .as("Test if updating a non-existent company throws NoSuchElementException")
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Coupon dose not exists");
    }

    @Test
    void deleteCoupon() {
        int id =1;
        Assertions.assertThatCode(() -> companyServices.deleteCoupon(id)).doesNotThrowAnyException();
        //NoSuchElementException
        Assertions.assertThatThrownBy(() -> companyServices.deleteCoupon(id))
                .as("test if coupon was deleted successful")
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("coupon does not exist");
    }

    @Test
    void getCompanyCoupons() {
        int id =1;
        List<Coupon> coupons = companyServices.getCompanyCoupons(id);
        Assertions.assertThat(coupons).as("check coupons by company")
                .isNotNull()
                .hasSize(1)
                .extracting(Coupon::getTitle).containsExactlyInAnyOrder("Coupon1", "Coupon2");
    }


}
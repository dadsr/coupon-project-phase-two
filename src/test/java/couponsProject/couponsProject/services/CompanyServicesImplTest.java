package couponsProject.couponsProject.services;

import couponsProject.couponsProject.TestsUtils;
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

    int companyId =6;
    String companyEmail ="compAndCo@comp.co.il";
    String clientEmail ="compAndCo@comp.co.il";

    /**********************************************************************************/


    @Test
    void login() {
        Assertions.assertThat(companyServices.login(companyEmail,"123456789"))
                .as("test login success")
                .isEqualTo(companyId);

        Assertions.assertThatThrownBy(() -> companyServices.login("wrongemail@admin.com", "123456789"))
                .as("test login wrong mail Failure")
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("No such company");

        Assertions.assertThatThrownBy(() -> companyServices.login(companyEmail, "wrongpassword"))
                .as("test login wrong password Failure")
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("No such company");

      }

    @Test
    void getCompanyDetails() {

        int id = companyServices.login(companyEmail,"123456789");
        Company company = companyServices.getCompanyDetails(id);
        Assertions.assertThat(company).as("test getting company by id").isNotNull().hasFieldOrPropertyWithValue("id",id);

        Assertions.assertThatThrownBy(() -> companyServices.getCompanyDetails(9999))
                .as("test when company does not exist")
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("no such company to get");
    }


    @Test
    void addCoupon() {
        int id = companyServices.login(companyEmail,"123456789");
        Coupon coupon = Coupon.builder()
                .company(companyServices.getCompanyDetails(id))
                .category(CategoryEnum.FASHION)
                .title("title")
                .description("desc")
                .startDate(Date.valueOf("2024-11-01"))
                .endDate(Date.valueOf("2024-12-01"))
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
               // .hasMessageContaining("Coupon already exists")
        ;
    }

    @Test
    void updateCoupon() {
        int id =companyServices.login(companyEmail,"123456789");
        Coupon coupon = companyServices.getCompanyCoupons(id).get(0);
        coupon.setPrice(coupon.getPrice()/2);

        Assertions.assertThatCode(() -> companyServices.updateCoupon(coupon)).doesNotThrowAnyException();
        Assertions.assertThat(
                        companyServices.getCompanyCoupons(id).get(0).getPrice())
                .as("test if updated coupon was updated successful")
                .isEqualTo(coupon.getPrice());

        //NoSuchElementException
        Coupon coupon2 = Coupon.builder()
                .company(companyServices.getCompanyDetails(id))
                .category(CategoryEnum.FASHION)
                .title("non")
                .description("exists")
                .build();
        Assertions.assertThatThrownBy(() -> companyServices.updateCoupon(coupon2))
                .as("Test if updating a non-exists coupon throws NoSuchElementException")
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Coupon does not exists");
    }

    @Test
    void deleteCoupon() {
        int id = companyServices.login(companyEmail,"123456789");
        int coupId = companyServices.getCompanyCoupons(id).get(0).getId();

        Assertions.assertThatCode(() -> companyServices.deleteCoupon(coupId)).doesNotThrowAnyException();

        //NoSuchElementException
        Assertions.assertThatThrownBy(() -> companyServices.deleteCoupon(coupId))
                .as("test if coupon was deleted successful")
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("coupon does not exist");
    }

    @Test
    void getCompanyCoupons() {
        int id = companyServices.login(companyEmail,"123456789");
        List<Coupon> coupons = companyServices.getCompanyCoupons(id);
        int size = coupons.size();
        companyServices.addCoupon(TestsUtils.createCoupons(companyServices.getCompanyDetails(id),1).get(0));
        coupons = companyServices.getCompanyCoupons(id);
        Assertions.assertThat(coupons).as("check coupons by company")
                .isNotNull()
                .hasSize(size+1);
    }


}
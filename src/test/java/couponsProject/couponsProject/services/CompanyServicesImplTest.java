package couponsProject.couponsProject.services;

import couponsProject.couponsProject.TestsUtils;
import couponsProject.couponsProject.beans.CategoryEnum;
import couponsProject.couponsProject.beans.Company;
import couponsProject.couponsProject.beans.Coupon;
import couponsProject.couponsProject.beans.Customer;
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
    private AdminServices adminServices;
    @Autowired
    private CompanyServices companyServices;
    @Autowired
    private CustomerServices customerServices;


    /**********************************************************************************/


    @Test
    void login() {
        Company company = TestsUtils.createCompanies(1).get(0);
        adminServices.addCompany(company);

        Assertions.assertThat(companyServices.login(company.getEmail(), company.getPassword()))
                .as("test login success")
                .isEqualTo(company.getId());

        Assertions.assertThatThrownBy(() -> companyServices.login("wrongemail@admin.com",  company.getPassword()))
                .as("test login wrong mail Failure")
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("No such company");

        Assertions.assertThatThrownBy(() -> companyServices.login( company.getEmail(), "wrongpassword"))
                .as("test login wrong password Failure")
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("No such company");
      }

    @Test
    void getCompanyDetails() {
        Company company = TestsUtils.createCompanies(1).get(0);
        adminServices.addCompany(company);

        Company companyDb = companyServices.getCompanyDetails(company.getId());
        Assertions.assertThat(companyDb).as("test getting company by id").isNotNull().hasFieldOrPropertyWithValue("id",company.getId());

        Assertions.assertThatThrownBy(() -> companyServices.getCompanyDetails(9999))
                .as("test when company does not exist")
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("no such company to get");
    }


    @Test
    void addCoupon() {
        Company company = TestsUtils.createCompanies(1).get(0);
        adminServices.addCompany(company);
        Coupon coupon= TestsUtils.createCoupons(company,1).get(0);

        Assertions.assertThatCode(() -> companyServices.addCoupon(coupon))
                .as("test if adding coupon does not throw any exception")
                .doesNotThrowAnyException();

        Assertions.assertThat(companyServices.getCompanyCoupons(company.getId()).stream().filter(c -> c.getTitle().equals(coupon.getTitle())
                                && c.getDescription().equals(coupon.getDescription())).count())
                .as("test if adding coupon was successful")
                .isEqualTo(1);

//todo
        Assertions.assertThatThrownBy(() -> companyServices.addCoupon(coupon))
                .as("test if adding exist coupon does throw exception")
                .isInstanceOf(CouponException.class)
                .hasMessageContaining("Coupon already exists");

    }

    @Test
    void updateCoupon() {
        Company company = TestsUtils.createCompanies(1).get(0);
        adminServices.addCompany(company);
        Coupon coupon= TestsUtils.createCoupons(company,1).get(0);
        companyServices.addCoupon(coupon);

        Coupon couponDb = companyServices.getCompanyCoupons(company.getId()).get(0);
        Double price = coupon.getPrice()/2;
        couponDb.setPrice(price);

        Assertions.assertThatCode(() -> companyServices.updateCoupon(couponDb)).doesNotThrowAnyException();

        Assertions.assertThat(companyServices.getCompanyCoupons(company.getId()).get(0).getPrice())
                .as("test if updated coupon was updated successful")
                .isEqualTo(price);

        //NoSuchElementException
        Coupon coupon2 = TestsUtils.createCoupons(company,1).get(0);

        Assertions.assertThatThrownBy(() -> companyServices.updateCoupon(coupon2))
                .as("Test if updating a non-exists coupon throws NoSuchElementException")
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Coupon does not exists");
    }
//todo
    @Test
    void deleteCoupon() {

        Company company = TestsUtils.createCompanies(1).get(0);
        adminServices.addCompany(company);
        Coupon coupon= TestsUtils.createCoupons(company,1).get(0);
        companyServices.addCoupon(coupon);
        Customer customer =TestsUtils.createCustomers(1).get(0);
        adminServices.addCustomer(customer);
        customerServices.couponPurchase(customer.getId(), coupon.getId());
;

        Assertions.assertThatCode(() -> companyServices.deleteCoupon(coupon.getId())).doesNotThrowAnyException();

        //NoSuchElementException
       Assertions.assertThatThrownBy(() -> companyServices.deleteCoupon(coupon.getId()))
                .as("test if coupon was deleted successful")
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("coupon does not exist");

    }

    @Test
    void getCompanyCoupons() {
        Company company = TestsUtils.createCompanies(1).get(0);
        adminServices.addCompany(company);
        List <Coupon> coupons = TestsUtils.createCoupons(company,20);
        for (Coupon coupon : coupons) {
            companyServices.addCoupon(coupon);
        }
        List <Coupon> couponsDb = companyServices.getCompanyCoupons(company.getId());
        Assertions.assertThat(couponsDb)
                .as("check coupons by company")
                .isNotNull()
                .hasSize(coupons.size());
    }


}
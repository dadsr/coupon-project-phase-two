package couponsProject.couponsProject.services;

import couponsProject.couponsProject.TestsUtils;
import couponsProject.couponsProject.beans.Company;
import couponsProject.couponsProject.beans.Coupon;
import couponsProject.couponsProject.beans.Customer;
import couponsProject.couponsProject.exseptions.CouponException;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
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
    void getCompanyDetails() {
        log.info(" *********************** Testing getCompanyDetails *********************** ");
        Company company = TestsUtils.createCompanies(1).get(0);
        adminServices.addCompany(company);

        log.info("Testing getCompanyDetails - *********************** getting company by id *********************** ");
        Company companyDb = companyServices.getCompanyDetails(company.getId());
        Assertions.assertThat(companyDb).as("test getting company by id")
                .isNotNull()
                .hasFieldOrPropertyWithValue("id",company.getId());

        log.info("Testing getCompanyDetails - *********************** getting non existing company *********************** ");
        Assertions.assertThatThrownBy(() -> companyServices.getCompanyDetails(9999))
                .as("test when company does not exist")
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("no such company to get");
    }
    @Test
    void addCoupon() {
        log.info(" *********************** Testing addCoupon *********************** ");
        Company company = TestsUtils.createCompanies(1).get(0);
        adminServices.addCompany(company);
        Coupon coupon= TestsUtils.createCoupons(company,1).get(0);

        log.info(" *********************** Testing addCoupon - adding coupon *********************** ");
        Assertions.assertThatCode(() -> companyServices.addCoupon(coupon))
                .as("test if adding coupon does not throw any exception")
                .doesNotThrowAnyException();

        Assertions.assertThat(companyServices.getCompanyCoupons(company.getId()).stream().filter(c -> c.getTitle().equals(coupon.getTitle())
                        && c.getDescription().equals(coupon.getDescription())).count())
                .as("test if adding coupon was successful")
                .isEqualTo(1);

        log.info("Testing addCoupon - *********************** adding existing coupon *********************** ");
        Assertions.assertThatThrownBy(() -> companyServices.addCoupon(coupon))
                .as("test if adding exist coupon does throw exception")
                .isInstanceOf(CouponException.class)
                .hasMessageContaining("Coupon already exists");

    }
    @Test
    void updateCoupon() {
        log.info(" *********************** Testing updateCoupon *********************** ");
        Company company = TestsUtils.createCompanies(1).get(0);
        adminServices.addCompany(company);
        Coupon coupon= TestsUtils.createCoupons(company,1).get(0);
        companyServices.addCoupon(coupon);

        Coupon couponDb = companyServices.getCompanyCoupons(company.getId()).get(0);
        Double price = coupon.getPrice()/2;
        couponDb.setPrice(price);

        log.info("Testing updateCoupon - *********************** updating coupon *********************** ");
        Assertions.assertThatCode(() -> companyServices.updateCoupon(couponDb)).doesNotThrowAnyException();

        Assertions.assertThat(companyServices.getCompanyCoupons(company.getId()).get(0).getPrice())
                .as("test if updated coupon was updated successful")
                .isEqualTo(price);

        //NoSuchElementException
        log.info("Testing updateCoupon - *********************** updating non existing coupon *********************** ");
        Coupon coupon2 = TestsUtils.createCoupons(company,1).get(0);

        Assertions.assertThatThrownBy(() -> companyServices.updateCoupon(coupon2))
                .as("Test if updating a non-exists coupon throws NoSuchElementException")
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Coupon does not exists");
    }

    @Test
    void deleteCoupon() {
        log.info(" *********************** Testing deleteCoupon *********************** ");
        Company company = TestsUtils.createCompanies(1).get(0);
        adminServices.addCompany(company);
        Coupon coupon= TestsUtils.createCoupons(company,1).get(0);
        companyServices.addCoupon(coupon);
        Customer customer =TestsUtils.createCustomers(1).get(0);
        adminServices.addCustomer(customer);
        customerServices.couponPurchase(customer.getId(), coupon.getId());


        log.info("Testing deleteCoupon - *********************** deleting coupon *********************** ");
        Assertions.assertThatCode(() -> companyServices.deleteCoupon(coupon.getId()))
                .doesNotThrowAnyException();

        //NoSuchElementException
        Assertions.assertThatThrownBy(() -> companyServices.deleteCoupon(coupon.getId()))
                .as("test if coupon was deleted successful")
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("coupon does not exist");

    }
    @Test
    void getCompanyCoupons() {
        log.info(" *********************** Testing getCompanyCoupons *********************** ");
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
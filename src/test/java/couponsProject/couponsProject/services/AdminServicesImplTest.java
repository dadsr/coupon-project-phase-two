package couponsProject.couponsProject.services;

import couponsProject.couponsProject.TestsUtils;
import couponsProject.couponsProject.beans.Company;
import couponsProject.couponsProject.beans.Coupon;
import couponsProject.couponsProject.beans.Customer;
import couponsProject.couponsProject.exseptions.CompanyException;
import couponsProject.couponsProject.exseptions.CustomerException;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@SpringBootTest
class AdminServicesImplTest {
    @Autowired
    private AdminServices adminServices;
    @Autowired
    private CompanyServices companyServices;
    @Autowired
    private CustomerServices customerServices;


    /********************************** companies *************************************/


    @BeforeEach
    @Test
    void addCompany() {
        log.info(" *********************** Testing addCompany *********************** ");
        Company company = TestsUtils.createCompanies(1).get(0);

        log.info("Testing addCompany - *********************** adding company does not throw any exception *********************** ");
        Assertions.assertThatCode(
                        () -> adminServices.addCompany(company))
                .as("addCompany - testing adding company does not throw any exception")
                .doesNotThrowAnyException();

        log.info("Testing addCompany - *********************** testing add company *********************** ");
        Assertions.assertThat(
                        adminServices.getAllCompanies().stream().filter(c -> c.getName().equals(company.getName())&& c.getEmail().equals(company.getEmail())).count())
                .as("addCompany - testing add company")
                .isEqualTo(1);

        log.info("Testing addCompany - *********************** adding exist company does throw exception *********************** ");
        Assertions.assertThatThrownBy(
                        () -> adminServices.addCompany(company))
                .as("addCompany - testing if adding exist company does throw exception")
                .isInstanceOf(CompanyException.class)
                .hasMessageContaining("company already exists");

    }
    @Test
    void updateCompany() {
        log.info(" *********************** Testing updateCompany *********************** ");
        Company company = TestsUtils.createCompanies(1).get(0);
        adminServices.addCompany(company);
        String updatedEmail ="update" + company.getEmail();
        company.setEmail(updatedEmail);

        log.info("Testing updateCompany - *********************** update does not throw exception *********************** ");
        Assertions.assertThatCode(() -> adminServices.updateCompany(company))
                .as("updateCompany - update does not throw exception")
                .doesNotThrowAnyException();

        log.info("Testing updateCompany - *********************** updated company was updated successful *********************** ");
        Assertions.assertThat(
                        adminServices.getOneCompany(company.getId()).getEmail())
                .as("updateCompany - test if updated company was updated successful")
                .isEqualTo(updatedEmail);

        log.info("Testing updateCompany - *********************** updating a non-existent company *********************** ");
        //creating non existing company and updating it
        Company company2 = TestsUtils.createCompanies(1).get(0);

        Assertions.assertThatThrownBy(() -> adminServices.updateCompany(company2))
                .as("updateCompany - Test if updating a non-existent company throws NoSuchElementException")
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Company does not exist");
    }
    @Test
    void getOneCompany() {

        log.info(" *********************** Testing getOneCompany *********************** ");
        Company company = TestsUtils.createCompanies(1).get(0);
        adminServices.addCompany(company);

        log.info("Testing getOneCompany - *********************** getting company by id *********************** ");
        Assertions.assertThat(company)
                .as("getOneCompany - test getting company by id")
                .isNotNull()
                .hasFieldOrPropertyWithValue("id",company.getId());

        log.info("Testing getOneCompany - *********************** when company dose not exist *********************** ");
        Assertions.assertThatThrownBy(() -> adminServices.getOneCompany(9999))
                .as("getOneCompany - test when company dose not exist")
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("no such element");
    }
    @Test
    void deleteCompany() {

        log.info(" *********************** Testing deleteCompany *********************** ");
        Company company = TestsUtils.createCompanies(1).get(0);
        adminServices.addCompany(company);

        List<Coupon> coupons =TestsUtils.createCoupons(company,3);
        for (Coupon coupon : coupons) {
            companyServices.addCoupon(coupon);
        }
        coupons = companyServices.getCompanyCoupons(company.getId());

        Customer customer = TestsUtils.createCustomers(1).get(0);
        adminServices.addCustomer(customer);
        customer =customerServices.getCustomer(customerServices.login(customer.getEmail(),customer.getPassword()));

        for (Coupon coupon : coupons) {
            customerServices.couponPurchase(customer.getId(),coupon.getId());
        }

        log.info("Testing deleteCompany - *********************** deletion of company *********************** ");
        Assertions.assertThatCode(() -> adminServices.deleteCompany(company.getId()))
                .as("deleteCompany - test deletion of company")
                .doesNotThrowAnyException();

        Assertions.assertThat(customerServices.getCoupons(customer.getId()))
                .as("deleteCompany - *********************** test deletion of coupons and purchases *********************** ")
                .isNullOrEmpty();

        log.info("Testing deleteCompany - *********************** deletion of non existing company *********************** ");
        Assertions.assertThatThrownBy(
                        () -> adminServices.deleteCompany(company.getId()))
                .as("test if company was deleted successful")
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Company does not exist");
    }
    @Test
    void getAllCompanies() {

        log.info(" *********************** Testing getAllCompanies *********************** ");
        int size = adminServices.getAllCompanies().size();

        List<Company> companies = TestsUtils.createCompanies(10);

        for (Company company : companies) {
            adminServices.addCompany(company);
        }

        Assertions.assertThat(adminServices.getAllCompanies()).hasSize(size +10);

    }
    /********************************** customers *************************************/
    @Test
    void addCustomer() {

        log.info(" *********************** Testing addCustomer *********************** ");
        Customer customer = TestsUtils.createCustomers(1).get(0);

        log.info("Testing addCustomer - *********************** adding customer *********************** ");
        Assertions.assertThatCode(() -> adminServices.addCustomer(customer))
                .as("test if adding customer does not throw any exception")
                .doesNotThrowAnyException();

        Assertions.assertThat(
                        adminServices.getAllCustomers().stream().filter(
                                        c ->c.getFirstName().equals(customer.getFirstName())
                                                && c.getLastName().equals(customer.getLastName())
                                                && c.getEmail().equals(customer.getEmail()))
                                .count())
                .as("test if adding customer was successful")
                .isEqualTo(1);

        log.info("Testing addCustomer - *********************** adding existing customer *********************** ");
        Assertions.assertThatThrownBy(
                        () -> adminServices.addCustomer(customer))
                .as("test if adding existing customer does not throw exception")
                .isInstanceOf(CustomerException.class)
                .hasMessageContaining("customer already exists");
    }
    @Test
    void updateCustomer() {

        log.info(" *********************** Testing updateCustomer *********************** ");
        Customer customer = TestsUtils.createCustomers(1).get(0);
        adminServices.addCustomer(customer);
        customer.setEmail("update" + customer.getEmail());

        log.info("Testing updateCustomer - *********************** updating customer *********************** ");
        Assertions.assertThatCode(() -> adminServices.updateCustomer(customer)).doesNotThrowAnyException();

        Assertions.assertThat(
                        adminServices.getOneCustomer(customer.getId()).getEmail())
                .as("test if updated customer was updated successful")
                .isEqualTo(customer.getEmail());

        //NoSuchElementException
        log.info("Testing updateCustomer - *********************** updating non-existent customer *********************** ");
        Customer customer2 = TestsUtils.createCustomers(1).get(0);
        Assertions.assertThatThrownBy(() -> adminServices.updateCustomer(customer2))
                .as("Test if updating a non-existent customer throws NoSuchElementException")
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("customer does not exist");
    }
    @Test
    void getOneCustomer() {
        log.info(" *********************** Testing getOneCustomer *********************** ");
        Customer customer = TestsUtils.createCustomers(1).get(0);
        adminServices.addCustomer(customer);

        Customer customerDb = adminServices.getOneCustomer(customer.getId());

        log.info("Testing getOneCustomer - *********************** getting customer by id *********************** ");
        Assertions.assertThat(customerDb)
                .as("test getting customer by id")
                .isNotNull()
                .hasFieldOrPropertyWithValue("id",customer.getId());

        log.info("Testing getOneCustomer - *********************** getting non existing customer by id *********************** ");
        Assertions.assertThatThrownBy(() -> adminServices.getOneCustomer(9999))
                .as("test when customer dose not exist")
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("customer dose not exists");
    }
    @Test
    void deleteCustomer() {

        log.info(" *********************** Testing deleteCustomer *********************** ");
        Customer customer = TestsUtils.createCustomers(1).get(0);
        adminServices.addCustomer(customer);

        log.info("Testing deleteCustomer - *********************** deleting customer by id *********************** ");
        Assertions.assertThatCode(() -> adminServices.deleteCustomer(customer.getId())).doesNotThrowAnyException();
        //NoSuchElementException
        Assertions.assertThatThrownBy(() -> adminServices.deleteCustomer(customer.getId()))
                .as("test if customer was deleted successful")
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("customer does not exists");

    }
    @Test
    void getAllCustomers() {

        log.info(" *********************** Testing getAllCustomers *********************** ");
        int size = adminServices.getAllCustomers().size();
        List<Customer> customers = TestsUtils.createCustomers(10);

        for (Customer customer : customers) {
            adminServices.addCustomer(customer);
        }
        Assertions.assertThat(adminServices.getAllCustomers()).hasSize(size +10);
    }
}
package couponsProject.couponsProject.services;

import couponsProject.couponsProject.TestsUtils;
import couponsProject.couponsProject.beans.Company;
import couponsProject.couponsProject.beans.Coupon;
import couponsProject.couponsProject.beans.Customer;
import couponsProject.couponsProject.controllers.exseptions.CompanyException;
import couponsProject.couponsProject.controllers.exseptions.CustomerException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.NoSuchElementException;

@SpringBootTest
class AdminServicesImplTest {
    @Autowired
    private AdminServices adminServices;
    @Autowired
    private CompanyServices companyServices;
    @Autowired
    private CustomerServices customerServices;


/********************************** companies *************************************/

    @Test
    void Login() {
        //admin@admin.com:admin
        Assertions.assertThat(adminServices.login("admin@admin.com", "admin"))
                .as("test login success")
                .isEqualTo(1);
        Assertions.assertThat(adminServices.login("wrongemail@admin.com", "admin"))
                .as("test login wrong mail Failure")
                .isEqualTo(0);
        Assertions.assertThat(adminServices.login("admin@admin.com", "wrongpassword"))
                .as("test login wrong password Failure")
                .isEqualTo(0);
    }
@BeforeEach
    @Test
    void addCompany() {
        Company company = TestsUtils.createCompanies(1).get(0);

        Assertions.assertThatCode(
                () -> adminServices.addCompany(company))
                .as("addCompany - testing adding company does not throw any exception")
                .doesNotThrowAnyException();

        Assertions.assertThat(
                adminServices.getAllCompanies().stream().filter(c -> c.getName().equals(company.getName())&& c.getEmail().equals(company.getEmail())).count())
                .as("addCompany - testing add company")
                .isEqualTo(1);


        Assertions.assertThatThrownBy(
                () -> adminServices.addCompany(company))
                .as("addCompany - testing if adding exist company does throw exception")
                .isInstanceOf(CompanyException.class)
                .hasMessageContaining("company already exists");

    }

    @Test
    void updateCompany() {
        Company company = TestsUtils.createCompanies(1).get(0);
        adminServices.addCompany(company);
        String updatedEmail ="update" + company.getEmail();
        company.setEmail(updatedEmail);

        Assertions.assertThatCode(() -> adminServices.updateCompany(company))
                .as("updateCompany - update does not throw exception")
                .doesNotThrowAnyException();

        Assertions.assertThat(
                adminServices.getOneCompany(company.getId()).getEmail())
                .as("updateCompany - test if updated company was updated successful")
                .isEqualTo(updatedEmail);

        //creating non existing company and updateing it
        Company company2 = TestsUtils.createCompanies(1).get(0);

        Assertions.assertThatThrownBy(() -> adminServices.updateCompany(company2))
                .as("updateCompany - Test if updating a non-existent company throws NoSuchElementException")
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Company does not exist");
    }

    @Test
    void getOneCompany() {
        Company company = TestsUtils.createCompanies(1).get(0);
        adminServices.addCompany(company);
        int id =companyServices.login(company.getEmail(),company.getPassword());

        Assertions.assertThat(company).as("getOneCompany - test getting company by id").isNotNull().hasFieldOrPropertyWithValue("id",id);

        Assertions.assertThatThrownBy(() -> adminServices.getOneCompany(9999))
                .as("getOneCompany - test when company dose not exist")
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("no such element");
    }
//todo
    @Test
    void deleteCompany() {
        Company company = TestsUtils.createCompanies(1).get(0);
        adminServices.addCompany(company);
        Company companyDb = companyServices.getCompanyDetails(companyServices.login(company.getEmail(),company.getPassword()));

        List<Coupon> coupons =TestsUtils.createCoupons(companyDb,3);
        for (Coupon coupon : coupons) {
            companyServices.addCoupon(coupon);
        }
        coupons = companyServices.getCompanyCoupons(companyDb.getId());

        Customer customer = TestsUtils.createCustomers(1).get(0);
        adminServices.addCustomer(customer);
        customer =customerServices.getCustomer(customerServices.login(customer.getEmail(),customer.getPassword()));

        for (Coupon coupon : coupons) {
            customerServices.couponPurchase(customer.getId(),coupon.getId());
        }


        Assertions.assertThatCode(() -> adminServices.deleteCompany(companyDb.getId()))
                .as("deleteCompany - test deletion of company")
                .doesNotThrowAnyException();

        Assertions.assertThat(customerServices.getCoupons(customer.getId()))
                .as("deleteCompany - test deletion of coupons and purchases")
                .isNullOrEmpty();

        //NoSuchElementException
        Assertions.assertThatThrownBy(
                () -> adminServices.deleteCompany(companyDb.getId()))
                .as("test if company was deleted successful")
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Company does not exist");
    }

    @Test
    void getAllCompanies() {
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
        Customer customer = TestsUtils.createCustomers(1).get(0);

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

        Assertions.assertThatThrownBy(
                () -> adminServices.addCustomer(customer))
                .as("test if adding customer does not throw exception")
                .isInstanceOf(CustomerException.class)
                .hasMessageContaining("customer already exists");
    }

    @Test
    void updateCustomer() {
        Customer customer = TestsUtils.createCustomers(1).get(0);
        adminServices.addCustomer(customer);
        customer.setEmail("update" + customer.getEmail());

        Assertions.assertThatCode(() -> adminServices.updateCustomer(customer)).doesNotThrowAnyException();
        Assertions.assertThat(
                        adminServices.getOneCustomer(customer.getId()).getEmail())
                .as("test if updated customer was updated successful")
                .isEqualTo(customer.getEmail());

        //NoSuchElementException
        Customer customer2 = TestsUtils.createCustomers(1).get(0);
        Assertions.assertThatThrownBy(() -> adminServices.updateCustomer(customer2))
                .as("Test if updating a non-existent customer throws NoSuchElementException")
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("customer does not exist");
    }
    @Test
    void getOneCustomer() {
        Customer customer = TestsUtils.createCustomers(1).get(0);
        adminServices.addCustomer(customer);

        Customer customerDb = adminServices.getOneCustomer(customer.getId());

        Assertions.assertThat(customerDb).as("test getting customer by id").isNotNull().hasFieldOrPropertyWithValue("id",customer.getId());

        Assertions.assertThatThrownBy(() -> adminServices.getOneCustomer(9999))
                .as("test when customer dose not exist")
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("customer dose not exists");
    }

    @Test
    void deleteCustomer() {
        Customer customer = TestsUtils.createCustomers(1).get(0);
        adminServices.addCustomer(customer);

        Assertions.assertThatCode(() -> adminServices.deleteCustomer(customer.getId())).doesNotThrowAnyException();
        //NoSuchElementException
        Assertions.assertThatThrownBy(() -> adminServices.deleteCustomer(customer.getId()))
                .as("test if customer was deleted successful")
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("customer does not exists");

    }

    @Test
    void getAllCustomers() {
        int size = adminServices.getAllCustomers().size();

        List<Customer> customers = TestsUtils.createCustomers(10);

        for (Customer customer : customers) {
            adminServices.addCustomer(customer);
        }
        Assertions.assertThat(adminServices.getAllCustomers()).hasSize(size +10);
    }
}
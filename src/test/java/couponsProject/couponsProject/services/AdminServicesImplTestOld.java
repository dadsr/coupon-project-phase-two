package couponsProject.couponsProject.services;

import couponsProject.couponsProject.beans.Company;
import couponsProject.couponsProject.beans.Customer;
import couponsProject.couponsProject.controllers.exseptions.CompanyException;
import couponsProject.couponsProject.controllers.exseptions.CustomerException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.NoSuchElementException;

@SpringBootTest
class AdminServicesImplTestOld {
    @Autowired
    private AdminServices adminServices;
    @Autowired
    private CompanyServices companyServices;
    @Autowired
    private CustomerServices customerServices;

/**********************************************************************************/
    int companiesSize = 9;
    String companyName = "compAndCo";
    String companyEmail ="compAndCo@comp.co.il";
    int clientsSize = 5;
    String clientEmail ="client@mail.co.il";

/********************************** companies *************************************/
    @Test
    void testLogin() {
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

    @Test
    void addCompany() {
        Company company = Company.builder()
                .name("compAndCo")
                .email(companyEmail)
                .password("123456789")
                .build();

        Assertions.assertThatCode(
                () -> adminServices.addCompany(company))
                .as("test if adding company does not throw any exception")
                .doesNotThrowAnyException();

        Assertions.assertThat(
                adminServices.getAllCompanies().stream().filter(c -> c.getName().equals(company.getName())&& c.getEmail().equals(company.getEmail())).count())
                .as("test if adding company was successful")
                .isEqualTo(1);


        Assertions.assertThatThrownBy(
                () -> adminServices.addCompany(company))
                .as("test if adding exist company does throw exception")
                .isInstanceOf(CompanyException.class)
                .hasMessageContaining("company already exists");

    }

    @Test
    void updateCompany() {

        Company company = adminServices.getOneCompany(companyServices.login(companyName,companyEmail));
        String updatedEmail = "update" + company.getEmail();
        company.setEmail(updatedEmail);

        Assertions.assertThatCode(() -> adminServices.updateCompany(company)).doesNotThrowAnyException();
        Assertions.assertThat(
                adminServices.getOneCompany(company.getId()).getEmail())
                .as("test if updated company was updated successful")
                .isEqualTo(updatedEmail);

        //NoSuchElementException
        Assertions.assertThatThrownBy(
                () -> adminServices.updateCompany(company))
                .as("Test if updating a non-existent company throws NoSuchElementException")
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Company does not exist");
    }

    @Test
    void getOneCompany() {
        int id =companyServices.login(companyEmail,"123456789");
        Company company = adminServices.getOneCompany(id);
        Assertions.assertThat(company).as("test getting company by id").isNotNull().hasFieldOrPropertyWithValue("id",id);

        Assertions.assertThatThrownBy(() -> adminServices.getOneCompany(9999))
                .as("test when company dose not exist")
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("no such company to get");
    }

    @Test
    void deleteCompany() {
        int id =companyServices.login(companyEmail,"123456789");

        Assertions.assertThatCode(() -> adminServices.deleteCompany(id)).as("test of deleteCompany").doesNotThrowAnyException();
        //test deletion of purchases
        //test deletion of  coupons


        //NoSuchElementException
        Assertions.assertThatThrownBy(
                () -> adminServices.deleteCompany(id))
                .as("test if company was deleted successful")
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Company does not exist");
    }

    @Test
    void getAllCompanies() {
        Assertions.assertThat(adminServices.getAllCompanies()).hasSize(companiesSize);
    }

    /********************************** customers *************************************/
    @Test
    void addCustomer() {
        Customer customer =Customer.builder()
                .firstName("first")
                .lastName("last")
                .email(clientEmail)
                .password("987654321")
                .build();

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
        Customer customer = adminServices.getOneCustomer(customerServices.login(clientEmail,"987654321"));
        String updatedEmail = "update" + customer.getEmail();
        customer.setEmail(updatedEmail);

        Assertions.assertThatCode(() -> adminServices.updateCustomer(customer)).doesNotThrowAnyException();
        Assertions.assertThat(
                        adminServices.getOneCustomer(customer.getId()).getEmail())
                .as("test if updated customer was updated successful")
                .isEqualTo(updatedEmail);

        //NoSuchElementException
        Assertions.assertThatThrownBy(() -> adminServices.updateCustomer(customer))
                .as("Test if updating a non-existent customer throws NoSuchElementException")
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Customer does not exist");
    }
    @Test
    void getOneCustomer() {
        int id =customerServices.login(clientEmail,"987654321");
        Customer customer = adminServices.getOneCustomer(id);
        Assertions.assertThat(customer).as("test getting customer by id").isNotNull().hasFieldOrPropertyWithValue("id",id);

        Assertions.assertThatThrownBy(() -> adminServices.getOneCustomer(9999))
                .as("test when customer dose not exist")
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("customer dose not exists");
    }

    @Test
    void deleteCustomer() {
        int id =customerServices.login(clientEmail,"987654321");

        Assertions.assertThatCode(() -> adminServices.deleteCustomer(id)).doesNotThrowAnyException();
        //NoSuchElementException
        Assertions.assertThatThrownBy(() -> adminServices.deleteCustomer(id))
                .as("test if customer was deleted successful")
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Customer does not exist");

    }

    @Test
    void getAllCustomers() {
        Assertions.assertThat(adminServices.getAllCustomers()).hasSize(clientsSize);
    }
}
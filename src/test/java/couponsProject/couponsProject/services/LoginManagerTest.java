package couponsProject.couponsProject.services;

import couponsProject.couponsProject.TestsUtils;
import couponsProject.couponsProject.beans.Company;
import couponsProject.couponsProject.beans.Customer;
import couponsProject.couponsProject.exseptions.LoginManagerException;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class LoginManagerTest {

    @Autowired
    private AdminServices adminServices;
    @Autowired
    private CompanyServices companyServices;
    @Autowired
    private CustomerServices customerServices;
    @Autowired
    private LoginManager loginManager;


    @Test
    void login() {
        log.info(" *********************** Testing login *********************** ");

        log.info("Testing login - *********************** ADMINISTRATOR *********************** ");
        Assertions.assertThatCode(
                        ()-> loginManager.login("admin@admin.com", "admin", ClientTypeEnum.ADMINISTRATOR))
                .as("test login success")
                .doesNotThrowAnyException();
        try {
            ClientServices result = loginManager.login("admin@admin.com", "admin", ClientTypeEnum.ADMINISTRATOR);
            Assertions.assertThat(result)
                    .as("Login result should be an instance of AdminServices")
                    .isInstanceOf(AdminServices.class);
        } catch (LoginManagerException e) {
            throw new RuntimeException(e);
        }

        log.info("Testing login - *********************** ADMINISTRATOR wrong mail *********************** ");
        Assertions.assertThatThrownBy(
                        ()-> loginManager.login("wrongemail@admin.com", "admin",ClientTypeEnum.ADMINISTRATOR))
                .as("test login wrong mail Failure")
                .isInstanceOf(LoginManagerException.class)
                .hasMessageContaining("Your account name or password is incorrect.");

        log.info("Testing login - *********************** ADMINISTRATOR wrong password *********************** ");
        Assertions.assertThatThrownBy(
                        ()-> loginManager.login("admin@admin.com", "wrongPass",ClientTypeEnum.ADMINISTRATOR))
                .as("test login wrong password Failure")
                .isInstanceOf(LoginManagerException.class)
                .hasMessageContaining("Your account name or password is incorrect.");

        log.info("Testing login - *********************** ADMINISTRATOR wrong ClientType *********************** ");
        Assertions.assertThatThrownBy(
                        ()-> loginManager.login("wrongemail@admin.com", "admin",ClientTypeEnum.COMPANY))
                .as("test login wrong ClientType Failure")
                .isInstanceOf(LoginManagerException.class)
                .hasMessageContaining("Your account name or password is incorrect.");

        Assertions.assertThatThrownBy(
                        ()-> loginManager.login("wrongemail@admin.com", "admin",ClientTypeEnum.CUSTOMER))
                .as("test login wrong ClientType Failure")
                .isInstanceOf(LoginManagerException.class)
                .hasMessageContaining("Your account name or password is incorrect.");

        //COMPANY
        Company company = TestsUtils.createCompanies(1).get(0);
        adminServices.addCompany(company);

        log.info("Testing login - *********************** COMPANY wrong mail *********************** ");
        Assertions.assertThatThrownBy(
                        ()-> loginManager.login("wrongemail@admin.com",  company.getPassword(),ClientTypeEnum.COMPANY))
                .as("test login wrong mail Failure")
                .isInstanceOf(LoginManagerException.class)
                .hasMessageContaining("Your account name or password is incorrect.");

        log.info("Testing login - *********************** COMPANY wrong password *********************** ");
        Assertions.assertThatThrownBy(
                        ()-> loginManager.login( company.getEmail(), "wrongpassword",ClientTypeEnum.COMPANY))
                .as("test login wrong password Failure")
                .isInstanceOf(LoginManagerException.class)
                .hasMessageContaining("Your account name or password is incorrect.");

        log.info("Testing login - *********************** COMPANY wrong ClientType *********************** ");
        Assertions.assertThatThrownBy(
                        ()-> loginManager.login( company.getEmail(), company.getPassword(),ClientTypeEnum.CUSTOMER))
                .as("test login wrong ClientType Failure")
                .isInstanceOf(LoginManagerException.class)
                .hasMessageContaining("Your account name or password is incorrect.");

        //CUSTOMER
        log.info("Testing login - *********************** CUSTOMER *********************** ");
        Customer customer = TestsUtils.createCustomers(1).get(0);
        adminServices.addCustomer(customer);

        Assertions.assertThatCode(
                        ()-> loginManager.login(customer.getEmail(), customer.getPassword(),ClientTypeEnum.CUSTOMER))
                .as("test login success")
                .doesNotThrowAnyException();
        try {
            ClientServices result = loginManager.login(customer.getEmail(), customer.getPassword(),ClientTypeEnum.CUSTOMER);
            Assertions.assertThat(result)
                    .as("Login result should be an instance of CustomerServices")
                    .isInstanceOf(CustomerServices.class);
        } catch (LoginManagerException e) {
            throw new RuntimeException(e);
        }

        log.info("Testing login - *********************** CUSTOMER wrong mail *********************** ");
        Assertions.assertThatThrownBy(
                        ()-> loginManager.login("wrongemail@admin.com", customer.getPassword(),ClientTypeEnum.CUSTOMER))
                .as("test login wrong mail Failure")
                .isInstanceOf(LoginManagerException.class)
                .hasMessageContaining("Your account name or password is incorrect.");

        log.info("Testing login - *********************** CUSTOMER wrong password *********************** ");
        Assertions.assertThatThrownBy(
                        ()-> loginManager.login(customer.getEmail(), "wrongpassword",ClientTypeEnum.CUSTOMER))
                .as("test login wrong password Failure")
                .isInstanceOf(LoginManagerException.class)
                .hasMessageContaining("Your account name or password is incorrect.");

        log.info("Testing login - *********************** CUSTOMER wrong ClientType *********************** ");
        Assertions.assertThatThrownBy(
                        ()-> loginManager.login(customer.getEmail(), customer.getPassword(),ClientTypeEnum.COMPANY))
                .as("test login wrong ClientType Failure")
                .isInstanceOf(LoginManagerException.class)
                .hasMessageContaining("Your account name or password is incorrect.");
    }
}
package couponsProject.couponsProject.services;

import couponsProject.couponsProject.beans.Company;
import couponsProject.couponsProject.controllers.exseptions.CompanyException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AdminServicesImplTest {
    @Autowired
    private AdminServices adminServices;

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
                .email("compAndCo@comp.co.il")
                .password(String.valueOf(Math.random()))
                .build();

        Assertions.assertThatCode(() -> adminServices.addCompany(company))
                .as("test if adding company does not throw any exception")
                .doesNotThrowAnyException();

        Assertions.assertThat(
                adminServices.getAllCompanies().stream().filter(c -> c.getName().equals(company.getName())&& c.getEmail().equals(company.getEmail())).count())
                .as("test if adding company was successful")
                .isEqualTo(1);


        Assertions.assertThatThrownBy(() -> adminServices.addCompany(company))
                .as("test if adding company does not throw exception")
                .isInstanceOf(CompanyException.class)
                .hasMessageContaining("company already exists");

    }

    @Test
    void updateCompany() {
        int id =1;
        Company company = adminServices.getOneCompany(id);
        String updatedEmail = "update" + company.getEmail();
        company.setEmail(updatedEmail);

        Assertions.assertThatCode(() -> adminServices.updateCompany(company)).doesNotThrowAnyException();
        Assertions.assertThat(
                adminServices.getOneCompany(id).getEmail())
                .as("test if updated company was updated successful")
                .isEqualTo(updatedEmail);

        //NoSuchElementException
        //id is empty
        Assertions.assertThatThrownBy(() -> adminServices.updateCompany(company))
                .as("Test if updating a non-existent company throws NoSuchElementException")
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Company does not exist");
    }

    @Test
    void deleteCompany() {
    }

    @Test
    void getOneCompany() {
    }

    @Test
    void getAllCompanies() {
    }

    @Test
    void addCustomer() {
    }

    @Test
    void updateCustomer() {
    }

    @Test
    void deleteCustomer() {
    }

    @Test
    void getOneCustomer() {
    }

    @Test
    void getAllCustomers() {
    }
}
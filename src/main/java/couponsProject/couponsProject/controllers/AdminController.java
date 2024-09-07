package couponsProject.couponsProject.controllers;

import couponsProject.couponsProject.Company;
import couponsProject.couponsProject.beans.Customer;
import couponsProject.couponsProject.services.AdminServices;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@AllArgsConstructor
@Slf4j
@RequestMapping("admin")
@ControllerAdvice
@RestController
public class AdminController {
    AdminServices adminServices;

    @GetMapping("login")
    public int login(String email, String password) {
        return adminServices.login(email, password);
    }

    @PostMapping("company/add")
    public void addCompany(Company company) {
        adminServices.addCompany(company);
    }

    @PutMapping("company/update")
    public void updateCompany(Company company) {
        adminServices.updateCompany(company);
    }
    @DeleteMapping("company/delete")
    public void deleteCompany(int companyID) {
        adminServices.deleteCompany(companyID);
    }

    @GetMapping("company/get")
    public Company getOneCompany(int companyID) {
        return adminServices.getOneCompany(companyID);
    }

    @GetMapping("company/all")
    public ArrayList<Company> getAllCompanies() {
        return adminServices.getAllCompanies();
    }

    @PostMapping("customer/add")
    public void addCustomer(Customer customer) {
        adminServices.addCustomer(customer);
    }

    @PutMapping("customer/update")
    public void updateCustomer(Customer customer) {
        adminServices.updateCustomer(customer);
    }

    @DeleteMapping("customer/delete")
    public void deleteCustomer(int customerID) {
        adminServices.deleteCustomer(customerID);
    }

    @GetMapping("customer/get")
    public Customer getOneCustomer(int customerID) {
        return adminServices.getOneCustomer(customerID);
    }

    @GetMapping("customer/all")
    public ArrayList<Customer> getAllCustomers() {
        return adminServices.getAllCustomers();
    }

    //
}

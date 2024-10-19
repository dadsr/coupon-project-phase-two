package couponsProject.couponsProject.controllers;

import couponsProject.couponsProject.beans.Company;
import couponsProject.couponsProject.beans.Customer;
import couponsProject.couponsProject.services.AdminServices;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Slf4j
@RequestMapping("admin")
@ControllerAdvice
@RestController
public class AdminController {
    AdminServices adminServices;

    //admin@admin.com:admin
    @GetMapping("login")
    public int login(String email, String password) {
        return adminServices.login(email, password);
    }

    @PostMapping("company/add")
    public void addCompany(String name, String email, String password) {
   //     adminServices.addCompany(new Company(name, email, password));
    }

    @PutMapping("company/update")
    public void updateCompany(int id, String name, String email, String password)  {
      //  adminServices.updateCompany(new Company(id, name, email, password));
    }
    @DeleteMapping("company/delete")
    public void deleteCompany(int companyID) {
  //      adminServices.deleteCompany(companyID);
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
    public void addCustomer(String firstName, String lastName, String email, String password) {
       // adminServices.addCustomer(new Customer(firstName,lastName,email,password));
    }

    @PutMapping("customer/update")
    public void updateCustomer(int id, String firstName, String lastName, String email, String password) {
      //  adminServices.updateCustomer(new Customer(id,firstName,lastName,email,password));
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
    public List<Customer> getAllCustomers() {
        return adminServices.getAllCustomers();
    }

    //
}

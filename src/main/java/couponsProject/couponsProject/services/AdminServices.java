package couponsProject.couponsProject.services;

import couponsProject.couponsProject.beans.Company;
import couponsProject.couponsProject.beans.Customer;

import java.util.ArrayList;

public interface AdminServices {
    int login(String email, String password);

    void addCompany(Company company);

    //todo is it possible to change Email ? this can cause problems in DB
    void updateCompany(Company company);

    void deleteCompany(int companyID);

    Company getOneCompany(int companyID);

    //todo
    //  @Override
    ArrayList<Company> getAllCompanies();

    void addCustomer(Customer customer);

    void updateCustomer(Customer customer);

    void deleteCustomer(int customerID);

    Customer getOneCustomer(int customerID);

    //todo
    ArrayList<Customer> getAllCustomers();

}

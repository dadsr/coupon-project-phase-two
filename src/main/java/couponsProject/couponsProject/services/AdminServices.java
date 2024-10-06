package couponsProject.couponsProject.services;

import couponsProject.couponsProject.beans.Company;
import couponsProject.couponsProject.beans.Coupon;
import couponsProject.couponsProject.beans.Customer;

import java.util.ArrayList;
import java.util.List;

public interface AdminServices {
    int login(String email, String password);

    void addCompany(Company company);

    void updateCompany(Company company);

    void deleteCompany(int companyID);

    Company getOneCompany(int companyID);

    ArrayList<Company> getAllCompanies();

    void addCustomer(Customer customer);

    void updateCustomer(Customer customer);

    void deleteCustomer(int customerID);

    Customer getOneCustomer(int customerID);

    ArrayList<Customer> getAllCustomers();

    List<Coupon> getAllCoupons();

}

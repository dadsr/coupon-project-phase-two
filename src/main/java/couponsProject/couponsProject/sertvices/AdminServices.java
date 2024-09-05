package couponsProject.couponsProject.sertvices;

import couponsProject.couponsProject.Company;
import couponsProject.couponsProject.beans.Customer;

import java.util.ArrayList;

public interface AdminServices {

    int login(String email, String password);

    void addCompany(Company company);

    void addCustomer(Customer customer);

    void updateCompany(Company company);

    void updateCustomer(Customer customer);

    void deleteCompany(int companyID);

    void deleteCustomer(int customerID);

    Company getOneCompany(int companyID);

    Customer getOneCustomer(int customerID);

}

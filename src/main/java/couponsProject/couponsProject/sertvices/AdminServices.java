package couponsProject.couponsProject.sertvices;

import couponsProject.couponsProject.Company;
import couponsProject.couponsProject.beans.Customer;

import java.util.ArrayList;

public interface AdminServices {
    //todo
    int login(String email, String password);

    void addCompany(Company company);

    void addCustomer(Customer customer);

    void updateCompany(Company company);

    void updateCustomer(Customer customer);

    void deleteCompany(int companyID);

    void deleteCustomer(int customerID);

    Company getOneCompany(int companyID);

    Company getOneCompany(String email, String password);

    Customer getOneCustomer(int customerID);

    Customer getOneCustomer(String email, String password);

}

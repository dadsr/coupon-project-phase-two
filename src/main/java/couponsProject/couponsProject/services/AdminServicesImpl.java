package couponsProject.couponsProject.services;


import couponsProject.couponsProject.beans.Company;
import couponsProject.couponsProject.beans.Coupon;
import couponsProject.couponsProject.beans.Customer;
import couponsProject.couponsProject.exseptions.CompanyException;
import couponsProject.couponsProject.exseptions.CustomerException;
import couponsProject.couponsProject.repository.CompanyRepository;
import couponsProject.couponsProject.repository.CouponRepository;
import couponsProject.couponsProject.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@AllArgsConstructor
@Slf4j
@Service
@Scope("singleton")
public class AdminServicesImpl implements AdminServices{
    private final CompanyRepository companyRepository;
    private final CustomerRepository customerRepository;
    private final CouponRepository couponRepository;

    /**
     * Authenticates an admin user.
     *
     * @param email The email address for login
     * @param password The password for login
     * @return 1 if authentication is successful (admin credentials match), 0 otherwise
     * @Override Overrides the login method from a parent class or interface
     */
    @Override
    public int login(String email, String password){
        log.info("Entering login using Email: {} Password: {}", email, password);
        return (Objects.equals(email, "admin@admin.com") && Objects.equals(password, "admin"))?1:0;
    }

    /****************************** Company methods **********************************/

    /**
     * Adds a new company to the system.
     *
     * @param company The Company object to be added
     * @throws CompanyException if a company with the same name or email already exists
     * @Override Overrides the addCompany method from a parent class or interface
     */
    @Override
    public void addCompany(Company company)  {
        log.info("entering addCompany company name:{} and company email:{}",company.getName(), company.getEmail());
        if(!companyRepository.existsByNameOrEmail(company.getName(),company.getEmail())) {
            companyRepository.save(company);
        }else {
            log.error("Company already exist for name:{} or email:{}", company.getName(), company.getEmail());
            throw new CompanyException("company already exists");
        }
    }

    /**
     * Updates an existing company in the system.
     *
     * @param company The Company object with updated information
     * @throws NoSuchElementException if no company exists with the given ID
     * @Override Overrides the updateCompany method from a parent class or interface
     */
    @Override
    public void updateCompany(Company company){
        log.info("entering updateCompany, using company id:{}",company.getId());
        if(companyRepository.existsById(company.getId())){
            companyRepository.save(company);
            log.debug("updateCompany succeeded, company id:{}",company.getId());
        }else{
            log.error("No such company to update, company id:{}",company.getId());
            throw new NoSuchElementException("Company does not exist");
        }
    }

    /**
     * Deletes a company and its associated coupons from the system.
     * This method is transactional to ensure data consistency.
     *
     * @param companyID The ID of the company to be deleted
     * @throws NoSuchElementException if no company exists with the given ID
     * @Transactional Ensures that all operations within the method are part of a single transaction
     * @Override Overrides the deleteCompany method from a parent class or interface
     */
    @Transactional
    @Override
    public void deleteCompany(int companyID) {
        log.info("Entering deleteCompany, using company id:{}",companyID);

        log.info("Entering companyRepository.findById company id:{}",companyID);
        Company company = companyRepository.findById(companyID)
                .orElseThrow(()-> {
                    log.error("deleteCompany throw NoSuchElementException company id:{}",companyID);
                    return new NoSuchElementException("Company does not exist");
                });
        for (Coupon coupon : company.getCoupons()) {
            for (Customer customer : coupon.getCustomers()) {
                //detach customer
                customer.getCoupons().remove(coupon);
            }
        }

        log.info("Entering companyRepository.deleteAllIgnoreCase(company) delete company id:{}",companyID);
        companyRepository.delete(company);

    }

    /**
     * Retrieves a single company by its ID.
     *
     * @param companyId The ID of the company to retrieve
     * @return The Company object if found
     * @throws NoSuchElementException if no company exists with the given ID
     * @Override Overrides the getOneCompany method from a parent class or interface
     */
    @Override
    public Company getOneCompany(int companyId){
        log.info("Entering getOneCompany using company id: {}",companyId);
        if(companyRepository.existsById(companyId)) {
            return companyRepository.findCompaniesById(companyId);
        }else {
            log.error("getOneCompany throw NoSuchElementException company id: {}",companyId);
            throw new NoSuchElementException("no such element");
        }
    }

    /**
     * Retrieves all companies from the system.
     *
     * @return An ArrayList containing all Company objects
     * @Override Overrides the getAllCompanies method from a parent class or interface
     */
    @Override
    public ArrayList<Company> getAllCompanies(){
        log.info("entering getAllCompanies");
        return companyRepository.getAllCompanies();
    }

    /****************************** Customer methods **********************************/

    /**
     * Adds a new customer to the system.
     *
     * @param customer The Customer object to be added
     * @throws CustomerException if a customer with the same email already exists
     * @Override Overrides the addCustomer method from a parent class or interface
     */
    @Override
    public void addCustomer(Customer customer){
        log.info("Entering addCustomer customer email:{}",customer.getEmail());
        if(!customerRepository.existsByEmail(customer.getEmail())){
            customerRepository.save(customer);
        }else{
            log.error("addCustomer throw CustomerException email:{} already exists", customer.getEmail());
            throw new CustomerException("customer already exists");
        }
    }

    /**
     * Updates an existing customer in the system.
     *
     * @param customer The Customer object with updated information
     * @throws NoSuchElementException if no customer exists with the given ID
     * @Override Overrides the updateCustomer method from a parent class or interface
     */
    @Override
    public void updateCustomer(Customer customer){
        log.info("entering updateCustomer using customer id:{}",customer.getId());
        if(customerRepository.existsById(customer.getId())){
            customerRepository.save(customer);
        }else{
            log.error("updateCustomer throw NoSuchElementException customer id:{}",customer.getId());
            throw new NoSuchElementException("customer does not exist");
        }
    }

    /**
     * Deletes a customer from the system by their ID.
     *
     * @param customerID The ID of the customer to be deleted
     * @throws NoSuchElementException if no customer exists with the given ID
     * @Override Overrides the deleteCustomer method from a parent class or interface
     */
    @Override
    public void deleteCustomer(int customerID){
        log.info("entering deleteCustomer using customer id:{}",customerID);
        if(customerRepository.existsById(customerID)) {
            customerRepository.deleteById(customerID);
            log.debug("deleteCustomer succeeded, customer id:{}",customerID);
        }else {
            log.error("No such customer to delete, customer id:{}",customerID);
            throw new NoSuchElementException("customer does not exists");
        }
    }
    /**
     * Retrieves a single customer from the system by their ID.
     *
     * @param customerID The ID of the customer to retrieve
     * @return The Customer object if found
     * @throws NoSuchElementException if no customer exists with the given ID
     * @Override Overrides the getOneCustomer method from a parent class or interface
     */
    @Override
    public Customer getOneCustomer(int customerID){
        log.info("entering getOneCustomer using customer id:{}",customerID);
        Customer customer =customerRepository.getCustomerById(customerID);
        if(customer != null) {
            log.debug("getOneCustomer succeeded, customer id:{}",customerID);
            return customer;
        }else{
            log.error("No such customer to get, customer id:{}",customerID);
            throw new NoSuchElementException("customer dose not exists");
        }
    }

    /**
     * Retrieves all customers from the system.
     *
     * @return An ArrayList containing all Customer objects
     * @Override Overrides the getAllCustomers method from a parent class or interface
     */
    @Override
    public List<Customer> getAllCustomers(){
        log.info("entering getAllCustomers");
        return customerRepository.findAll();
    }

    /**
     * Retrieves a list of coupons that expire before the specified date.
     *
     * @param date The date to compare against coupon end dates
     * @return A list of Coupon objects with end dates before the given date
     */
    @Override
    public List<Coupon> findByEndDateBefore(java.sql.Date date) {
        log.info("entering findByEndDateBefore: {}", date);
        return couponRepository.findByEndDateBefore(date);
    }

    //
}

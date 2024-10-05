package couponsProject.couponsProject.services;


import couponsProject.couponsProject.beans.Company;
import couponsProject.couponsProject.beans.Coupon;
import couponsProject.couponsProject.beans.Customer;
import couponsProject.couponsProject.controllers.exseptions.CompanyException;
import couponsProject.couponsProject.controllers.exseptions.CustomerException;
import couponsProject.couponsProject.repository.CompanyRepository;
import couponsProject.couponsProject.repository.CouponRepository;
import couponsProject.couponsProject.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@AllArgsConstructor
@Slf4j
@Service
public class AdminServicesImpl implements AdminServices {
    private final CompanyRepository companyRepository;
    private final CustomerRepository customerRepository;
    private final CouponRepository couponRepository;


    @Override
    public int login(String email, String password){
        log.info("Entering login using Email: {} Password: {}", email, password);
        return (Objects.equals(email, "admin@admin.com") && Objects.equals(password, "admin"))?1:0;
    }

    /****************************** Company methods **********************************/

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

    //todo is it possible to change Email ? this can cause problems in DB
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

    //todo
    //  @Override
    @Override
    public ArrayList<Company> getAllCompanies(){
        log.info("entering getAllCompanies");
        return companyRepository.getAllCompanies();
    }

    /****************************** Customer methods **********************************/

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

    @Override
    public void deleteCustomer(int customerID){
        log.info("entering deleteCustomer using customer id:{}",customerID);
        if(customerRepository.existsById(customerID)) {
            //todo should check if cascade deletes perches too
            customerRepository.deleteById(customerID);
            log.debug("deleteCustomer succeeded, customer id:{}",customerID);
        }else {
            log.error("No such customer to delete, customer id:{}",customerID);
            throw new NoSuchElementException("customer does not exists");
        }
    }

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

    //todo
    // @Override
    @Override
    public ArrayList<Customer> getAllCustomers(){
        log.info("entering getAllCustomers");
        return customerRepository.findAll();
    }

    @Override
    public List<Coupon> getAllCoupons() {
        log.info("entering getAllCoupons");
        return couponRepository.findAll();
    }

    /****************************** service methods **********************************/

    // for company login
    private Company getOneCompany(String email, String password){
        log.info("entering getOneCompany using Email: {} Password: {}", email, password);
        Company company =companyRepository.findCompaniesByEmailAndPassword(email,password);
        if(company != null) {
            log.debug("getOneCompany succeeded, Email: {} Password: {}", email, password);
            return company;
        }else{
            log.error("No such Company, Email: {} Password: {}", email, password);
            throw new NoSuchElementException("no such company to get");
        }
    }
    // for customer login
    private Customer getOneCustomer(String email, String password){
        log.info("Entering getOneCustomer, using Email: {} Password: {}", email, password);
        Customer customer =customerRepository.findCustomerByEmailAndPassword(email,password);
        if(customer != null) {
            log.debug("getOneCustomer succeeded, Email: {} Password: {}", email, password);
            return customer;
        }else{
            log.error("getOneCustomer throw NoSuchElementException Email: {} Password: {}", email, password);
            throw new NoSuchElementException("Customer dose not exists");
        }
    }

    private void deleteCoupons(int companyId) {
        log.info("Entering deleteCoupons using company Id: {}", companyId);
        companyRepository.deleteById(companyId);
    }
    //
}

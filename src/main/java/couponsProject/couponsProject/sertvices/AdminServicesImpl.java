package couponsProject.couponsProject.sertvices;

import couponsProject.couponsProject.Company;
import couponsProject.couponsProject.beans.Customer;
import couponsProject.couponsProject.repository.CompanyRepository;
import couponsProject.couponsProject.repository.CouponRepository;
import couponsProject.couponsProject.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Objects;

@AllArgsConstructor
@Slf4j
@Service
public class AdminServicesImpl implements AdminServices {
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    private CouponRepository couponRepository;

    @Override
    public int login(String email, String password){
        log.info("entering login using Email: {} Password: {}", email, password);
        return (Objects.equals(email, "admin@admin.com") && Objects.equals(password, "admin"))?1:0;
    }

    /****************************** Company methods **********************************/

    @Override
    public void addCompany(Company company)  {
        log.info("entering addCompany company name: {} company email {}",company.getName(), company.getEmail());
        if(!companyRepository.existsByNameOrEmail(company.getName(),company.getEmail()) {
            companyRepository.save(company);
        }else {
            log.error("addCompany throw CompanyException name {} or email {} already exists", company.getName(), company.getEmail());
            throw new CompanyException("company name {} or email {} already exists" + company.getName()+ company.getEmail());
        }
    }

    //todo is it possible to change Email ? this can cause problems in DB
    @Override
    public void updateCompany(Company company){
        log.info("entering updateCompany using company id: {}",company.getId());
        if(companyRepository.existsById(company.getId())){
            companyRepository.save(company);
        }else{
            log.error("updateCompany throw NoSuchElementException company id: {}",company.getId());
            throw new NoSuchElementException("no such element");
        }
    }
    @Override
    public void deleteCompany(int companyID) {
        log.info("entering deleteCompany using company id: {}",companyID);
        if(companyRepository.existsById(companyID)) {
            companyRepository.deleteById(companyID);
            deleteCoupons(companyID);
        }else {
            log.error("deleteCompany throw NoSuchElementException customer id: {}",companyID);
            throw new NoSuchElementException("no such element");
        }
    }
    @Override
    public Company getOneCompany(int companyID){
        log.info("entering getOneCompany using company id: {}",companyID);
        if(companyRepository.existsById(companyID)) {
            return companyRepository.findCompaniesById(companyID);
        }else {
            log.error("getOneCompany throw NoSuchElementException company id: {}",companyID);
            throw new NoSuchElementException("no such element");
        }
    }

    //todo
    //  @Override
    public ArrayList<Company> getAllCompanies(){
        log.info("entering getAllCompanies");
        return null;
    }

    /****************************** Customer methods **********************************/

    @Override
    public void addCustomer(Customer customer){
        log.info("entering addCustomer customer email: {}",customer.getEmail());
        if(!customerRepository.existsByEmail(customer.getEmail())){
            customerRepository.save(customer);
        }else{
            log.error("addCustomer throw CustomerException email {} already exists", customer.getEmail());
            throw new CustomerException("customer email {} already exists" + customer.getEmail());
        }
    }

    @Override
    public void updateCustomer(Customer customer){
        log.info("entering updateCustomer using customer id: {}",customer.getId());
        if(customerRepository.existsById(customer.getId())){
            customerRepository.save(customer);
        }else{
            log.error("updateCustomer throw NoSuchElementException customer id: {}",customer.getId());
            throw new NoSuchElementException("no such element");
        }
    }

    @Override
    public void deleteCustomer(int customerID){
        log.info("entering deleteCustomer using customer id: {}",customerID);
        if(customerRepository.existsById(customerID)) {
            //todo should check if cascade deletes perches too
            customerRepository.deleteById(customerID);
        }else {
            log.error("deleteCustomer throw NoSuchElementException customer id: {}",customerID);
            throw new NoSuchElementException("no such element");
        }
    }

    @Override
    public Customer getOneCustomer(int customerID){
        log.info("entering getOneCustomer using customer id: {}",customerID);
        Customer customer =customerRepository.getCustomerById(customerID);
        if(customer != null) {
            return customer;
        }else{
            log.error("getOneCustomer throw NoSuchElementException customer id: {}",customerID);
            throw new NoSuchElementException("no such element");
        }
    }

    //todo
    // @Override
    public ArrayList<Customer> getAllCustomers(){
        log.info("entering getAllCustomers");
        return null;
    }

    /****************************** service methods **********************************/

    // for company login
    public Company getOneCompany(String email, String password){
        log.info("entering getOneCompany using Email: {} Password: {}", email, password);
        Company company =companyRepository.findCompaniesByEmailAndPassword(email,password);
        if(company != null) {
            return company;
        }else{
            log.error("getOneCompany throw NoSuchElementException Email: {} Password: {}", email, password);
            throw new NoSuchElementException("no such element");
        }
    }
    // for customer login
    public Customer getOneCustomer(String email, String password){
        log.info("entering getOneCustomer using Email: {} Password: {}", email, password);
        Customer customer =customerRepository.findCustomerByEmailAndPassword(email,password);
        if(customer != null) {
            return customer;
        }else{
            log.error("getOneCustomer throw NoSuchElementException Email: {} Password: {}", email, password);
            throw new NoSuchElementException("no such element");
        }
    }
    // for deleteCompany
    private void deleteCoupons(int companyID) {
        log.info("entering deleteCoupons using company id: {}",companyID);
        //todo should check if cascade deletes perches too
        couponRepository.deleteCouponByCompanyId(companyID);
    }

    //
}

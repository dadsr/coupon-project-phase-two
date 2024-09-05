package couponsProject.couponsProject.sertvices;

import couponsProject.couponsProject.Company;
import couponsProject.couponsProject.beans.Customer;
import couponsProject.couponsProject.repository.CompanyRepository;
import couponsProject.couponsProject.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.NoSuchElementException;

@AllArgsConstructor
@Service
public class AdminServicesImpl implements AdminServices {
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    CustomerRepository customerRepository;


    //todo
    @Override
    public int login(String email, String password){
        return 5;
    }

    @Override
    public void addCompany(Company company){
        companyRepository.save(company);
    }

    @Override
    public void addCustomer(Customer customer){
        customerRepository.save(customer);
    }


    @Override
    public void updateCompany(Company company){
        if(companyRepository.existsById(company.getId())){
            companyRepository.save(company);
        }else{
            throw new NoSuchElementException("no such element");
        }

    }

    @Override
    public void updateCustomer(Customer customer){
        if(customerRepository.existsById(customer.getId())){
            customerRepository.save(customer);
        }else{
            throw new NoSuchElementException("no such element");
        }
    }


    @Override
    public void deleteCompany(int companyID) {
        if(companyRepository.existsById(companyID)) {
            companyRepository.deleteById(companyID);
        }else {
            throw new NoSuchElementException("no such element");
        }
    }


    @Override
    public void deleteCustomer(int customerID){
        if(customerRepository.existsById(customerID)) {
            customerRepository.deleteById(customerID);
        }else {
            throw new NoSuchElementException("no such element");
        }
    }



    @Override
    public Company getOneCompany(int companyID){
        if(companyRepository.existsById(companyID)) {
            return companyRepository.findCompaniesById(companyID);
        }else {
            throw new NoSuchElementException("no such element");
        }
    }


    @Override
    public Company getOneCompany(String email, String password){
        Company company =companyRepository.findCompaniesByEmailAndPassword(email,password);
        if(company != null) {
            return company;
        }else{
            throw new NoSuchElementException("no such element");
        }
    }

    @Override
    public Customer getOneCustomer(int customerID){
        Customer customer =customerRepository.getCustomerById(customerID);
        if(customer != null) {
            return customer;
        }else{
            throw new NoSuchElementException("no such element");
        }
    }

    @Override
    public Customer getOneCustomer(String email, String password){
        Customer customer =customerRepository.findCustomerByEmailAndPassword(email,password);
        if(customer != null) {
            return customer;
        }else{
            throw new NoSuchElementException("no such element");
        }
    }

    //todo
    @Override
    public ArrayList<Company> getAllCompanies(){
        return null;
    }

    //todo
    @Override
    public ArrayList<Customer> getAllCustomers(){
       return null;
    }


    //
}

package couponsProject.couponsProject.repository;

import couponsProject.couponsProject.beans.Category;
import couponsProject.couponsProject.beans.Coupon;
import couponsProject.couponsProject.beans.Customer;
import couponsProject.couponsProject.sertvices.CustomerServicesImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {


    //@Query("select c from Customer c where c.id = ?1")
    Customer getCustomerById(int customerID);

    //@Query("select c from Customer c where c.email = ?1 and c.password = ?2")
    Customer findCustomerByEmailAndPassword(String email, String password);

    //@Query("select (count(c) > 0) from Customer c where c.email = ?1")
    boolean existsByEmail(String email);

    //@Query("select c from Customer c where c.email = ?1 and c.password = ?2")
    int getCustomerByEmailAndPassword(String email, String password);

}
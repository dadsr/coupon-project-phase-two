package couponsProject.couponsProject.sertvices;

import couponsProject.couponsProject.beans.Category;
import couponsProject.couponsProject.beans.Coupon;
import couponsProject.couponsProject.beans.Customer;
import couponsProject.couponsProject.controllers.CouponException;
import couponsProject.couponsProject.repository.CouponRepository;
import couponsProject.couponsProject.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CustomerServicesImpl {
    private CustomerRepository customerRepository;
    private CouponRepository couponRepository;

    public int login(String email, String password){
        log.info("Entering login using Email: {} Password: {}", email, password);
        int id =-1;
        id = customerRepository.getCustomerByEmailAndPassword(email,password);
        if (id >-1) {
            log.debug("Login succeeded, customer id {}", id);
            return id;
        }else {
            log.error("Login failed for Email: {} Password: {}", email, password);
            throw new NoSuchElementException("No such customer");
        }
    }

    //todo where to put transaction
    @Transactional
    public void couponPurchase (int customerId,int couponId){
        log.info("Entering couponPurchase using customerId: {} couponId: {}", customerId, couponId);
        //todo not working
        if(couponRepository.existPurchase(customerId,couponId)){
            Customer customer = customerRepository.getCustomerById(customerId);
            Coupon coupon = couponRepository.getCouponById(couponId);
            //coupon list
            customer.setCoupons(addToList(customer.getCoupons(),coupon));
            customerRepository.save(customer);
            //customers list
            coupon.setCustomers(addToList(coupon.getCustomers(),customer));
            couponRepository.save(coupon);
            //amount
            coupon.setAmount(coupon.getAmount()-1);
        }else {
            log.error("Purchase is not possible for customerId: {} couponId: {}", customerId, couponId);
            throw new CouponException("Purchase is not possible");
        }
    }

    public List<Coupon> getCoupons(int customerId) {
        log.info("Entering getCoupons using customerId: {}", customerId);
        return customerRepository.getCustomerById(customerId).getCoupons();
    }

    public List<Coupon> getCoupons(int customerId, Category category) {
        log.info("Entering getCoupons using customerId: {} and category: {}", customerId, category);
        Customer customer = customerRepository.getCustomerById(customerId);
        if (customer != null) {
            return customer.getCoupons().stream()
                    .filter(coupon -> coupon.getCategory().equals(category))
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    public List<Coupon> getCoupons(int customerId, double maxPrice) {
        log.info("Entering getCoupons using customerId: {} and max price of: {}", customerId, maxPrice);
        Customer customer = customerRepository.getCustomerById(customerId);
        if (customer != null) {
            return customer.getCoupons().stream()
                    .filter(coupon -> coupon.getPrice()<=maxPrice)
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    /****************************** service methods **********************************/
//generic add to list

    private <T> List<T> addToList(List<T> list, T obj) {
        list.add(obj);
        return list;

    }
}


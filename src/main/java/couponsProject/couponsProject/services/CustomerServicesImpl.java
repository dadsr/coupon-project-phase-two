package couponsProject.couponsProject.services;

import couponsProject.couponsProject.beans.CategoryEnum;
import couponsProject.couponsProject.beans.Coupon;
import couponsProject.couponsProject.beans.Customer;
import couponsProject.couponsProject.controllers.exseptions.CouponException;
import couponsProject.couponsProject.repository.CouponRepository;
import couponsProject.couponsProject.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
@Service
public class CustomerServicesImpl implements CustomerServices {
    private CustomerRepository customerRepository;
    private CouponRepository couponRepository;

    @Override
    public int login(String email, String password){
        log.info("Entering login using Email: {} Password: {}", email, password);
        int id = customerRepository.getCustomerByEmailAndPassword(email,password);
        if (id >0) {
            log.debug("Login succeeded, customer id {}", id);
            return id;
        }else {
            log.error("Login failed for Email: {} Password: {}", email, password);
            throw new NoSuchElementException("No such customer");
        }
    }

    @Override
    public Customer getCustomer(int customerId){
        log.info("Entering getCustomer using customerId: {}", customerId);
        return customerRepository.getCustomerById(customerId);
    }
    //todo where to put transaction
    @Transactional
    @Override
    public void couponPurchase(int customerId, int couponId){
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

    @Override
    public List<Coupon> getCoupons(int customerId) {
        log.info("Entering getCoupons using customerId: {}", customerId);
        return customerRepository.getCustomerById(customerId).getCoupons();
    }

   @Override
    public List<Coupon> getCoupons(int customerId, CategoryEnum category) {
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

    @Override
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


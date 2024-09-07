package couponsProject.couponsProject.services;

import couponsProject.couponsProject.beans.Category;
import couponsProject.couponsProject.beans.Coupon;
import couponsProject.couponsProject.beans.Customer;
import jakarta.transaction.Transactional;

import java.util.List;

public interface CustomerServices {
    int login(String email, String password);

    Customer getCustomer(int customerId);

    //todo where to put transaction
    @Transactional
    void couponPurchase(int customerId, int couponId);

    List<Coupon> getCoupons(int customerId);

    List<Coupon> getCoupons(int customerId, Category category);

    List<Coupon> getCoupons(int customerId, double maxPrice);
}

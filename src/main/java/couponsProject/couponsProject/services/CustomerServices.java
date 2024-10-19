package couponsProject.couponsProject.services;

import couponsProject.couponsProject.beans.CategoryEnum;
import couponsProject.couponsProject.beans.Coupon;
import couponsProject.couponsProject.beans.Customer;
import jakarta.transaction.Transactional;

import java.util.List;

public interface CustomerServices  extends ClientServices {
    int login(String email, String password);

    Customer getCustomer(int customerId);

    @Transactional
    void couponPurchase(int customerId, int couponId);

    List<Coupon> getCoupons(int customerId);

    List<Coupon> getCoupons(int customerId, CategoryEnum category);

    List<Coupon> getCoupons(int customerId, double maxPrice);
}

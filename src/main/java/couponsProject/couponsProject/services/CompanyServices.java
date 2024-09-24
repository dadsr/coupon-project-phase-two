package couponsProject.couponsProject.services;

import couponsProject.couponsProject.beans.CategoryEnum;
import couponsProject.couponsProject.beans.Company;
import couponsProject.couponsProject.beans.Coupon;

import java.util.List;

public interface CompanyServices {
    int login(String email, String password);

    Company getCompanyDetails(int companyId);

    void addCoupon(Coupon coupon);

    void updateCoupon(Coupon coupon);

    void deleteCoupon(int couponID);

    List<Coupon> getCompanyCoupons(int companyId);

    List<Coupon> getCompanyCoupons(int companyId, CategoryEnum category);

    List<Coupon> getCompanyCoupons(int companyId, Double maxPrice);
}

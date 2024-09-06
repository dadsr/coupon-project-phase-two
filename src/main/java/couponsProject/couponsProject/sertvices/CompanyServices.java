package couponsProject.couponsProject.sertvices;

import couponsProject.couponsProject.Company;
import couponsProject.couponsProject.beans.Category;
import couponsProject.couponsProject.beans.Coupon;

import java.util.List;

public interface CompanyServices {
    int login(String email, String password);

    Company getCompanyDetails(int companyId);

    void addCoupon(Coupon coupon);

    void updateCoupon(Coupon coupon);

    void deleteCoupon(int couponID);

    List<Coupon> getCompanyCoupons(int companyId);

    List<Coupon> getCompanyCoupons(int companyId, Category category);

    List<Coupon> getCompanyCoupons(int companyId, Double maxPrice);
}

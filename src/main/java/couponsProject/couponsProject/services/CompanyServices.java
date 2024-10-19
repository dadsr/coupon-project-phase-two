package couponsProject.couponsProject.services;

import couponsProject.couponsProject.beans.Company;
import couponsProject.couponsProject.beans.Coupon;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CompanyServices extends ClientServices {
    int login(String email, String password);

    Company getCompanyDetails(int companyId);

    void addCoupon(Coupon coupon);

    void updateCoupon(Coupon coupon);
    @Transactional
    void deleteCoupon(int couponID);

    List<Coupon> getCompanyCoupons(int companyId);

}

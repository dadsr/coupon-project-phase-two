package couponsProject.couponsProject.sertvices;

import couponsProject.couponsProject.Company;
import couponsProject.couponsProject.beans.Category;
import couponsProject.couponsProject.beans.Coupon;
import couponsProject.couponsProject.repository.CompanyRepository;
import couponsProject.couponsProject.repository.CouponRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.NoSuchElementException;

@Slf4j
@AllArgsConstructor
@Service
public class CompanyServicesImpl {
    private CompanyRepository companyRepository;
    private CouponRepository couponRepository;


    public int login(String email, String password){
        log.info("entering login using Email: {} Password: {}", email, password);
        int id =-1;
        id = companyRepository.getCompanyIdByEmailAndPassword(email,password);
        if (id >-1) {
            log.debug("login succeeded company id {}",id);
            return id;
        }else {
            log.error("login throw NoSuchElementException Email: {} Password: {}", email, password);
            throw new NoSuchElementException("no such company");
        }
    }


    void addCoupon(Coupon coupon){
        log.info("entering addCoupon company id: {} and title: {}", coupon.getCompany().getId(),coupon.getTitle());
        if (!couponRepository.existsByCompanyIdAndTitle(coupon.getCompany().getId(),coupon.getTitle())){
            couponRepository.save(coupon);
            log.debug("addCoupon succeeded coupon id {}",coupon.getId());
        }else{
            log.error("addCoupon throw CouponException  coupon already exists company id: {} and title: {}",coupon.getCompany().getId(),coupon.getTitle());
            throw new CouponException("coupon already exists");
        }
    }

    void updateCoupon(Coupon coupon){
        log.info("entering updateCoupon coupon id: {}", coupon.getId());
        if (couponRepository.existsById(coupon.getId())){
            couponRepository.save(coupon);
            log.debug("updateCoupon succeeded coupon id {}",coupon.getId());
        }else{
            log.error("addCoupon throw NoSuchElementException  company id: {} and title: {}", coupon.getCompany().getId(),coupon.getTitle());
            throw new NoSuchElementException("coupon already exists");
        }
    }

    void deleteCoupon(int couponID){

    }



    Company getCompanyDetails()

    int getCompanyID()

    void setCompanyID(int companyID)

    ArrayList<Coupon> getCompanyCoupons()

    ArrayList<Coupon> getCompanyCoupons(Category category)

    ArrayList<Coupon> getCompanyCoupons(Double maxPrice)

}

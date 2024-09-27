package couponsProject.couponsProject.services;

import couponsProject.couponsProject.beans.CategoryEnum;
import couponsProject.couponsProject.beans.Company;
import couponsProject.couponsProject.beans.Coupon;
import couponsProject.couponsProject.controllers.exseptions.CouponException;
import couponsProject.couponsProject.repository.CompanyRepository;
import couponsProject.couponsProject.repository.CouponRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@AllArgsConstructor
@Slf4j
@Service
public class CompanyServicesImpl implements CompanyServices {
    private CompanyRepository companyRepository;
    private CouponRepository couponRepository;

    @Transactional(readOnly = true)
    @Override
    public int login(String email, String password){
        log.info("Entering login using Email: {} Password: {}", email, password);

            Integer id = companyRepository.getCompanyIdByEmailAndPassword(email,password);


        if (id != null && id > 0) {
            log.debug("Login succeeded, company id {}", id);
            return id;
        }else {
            log.error("Login failed for Email: {} Password: {}", email, password);
            throw new NoSuchElementException("No such company");
        }
    }

    @Override
    public Company getCompanyDetails(int companyId){
        log.info("entering getCompanyDetails using company id : {}",companyId);
        return companyRepository.findCompaniesById(companyId);
    }

    @Override
    public void addCoupon(Coupon coupon){
        Company company =coupon.getCompany();
        log.info("Entering addCoupon, company id: {} and title: {}",company.getId(),coupon.getTitle());
            if(company.getCoupons().stream().anyMatch(coupon::equals)){
                log.error("Coupon already exists for company id: {} and title: {}",company.getId(),coupon.getTitle());
                throw new CouponException("Coupon already exists");
            }else {
                couponRepository.save(coupon);
                company.getCoupons().add(coupon);
                log.debug("AddCoupon succeeded, coupon id {}",coupon.getId());
            }
    }

    @Override
    public void updateCoupon(Coupon coupon){
        log.info("Entering updateCoupon, coupon id: {}", coupon.getId());
        if (couponRepository.existsById(coupon.getId())){
            couponRepository.save(coupon);
            log.debug("UpdateCoupon succeeded, coupon id {}",coupon.getId());
        }else{
            log.error("No such coupon to update, company id: {} and title: {}", coupon.getCompany().getId(),coupon.getTitle());
            throw new NoSuchElementException("Coupon dose not exists");
        }
    }

    @Override
    public void deleteCoupon(int couponID){
        log.info("Entering deleteCoupon coupon id: {}", couponID);
        if (couponRepository.existsById(couponID)){
            couponRepository.deleteCouponById(couponID);
            log.debug("DeleteCoupon succeeded, coupon id {}",couponID);
        }else{
            log.error("No such coupon to update, coupon id: {}", couponID);
            throw new NoSuchElementException("coupon does not exist");
        }
    }

    @Override
    public List<Coupon> getCompanyCoupons(int companyId){
        log.info("Entering getCompanyCoupons, using company id : {}",companyId);
        return couponRepository.findAllByCompanyId(companyId);
    }


  @Override
    public List<Coupon> getCompanyCoupons(int companyId, CategoryEnum category){
        log.info("Entering getCompanyCoupons, using company id : {} category : {}",companyId,category);
        return couponRepository.findAllByCompanyIdAndCategory(companyId,category);
    }



    @Override
    public List<Coupon> getCompanyCoupons(int companyId, Double maxPrice){
        log.info("Entering getCompanyCoupons, using company id : {} maxPrice : {}",companyId,maxPrice);
        return couponRepository.findAllByCompanyIdAndPriceIsLessThanEqual(companyId,maxPrice);
    }

//
}

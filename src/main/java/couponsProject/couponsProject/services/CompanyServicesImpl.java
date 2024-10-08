package couponsProject.couponsProject.services;

import couponsProject.couponsProject.beans.CategoryEnum;
import couponsProject.couponsProject.beans.Company;
import couponsProject.couponsProject.beans.Coupon;
import couponsProject.couponsProject.beans.Customer;
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

    /**
     * Authenticates a company user and retrieves their ID.
     *
     * @param email The email address for login
     * @param password The password for login
     * @return The ID of the authenticated company
     * @throws NoSuchElementException if no company matches the provided credentials
     * @Transactional(readOnly = true) Indicates that this method is a read-only transaction
     * @Override Overrides the login method from a parent class or interface
     */
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

    /**
     * Retrieves detailed information about a specific company.
     *
     * @param companyId The ID of the company to retrieve
     * @return The Company object containing detailed information
     * @throws NoSuchElementException if no company exists with the given ID
     * @Override Overrides the getCompanyDetails method from a parent class or interface
     */
    @Override
    public Company getCompanyDetails(int companyId){
        log.info("Entering getCompanyDetails using company id : {}",companyId);
        if(companyRepository.existsById(companyId)) {
            return companyRepository.findCompaniesById(companyId);
        }else {
            log.error("getOneCompany throw NoSuchElementException company id: {}",companyId);
            throw new NoSuchElementException("no such company to get");
        }
    }

    /**
     * Adds a new coupon to the system for a specific company.
     *
     * @param coupon The Coupon object to be added
     * @throws CouponException if a coupon with the same title already exists for the company
     * @Override Overrides the addCoupon method from a parent class or interface
     */
    @Override
    public void addCoupon(Coupon coupon){
        Company company =coupon.getCompany();
        log.info("Entering addCoupon, company id: {} and title: {}",company.getId(),coupon.getTitle());
        if(company.getCoupons()!=null && company.getCoupons().stream().anyMatch(coupon::equals)){
            log.error("Coupon already exists for company id: {} and title: {}",company.getId(),coupon.getTitle());
            throw new CouponException("Coupon already exists");
        }else {
            couponRepository.save(coupon);
            //  company.getCoupons().add(coupon);
            log.debug("AddCoupon succeeded, coupon id {}",coupon.getId());
        }
    }

    /**
     * Updates an existing coupon in the system.
     *
     * @param coupon The Coupon object with updated information
     * @throws NoSuchElementException if no coupon exists with the given ID
     * @Override Overrides the updateCoupon method from a parent class or interface
     */
    @Override
    public void updateCoupon(Coupon coupon){
        log.info("Entering updateCoupon, coupon id: {}", coupon.getId());
        if (couponRepository.existsById(coupon.getId())){
            couponRepository.save(coupon);
            log.debug("UpdateCoupon succeeded, coupon id {}",coupon.getId());
        }else{
            log.error("No such coupon to update, company id: {} and title: {}", coupon.getCompany().getId(),coupon.getTitle());
            throw new NoSuchElementException("Coupon does not exists");
        }
    }

    /**
     * Deletes a coupon from the system and removes its associations.
     *
     * @param couponID The ID of the coupon to be deleted
     * @throws NoSuchElementException if no coupon exists with the given ID
     * @Transactional Ensures that all operations within the method are part of a single transaction
     * @Override Overrides the deleteCoupon method from a parent class or interface
     */
    @Transactional
    @Override
    public void deleteCoupon(int couponID){
        log.info("Entering deleteCoupon coupon id: {}", couponID);
        Coupon coupon = couponRepository.findById(couponID).orElseThrow(()->{
            log.error("No such coupon to update, coupon id: {}", couponID);
            return new NoSuchElementException("coupon does not exist");
        });
        for (Customer customer : coupon.getCustomers()) {
            customer.getCoupons().remove(coupon);
        }
        //detach company
        coupon.detachCompany();
        couponRepository.delete(coupon);
        log.debug("DeleteCoupon succeeded, coupon id {}",couponID);
    }

    /**
     * Retrieves all coupons associated with a specific company.
     *
     * @param companyId The ID of the company whose coupons are to be retrieved
     * @return A List of Coupon objects associated with the specified company
     * @Override Overrides the getCompanyCoupons method from a parent class or interface
     */
    @Override
    public List<Coupon> getCompanyCoupons(int companyId){
        log.info("Entering getCompanyCoupons, using company id : {}",companyId);
        return couponRepository.findAllByCompanyId(companyId);
    }

//
}

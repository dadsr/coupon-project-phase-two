package couponsProject.couponsProject.repository;

import couponsProject.couponsProject.beans.Category;
import couponsProject.couponsProject.beans.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Integer> {
    //@Query("delete from Coupon c where c.company.id = ?1")
    void deleteCouponByCompanyId(int companyID);

    //@Query("select (count(c) > 0) from Coupon c where c.company.id = ?1 and c.title = ?2")
    boolean existsByCompanyIdAndTitle(int id, String title);

    //@Query("delete from Coupon c where c.id = ?1")
    void deleteCouponById(int couponID);

    //@Query("select c from Coupon c where c.company.id = ?1")
    List<Coupon> findAllByCompanyId(int companyID);

    //@Query("select c from Coupon c where c.company.id = ?1 and c.category = ?2")
    List<Coupon> findAllByCompanyIdAndCategory(int companyID, Category category);

    //@Query("select c from Coupon c where c.company.id = ?1 and c.price <= ?2")
    List<Coupon> findAllByCompanyIdAndPriceIsLessThanEqual(int companyID, Double maxPrice);
}
package couponsProject.couponsProject.repository;

import couponsProject.couponsProject.beans.CategoryEnum;
import couponsProject.couponsProject.beans.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.sql.Date;
import java.util.List;

/**
 * Repository interface for Coupon entity operations.
 * Extends JpaRepository for basic CRUD operations.
 */
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
    List<Coupon> findAllByCompanyIdAndCategory(int company_id, CategoryEnum category);


    //@Query("select c from Coupon c where c.company.id = ?1 and c.price <= ?2")
    List<Coupon> findAllByCompanyIdAndPriceIsLessThanEqual(int companyID, Double maxPrice);

    //JPA's method query derivation is limited when handling nested relationships such as ManyToMany.
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
            "FROM Customer cust JOIN cust.coupons c " +
            "WHERE cust.id = :customerId AND c.id = :couponId")
    boolean existsPurchase(@Param("customerId") int customerId, @Param("couponId") int couponId);


    @Query("select c from Coupon c where c.id = ?1")
    Coupon getCouponById(int couponId);

    void delete(@NonNull Coupon coupon);

    //@Query("select c from Coupon c where c.endDate < ?1")
    List<Coupon> findByEndDateBefore(Date date);
}
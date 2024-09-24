package couponsProject.couponsProject.repository;

import couponsProject.couponsProject.beans.CategoryEnum;
import couponsProject.couponsProject.beans.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
    List<Coupon> findAllByCompanyIdAndCategory(int company_id, CategoryEnum category);


    //@Query("select c from Coupon c where c.company.id = ?1 and c.price <= ?2")
    List<Coupon> findAllByCompanyIdAndPriceIsLessThanEqual(int companyID, Double maxPrice);

/*    @Modifying
    @Query("select c from Coupon c where c.id = ?1 and c.startDate >= getdate() and c.endDate >= getdate() and c.amount >0")
    boolean findCouponsByIdAndStartDateIsAfterAndEndDateIsAfterAndAmountGreaterThan(int id);
*/
    @Query(value = "SELECT 1 WHERE " +
            "EXISTS (" +
            "SELECT 1 FROM customers_vs_coupons WHERE customer_id = :customerId AND coupons_id = :couponId) " +
            "AND EXISTS (" +
            "SELECT 1 FROM coupons WHERE id = :couponId AND start_date <= CURDATE() AND end_date >= CURDATE() AND amount > 0);"
            , nativeQuery = true)
    boolean existPurchase(@Param("customerId") int customerId,
                          @Param("couponId") int couponId);

    @Query("select c from Coupon c where c.id = ?1")
    Coupon getCouponById(int couponId);
}
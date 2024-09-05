package couponsProject.couponsProject.repository;

import couponsProject.couponsProject.beans.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface CouponRepository extends JpaRepository<Coupon, Integer> {
    //@Query("delete from Coupon c where c.company.id = ?1")
    void deleteCouponByCompanyId(int companyID);

    //@Query("select (count(c) > 0) from Coupon c where c.company.id = ?1 and c.title = ?2")
    boolean existsByCompanyIdAndTitle(int id, String title);
}
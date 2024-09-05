package couponsProject.couponsProject.repository;

import couponsProject.couponsProject.beans.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Integer> {
}
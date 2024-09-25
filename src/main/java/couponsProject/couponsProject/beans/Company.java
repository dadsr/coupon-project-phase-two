package couponsProject.couponsProject.beans;


import couponsProject.couponsProject.beans.Coupon;
import couponsProject.couponsProject.repository.CouponRepository;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "companies")
public class  Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Setter(AccessLevel.NONE)
    private int id;
    @Setter(AccessLevel.NONE)
    private String name;
    private String email;
    private String password;
    @OneToMany(mappedBy = "company", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Coupon> coupons = new ArrayList<>();

    public Company(String name, String email, String password, List<Coupon> coupons) {
        this.name = name;
        this.email = email;
        this.password = password;
        if (coupons != null) {
            this.coupons = coupons;
            coupons.forEach(coupon -> coupon.setCompany(this));//just in case
        }

    }

    public Company(int id, String name, String email, String password, List<Coupon> coupons) {

        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        if (coupons != null)
            this.coupons = coupons;
    }

    @Builder
    public static Company createInstance(String name, String email, String password, List<Coupon> coupons) {
        return new Company(name, email, password, coupons);
    }

    public void addCoupon(Coupon coupon) {
        this.coupons.add(coupon);
        coupon.setCompany(this);
    }

    public void removeCoupon(Coupon coupon) {
        this.coupons.remove(coupon);
        coupon.setCompany(null);
    }
}
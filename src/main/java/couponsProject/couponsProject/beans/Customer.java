package couponsProject.couponsProject.beans;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Setter(AccessLevel.NONE)
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "customers_vs_coupons",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "coupons_id"))
    private List<Coupon> coupons ;

    public Customer(String firstName, String lastName, String email, String password, List<Coupon> coupons) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        if (coupons != null) {
            this.coupons = coupons;
        }else {
            this.coupons = new ArrayList<>();
        }
    }

    public Customer(int id, String firstName, String lastName, String email, String password, List<Coupon> coupons) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        if (coupons != null) {
            this.coupons = coupons;
        }else{
            this.coupons = new ArrayList<>();
        }
    }
    @Builder
    public static Customer createInstance(String firstName, String lastName, String email, String password, List<Coupon> coupons) {
        return new Customer(firstName, lastName, email, password,coupons);
    }

    public void addCoupon(Coupon coupon) {
        coupons.add(coupon);
        coupon.getCustomers().add(this);
    }


    public void removeCoupon(Coupon coupon) {
        coupons.remove(coupon);
        coupon.getCustomers().remove(this);
    }

}
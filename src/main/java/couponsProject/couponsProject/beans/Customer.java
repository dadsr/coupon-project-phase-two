package couponsProject.couponsProject.beans;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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
    @ManyToMany(cascade = {CascadeType.REMOVE})
    @JoinTable(name = "customers_vs_coupons",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "coupons_id"))
    private List<Coupon> coupons = new ArrayList<>();

}
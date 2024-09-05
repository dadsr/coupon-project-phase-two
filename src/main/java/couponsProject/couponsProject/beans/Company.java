package couponsProject.couponsProject;

import couponsProject.couponsProject.beans.Coupon;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "companies")
public class  Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;
    private String name;
    private String email;
    private String password;
    @OneToMany(mappedBy = "company", orphanRemoval = true)
    private List<Coupon> coupons = new ArrayList<>();

}
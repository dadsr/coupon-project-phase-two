package couponsProject.couponsProject.beans;

import couponsProject.couponsProject.Company;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "coupons")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Setter(AccessLevel.NONE)
    private int id;
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
    @ManyToOne
    @JoinColumn(name = "categor_id")
    private Category category;
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
    private int amount;
    private double price;
    private String image;
    @ManyToMany(mappedBy = "coupons", cascade = CascadeType.REMOVE)
    private List<Customer> customers = new ArrayList<>();

}
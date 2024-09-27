package couponsProject.couponsProject.beans;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
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
    @Enumerated
    @JdbcTypeCode(SqlTypes.INTEGER)
    private CategoryEnum category;
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
    private int amount;
    private double price;
    private String image;
    @ManyToMany(mappedBy = "coupons")
    private List<Customer> customers;


    public Coupon(Company company, CategoryEnum category, String title, String description, Date startDate, Date endDate, int amount, double price, String image, List<Customer> customers) {
        this.company = company;
        this.category = category;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.amount = amount;
        this.price = price;
        this.image = image;
        if(customers!=null) {
            this.customers = customers;
        }
    }

    public Coupon(int id, Company company, CategoryEnum category, String title, String description, Date startDate, Date endDate, int amount, double price, String image, List<Customer> customers) {
        this.id = id;
        this.company = company;
        this.category = category;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.amount = amount;
        this.price = price;
        this.image = image;
        if(customers!=null) {
            this.customers = customers;
        }
    }

    @Builder
    public static Coupon createInstance(Company company, CategoryEnum category, String title, String description, Date startDate, Date endDate, int amount, double price, String image){
        return new Coupon(company, category, title, description, startDate, endDate, amount, price,image, null);
    }

    public void addCustomer(Customer customer) {
        if(customers==null) {
            customers = new ArrayList<>();
        }
        customers.add(customer);
    }

    public void removeCustomer(Customer customer) {
        customers.remove(customer);
    }
}
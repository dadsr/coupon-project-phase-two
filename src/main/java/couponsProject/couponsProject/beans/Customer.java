package couponsProject.couponsProject.beans;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;


/**
 * Represents a Customer entity in the system.
 *
 * @NoArgsConstructor Generates a no-argument constructor
 * @Getter/@Setter Generate getter and setter methods for fields
 * @Entity Marks this class as a JPA entity
 * @Table(name = "customers") Specifies the database table name
 * <p>
 * Fields:
 * - id: Unique identifier (auto-generated, immutable)
 * - firstName: Customer's first name
 * - lastName: Customer's last name
 * - email: Customer's email address
 * - password: Customer's account password
 * - coupons: List of coupons purchased by the customer
 * <p>
 * The coupons field represents a Many-to-Many relationship with Coupon entities:
 * @ManyToMany(fetch = FetchType.EAGER) Specifies eager loading of coupons
 * @JoinTable Defines the join table for the Many-to-Many relationship
 *   - name: "customers_vs_coupons"
 *   - joinColumns: Maps the customer_id
 *   - inverseJoinColumns: Maps the coupons_id
 */
@Slf4j
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

    /**
     * @Builder Enables builder pattern for flexible object creation
     * <p>
     * Factory method for creating Customer instances.
     * @param firstName Customer's first name
     * @param lastName Customer's last name
     * @param email Customer's email address
     * @param password Customer's account password
     * @param coupons List of coupons associated with the customer (can be null or empty)
     * @return A new Customer instance
     */
    @Builder
    public static Customer createInstance(String firstName, String lastName, String email, String password, List<Coupon> coupons) {
        return new Customer(firstName, lastName, email, password,coupons);
    }

    /**
     * Adds a coupon to the customer's list of coupons.
     * Also adds this customer to the coupon's list of customers,
     * maintaining the bidirectional relationship.
     *
     * @param coupon The coupon to be added
     */
    public void addCoupon(Coupon coupon) {
        log.info("Entering addCoupon using coupon id: {}", coupon.getId());
        coupons.add(coupon);
        coupon.getCustomers().add(this);
    }

    /**
     * Removes a coupon from the customer's list of coupons.
     * Also removes this customer from the coupon's list of customers,
     * maintaining the bidirectional relationship.
     *
     * @param coupon The coupon to be removed
     */
    public void removeCoupon(Coupon coupon) {
        log.info("Entering removeCoupon using coupon id: {}", coupon.getId());
        coupon.getCustomers().remove(this);
    }

}
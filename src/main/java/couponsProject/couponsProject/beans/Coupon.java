package couponsProject.couponsProject.beans;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a Coupon entity in the system.
 *
 * @NoArgsConstructor Generates a no-argument constructor
 * @Getter/@Setter Generate getter and setter methods for fields
 * @Entity Marks this class as a JPA entity
 * @Table(name = "coupons") Specifies the database table name
 * <p>
 * Fields:
 * - id: Unique identifier (auto-generated, immutable)
 * - company: Associated Company (Many-to-One relationship)
 * - category: Coupon category (Enumerated type)
 * - title: Coupon title
 * - description: Coupon description
 * - startDate: Coupon validity start date
 * - endDate: Coupon expiration date
 * - amount: Available quantity of the coupon
 * - price: Price of the coupon
 * - image: Image URL or path for the coupon
 * - customers: List of customers who purchased this coupon (Many-to-Many relationship)
 *
 * Note: The customers relationship is eagerly fetched and mapped by the 'coupons' field in the Customer entity.
 */
@Slf4j
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
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "coupons")
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
        }else {
            this.customers = new ArrayList<>();
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
        }else {
            this.customers = new ArrayList<>();
        }
    }

    /**
     * @Builder Enables builder pattern for flexible object creation
     * <p>
     * Factory method for creating Coupon instances.
     * @param company Associated Company
     * @param category Coupon category
     * @param title Coupon title
     * @param description Coupon description
     * @param startDate Validity start date
     * @param endDate Expiration date
     * @param amount Available quantity
     * @param price Coupon price
     * @param image Image URL or path
     * @return A new Coupon instance with no associated customers
     */
    @Builder
    public static Coupon createInstance(Company company, CategoryEnum category, String title, String description, Date startDate, Date endDate, int amount, double price, String image){
        return new Coupon(company, category, title, description, startDate, endDate, amount, price,image, null);
    }

    /**
     * Adds a customer to this coupon's list of customers.
     * Initializes the customers list if it's null.
     * Also adds this coupon to the customer's list of coupons.
     *
     * @param customer The customer to be added
     */
    public void addCustomer(Customer customer) {
        if(customers==null) {
            customers = new ArrayList<>();
            customer.getCoupons().add(this);
        }
        customers.add(customer);
    }

    /**
     * Removes a customer from this coupon's list of customers.
     * Also removes this coupon from the customer's list of coupons.
     *
     * @param customer The customer to be removed
     */
    public void removeCustomer(Customer customer) {
        customers.remove(customer);
        customer.getCoupons().remove(this);
    }

    /**
     * Compares this coupon with another object for equality.
     * Coupons are considered equal if they have the same title and belong to the same company.
     *
     * @param o The object to compare with this coupon
     * @return true if the objects are equal, false otherwise
     * @Override Overrides Object.equals(Object)
     */
    @Override
    public final boolean equals(Object o) {
        log.info("Entering equals using coupon title: {} and this coupon title: {} ",((Coupon)o).getTitle(), this.getTitle());
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coupon coupon = (Coupon) o;
        return Objects.equals(title, coupon.title) &&
                Objects.equals(company.getId(), coupon.company.getId());
    }

    /**
     * Detaches this coupon from its associated company.
     * Removes this coupon from the company's list of coupons and sets the company reference to null.
     * This method maintains the bidirectional relationship between Coupon and Company.
     */
    public void detachCompany() {
        log.info("Entering detachCompany using company id: {}", this.getId());
        getCompany().getCoupons().remove(this);
        setCompany(null);
    }

}
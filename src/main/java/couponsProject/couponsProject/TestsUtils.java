package couponsProject.couponsProject;

import couponsProject.couponsProject.beans.CategoryEnum;
import couponsProject.couponsProject.beans.Company;
import couponsProject.couponsProject.beans.Coupon;
import couponsProject.couponsProject.beans.Customer;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.function.Supplier;

public  class TestsUtils {
    static Random rand = new Random();


    // Generic method to create a list of entities
    public static <T> List<T> createEntities(int num, Supplier<T> entitySupplier) {
        List<T> entities = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            entities.add(entitySupplier.get());
        }
        return entities;
    }


    public static List<Company> createCompanies(int num) {
        return createEntities(num, () -> Company.builder()
                .name("comp" + Math.random())
                .email((int) (Math.random() * 100) + "@comp.co.il")
                .password(String.valueOf(Math.random() * 10))
                .build());
    }


    public static List<Customer> createCustomers(int num) {
        return createEntities(num, () -> Customer.builder()
                .firstName("first" + Math.random())
                .lastName("last")
                .email((int) (Math.random() * 100) + "@walla.co.il")
                .password(String.valueOf(Math.random() * 10))
                .build());
    }

    /**
     * dose not update company for coupon
     */
    public static List<Coupon> createCoupons(Company company ,int num) {
        return createEntities(num,() -> Coupon.builder()
                .company(company)
                .category(CategoryEnum.fromId(rand.nextInt(CategoryEnum.values().length)))
                .title("title" + rand.nextInt(1,99))
                .description("desc" + rand.nextInt(1,99))
                .startDate(Date.valueOf("2024-11-01"))
                .endDate(Date.valueOf("2024-12-01"))
                .amount(rand.nextInt(1, 30))
                .price(rand.nextDouble(100.00))
                .image(null)
                .build());
    }



}

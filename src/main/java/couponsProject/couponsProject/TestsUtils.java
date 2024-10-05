package couponsProject.couponsProject;

import couponsProject.couponsProject.beans.CategoryEnum;
import couponsProject.couponsProject.beans.Company;
import couponsProject.couponsProject.beans.Coupon;
import couponsProject.couponsProject.beans.Customer;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public  class TestsUtils {
    static Random rand = new Random();

    public static Date getRandomDateFromNow(int maxDays) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new java.util.Date());
        cal.add(Calendar.DAY_OF_YEAR, rand.nextInt(maxDays));
        return (Date) cal.getTime();
    }

    // Generic method to create a list of entities
    public static <T> List<T> createEntities(int num, Supplier<T> entitySupplier) {
        List<T> entities = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            entities.add(entitySupplier.get());
        }
        return entities;
    }


    public static List<Company> createCompanies(int num) {
        int rndNum = rand.nextInt(1,1000);
        return createEntities(num, () -> Company.builder()
                .name("comp" + rndNum)
                .email(rndNum + "@comp.co.il")
                .password(String.valueOf(rand.nextInt(1,1000000)))
                .build());
    }


    public static List<Customer> createCustomers(int num) {
        return createEntities(num, () -> Customer.builder()
                .firstName("first" + Math.random())
                .lastName("last")
                .email(rand.nextInt(1,1000) + "@walla.co.il")
                .password(String.valueOf(rand.nextInt(1,1000000)))
                .build());
    }

    /**
     * dose not update company for coupon
     */
    public static List<Coupon> createCoupons(Company company ,int num) {
        return createEntities(num,() -> Coupon.builder()
                .company(company)
                .category(CategoryEnum.fromId(rand.nextInt(CategoryEnum.values().length)))
                .title("title" + rand.nextInt(1,1000000))
                .description("desc" + rand.nextInt(1,1000000))
                .startDate((Date) new java.util.Date())
                .endDate(getRandomDateFromNow(30))
                .amount(rand.nextInt(1, 30))
                .price(rand.nextDouble(100.00))
                .image(null)
                .build());
    }




}

package couponsProject.couponsProject;

import couponsProject.couponsProject.beans.*;

import couponsProject.couponsProject.services.AdminServices;
import couponsProject.couponsProject.services.CompanyServices;
import couponsProject.couponsProject.services.CustomerServices;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootApplication
@EnableScheduling
public class PhaseTwoApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(PhaseTwoApplication.class, args);
        AdminServices adminService = ctx.getBean(AdminServices.class);
        CompanyServices companyServices = ctx.getBean(CompanyServices.class);
        CustomerServices customerServices = ctx.getBean(CustomerServices.class);

        Random rand = new Random();
        ArrayList<Company> companies = new ArrayList<>();
        ArrayList<Customer> customers = new ArrayList<>();


        Company company = TestsUtils.createCompanies(1).get(0);
        List<Coupon> coupons = TestsUtils.createCoupons(company,10);
        Customer customer = TestsUtils.createCustomers(1).get(0);
        adminService.addCompany(company);
        adminService.addCustomer(customer);
        for (Coupon coupon : coupons) {
            companyServices.addCoupon(coupon);
            customerServices.couponPurchase(customer.getId(), coupon.getId());
        }
        company =adminService.getOneCompany(company.getId());
        System.out.print(company.getCoupons());
        System.out.print(customer.getCoupons());
        System.out.print(coupons.get(0).getCustomers());









    }
}


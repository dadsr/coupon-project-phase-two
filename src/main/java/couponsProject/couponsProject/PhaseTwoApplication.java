package couponsProject.couponsProject;

import couponsProject.couponsProject.beans.*;

import couponsProject.couponsProject.services.AdminServices;
import couponsProject.couponsProject.services.CompanyServices;
import couponsProject.couponsProject.services.CustomerServices;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Random;

@SpringBootApplication
public class PhaseTwoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(PhaseTwoApplication.class, args);
        AdminServices adminService = ctx.getBean(AdminServices.class);
        CompanyServices companyServices = ctx.getBean(CompanyServices.class);
        CustomerServices companyCustomerServices = ctx.getBean(CustomerServices.class);

        Random rand = new Random();
        ArrayList<Company> companies = new ArrayList<>();
        ArrayList<Customer> customers = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            companies.add(
                    Company.builder()
                            .name("comp"+i)
                            .email(i+"@"+"comp"+i+".co.il")
                            .password(String.valueOf(Math.random()))
                            .build()
            );
        }

        for (int i = 0; i < 5; i++) {
            customers.add(
                    Customer.builder()
                            .firstName("first"+i)
                            .lastName("last")
                            .email(i+"@walla.co.il")
                            .password(String.valueOf(Math.random()*10))
                            .build()
            );
        }

        // adding company
        for (Company company : companies) {
            adminService.addCompany(company);
        }
        //adding customer
        for (Customer customer : customers) {
            adminService.addCustomer(customer);
        }


            for (Company company : companies) {
                for (int i = 0; i < 30; i++) {
                    companyServices.addCoupon(
                            Coupon.builder()
                                    .company(company)
                                    .category(CategoryEnum.fromId(rand.nextInt(CategoryEnum.values().length)))
                                    .title("title" + i)
                                    .description("desc" + i)
                                    .startDate(Date.valueOf("2024-11-01"))
                                    .endDate(Date.valueOf("2024-12-01"))
                                    .amount(rand.nextInt(1, 30))
                                    .price(rand.nextDouble(100.00))
                                    .image(null)
                                    .build()
                    );
                }
            }

    }


        // update company

        //update coupon

        //update customer

        // delete company

        //delete coupon

        //delete customer

        //get all companies

        //get all coupons

        //get all customers


    }


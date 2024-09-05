package couponsProject.couponsProject.repository;

import couponsProject.couponsProject.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;

public interface CompanyRepository extends JpaRepository<Company, Integer>, JpaSpecificationExecutor<Company> {

    //@Query("select c from Company c where c.id = ?1")
    Company findCompaniesById(int companyID);

    //@Query("select c from Company c where c.email = ?1 and c.password = ?2")
    Company findCompaniesByEmailAndPassword(String email, String password);

    //@Query("select c from Company c")
    ArrayList<Company> getAllCompanies();

    //@Query("select (count(c) > 0) from Company c where c.name = ?1")
    boolean existsByName(String name);
}
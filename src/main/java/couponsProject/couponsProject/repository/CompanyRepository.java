package couponsProject.couponsProject.repository;

import couponsProject.couponsProject.beans.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;


public interface CompanyRepository extends JpaRepository<Company, Integer>, JpaSpecificationExecutor<Company> {

    //@Query("select c from Company c where c.id = ?1")
    Company findCompaniesById(int companyID);

    //@Query("select c from Company c where c.email = ?1 and c.password = ?2")
    Company findCompaniesByEmailAndPassword(String email, String password);

    //@Query("select (count(c) > 0) from Company c where c.name = ?1 or c.email = ?2")
    boolean existsByNameOrEmail(String name, String email);

    @Query("SELECT c.id FROM Company c WHERE c.email = :email AND c.password = :password")
    Integer getCompanyIdByEmailAndPassword(String email, String password);

    @Query("select c from Company c")
    ArrayList<Company> getAllCompanies();



    void delete(Company company);

}
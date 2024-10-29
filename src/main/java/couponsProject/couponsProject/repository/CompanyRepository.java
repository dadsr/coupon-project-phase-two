package couponsProject.couponsProject.repository;

import couponsProject.couponsProject.beans.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.util.ArrayList;

/**
 * Repository interface for Company entity operations.
 * Extends JpaRepository for basic CRUD operations and JpaSpecificationExecutor for complex queries.
 */
public interface CompanyRepository extends JpaRepository<Company, Integer>, JpaSpecificationExecutor<Company> {

    //@Query("select c from Company c where c.id = ?1")
    Company findCompaniesById(int companyID);

    //@Query("select c from Company c where c.email = ?1 and c.password = ?2")
    Company findCompaniesByEmailAndPassword(String email, String password);

    //@Query("select (count(c) > 0) from Company c where c.name = ?1 or c.email = ?2")
    boolean existsByNameOrEmail(String name, String email);



    @Query("select c from Company c")
    ArrayList<Company> getAllCompanies();

    void delete(@NonNull Company company);

    @Query("select c from Company c where c.email = ?1 and c.password = ?2")
    int getCompanyByEmailAndPassword(String email, String password);
}
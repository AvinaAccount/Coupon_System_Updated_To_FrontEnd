package com.Avinadav.couponsystem.data.repo;

import com.Avinadav.couponsystem.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<Company> findCompanyByEmail(String email);

}

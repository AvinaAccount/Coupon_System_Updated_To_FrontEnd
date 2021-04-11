package com.Avinadav.couponsystem.data.repo;

import com.Avinadav.couponsystem.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findAdminByEmailAndPassword(String email, String password);
}

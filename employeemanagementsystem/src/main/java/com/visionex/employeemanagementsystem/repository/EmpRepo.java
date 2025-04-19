package com.visionex.employeemanagementsystem.repository;

import com.visionex.employeemanagementsystem.entity.Employees;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmpRepo extends JpaRepository<Employees, Integer> {
    Optional<Employees> findByEmail(String email);


}

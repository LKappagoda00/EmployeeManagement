package com.visionex.employeemanagementsystem.service;

import com.visionex.employeemanagementsystem.repository.EmpRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class EmployeeDetailsService implements UserDetailsService {

    @Autowired
    private EmpRepo empRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return empRepo.findByEmail(username).orElse(null);
    }
}

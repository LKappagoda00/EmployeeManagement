package com.visionex.employeemanagementsystem.controller;

import com.visionex.employeemanagementsystem.dto.ReqRes;
import com.visionex.employeemanagementsystem.entity.Employees;
import com.visionex.employeemanagementsystem.repository.EmpRepo;
import com.visionex.employeemanagementsystem.service.EmployeeManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class EmployeeManagementController {

    @Autowired
    private EmpRepo empRepo;

    @Autowired
    private EmployeeManagementService employeeManagementService;

    @PostMapping("/auth/register")
    public ResponseEntity<ReqRes> registerEmployee(@RequestBody ReqRes reqRes) {
        return ResponseEntity.ok(employeeManagementService.register(reqRes));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ReqRes> loginEmployee(@RequestBody ReqRes reqRes) {
        return ResponseEntity.ok(employeeManagementService.login(reqRes));

    }

    @GetMapping("/admin/get-all-employees")
    public ResponseEntity<ReqRes> getAllEmployees() {
        return ResponseEntity.ok(employeeManagementService.getAllEmployees());
    }

    @GetMapping("/admin/get-employees/{employeeId}")
    public ResponseEntity<ReqRes> getEmployeeById(@PathVariable Integer employeeId) {
        return ResponseEntity.ok(employeeManagementService.getEmployeesById(employeeId));
    }

    @PutMapping("/admin/update/{employeeId}")
    public ResponseEntity<ReqRes> updateEmployee(@PathVariable Integer employeeId, @RequestBody Employees reqRes) {
      return ResponseEntity.ok(employeeManagementService.updateEmployee(employeeId, reqRes));
    }

    @GetMapping("/adminEmployee/get-profile")
    public ResponseEntity<ReqRes> getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email =authentication.getName();
        ReqRes reqRes = employeeManagementService.getMyInfo(email);
        return ResponseEntity.status(reqRes.getStatusCode()).body(reqRes);
    }

    @DeleteMapping("/admin/delete/{employeeId}")
    public ResponseEntity<ReqRes> deleteEmployee(@PathVariable Integer employeeId) {
        return ResponseEntity.ok(employeeManagementService.deleteEmployee(employeeId));
    }
    @GetMapping("/auth/check-email")
    public ResponseEntity<Map<String, Boolean>> checkEmailExists(@RequestParam String email) {
        boolean exists = empRepo.findByEmail(email).isPresent();
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

}
